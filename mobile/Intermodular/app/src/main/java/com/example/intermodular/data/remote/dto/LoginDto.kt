package com.example.intermodular.data.remote.dto

/**
 * DTO que representa la respuesta del backend tras una autenticación exitosa.
 *
 * Contiene la información necesaria para gestionar la sesión del usuario:
 * - Token JWT o token de acceso
 * - Rol del usuario
 * - ID del usuario autenticado
 *
 * Este modelo pertenece a la capa remota y normalmente
 * sus valores se almacenan mediante el SessionManager
 * para mantener la sesión activa.
 *
 * @author Ian Rodriguez
 *
 * @param token - Token de autenticación emitido por el servidor
 * @param rol - Rol del usuario (ej: ADMIN, USER, etc.)
 * @param id - Identificador único del usuario
 */
data class LoginDto(
    val token: String,
    val rol: String,
    val id: String
)