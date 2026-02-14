package com.example.intermodular.viewmodels.viewModelFacotry

import com.example.intermodular.viewmodels.MyBookingsViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.viewmodels.BookingViewModel

/**
 * Fábrica de ViewModels para crear instancias de [MyBookingsViewModel] con las dependencias necesarias
 * @author Axel Zaragoci
 *
 * @param bookingRepository - Repositorio de reservas
 * @param roomRepository - Repositorio de habitaciones
 */
class MyBookingsViewModelFactory(
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository
) : ViewModelProvider.Factory {

    /**
     * Crea una instancia de [MyBookingsViewModel]
     * Este método verifica que la clase solicitada sea [MyBookingsViewModel] y si es así, crea una nueva instancia inyectando los repositorios necesarios.
     * Si la clase no es compatible, lanza una excepción.
     *
     * @param modelClass La clase del ViewModel que se desea instanciar
     * @return Una nueva instancia de [T] ([MyBookingsViewModel])
     * @throws IllegalArgumentException Si [modelClass] no es asignable desde [MyBookingsViewModel]
     *
     * @suppress UNCHECKED_CAST La conversión a T es segura porque hemos verificado que modelClass es asignable desde BookingViewModel
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyBookingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyBookingsViewModel(bookingRepository, roomRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}