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

/**
 * ViewModel encargado de gestionar la lógica de negocio y el estado de la pantalla de detalle de habitación.
 *
 * @property roomId Identificador de la habitación que se está visualizando.
 * @property roomRepository Repositorio para acceder a los datos de las habitaciones.
 * @property reviewRepository Repositorio para acceder a las reseñas.
 */
class RoomDetailViewModel(
    private val roomId: String,
    private val roomRepository: RoomRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {
    
    private val _room = MutableStateFlow<Room?>(null)
    /**
     * Estado que contiene la información de la habitación.
     */
    val room: StateFlow<Room?> = _room
    
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    /**
     * Estado que contiene la lista de reseñas asociadas a la habitación.
     */
    val reviews: StateFlow<List<Review>> = _reviews
    
    private val _isLoading = MutableStateFlow(false)
    /**
     * Estado que indica si se está realizando una operación de carga de datos.
     */
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    /**
     * Estado que contiene un mensaje de error en caso de fallo, o null si no hay error.
     */
    val errorMessage: StateFlow<String?> = _errorMessage
    
    /**
     * Estado calculado que representa la calificación promedio de la habitación basada en las reseñas cargadas.
     * Retorna null si no hay reseñas.
     */
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
    
    /**
     * Carga los datos de la habitación desde el repositorio utilizando el [roomId].
     * Actualiza el estado [_room] o [_errorMessage] según el resultado.
     */
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
    
    /**
     * Carga las reseñas asociadas a la habitación desde el repositorio.
     * Gestiona el estado de carga [_isLoading] y actualiza [_reviews] o [_errorMessage].
     */
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
