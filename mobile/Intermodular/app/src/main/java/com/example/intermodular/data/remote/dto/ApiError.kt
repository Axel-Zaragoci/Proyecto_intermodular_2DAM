package com.example.intermodular.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("error")
    val error: String?
)
