package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.auth.SessionManager
import com.example.intermodular.data.remote.dto.LoginDto

/**
 * Repositorio encargado de la autenticación del usuario.
 *
 * Gestiona:
 * - Inicio de sesión
 * - Cierre de sesión
 *
 * Además, se encarga de almacenar y limpiar la sesión
 * mediante [SessionManager].
 *
 * @author Ian Rodriguez
 *
 * @param api - Servicio de API que expone los endpoints de autenticación
 */
class LoginRepository(
    private val api: ApiService
) {

    /**
     * Realiza el inicio de sesión del usuario.
     *
     * Flujo:
     * 1. Envía email y password al endpoint login.
     * 2. Recibe un [LoginDto] con token e id.
     * 3. Guarda la sesión mediante [SessionManager].
     * 4. Devuelve la respuesta obtenida.
     *
     * @param email - Email del usuario
     * @param password - Contraseña del usuario
     * @return [LoginDto] con los datos de autenticación
     */
    suspend fun login(email: String, password: String): LoginDto {
        val response = api.login(
            mapOf(
                "email" to email,
                "password" to password
            )
        )
        SessionManager.setSession(response.token, response.id)
        return response
    }

    /**
     * Cierra la sesión del usuario actual.
     *
     * Elimina el token y los datos de sesión almacenados
     * en [SessionManager].
     */
    fun logout() {
        SessionManager.clearSession()
    }
}