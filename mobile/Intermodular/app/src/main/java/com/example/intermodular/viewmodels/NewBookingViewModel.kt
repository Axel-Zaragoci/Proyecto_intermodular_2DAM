package com.example.intermodular.viewmodels

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.models.Room
import com.example.intermodular.models.RoomFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class NewBookingViewModel(
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository,
    private val roomId : String,
    private val startDate : Long,
    private val endDate : Long,
    private val guests : String
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _guests = MutableStateFlow<String>(guests)
    val Guests : StateFlow<String> = _guests

    private val _startDate = MutableStateFlow<Long>(startDate)
    val StartDate: StateFlow<Long?> = _startDate

    private val _endDate = MutableStateFlow<Long>(endDate)
    val EndDate : StateFlow<Long?> = _endDate

    private val _room = MutableStateFlow<Room?>(null)
    val Room : StateFlow<Room?> = _room

    private val _totalPrice = MutableStateFlow<Int?>(null)
    val totalPrice : StateFlow<Int?> = _totalPrice;

    private val _bookingCreated = MutableStateFlow(false)
    val bookingCreated: StateFlow<Boolean> = _bookingCreated

    init {
        loadRoom()
    }

    fun loadRoom() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val rooms = roomRepository.getRooms(RoomFilter())
                _room.value = rooms.first{ it.id == roomId }
                calculateTotalPrice()
            }
            catch (e: Exception) {
                _errorMessage.value = e.message
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun startDateAsLocalDate(): LocalDate? =
        StartDate.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

    fun endDateAsLocalDate(): LocalDate? =
        EndDate.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

    fun onStartDateChange(date: Long?) {
        date?.let { _startDate.value = it }
        calculateTotalPrice()
    }

    fun onEndDateChange(date: Long?) {
        date?.let { _endDate.value = it }
        calculateTotalPrice()
    }

    fun onGuestsChange(value: String) {
        _guests.value = value
    }

    private fun calculateTotalPrice() {
        val room = _room.value ?: return
        val start = startDateAsLocalDate() ?: return
        val end = endDateAsLocalDate() ?: return

        val nights = java.time.temporal.ChronoUnit.DAYS.between(start, end).toInt()
        if (nights <= 0) return

        val base = nights * room.pricePerNight
        val discount = room.offer?.let { base * (it / 100) } ?: 0.0
        _totalPrice.value = (base - discount).toInt()
    }

    fun createBooking() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                bookingRepository.createBooking(
                    roomId = roomId,
                    checkIn = _startDate.value,
                    checkOut = _endDate.value,
                    guests = _guests.value.toInt()
                )

                _bookingCreated.value = true

            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

}