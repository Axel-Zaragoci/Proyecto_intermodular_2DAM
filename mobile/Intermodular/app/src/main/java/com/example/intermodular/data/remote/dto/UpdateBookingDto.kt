package com.example.intermodular.data.remote.dto

/**
 * Clase que almacena la plantilla de datos a enviar para actualizar una reserva
 * @author Axel Zaragoci
 *
 * @param checkInDate - Fecha y hora de inicio de la reserva
 * @param checkOutDate - Fecha y hora de fin de la reserva
 * @param guests - Cantidad de huéspedes para la reserva
 */
data class UpdateBookingDto(
    val checkInDate: Long,
    val checkOutDate: Long,
    val guests: Int
)