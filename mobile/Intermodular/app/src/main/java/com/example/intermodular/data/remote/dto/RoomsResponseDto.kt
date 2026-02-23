package com.example.intermodular.data.remote.dto

/**
 * Objeto de Transferencia de Datos (DTO) que envuelve la respuesta de una lista de habitaciones.
 *
 * Utilizado cuando el backend devuelve una estructura paginada, con filtros o metadatos adicionales
 * envolviendo a la propia lista de habitaciones.
 *
 * @property items La lista de habitaciones devuelta ([RoomDto]).
 * @property appliedFilter Objeto o mapa que representa el filtro aplicado en la consulta.
 * @property sort Mapa con los criterios de ordenación aplicados en la consulta.
 */
data class RoomsResponseDto(
    val items: List<RoomDto>,
    val appliedFilter: Any?,
    val sort: Map<String, Int>?
)
