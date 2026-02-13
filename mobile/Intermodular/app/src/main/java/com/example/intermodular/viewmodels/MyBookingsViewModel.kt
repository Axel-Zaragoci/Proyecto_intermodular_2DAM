package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.remote.ApiErrorHandler
import com.example.intermodular.data.remote.auth.SessionManager
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.models.Booking
import com.example.intermodular.models.Room
import com.example.intermodular.models.RoomFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyBookingsViewModel(
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {
    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings: StateFlow<List<Booking>> = _bookings

    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms : StateFlow<List<Room>> = _rooms

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadBookings()
    }

    fun loadBookings() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val rooms = roomRepository.getRooms(RoomFilter())
                _rooms.value = rooms

                val bookings = bookingRepository.getBookingsByUserId(SessionManager.getUserId()!!)
                _bookings.value = bookings
            } catch (e: Exception) {
                _errorMessage.value = ApiErrorHandler.getErrorMessage(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}