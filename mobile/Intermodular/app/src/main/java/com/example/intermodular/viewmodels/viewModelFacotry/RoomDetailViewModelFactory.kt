package com.example.intermodular.viewmodels.viewModelFacotry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intermodular.data.repository.ReviewRepository
import com.example.intermodular.data.repository.RoomRepository
import com.example.intermodular.viewmodels.RoomDetailViewModel

/**
 * Fábrica (Factory) encargada de instanciar [RoomDetailViewModel].
 * 
 * Es necesaria para poder inyectar por constructor tanto el identificador de la habitación
 * seleccionada (`roomId`) como los repositorios necesarios para consultar los datos
 * correspondientes a esa habitación.
 *
 * @property roomId Identificador único de la habitación seleccionada.
 * @property roomRepository Repositorio de habitaciones de donde obtener los detalles.
 * @property reviewRepository Repositorio de reseñas de donde obtener las valoraciones.
 */
class RoomDetailViewModelFactory(
    private val roomId: String,
    private val roomRepository: RoomRepository,
    private val reviewRepository: ReviewRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomDetailViewModel::class.java)) {
            return RoomDetailViewModel(roomId, roomRepository, reviewRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
