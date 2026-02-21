package com.example.intermodular.data.remote.dto

import com.example.intermodular.models.Review
import com.squareup.moshi.Json

/**
 * Objeto de Transferencia de Datos (DTO) que representa una Reseña (Review) recibida desde la API.
 *
 * @property id Identificador único de la reseña devuelto por MongoDB.
 * @property user Datos básicos del usuario que escribió la reseña ([ReviewUserDto]).
 * @property room ID de la habitación a la que se le hizo la reseña.
 * @property booking Datos de la reserva a la que pertenece esta reseña ([ReviewBookingDto]).
 * @property rating Puntuación del 1 al 5 dada por el usuario.
 * @property description Comentario de texto dejado por el usuario en la reseña.
 * @property createdAt Fecha y hora de creación de la reseña.
 * @property updatedAt Fecha y hora de la última modificación de la reseña.
 */
data class ReviewDto(
    @Json(name = "_id")
    val id: String,
    
    @Json(name = "user")
    val user: ReviewUserDto,
    
    @Json(name = "room")
    val room: String,
    
    @Json(name = "booking")
    val booking: ReviewBookingDto,
    
    @Json(name = "rating")
    val rating: Int,
    
    @Json(name = "description")
    val description: String,
    
    @Json(name = "createdAt")
    val createdAt: String,
    
    @Json(name = "updatedAt")
    val updatedAt: String
)

/**
 * DTO para la información anidada del usuario dentro de una [ReviewDto].
 *
 * @property id Identificador único del usuario.
 * @property firstName Nombre del usuario.
 * @property lastName Apellidos del usuario.
 * @property email Correo electrónico del usuario.
 */
data class ReviewUserDto(
    @Json(name = "_id")
    val id: String,
    
    @Json(name = "firstName")
    val firstName: String?,
    
    @Json(name = "lastName")
    val lastName: String?,
    
    @Json(name = "email")
    val email: String?
)

/**
 * DTO para la información de la reserva a la que se asocia la [ReviewDto].
 *
 * @property id Identificador único de la reserva.
 * @property checkInDate Fecha de entrada (puede no estar presente si la API no la envía).
 * @property checkOutDate Fecha de salida (puede no estar presente).
 */
data class ReviewBookingDto(
    @Json(name = "_id")
    val id: String,
    
    @Json(name = "checkInDate")
    val checkInDate: String?,
    
    @Json(name = "checkOutDate")
    val checkOutDate: String?
)

/**
 * Función de extensión que convierte un [ReviewDto] de la capa de acceso a datos
 * en un modelo de dominio [Review] listo para usarse en ViewModels y la interfaz de usuario.
 *
 * Esta función es la encargada de resolver variables complejas, como unir el `firstName` y `lastName`
 * para generar el `userName`.
 *
 * @return El objeto [Review] mapeado.
 */
fun ReviewDto.toReview(): Review {
    val userName = buildString {
        if (user.firstName != null) append(user.firstName)
        if (user.firstName != null && user.lastName != null) append(" ")
        if (user.lastName != null) append(user.lastName)
    }.ifEmpty { "Usuario" }
    
    return Review(
        id = id,
        userId = user.id,
        userName = userName,
        roomId = room,
        bookingId = booking.id,
        rating = rating,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
