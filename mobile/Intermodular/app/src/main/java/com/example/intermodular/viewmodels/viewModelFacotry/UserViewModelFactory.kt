package com.example.intermodular.viewmodels.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intermodular.data.remote.auth.SessionManager
import com.example.intermodular.data.repository.UserRepository
import com.example.intermodular.viewmodels.UserViewModel

class UserViewModelFactory(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}