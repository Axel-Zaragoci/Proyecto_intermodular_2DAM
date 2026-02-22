package com.example.intermodular.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermodular.data.remote.ApiErrorHandler
import com.example.intermodular.data.remote.auth.SessionManager
import com.example.intermodular.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.intermodular.models.UserModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

data class UserUiState(
    val isLoading: Boolean = false,
    val user: UserModel? = null
)

class UserViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            runCatching {
                repository.getMe()
            }.onSuccess { user ->
                _uiState.value = UserUiState(
                    isLoading = false,
                    user = user
                )
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(isLoading = false)
                _errorMessage.value =
                    ApiErrorHandler.getErrorMessage(throwable)
            }
        }
    }

    fun updatePhoto(photoPart: MultipartBody.Part) {
        val currentUser = _uiState.value.user ?: run {
            _errorMessage.value = "No hay datos del usuario cargados"
            return
        }

        viewModelScope.launch {
            runCatching {
                repository.updateProfilePhoto(currentUser, photoPart)
            }.onSuccess { updatedUser ->
                _uiState.value = _uiState.value.copy(user = updatedUser)
            }.onFailure { throwable ->
                _errorMessage.value = ApiErrorHandler.getErrorMessage(throwable)
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}