package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.models.Booking
import com.example.intermodular.models.Room
import com.example.intermodular.models.RoomFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyBookingDetailsViewModel (
    private val bookingId : String,
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {
    private val _booking = MutableStateFlow<Booking?>(null)
    val booking: StateFlow<Booking?> = _booking

    private val _room = MutableStateFlow<Room?>(null)
    val room : StateFlow<Room?> = _room

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadBooking()
    }

    fun loadBooking() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _booking.value = bookingRepository.getBookingById(bookingId)
                if (_booking.value == null) throw Exception("No se pudo conseguir la reserva")
                val rooms = roomRepository.getRooms(RoomFilter())


                if (_booking.value == null) throw Exception("No se pudo conseguir la habitación de la reserva")
                _room.value = rooms.first { it.id == _booking.value?.roomId }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                println(_errorMessage.value)
            } finally {
                _isLoading.value = false
            }
        }
    }
}