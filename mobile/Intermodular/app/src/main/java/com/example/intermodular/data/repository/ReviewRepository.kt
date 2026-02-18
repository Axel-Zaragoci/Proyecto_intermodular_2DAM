package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.auth.SessionManager
import com.example.intermodular.data.remote.dto.CreateReviewDto
import com.example.intermodular.data.remote.dto.toReview
import com.example.intermodular.models.Booking
import com.example.intermodular.models.Review

/**
 * Repositorio encargado de gestionar las operaciones relacionadas con las reseñas
 *
 * @param apiService - Servicio de API para realizar las peticiones HTTP
 */
class ReviewRepository(
    private val apiService: ApiService
) {
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
