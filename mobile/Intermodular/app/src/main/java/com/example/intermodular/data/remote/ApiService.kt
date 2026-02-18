package com.example.intermodular.data.remote

import com.example.intermodular.data.remote.dto.BookingDto
import com.example.intermodular.data.remote.dto.CreateBookingDto
import com.example.intermodular.data.remote.dto.CreateReviewDto
import com.example.intermodular.data.remote.dto.LoginDto
import com.example.intermodular.data.remote.dto.ReviewDto
import com.example.intermodular.data.remote.dto.RoomsResponseDto
import com.example.intermodular.data.remote.dto.UpdateBookingDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz para definir los endpoints de la API
 * Utiliza Retrofit con funciones que utilizan corrutinas
 */
interface ApiService {

    /**
     * Obtiene todas las reservas en el sistema
     * @author Axel Zaragoci
     *
     * @return [List<BookingDto>] - Lista con todas la información de todas las reservas
     */
    @GET("booking")
    suspend fun getBookings() : List<BookingDto>


    /**
     * Obtiene una reserva específica filtrada por su ID
     * @author Axel Zaragoci
     *
     * @param id - ID de la reserva
     * @return [BookingDto] - Objeto con la información de la reserva
     */
    @GET("booking/{id}")
    suspend fun getBookingById(@Path("id") id : String) : BookingDto


    /**
     * Obtiene la lista de las reservas de un usuario específico
     * @author Axel Zaragoci
     *
     * @param id - ID del cliente
     * @return [List<BookingDto>] - Lista con la información de las reservas del usuario
     */
    @GET("booking/client/{id}")
    suspend fun getBookingsByUserId(@Path("id") id: String) : List<BookingDto>


    /**
     * Crea una nueva reserva
     * @author Axel Zaragoci
     *
     * @param body - Objeto con los datos necesarios para crear la reserva
     * @return [BookingDto] - Objeto con la información completa de la nueva reserva
     */
    @POST("booking")
    suspend fun createBooking(
        @Body body: CreateBookingDto
    ): BookingDto


    /**
     * Cancela una reserva existente
     * @author Axel Zaragoci
     *
     * @param id - ID de la reserva a cancelar
     * @return [BookingDto] - Objeto con la información completa de la reserva cancelada
     */
    @PATCH("booking/{id}/cancel")
    suspend fun cancelBooking(@Path("id") id: String): BookingDto

    /**
     * Edita una reserva existente
     * @author Axel Zaragoci
     *
     * @param id - ID de la reserva a editar
     * @param body - Objeto con la información a modificar en la reserva
     * @return [BookingDto] - Objeto con la información completa de la reserva editada
     */
    @PATCH("booking/{id}")
    suspend fun updateBooking(
        @Path("id") id: String,
        @Body body: UpdateBookingDto
    ): BookingDto


    @GET("room")
    suspend fun getRooms(
        @Query("type") type: String? = null,
        @Query("isAvailable") isAvailable: Boolean? = null,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("guests") guests: Int? = null,
        @Query("hasExtraBed") hasExtraBed: Boolean? = null,
        @Query("hasCrib") hasCrib: Boolean? = null,
        @Query("hasOffer") hasOffer: Boolean? = null,
        @Query("extras") extras: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null
    ) : RoomsResponseDto

    @GET("review/room/{roomID}")
    suspend fun getReviewsByRoom(@Path("roomID") roomId: String): List<ReviewDto>

    @POST("review")
    suspend fun createReview(
        @Body body: CreateReviewDto
    ): ReviewDto

    @POST("auth/login")
    suspend fun login(
        @Body body: Map<String, String>
    ): LoginDto
}