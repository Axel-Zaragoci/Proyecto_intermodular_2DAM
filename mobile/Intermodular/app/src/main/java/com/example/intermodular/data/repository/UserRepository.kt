package com.example.intermodular.data.repository

import com.example.intermodular.data.remote.ApiService
import com.example.intermodular.data.remote.auth.SessionManager
import com.example.intermodular.data.remote.dto.UpdateUserRequestDto
import com.example.intermodular.data.remote.mapper.toUserModel
import com.example.intermodular.models.UserModel
import okhttp3.MultipartBody
import java.time.format.DateTimeFormatter

/**
 * Repositorio encargado de gestionar las operaciones relacionadas con el usuario.
 *
 * Esta clase actúa como intermediario entre el ViewModel y la API remota,
 * encapsulando la lógica de:
 * - Obtención del usuario autenticado
 * - Actualización de datos personales
 * - Actualización de la foto de perfil
 * - Cambio de contraseña
 *
 * También se encarga de transformar los DTO recibidos desde la API
 * a modelos de dominio mediante los mappers correspondientes.
 *
 * @author Ian Rodriguez
 *
 * @param api - Servicio de API que expone los endpoints remotos
 */
class UserRepository(
    private val api: ApiService
) {

    /**
     * Obtiene los datos del usuario autenticado.
     *
     * Llama al endpoint getMe() y convierte el resultado
     * al modelo de dominio [UserModel].
     *
     * @return Usuario autenticado convertido a modelo de dominio
     */
    suspend fun getMe(): UserModel {
        return api.getMe().toUserModel()
    }

    /**
     * Actualiza la foto de perfil del usuario.
     *
     * Flujo:
     * 1. Sube la imagen mediante uploadPhoto().
     * 2. Construye un [UpdateUserRequestDto] reutilizando los datos actuales.
     * 3. Sustituye imageRoute por la nueva URL devuelta por el backend.
     * 4. Llama a updateUser() para persistir el cambio.
     * 5. Devuelve el usuario actualizado convertido a modelo de dominio.
     *
     * @param currentUser - Usuario actual cuyos datos se reutilizan
     * @param photoPart - Imagen convertida a MultipartBody.Part
     * @return Usuario actualizado con la nueva imagen
     */
    suspend fun updateProfilePhoto(
        currentUser: UserModel,
        photoPart: MultipartBody.Part
    ): UserModel {

        // Subida de imagen al servidor
        val upload = api.uploadPhoto(photoPart)

        // Construcción del cuerpo con la nueva ruta de imagen
        val body = UpdateUserRequestDto(
            id = SessionManager.getUserId(),
            firstName = currentUser.firstName,
            lastName = currentUser.lastName,
            email = currentUser.email,
            dni = currentUser.dni,
            phoneNumber = currentUser.phoneNumber,
            birthDate = currentUser.birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
            cityName = currentUser.cityName,
            gender = currentUser.gender,
            imageRoute = upload.url
        )

        // Actualización del usuario en backend
        val updated = api.updateUser(body).user
        return updated.toUserModel()
    }

    /**
     * Actualiza los datos personales del usuario.
     *
     * Envía el [UpdateUserRequestDto] al backend y devuelve
     * el usuario actualizado como modelo de dominio.
     *
     * @param body - DTO con los nuevos datos del usuario
     * @return Usuario actualizado
     */
    suspend fun updateUser(body: UpdateUserRequestDto): UserModel {
        val updated = api.updateUser(body).user
        return updated.toUserModel()
    }

    /**
     * Cambia la contraseña del usuario autenticado.
     *
     * Construye un mapa con las contraseñas requeridas
     * y lo envía al endpoint correspondiente.
     *
     * La validación de reglas de negocio debe realizarse
     * en capas superiores (ViewModel o backend).
     *
     * @param oldPassword - Contraseña actual
     * @param newPassword - Nueva contraseña
     * @param repeatNewPassword - Repetición de la nueva contraseña
     */
    suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        repeatNewPassword: String
    ) {
        val body = mapOf(
            "oldPassword" to oldPassword,
            "newPassword" to newPassword,
            "repeatNewPassword" to repeatNewPassword
        )

        api.changeMyPassword(body)
    }
}