package com.example.intermodular.data.remote

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant

class InstantJsonAdapter {

    @FromJson
    fun fromJson(value: String): Instant {
        return Instant.parse(value)
    }

    @ToJson
    fun toJson(value: Instant): String {
        return value.toString()
    }
}