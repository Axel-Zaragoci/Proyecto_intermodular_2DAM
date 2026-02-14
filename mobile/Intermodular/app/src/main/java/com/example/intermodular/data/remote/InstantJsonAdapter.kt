package com.example.intermodular.data.remote

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant

/**
 * Clase para utilizarse al serializar y deserializar JSON recibido de la API
 *
 * @author Axel Zaragoci
 */
class InstantJsonAdapter {

    /**
     * Convierte la cadena recibida en una instancia de la clase Instant para poder ser interpretado como fecha
     *
     * @param value - Cadena de texto a transformar en Instant
     * @return [Instant] - Objeto para crear la fecha
     */
    @FromJson
    fun fromJson(value: String): Instant {
        return Instant.parse(value)
    }


    /**
     * Convierte una instancia de la clase Instant en una cadena para ser enviada por la API
     *
     * @param value - Objeto con la información de la fecha a enviar a la API
     * @return [String] - Cadena lista para enviar a la API
     */
    @ToJson
    fun toJson(value: Instant): String {
        return value.toString()
    }
}