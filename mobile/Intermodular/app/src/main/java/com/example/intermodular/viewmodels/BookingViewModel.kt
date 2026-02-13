package com.example.intermodular.viewmodels

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.models.Booking
import com.example.intermodular.models.Room
import com.example.intermodular.models.RoomFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class BookingViewModel(
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {
    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms

    private val _filteredRooms = MutableStateFlow<List<Room>>(emptyList())
    val filteredRooms: StateFlow<List<Room>> = _filteredRooms

    private var _bookings : MutableList<Booking> = mutableListOf()

    private val _currentFilter = MutableStateFlow(RoomFilter())
    val currentFilter: StateFlow<RoomFilter> = _currentFilter

    private val _selectedStartDate = MutableStateFlow<Long?>(null)
    val selectedStartDate: StateFlow<Long?> = _selectedStartDate

    private val _selectedEndDate = MutableStateFlow<Long?>(null)
    val selectedEndDate : StateFlow<Long?> = _selectedEndDate

    private val _maxPrice = MutableStateFlow(1000)
    val maxPrice : StateFlow<Int> = _maxPrice

    private val _guests = MutableStateFlow<String>("")
    val guests : StateFlow<String> = _guests

    private val _extraBed = MutableStateFlow(false)
    val extraBed : StateFlow<Boolean> = _extraBed


    private val _showFilters = MutableStateFlow(true);
    val showFilters : StateFlow<Boolean> = _showFilters;

    private val _cradle = MutableStateFlow(false)
    val cradle : StateFlow<Boolean> = _cradle



    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading





    fun loadData() {
        viewModelScope.launch {
            _errorMessage.value = ""
            _isLoading.value = true
            try {
                val rooms = roomRepository.getRooms(_currentFilter.value)
                _rooms.value = rooms

                val bookings = bookingRepository.getBookings()
                _bookings = bookings.toMutableList()

                val startDate = selectedStartDateAsLocalDate()
                val endDate = selectedEndDateAsLocalDate()

                if (startDate == null || endDate == null) {
                    _errorMessage.value = "Las fechas son obligatorias"
                    return@launch
                }
                if (_guests.value.isEmpty()) {
                    _errorMessage.value = "Las cantidad de huéspedes es obligatoria"
                    return@launch
                }


                if (!startDate.isBefore(endDate)) {
                    _errorMessage.value = "La fecha de salida debe ser posterior a la de entrada"
                    return@launch
                }

                _filteredRooms.value = rooms.filter { room ->
                    isRoomAvailable(room, selectedStartDateAsLocalDate()!!, selectedEndDateAsLocalDate()!!)
                }

                _showFilters.value = false
            }
            catch (e: Exception) {
                _errorMessage.value = e.message
            }
            finally {
                _isLoading.value = false
            }
        }
    }


    fun onStartDateSelected(dateMillis: Long?) {
        _selectedStartDate.value = dateMillis
    }

    fun onEndDateSelected(dateMillis: Long?) {
        _selectedEndDate.value = dateMillis
    }

    fun selectedStartDateAsLocalDate(): LocalDate? =
        selectedStartDate.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

    fun selectedEndDateAsLocalDate(): LocalDate? =
        selectedEndDate.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

    fun onMaxPriceChanged(value: Int) {
        _maxPrice.value = value
    }

    fun onGuestsChanged(value : String) {
        _guests.value = value
    }

    fun changeFilterVisibility() {
        _showFilters.value = !_showFilters.value
    }

    fun onExtraBedCheckChanged(checked : Boolean) {
        _extraBed.value = checked
    }

    fun onCradleCheckChanged(checked : Boolean) {
        _cradle.value = checked
    }

    private fun isRoomAvailable(
        room: Room,
        userStart: LocalDate,
        userEnd: LocalDate
    ): Boolean {

        val roomBookings = _bookings.filter { it.roomId == room.id }

        return roomBookings.filter { it.status == "Abierta" }.none { booking ->
            booking.checkInDate < userEnd &&
                    booking.checkOutDate > userStart
        }
    }

    fun filter(){
        _currentFilter.value = RoomFilter(
            maxPrice = _maxPrice.value.toDouble(),
            guests = _guests.value.toIntOrNull(),
            hasExtraBed = _extraBed.value,
            hasCrib = _cradle.value,
        )

        loadData()
    }

    fun filterOffer() {
        _currentFilter.value = RoomFilter(
            maxPrice = _maxPrice.value.toDouble(),
            guests = _guests.value.toIntOrNull(),
            hasExtraBed = _extraBed.value,
            hasCrib = _cradle.value,
            hasOffer = true
        )

        loadData()
    }
}