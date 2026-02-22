package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.dto.RegisterDto

/**
 * Repositorio encargado del registro de nuevos usuarios.
 *
 * Permite:
 * - Registrar un usuario en el backend.
 * - Iniciar sesión automáticamente tras un registro exitoso.
 *
 * Combina el uso de [ApiService] para el registro
 * y [LoginRepository] para iniciar sesión tras crear la cuenta.
 *
 * @author Ian Rodriguez
 *
 * @param api - Servicio de API que expone el endpoint de registro
 * @param loginRepository - Repositorio utilizado para iniciar sesión tras el registro
 */
class RegisterRepository(
    private val api: ApiService,
    private val loginRepository: LoginRepository
) {

    /**
     * Registra un nuevo usuario y, si el proceso es exitoso,
     * inicia sesión automáticamente.
     *
     * Flujo:
     * 1. Llama a api.register(body).
     * 2. Si no hay error, ejecuta loginRepository.login().
     * 3. La sesión queda almacenada mediante SessionManager
     *    dentro del proceso de login.
     *
     * @param body - DTO con los datos del nuevo usuario
     */
    suspend fun registerAndLogin(body: RegisterDto) {
        api.register(body)
        loginRepository.login(body.email, body.password)
    }
}