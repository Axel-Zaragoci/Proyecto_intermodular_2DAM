package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.dto.toReview
import com.example.intermodular.models.Review

/**
 * Repositorio encargado de gestionar la lógica de acceso a datos para las Reseñas ([Review]).
 *
 * Actúa como única fuente de verdad para obtener las valoraciones, comunicándose con la 
 * API remota mediante Retrofit.
 *
 * @property apiService El servicio de red configurado para realizar las peticiones al backend.
 */
class ReviewRepository(
    private val apiService: ApiService
) {
    /**
     * Recupera todas las reseñas asociadas de forma asíncrona a un identificador de habitación específico.
     *
     * Transforma automáticamente la respuesta DTO en modelos de dominio [Review] limpios.
     *
     * @param roomId El identificador único (`_id`) de la habitación de la que se quieren obtener las reseñas.
     * @return Una lista inmutable de valoraciones ([Review]) publicadas para esa habitación.
     * @throws Exception Si la llamada de red falla o hay un problema de parseo.
     */
    suspend fun getReviewsByRoom(roomId: String): List<Review> {
        return apiService.getReviewsByRoom(roomId).map { it.toReview() }
    }
}
