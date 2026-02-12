package com.example.intermodular.viewmodels

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.models.Room
import com.example.intermodular.models.RoomFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class NewBookingViewModel(
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository,
    private val roomId : String,
    private val startDate : Long,
    private val endDate : Long,
    private val guests : String
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _guests = MutableStateFlow<String>(guests)
    val Guests : StateFlow<String> = _guests

    private val _startDate = MutableStateFlow<Long>(startDate)
    val StartDate: StateFlow<Long?> = _startDate

    private val _endDate = MutableStateFlow<Long>(endDate)
    val EndDate : StateFlow<Long?> = _endDate

    private val _room = MutableStateFlow<Room?>(null)
    val Room : StateFlow<Room?> = _room

    init {
        loadRoom()
    }

    fun loadRoom() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val rooms = roomRepository.getRooms(RoomFilter())
                _room.value = rooms.first{ it.id == roomId }
            }
            catch (e: Exception) {
                _errorMessage.value = e.message
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun startDateAsLocalDate(): LocalDate? =
        StartDate.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

    fun endDateAsLocalDate(): LocalDate? =
        EndDate.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
}