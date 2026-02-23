package com.example.intermodular.data.remote.dto

/**
 * DTO utilizado para registrar un nuevo usuario en el sistema.
 *
 * Este modelo se envía al backend durante el proceso de registro
 * y contiene toda la información necesaria para crear una nueva cuenta.
 *
 * La fecha de nacimiento debe enviarse en formato ISO (yyyy-MM-dd).
 *
 * Pertenece exclusivamente a la capa remota y no debe utilizarse
 * directamente en la UI.
 *
 * @author Ian Rodriguez
 *
 * @param firstName - Nombre del usuario
 * @param lastName - Apellido del usuario
 * @param email - Email del usuario (utilizado como credencial)
 * @param password - Contraseña del usuario
 * @param dni - Documento nacional de identidad
 * @param phoneNumber - Número de teléfono (nullable)
 * @param birthDate - Fecha de nacimiento en formato ISO_LOCAL_DATE
 * @param cityName - Ciudad del usuario
 * @param gender - Género del usuario
 */
class RegisterDto(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val dni: String,
    val phoneNumber: Long?,
    val birthDate: String,
    val cityName: String,
    val gender: String
)