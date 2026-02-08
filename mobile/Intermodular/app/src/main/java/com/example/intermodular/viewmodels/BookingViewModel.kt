package com.example.intermodular.viewmodels

import java.util.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.models.Booking
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class BookingViewModel(
    private val repository: BookingRepository
) : ViewModel() {
    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings: StateFlow<List<Booking>> = _bookings

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadBookings()
    }

    fun loadBookings() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _bookings.value = repository.getBookings()
            }
            catch (e: Exception) {
                _errorMessage.value = e.message
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    private val _selectedStartDate = MutableStateFlow<Long?>(null)
    val selectedStartDate: StateFlow<Long?> = _selectedStartDate
    fun onStartDateSelected(dateMillis: Long?) {
        _selectedStartDate.value = dateMillis
    }

    private val _selectedEndDate = MutableStateFlow<Long?>(null)
    val selectedEndDate : StateFlow<Long?> = _selectedEndDate
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


    private val _maxPrice = MutableStateFlow(0)
    val maxPrice : StateFlow<Int> = _maxPrice
    fun onMaxPriceChanged(value: Int) {
        _maxPrice.value = value
    }


    private val _guests = MutableStateFlow<String>("")
    val guests : StateFlow<String> = _guests
    fun onGuestsChanged(value : String) {
        _guests.value = value
    }

    private val _showFilters = MutableStateFlow(false);
    val showFilters : StateFlow<Boolean> = _showFilters;

    fun changeFilterVisibility() {
        _showFilters.value = !_showFilters.value
    }

    private val _extraBed = MutableStateFlow(false)
    val extraBed : StateFlow<Boolean> = _extraBed

    fun onExtraBedCheckChanged(checked : Boolean) {
        _extraBed.value = checked
    }

    private val _cradle = MutableStateFlow(false)
    val cradle : StateFlow<Boolean> = _cradle

    fun onCradleCheckChanged(checked : Boolean) {
        _cradle.value = checked
    }

    fun filter() {

    }

    fun filterOffer() {

    }
}