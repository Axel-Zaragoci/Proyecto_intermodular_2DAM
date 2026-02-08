package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.models.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomViewModel(
    private val repository: RoomRepository
) : ViewModel() {
    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadRooms()
    }

    fun loadRooms() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _rooms.value = repository.getRooms()
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
