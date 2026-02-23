package com.example.intermodular.data.remote.dto

/**
 * Clase que almacena la plantilla de datos a enviar para crear una reserva
 * @author Axel Zaragoci
 *
 * @param client - Identificador único del cliente que ha realizado la reserva
 * @param room - Identificador único de la habitación reservada
 * @param checkInDate - Fecha y hora de inicio de la reserva
 * @param checkOutDate - Fecha y hora de fin de la reserva
 * @param guests - Cantidad de huéspedes para la reserva
 */
data class CreateBookingDto(
    val client: String,
    val room: String,
    val checkInDate: Long,
    val checkOutDate: Long,
    val guests: Int
)
