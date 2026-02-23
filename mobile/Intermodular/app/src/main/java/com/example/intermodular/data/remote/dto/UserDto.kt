package com.example.intermodular.data.remote.dto

import java.time.Instant

/**
 * Data Transfer Object (DTO) que representa al usuario recibido desde la API.
 *
 * Este modelo refleja exactamente la estructura enviada por el backend
 * y se utiliza exclusivamente en la capa de datos remota.
 *
 * Posteriormente, se transforma a un modelo de dominio [UserModel]
 * mediante el mapper correspondiente.
 *
 * Notas:
 * - Algunos campos son nullable porque pueden no estar presentes
 *   en la respuesta del backend.
 * - La fecha de nacimiento se recibe como [Instant].
 *
 * @author Ian Rodriguez
 *
 * @param firstName - Nombre del usuario
 * @param lastName - Apellido del usuario
 * @param email - Email del usuario
 * @param dni - Documento nacional de identidad
 * @param phoneNumber - Número de teléfono
 * @param birthDate - Fecha de nacimiento en formato Instant
 * @param cityName - Ciudad del usuario
 * @param gender - Género del usuario
 * @param imageRoute - Ruta o URL de la imagen de perfil
 * @param vipStatus - Indica si el usuario tiene estado VIP
 */
data class UserDto(
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val dni: String?,
    val phoneNumber: Long?,
    val birthDate: Instant,
    val cityName: String?,
    val gender: String?,
    val imageRoute: String?,
    val vipStatus: Boolean?
)