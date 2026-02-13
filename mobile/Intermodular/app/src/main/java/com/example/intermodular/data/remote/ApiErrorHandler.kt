package com.example.intermodular.data.remote

import org.json.JSONObject
import retrofit2.HttpException

object ApiErrorHandler {
    fun getErrorMessage(throwable: Throwable) : String {
        return when (throwable) {
            is HttpException -> handleHttpException(throwable)
            is java.net.ConnectException -> "Error de conexión: No se pudo conectar al servidor"
            is java.net.SocketTimeoutException -> "Tiempo de espera agotado"
            else -> throwable.message ?: "Error desconocido"
        }
    }

    private fun handleHttpException(e: HttpException): String {
        return try {
            val errorBody = e.response()?.errorBody()?.string()
            if (!errorBody.isNullOrBlank()) {
                parseApiError(errorBody, e.code())
            } else {
                getDefaultMessageForCode(e.code())
            }
        } catch (jsonException: Exception) {
            getDefaultMessageForCode(e.code())
        }
    }

    private fun parseApiError(errorBody: String, httpCode: Int): String {
        return try {
            val jsonObject = JSONObject(errorBody)

            if (jsonObject.has("error")) {
                jsonObject.getString("error")
            }
            else {
                getDefaultMessageForCode(httpCode)
            }
        } catch (e: Exception) {
            errorBody.takeIf { it.isNotBlank() } ?: getDefaultMessageForCode(httpCode)
        }
    }

    private fun getDefaultMessageForCode(code: Int): String {
        return when (code) {
            400 -> "Datos incorrectos"
            401 -> "No autorizado. Por favor, inicia sesión nuevamente"
            403 -> "Acceso prohibido"
            404 -> "Recurso no encontrado"
            500 -> "Error interno del servidor"
            else -> "Error del servidor (Código $code)"
        }
    }
}