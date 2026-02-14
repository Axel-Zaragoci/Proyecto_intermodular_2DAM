package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.repository.ReviewRepository
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.models.Review
import com.example.intermodular.models.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoomDetailViewModel(
    private val roomId: String,
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
    
    // Calculate average rating from reviews
    val averageRating: StateFlow<Double?> = _reviews.map { reviewList ->
        if (reviewList.isEmpty()) {
            null
        } else {
            reviewList.map { it.rating }.average()
        }
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, null)
    
    init {
        loadRoomData()
        loadReviews()
    }
    
    private fun loadRoomData() {
        viewModelScope.launch {
            try {
                android.util.Log.d("RoomDetailViewModel", "Loading room data for: $roomId")
                val rooms = roomRepository.getRooms()
                val room = rooms.find { it.id == roomId }
                android.util.Log.d("RoomDetailViewModel", "Room found: ${room != null}")
                _room.value = room
            } catch (e: Exception) {
                android.util.Log.e("RoomDetailViewModel", "Error loading room data", e)
                _errorMessage.value = "Error loading room: ${e.message}"
            }
        }
    }
    
    fun loadReviews() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                android.util.Log.d("RoomDetailViewModel", "Loading reviews for room: $roomId")
                val reviews = reviewRepository.getReviewsByRoom(roomId)
                android.util.Log.d("RoomDetailViewModel", "Loaded ${reviews.size} reviews")
                _reviews.value = reviews
            } catch (e: Exception) {
                android.util.Log.e("RoomDetailViewModel", "Error loading reviews", e)
                _errorMessage.value = "Error loading reviews: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
