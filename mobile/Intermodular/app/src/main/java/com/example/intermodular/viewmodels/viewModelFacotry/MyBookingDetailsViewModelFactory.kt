package com.example.intermodular.viewmodels.viewModelFacotry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.viewmodels.MyBookingDetailsViewModel

class MyBookingDetailsViewModelFactory(
    private val bookingId: String,
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyBookingDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyBookingDetailsViewModel(bookingId, bookingRepository, roomRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}