package com.example.intermodular.viewmodels.viewModelFacotry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.viewmodels.MyBookingDetailsViewModel

class MyBookingDetailsViewModelFactory(
    private val id: String,
    private val repository: BookingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyBookingDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyBookingDetailsViewModel(id, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}