package com.example.intermodular.data.remote.mapper

import com.example.intermodular.data.remote.dto.UserDto
import com.example.intermodular.models.UserModel
import java.time.ZoneOffset

/**
 * Extensión que convierte un [UserDto] (modelo remoto)
 * en un [UserModel] (modelo de dominio).
 *
 * Responsabilidades del mapper:
 * - Transformar valores null en valores por defecto seguros.
 * - Convertir la fecha de nacimiento desde [Instant] a LocalDate.
 * - Garantizar que el modelo de dominio nunca contenga campos nulos innecesarios.
 *
 * Conversión de fecha:
 * - Se transforma el Instant recibido desde backend
 *   a zona UTC mediante [ZoneOffset.UTC]
 * - Posteriormente se convierte a LocalDate.
 *
 * Valores por defecto:
 * - Strings null → "" (empty)
 * - vipStatus null → false
 *
 * Este mapper separa claramente la capa remota de la capa de dominio.
 *
 * @author Ian Rodriguez
 *
 * @receiver UserDto - Objeto recibido desde la API
 * @return UserModel - Modelo de dominio listo para usar en la UI
 */
fun UserDto.toUserModel(): UserModel {
    return UserModel(
        firstName = firstName.orEmpty(),
        lastName = lastName.orEmpty(),
        email = email.orEmpty(),
        dni = dni.orEmpty(),
        phoneNumber = phoneNumber,
        birthDate = birthDate
            .atZone(ZoneOffset.UTC)
            .toLocalDate(),
        cityName = cityName.orEmpty(),
        gender = gender.orEmpty(),
        imageRoute = imageRoute,
        vipStatus = vipStatus ?: false
    )
}