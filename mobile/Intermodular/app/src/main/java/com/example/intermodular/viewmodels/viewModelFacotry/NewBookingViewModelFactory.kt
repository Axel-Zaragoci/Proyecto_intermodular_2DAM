package com.example.intermodular.viewmodels.viewModelFacotry

import com.example.intermodular.viewmodels.NewBookingViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.intermodular.data.repository.BookingRepository;
import com.example.intermodular.data.repository.RoomRepository;
import com.example.intermodular.viewmodels.BookingViewModel

/**
 * Fábrica de ViewModels para crear instancias de [NewBookingViewModel] con las dependencias necesarias
 * @author Axel Zaragoci
 *
 * @param bookingRepository - Repositorio de reservas
 * @param roomRepository - Repositorio de habitaciones
 * @param roomId - ID de la habitación a reservar
 * @param startDate - Fecha de inicio de reserva en milisegundos
 * @param endDate - Fecha de fin de reserva en milisegundos
 * @param guests - Cantidad de huéspedes como String
 */
class NewBookingViewModelFactory (
    private val bookingRepository:BookingRepository,
    private val roomRepository:RoomRepository,
    private val roomId : String,
    private val startDate : Long,
    private val endDate : Long,
    private val guests : String
) : ViewModelProvider.Factory {

    /**
     * Crea una instancia de [NewBookingViewModel]
     * Este método verifica que la clase solicitada sea [NewBookingViewModel] y si es así, crea una nueva instancia inyectando los repositorios necesarios.
     * Si la clase no es compatible, lanza una excepción.
     *
     * @param modelClass La clase del ViewModel que se desea instanciar
     * @return Una nueva instancia de [T] ([NewBookingViewModel])
     * @throws IllegalArgumentException Si [modelClass] no es asignable desde [NewBookingViewModel]
     *
     * @suppress UNCHECKED_CAST La conversión a T es segura porque hemos verificado que modelClass es asignable desde BookingViewModel
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewBookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewBookingViewModel(bookingRepository, roomRepository, roomId, startDate, endDate, guests) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}