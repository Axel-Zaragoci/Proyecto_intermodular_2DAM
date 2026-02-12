package com.example.intermodular.viewmodels.viewModelFacotry

import com.example.intermodular.viewmodels.MyBookingsViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.data.repository.RoomRepository

class MyBookingsViewModelFactory(
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyBookingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyBookingsViewModel(bookingRepository, roomRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}