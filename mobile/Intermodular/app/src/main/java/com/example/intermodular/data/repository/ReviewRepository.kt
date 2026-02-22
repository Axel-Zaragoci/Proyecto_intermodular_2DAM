package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.auth.SessionManager
import com.example.intermodular.data.remote.dto.CreateReviewDto
import com.example.intermodular.data.remote.dto.toReview
import com.example.intermodular.models.Booking
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

    /**
     * Crea una nueva reseña para una reserva.
     *
     * @author Axel Zaragoci
     *
     * @param roomId - Identificador de la habitación reservada
     * @param bookingId - Identificador de la reserva a reseñar
     * @param rating - Calificación de reseña
     * @param description - Descripción de la reseña
     * @return [Review] - Objeto del dominio con la información de la reseña creada
     * @throws IllegalStateException - Si el usuario no está autenticado
     * @throws retrofit2.HttpException - Si hay un error en la petición HTTP
     * @throws IOException - Si hay un error de red
     */
    suspend fun createReview(
        roomId: String,
        bookingId: String,
        rating: Int,
        description: String
    ): Review {
        val userId = SessionManager.getUserId()
            ?: throw IllegalStateException("Usuario no autenticado")

        val dto = CreateReviewDto(
            user = userId,
            room = roomId,
            booking = bookingId,
            rating = rating,
            description = description
        )

        return apiService.createReview(dto).toReview()
    }
}
