package com.example.intermodular.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.models.Booking
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookingViewModel(
    private val repository: BookingRepository
) : ViewModel() {
    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings: StateFlow<List<Booking>> = _bookings

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
                _bookings.value = repository.getBookings()
            }
            catch (e: Exception) {
                _errorMessage.value = e.message
            }
            finally {
                _isLoading.value = false
            }
        }
    }
}