package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.remote.ApiErrorHandler
import com.example.intermodular.data.remote.dto.RegisterDto
import com.example.intermodular.data.repository.RegisterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: RegisterRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun register(body: RegisterDto) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                repository.registerAndLogin(body)
            } catch (t: Throwable) {
                _errorMessage.value = ApiErrorHandler.getErrorMessage(t)
            } finally {
                _isLoading.value = false
            }
        }
    }

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

        if (phoneNumber.isNotBlank() && phone == null) {
            _errorMessage.value = "Teléfono inválido"
            return
        }

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