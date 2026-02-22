package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.remote.ApiErrorHandler
import com.example.intermodular.data.remote.dto.RegisterDto
import com.example.intermodular.data.repository.RegisterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la lógica de registro de nuevos usuarios.
 *
 * Gestiona:
 * - Validación básica de campos
 * - Creación del DTO de registro
 * - Registro en backend
 * - Inicio automático de sesión tras registro exitoso
 * - Estado de carga
 * - Manejo de errores
 *
 * Utiliza [RegisterRepository] para ejecutar el proceso completo
 * de registro + login.
 *
 * Expone:
 * - [isLoading] → indica si el proceso está en curso
 * - [errorMessage] → mensaje de error en caso de fallo o validación incorrecta
 *
 * @author Ian Rodriguez
 *
 * @param repository - Repositorio encargado del registro
 */
class RegisterViewModel(
    private val repository: RegisterRepository
) : ViewModel() {

    // Estado interno de error
    private val _errorMessage = MutableStateFlow<String?>(null)

    // Flujo público de error
    val errorMessage: StateFlow<String?> = _errorMessage

    // Estado interno de carga
    private val _isLoading = MutableStateFlow(false)

    // Flujo público de carga
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Ejecuta el registro y login automático.
     *
     * Flujo:
     * 1. Activa estado de carga.
     * 2. Limpia errores previos.
     * 3. Llama a repository.registerAndLogin().
     * 4. Captura posibles excepciones y transforma el error.
     * 5. Desactiva estado de carga al finalizar.
     *
     * @param body - DTO con los datos del nuevo usuario
     */
    fun register(body: RegisterDto) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                repository.registerAndLogin(body)
            } catch (t: Throwable) {
                _errorMessage.value =
                    ApiErrorHandler.getErrorMessage(t)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Método de alto nivel llamado desde la UI al pulsar "Registrarse".
     *
     * Responsabilidades:
     * - Limpieza de espacios en blanco.
     * - Conversión segura del teléfono a Long.
     * - Validación básica de campos obligatorios.
     * - Creación del [RegisterDto].
     * - Llamada al método register().
     *
     * Validaciones realizadas:
     * - Campos obligatorios no vacíos.
     * - Teléfono válido si se proporciona.
     *
     * @param firstName - Nombre introducido
     * @param lastName - Apellido introducido
     * @param email - Email introducido
     * @param password - Contraseña introducida
     * @param dni - DNI introducido
     * @param phoneNumber - Teléfono introducido como String
     * @param birthDate - Fecha de nacimiento en formato ISO
     * @param cityName - Ciudad introducida
     * @param gender - Género seleccionado
     */
    fun onRegisterClick(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        dni: String,
        phoneNumber: String,
        birthDate: String,
        cityName: String,
        gender: String
    ) {
        val cleanedFirstName = firstName.trim()
        val cleanedLastName = lastName.trim()
        val cleanedEmail = email.trim()
        val cleanedDni = dni.trim()
        val cleanedBirthDate = birthDate.trim()
        val cleanedCityName = cityName.trim()

        val phone = phoneNumber.trim()
            .takeIf { it.isNotEmpty() }
            ?.toLongOrNull()

        // Validación de campos obligatorios
        if (cleanedFirstName.isEmpty() ||
            cleanedLastName.isEmpty() ||
            cleanedEmail.isEmpty() ||
            password.isEmpty() ||
            cleanedDni.isEmpty() ||
            cleanedBirthDate.isEmpty() ||
            cleanedCityName.isEmpty()
        ) {
            _errorMessage.value = "Rellena todos los campos obligatorios"
            return
        }

        // Validación de teléfono si se ha introducido
        if (phoneNumber.isNotBlank() && phone == null) {
            _errorMessage.value = "Teléfono inválido"
            return
        }

        // Construcción del DTO y ejecución del registro
        register(
            RegisterDto(
                firstName = cleanedFirstName,
                lastName = cleanedLastName,
                email = cleanedEmail,
                password = password,
                dni = cleanedDni,
                phoneNumber = phone,
                birthDate = cleanedBirthDate,
                cityName = cleanedCityName,
                gender = gender
            )
        )
    }
}