package com.example.intermodular.viewmodels.viewModelFacotry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.viewmodels.RoomViewModel

/**
 * Fábrica (Factory) encargada de instanciar [RoomViewModel].
 * 
 * Es obligatoria en Jetpack Compose cuando el ViewModel necesita recibir argumentos 
 * por constructor (en este caso, el [RoomRepository]). 
 * Asegura que se pase el mismo repositorio cuando se recrea el ViewModel.
 *
 * @property repository La instancia de [RoomRepository] que se inyectará en el ViewModel.
 */
class RoomViewModelFactory(
    private val repository: RoomRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
            return RoomViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}