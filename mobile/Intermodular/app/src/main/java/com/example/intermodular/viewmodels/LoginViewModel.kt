package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.remote.ApiErrorHandler
import com.example.intermodular.data.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la lógica de autenticación del usuario.
 *
 * Gestiona:
 * - Inicio de sesión
 * - Cierre de sesión
 * - Estado de carga
 * - Mensajes de error
 *
 * Utiliza [LoginRepository] para realizar las operaciones
 * relacionadas con la autenticación.
 *
 * Expone:
 * - [isLoading] → indica si el proceso de login está en curso
 * - [errorMessage] → mensaje de error en caso de fallo
 *
 * @author Ian Rodriguez
 *
 * @param repository - Repositorio que gestiona la autenticación
 */
class LoginViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    // Estado interno para mensajes de error
    private val _errorMessage = MutableStateFlow<String?>(null)

    // Flujo público de errores
    val errorMessage: StateFlow<String?> = _errorMessage

    // Estado interno de carga
    private val _isLoading = MutableStateFlow(false)

    // Flujo público de carga
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Ejecuta el proceso de inicio de sesión.
     *
     * Flujo:
     * 1. Activa el estado de carga.
     * 2. Limpia errores previos.
     * 3. Llama a repository.login().
     * 4. Si ocurre un error, lo transforma mediante [ApiErrorHandler].
     * 5. Desactiva el estado de carga al finalizar.
     *
     * @param email - Email del usuario
     * @param password - Contraseña del usuario
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                repository.login(email, password)
            } catch (t: Throwable) {
                _errorMessage.value =
                    ApiErrorHandler.getErrorMessage(t)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     *
     * Delegado al [LoginRepository], que limpia la sesión
     * almacenada en el SessionManager.
     */
    fun logout() {
        repository.logout()
    }
}