package com.example.intermodular.models

/**
 * Modelo de datos que representa los criterios de búsqueda y filtros
 * aplicables al listado de habitaciones.
 *
 * Todos los campos son opcionales (`null` por defecto), lo que significa que
 * si un campo es nulo, no se aplicará ese filtro en la consulta al servidor.
 *
 * @property type Filtro por tipo de habitación (ej. "doble", "suite").
 * @property isAvailable Si es `true`, filtra solo las habitaciones disponibles.
 * @property minPrice Precio mínimo por noche.
 * @property maxPrice Precio máximo por noche.
 * @property guests Número de huéspedes para filtrar capacidad.
 * @property hasExtraBed Si es `true`, requiere que la habitación tenga cama supletoria.
 * @property hasCrib Si es `true`, requiere que la habitación tenga cuna disponible.
 * @property hasOffer Si es `true`, filtra habitaciones que tengan algún descuento aplicado.
 * @property extras Lista de comodidades extra requeridas (ej. ["WIFI", "TV"]).
 * @property sortBy Campo por el cual ordenar los resultados (por defecto "roomNumber").
 * @property sortOrder Dirección de la ordenación ("asc" para ascendente, "desc" para descendente).
 */
data class RoomFilter(
    val type: String? = null,
    val isAvailable: Boolean? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val guests: Int? = null,
    val hasExtraBed: Boolean? = null,
    val hasCrib: Boolean? = null,
    val hasOffer: Boolean? = null,
    val extras: List<String>? = null,
    val sortBy: String? = "roomNumber",
    val sortOrder: String? = "asc"
)
