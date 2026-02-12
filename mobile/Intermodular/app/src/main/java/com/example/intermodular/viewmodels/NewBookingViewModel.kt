package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.data.repository.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _guests = MutableStateFlow<String>(guests)
    val Guests : StateFlow<String> = _guests

    private val _startDate = MutableStateFlow<Long>(startDate)
    val StartDate: StateFlow<Long?> = _startDate

    private val _endDate = MutableStateFlow<Long>(endDate)
    val EndDate : StateFlow<Long?> = _endDate

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