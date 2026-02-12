package com.example.intermodular.viewmodels.viewModelFacotry

import com.example.intermodular.viewmodels.NewBookingViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.intermodular.data.repository.BookingRepository;
import com.example.intermodular.data.repository.RoomRepository;

class NewBookingViewModelFactory (
    private val bookingRepository:BookingRepository,
    private val roomRepository:RoomRepository,
    private val roomId : String,
    private val startDate : Long,
    private val endDate : Long,
    private val guests : String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewBookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewBookingViewModel(bookingRepository, roomRepository, roomId, startDate, endDate, guests) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}