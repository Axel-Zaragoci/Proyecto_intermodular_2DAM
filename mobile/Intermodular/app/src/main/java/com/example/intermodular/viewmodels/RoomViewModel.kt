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

    private val _filteredRooms = MutableStateFlow<List<Room>>(emptyList())
    val filteredRooms: StateFlow<List<Room>> = _filteredRooms

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _showFilters = MutableStateFlow(true)
    val showFilters: StateFlow<Boolean> = _showFilters

    private val _type = MutableStateFlow("")
    val type: StateFlow<String> = _type

    private val _minPrice = MutableStateFlow(0)
    val minPrice: StateFlow<Int> = _minPrice

    private val _maxPrice = MutableStateFlow(500)
    val maxPrice: StateFlow<Int> = _maxPrice

    private val _guests = MutableStateFlow("")
    val guests: StateFlow<String> = _guests

    private val _isAvailable = MutableStateFlow(false)
    val isAvailable: StateFlow<Boolean> = _isAvailable

    private val _hasExtraBed = MutableStateFlow(false)
    val hasExtraBed: StateFlow<Boolean> = _hasExtraBed

    private val _hasCrib = MutableStateFlow(false)
    val hasCrib: StateFlow<Boolean> = _hasCrib

    private val _hasOffer = MutableStateFlow(false)
    val hasOffer: StateFlow<Boolean> = _hasOffer

    private val _sortBy = MutableStateFlow("roomNumber")
    val sortBy: StateFlow<String> = _sortBy

    private val _sortOrder = MutableStateFlow("asc")
    val sortOrder: StateFlow<String> = _sortOrder

    private val _currentFilter = MutableStateFlow(RoomFilter())

    init {
        loadRooms()
    }

    fun changeFilterVisibility() {
        _showFilters.value = !_showFilters.value
    }

    fun onTypeChanged(value: String) {
        _type.value = value
    }

    fun onMinPriceChanged(value: Int) {
        _minPrice.value = value
    }

    fun onMaxPriceChanged(value: Int) {
        _maxPrice.value = value
    }

    fun onGuestsChanged(value: String) {
        _guests.value = value
    }

    fun onIsAvailableChanged(value: Boolean) {
        _isAvailable.value = value
    }

    fun onExtraBedChanged(value: Boolean) {
        _hasExtraBed.value = value
    }

    fun onCribChanged(value: Boolean) {
        _hasCrib.value = value
    }

    fun onOfferChanged(value: Boolean) {
        _hasOffer.value = value
    }

    fun onSortByChanged(value: String) {
        _sortBy.value = value
    }

    fun onSortOrderChanged(value: String) {
        _sortOrder.value = value
    }

    fun filter() {
        _currentFilter.value = RoomFilter(
            type = _type.value.ifBlank { null },
            isAvailable = if (_isAvailable.value) true else null,
            minPrice = _minPrice.value.toDouble(),
            maxPrice = _maxPrice.value.toDouble(),
            guests = _guests.value.toIntOrNull(),
            hasExtraBed = if (_hasExtraBed.value) true else null,
            hasCrib = if (_hasCrib.value) true else null,
            hasOffer = if (_hasOffer.value) true else null,
            sortBy = _sortBy.value,
            sortOrder = _sortOrder.value
        )
        _showFilters.value = false
        loadRooms()
    }

    fun clearFilters() {
        _type.value = ""
        _minPrice.value = 0
        _maxPrice.value = 500
        _guests.value = ""
        _isAvailable.value = false
        _hasExtraBed.value = false
        _hasCrib.value = false
        _hasOffer.value = false
        _sortBy.value = "roomNumber"
        _sortOrder.value = "asc"
        
        _currentFilter.value = RoomFilter()
        _showFilters.value = false
        loadRooms()
    }

    fun loadRooms() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val rooms = repository.getRooms(_currentFilter.value)
                _rooms.value = rooms
                _filteredRooms.value = rooms
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
