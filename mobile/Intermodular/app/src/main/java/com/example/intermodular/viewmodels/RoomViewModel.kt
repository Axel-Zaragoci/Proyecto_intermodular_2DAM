package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.models.Room
import com.example.intermodular.models.RoomFilter
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

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _filteredRooms = MutableStateFlow<List<Room>>(emptyList())
    val filteredRooms: StateFlow<List<Room>> = _filteredRooms

    private val _currentFilter = MutableStateFlow(RoomFilter())
    val currentFilter: StateFlow<RoomFilter> = _currentFilter

    init {
        loadRooms()
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        
        // Apply client-side filtering on the currently loaded rooms
        val currentRooms = _rooms.value
        if (text.isBlank()) {
            _filteredRooms.value = currentRooms
        } else {
             _filteredRooms.value = currentRooms.filter { room ->
                room.roomNumber.contains(text, ignoreCase = true) ||
                        room.type.contains(text, ignoreCase = true) ||
                        room.description.contains(text, ignoreCase = true)
            }
        }
    }

    fun updateFilter(filter: RoomFilter) {
        _currentFilter.value = filter
        loadRooms()
    }

    fun clearFilters() {
        _currentFilter.value = RoomFilter()
        loadRooms()
    }

    fun loadRooms() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val rooms = repository.getRooms(_currentFilter.value)
                _rooms.value = rooms
                // Apply local search text filtering on the new result set
                if (_searchText.value.isNotBlank()) {
                     val query = _searchText.value
                     _filteredRooms.value = rooms.filter { room ->
                        room.roomNumber.contains(query, ignoreCase = true) ||
                                room.type.contains(query, ignoreCase = true) ||
                                room.description.contains(query, ignoreCase = true)
                    }
                } else {
                    _filteredRooms.value = rooms
                }
            }
            catch (e: Exception) {
                _errorMessage.value = "Error loading rooms: ${e.message}"
            }
            finally {
                _isLoading.value = false
            }
        }
    }
}
