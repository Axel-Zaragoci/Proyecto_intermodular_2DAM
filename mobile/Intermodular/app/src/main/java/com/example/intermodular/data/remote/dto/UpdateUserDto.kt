package com.example.intermodular.data.remote.dto

/**
 * DTO que representa la respuesta del backend al actualizar un usuario.
 *
 * Contiene:
 * - Un mensaje informativo (normalmente confirmación de éxito).
 * - El objeto [UserDto] actualizado.
 *
 * Este modelo pertenece exclusivamente a la capa remota y
 * posteriormente el usuario debe transformarse al modelo de dominio.
 *
 * @author Ian Rodriguez
 *
 * @param message - Mensaje devuelto por el backend
 * @param user - Usuario actualizado en formato DTO
 */
data class UpdateUserResponseDto(
    val message: String,
    val user: UserDto
)

/**
 * DTO utilizado para enviar al backend los datos actualizados del usuario.
 *
 * Se emplea tanto para:
 * - Actualización de datos personales
 * - Actualización indirecta tras cambiar la foto de perfil
 *
 * La fecha de nacimiento se envía en formato String ISO (yyyy-MM-dd).
 *
 * @author Ian Rodriguez
 *
 * @param id - ID del usuario (puede obtenerse desde SessionManager)
 * @param firstName - Nombre del usuario
 * @param lastName - Apellido del usuario
 * @param email - Email actualizado
 * @param dni - Documento nacional de identidad
 * @param phoneNumber - Número de teléfono (nullable)
 * @param birthDate - Fecha de nacimiento en formato ISO_LOCAL_DATE
 * @param cityName - Ciudad del usuario
 * @param gender - Género del usuario
 * @param imageRoute - Ruta de la imagen de perfil (nullable)
 */
data class UpdateUserRequestDto(
    val id: String?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val dni: String,
    val phoneNumber: Long?,
    val birthDate: String,
    val cityName: String,
    val gender: String,
    val imageRoute: String?
)