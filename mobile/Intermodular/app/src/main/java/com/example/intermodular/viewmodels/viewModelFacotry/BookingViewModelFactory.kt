package com.example.intermodular.viewmodels.viewModelFacotry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intermodular.data.repository.BookingRepository
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.viewmodels.BookingViewModel

/**
 * Fábrica de ViewModels para crear instancias de [BookingViewModel] con las dependencias necesarias
 * @author Axel Zaragoci
 *
 * @param bookingRepository - Repositorio de reservas
 * @param roomRepository - Repositorio de habitaciones
 */
class BookingViewModelFactory(
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository
) : ViewModelProvider.Factory {

    /**
     * Crea una instancia de [BookingViewModel]
     * Este método verifica que la clase solicitada sea [BookingViewModel] y si es así, crea una nueva instancia inyectando los repositorios necesarios.
     * Si la clase no es compatible, lanza una excepción.
     *
     * @param modelClass La clase del ViewModel que se desea instanciar
     * @return Una nueva instancia de [T] ([BookingViewModel])
     * @throws IllegalArgumentException Si [modelClass] no es asignable desde [BookingViewModel]
     *
     * @suppress UNCHECKED_CAST La conversión a T es segura porque hemos verificado que modelClass es asignable desde BookingViewModel
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookingViewModel(bookingRepository, roomRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
