package com.example.intermodular.data.remote.utils

import android.content.Context
import android.net.Uri
import com.example.intermodular.data.remote.auth.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

fun uriToMultipart(
    context: Context,
    uri: Uri,
    partName: String = "photo"
): MultipartBody.Part {

    val contentResolver = context.contentResolver
    val mimeType = contentResolver.getType(uri) ?: "image/*"

    val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }
        ?: throw IllegalStateException("No se pudo leer la imagen")

    val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())

    val fileName = "${SessionManager.getUserId()}_${System.currentTimeMillis()}.jpg"

    return MultipartBody.Part.createFormData(
        partName,
        fileName,
        requestBody
    )
}