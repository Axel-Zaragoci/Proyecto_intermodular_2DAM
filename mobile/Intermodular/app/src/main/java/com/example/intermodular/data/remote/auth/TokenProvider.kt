package com.example.intermodular.data.remote.auth

interface TokenProvider {
    fun getToken(): String?
    fun setToken(token: String?)
    fun clearToken()
}