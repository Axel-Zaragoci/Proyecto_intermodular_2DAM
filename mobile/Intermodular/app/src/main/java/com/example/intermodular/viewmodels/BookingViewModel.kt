package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.remote.ApiErrorHandler
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

/**
 * ViewModel para la vista que permite filtrar habitaciones para reservar
 * @author Axel Zaragoci
 *
 * @property bookingRepository - Repositorio para obtener datos de reservas de la API
 * @property roomRepository - Repositorio para obtener datos de habitaciones de la API
 */
class BookingViewModel(
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    // ==================== ESTADOS DE LA UI ====================
    /**
     * Lista completa de habitaciones sacadas del repositorio
     */
    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms

    /**
     * Lista de habitaciones filtradas por características de la habitación y por disponibilidad
     */
    private val _filteredRooms = MutableStateFlow<List<Room>>(emptyList())
    val filteredRooms: StateFlow<List<Room>> = _filteredRooms

    /**
     * Lista mutable de reservas para cálculos de disponibilidad
     */
    private var _bookings : MutableList<Booking> = mutableListOf()

    /**
     * Filtro actual para la búsqueda de habitaciones
     */
    private val _currentFilter = MutableStateFlow(RoomFilter())

    /**
     * Mensaje de error a mostrar al usuario
     */
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * Indicador de carga para funciones asíncronas
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Controlador de visibilidad del componente de filtros
     */
    private val _showFilters = MutableStateFlow(true)
    val showFilters : StateFlow<Boolean> = _showFilters


    // ==================== FILTROS ====================
    /**
     * Fecha de inicio seleccionada en milisegundos
     */
    private val _selectedStartDate = MutableStateFlow<Long?>(null)
    val selectedStartDate: StateFlow<Long?> = _selectedStartDate

    /**
     * Fecha de fin seleccionada en milisegundos
     */
    private val _selectedEndDate = MutableStateFlow<Long?>(null)
    val selectedEndDate : StateFlow<Long?> = _selectedEndDate

    /**
     * Precio máximo seleccionado
     */
    private val _maxPrice = MutableStateFlow(1000)
    val maxPrice : StateFlow<Int> = _maxPrice

    /**
     * Número de huéspedes guardado como String
     */
    private val _guests = MutableStateFlow("")
    val guests : StateFlow<String> = _guests

    /**
     * Filtro de cama extra
     */
    private val _extraBed = MutableStateFlow(false)
    val extraBed : StateFlow<Boolean> = _extraBed

    /**
     * Filtro de cuna
     */
    private val _cradle = MutableStateFlow(false)
    val cradle : StateFlow<Boolean> = _cradle


    // ==================== MÉTODOS PÚBLICOS ====================

    /**
     * Carga de datos filtrados
     *
     * Flujo principal:
     * 1. Obtiene las habitaciones filtradas por características y todas las reservas
     * 2. Valida los datos de entrada del usuario
     * 3. Filtra por disponibilidad
     * 4. Si el filtrado es exitoso, oculta el componente de filtros
     *
     * En caso de error, muestra el error correspondiente según [ApiErrorHandler]
     */
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
                _errorMessage.value = ApiErrorHandler.getErrorMessage(e)
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Actualiza la fecha de inicio seleccionada
     *
     * @param dateMillis - Milisegundos de la fecha seleccionada
     */
    fun onStartDateSelected(dateMillis: Long?) {
        _selectedStartDate.value = dateMillis
    }

    /**
     * Actualiza la fecha de fin seleccionada
     *
     * @param dateMillis - Milisegundos de la fecha seleccionada
     */
    fun onEndDateSelected(dateMillis: Long?) {
        _selectedEndDate.value = dateMillis
    }

    /**
     * Convierte la fecha de inicio seleccionada a [LocalDate]
     */
    fun selectedStartDateAsLocalDate(): LocalDate? =
        selectedStartDate.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

    /**
     * Convierte la fecha de fin seleccionada a [LocalDate]
     */
    fun selectedEndDateAsLocalDate(): LocalDate? =
        selectedEndDate.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

    /**
     * Actualiza el precio máximo seleccionado
     *
     * @param value - Precio máximo seleccionado
     */
    fun onMaxPriceChanged(value: Int) {
        _maxPrice.value = value
    }

    /**
     * Actualiza la cantidad de huéspedes
     *
     * @param value - Cantidad de huéspedes seleccionada
     */
    fun onGuestsChanged(value : String) {
        _guests.value = value
    }

    /**
     * Cambia la visibilidad del componente de filtros
     */
    fun changeFilterVisibility() {
        _showFilters.value = !_showFilters.value
    }

    /**
     * Actualiza el filtro de cama extra
     *
     * @param checked - `true` si se requiere cama extra, `false` en caso contrario
     */
    fun onExtraBedCheckChanged(checked : Boolean) {
        _extraBed.value = checked
    }

    /**
     * Actualiza el filtro de cuna
     *
     * @param checked - `true` si se requiere cuna, `false` en caso contrario
     */
    fun onCradleCheckChanged(checked : Boolean) {
        _cradle.value = checked
    }

    /**
     * Verifica si una habitación está disponible en un rango de fechas
     *
     * Una habitación está disponible si no tiene reserva en estado "Abierta" con la que solapen fechas
     *
     * @param room - Habitación a verificar
     * @param startDate - Fecha de inicio solicitada
     * @param endDate - Fecha de fin solicitada
     * @return [Boolean] - `true` si está disponible, `false` en caso contrario
     */
    private fun isRoomAvailable(
        room: Room,
        startDate: LocalDate,
        endDate: LocalDate
    ): Boolean {

        val roomBookings = _bookings.filter { it.roomId == room.id }

        return roomBookings.filter { it.status == "Abierta" }.none { booking ->
            booking.checkInDate < endDate &&
                    booking.checkOutDate > startDate
        }
    }

    /**
     * Aplica los filtros seleccionados y carga los datos
     * Crea un nuevo [RoomFilter] con los valores actuales seleccionados por el usuario
     */
    fun filter(){
        _currentFilter.value = RoomFilter(
            maxPrice = _maxPrice.value.toDouble(),
            guests = _guests.value.toIntOrNull(),
            hasExtraBed = _extraBed.value,
            hasCrib = _cradle.value,
        )

        loadData()
    }

    /**
     * Aplica los filtros seleccionados incluyendo ofertas y carga los datos
     * Mismo funcionamiento que [filter], pero añade el filtro de oferta
     */
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