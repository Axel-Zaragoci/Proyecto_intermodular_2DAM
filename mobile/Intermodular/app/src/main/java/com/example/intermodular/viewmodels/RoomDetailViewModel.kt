package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.repository.ReviewRepository
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.models.Review
import com.example.intermodular.models.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomDetailViewModel(
    private val roomRepository: RoomRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _room = MutableStateFlow<Room?>(null)
    val room: StateFlow<Room?> = _room

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadRoomDetails(roomId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val allRooms = roomRepository.getRooms()
                _room.value = allRooms.find { it.id == roomId }
                
                if (_room.value == null) {
                    _errorMessage.value = "Room not found"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading room: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadReviews(roomId: String) {
        viewModelScope.launch {
            try {
                _reviews.value = reviewRepository.getReviewsByRoom(roomId)
            } catch (e: Exception) {
                _errorMessage.value = "Error loading reviews: ${e.message}"
            }
        }
    }

    fun calculateAverageRating(): Double {
        val reviewList = _reviews.value
        if (reviewList.isEmpty()) return 0.0
        return reviewList.map { it.rating }.average()
    }
}
