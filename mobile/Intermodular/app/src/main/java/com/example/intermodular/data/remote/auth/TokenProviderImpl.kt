package com.example.intermodular.data.remote.auth

object TokenProviderImpl : TokenProvider {

    @Volatile private var token: String? = null

    override fun getToken(): String? = token

    override fun setToken(token: String?) {
        this.token = token
    }

    override fun clearToken() {
        token = null
    }
}