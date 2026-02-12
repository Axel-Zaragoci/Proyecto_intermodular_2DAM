package com.example.intermodular.data.remote.auth

object SessionManager {
    @Volatile private var token: String? = null
    @Volatile private var userId: String? = null
    fun setSession(token: String, userId: String) {
        this.token = token
        this.userId = userId
    }
    fun getToken(): String? = token
    fun getUserId(): String? = userId
    fun clearSession() {
        token = null
        userId = null
    }
}