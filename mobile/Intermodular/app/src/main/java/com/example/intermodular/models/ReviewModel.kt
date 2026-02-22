package com.example.intermodular.models

/**
 * Representa una reseña realizada por un usuario sobre una habitación.
 *
 * @property id Identificador único de la reseña.
 * @property userId Identificador del usuario que realizó la reseña.
 * @property userName Nombre del usuario que realizó la reseña.
 * @property roomId Identificador de la habitación reseñada.
 * @property bookingId Identificador de la reserva asociada a la reseña.
 * @property rating Calificación numérica otorgada (ej. de 1 a 5).
 * @property description Texto con el comentario o detalle de la reseña.
 * @property createdAt Fecha de creación de la reseña en formato ISO 8601.
 * @property updatedAt Fecha de última actualización de la reseña en formato ISO 8601.
 */
data class Review(
    val id: String,
    val userId: String,
    val userName: String,
    val roomId: String,
    val bookingId: String,
    val rating: Int,
    val description: String,
    val createdAt: String,
    val updatedAt: String
)
