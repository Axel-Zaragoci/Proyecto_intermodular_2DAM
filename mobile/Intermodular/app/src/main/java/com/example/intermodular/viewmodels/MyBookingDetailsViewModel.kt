package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.remote.ApiErrorHandler
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.models.Booking
import com.example.intermodular.models.Room
import com.example.intermodular.models.RoomFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.let

class MyBookingDetailsViewModel (
    private val bookingId : String,
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {
    private val _booking = MutableStateFlow<Booking?>(null)
    val booking: StateFlow<Booking?> = _booking

    private val _room = MutableStateFlow<Room?>(null)
    val room : StateFlow<Room?> = _room

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadBooking()
    }

    fun loadBooking() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _booking.value = bookingRepository.getBookingById(bookingId)
                if (_booking.value == null) throw Exception("No se pudo conseguir la reserva")
                val rooms = roomRepository.getRooms(RoomFilter())

                val found = rooms.firstOrNull { it.id == _booking.value?.roomId }
                if (found != null) {
                    _room.value = found
                }
                else {
                    throw Exception("No se encontró la habitación")
                }
            } catch (e: Exception) {
                _errorMessage.value = ApiErrorHandler.getErrorMessage(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun localDateToUtcMillis(date: LocalDate): Long {
        return date.atStartOfDay()
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()
    }

    private fun utcMillisToLocalDate(millis: Long): LocalDate {
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
    }

    fun checkInDateToMilliseconds(): Long? {
        return _booking.value?.checkInDate?.let { localDateToUtcMillis(it) }
    }

    fun checkOutDateToMilliseconds(): Long? {
        return _booking.value?.checkOutDate?.let { localDateToUtcMillis(it) }
    }

    fun onStartDateChange(newMillis: Long?) {
        if (newMillis != null) {
            val newDate = utcMillisToLocalDate(newMillis)
            _booking.value = _booking.value?.copy(checkInDate = newDate)
        }
    }

    fun onEndDateChange(newMillis: Long?) {
        if (newMillis != null) {
            val newDate = utcMillisToLocalDate(newMillis)
            _booking.value = _booking.value?.copy(checkOutDate = newDate)
        }
    }

    fun onGuestsChange(guests: String) {
        val intGuests = guests.toIntOrNull()
        if (intGuests != null && intGuests > 0) {
            _booking.value = _booking.value?.copy(guests = intGuests)
        }
    }

    fun updateBooking() {
        val currentBooking = _booking.value ?: run {
            _errorMessage.value = "No hay datos de reserva para actualizar"
            return
        }

        val startMillis = checkInDateToMilliseconds() ?: run {
            _errorMessage.value = "Fecha de entrada no válida"
            return
        }

        val endMillis = checkOutDateToMilliseconds() ?: run {
            _errorMessage.value = "Fecha de salida no válida"
            return
        }

        if (currentBooking.guests <= 0) {
            _errorMessage.value = "El número de huéspedes debe ser mayor que cero"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val updated = bookingRepository.updateBooking(
                    bookingId = currentBooking.id,
                    checkIn = startMillis,
                    checkOut = endMillis,
                    guests = currentBooking.guests
                )
                _booking.value = updated
            } catch (e: Exception) {
                _errorMessage.value = ApiErrorHandler.getErrorMessage(e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun cancelBooking() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                bookingRepository.cancelBooking(bookingId)
                _booking.value = _booking.value?.copy(status = "Cancelada")
            } catch (e: Exception) {
                _errorMessage.value = ApiErrorHandler.getErrorMessage(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}