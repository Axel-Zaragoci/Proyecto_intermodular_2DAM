package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.data.repository.RoomRepository

class NewBookingViewModel(
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository,
    private val roomId : String
) : ViewModel() {
}