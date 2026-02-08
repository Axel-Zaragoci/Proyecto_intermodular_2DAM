package com.example.intermodular.viewmodels.viewModelFacotry

import com.example.intermodular.viewmodels.MyBookingsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intermodular.data.repository.BookingRepository

class MyBookingsViewModelFactory(
    private val repository: BookingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyBookingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyBookingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}