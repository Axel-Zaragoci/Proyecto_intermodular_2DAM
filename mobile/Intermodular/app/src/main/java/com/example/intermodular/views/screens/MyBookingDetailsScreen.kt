package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.intermodular.BuildConfig
import com.example.intermodular.models.Booking
import com.example.intermodular.models.Room
import com.example.intermodular.viewmodels.MyBookingDetailsViewModel
import com.example.intermodular.views.components.BookingDataForm
import com.example.intermodular.views.components.PaymentPopup

/**
 * Pantalla de detalles y actualización de una reserva
 *
 * Flujo de UI:
 * 1. Muestra indicador de carga mientras se obtienen datos
 * 2. Muestra mensaje de error si ocurre algún problema
 *
 * Componentes externos:
 * - **Formulario de actualizar**: [BookingDataForm] para editar fechas y huéspedes
 * - **Popup de pago**: [PaymentPopup] para simular una pasarela de pago
 *
 * @author Axel Zaragoci
 *
 * @param loading - Estado de carga de datos
 * @param error - Mensaje de error a mostrar (null si no hay error)
 * @param room - Habitación asociada a la reserva
 * @param status - Estado actual de la reserva ("Abierta", "Cancelada")
 * @param booking - Datos completos de la reserva
 * @param startDate - Fecha de inicio seleccionada (en milisegundos)
 * @param endDate - Fecha de fin seleccionada (en milisegundos)
 * @param mostrarPopup - Controla la visibilidad del popup de pago
 * @param mensajePopup - Mensaje a mostrar en el popup de pago
 * @param onPopupDismiss - Callback al cerrar el popup
 * @param onUpdateClick - Callback al hacer clic en "Actualizar"
 * @param onCancelClick - Callback al hacer clic en "Cancelar reserva"
 * @param onStartDateChange - Callback al cambiar fecha de entrada
 * @param onEndDateChange - Callback al cambiar fecha de salida
 * @param onGuestsChange - Callback al cambiar número de huéspedes
 */
@Composable
fun MyBookingDetailsScreen(
    loading: Boolean,
    error: String?,
    room: Room?,
    status: String?,
    booking: Booking?,
    startDate: Long?,
    endDate: Long?,
    showPopup : Boolean,
    popupMessage : String,
    onPopupDismiss: () -> Unit,
    onUpdateClick: () -> Unit,
    onCancelClick: () -> Unit,
    onStartDateChange: (Long?) -> Unit,
    onEndDateChange: (Long?) -> Unit,
    onGuestsChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ESTADO DE CARGA
        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            // MENSAJE DE ERROR
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Construcción url
            val relativePath = if (room?.mainImage?.startsWith("/") == true)
                room.mainImage.substring(1)
            else
                room?.mainImage

            val imageUrl = if (room?.mainImage?.startsWith("http") == true)
                room.mainImage
            else
                "${BuildConfig.BASE_URL}$relativePath"

            // IMÁGEN PRINCIPAL DE LA HABITACIÓN
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de la habitación",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                error = ColorPainter(MaterialTheme.colorScheme.errorContainer)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // NÚMERO DE LA HABITACIÓN
            Text(
                text = "Habitación nº${room?.roomNumber}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // ESTADO DE LA RESERVA
            Text(
                text = "Estado: $status",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // FORMULARIO DE EDICIÓN DE RESERVA
            BookingDataForm(
                create = false,
                startDate = startDate ?: 0L,
                endDate = endDate ?: 0L,
                guests = booking?.guests?.toString() ?: "",
                totalPrice = booking?.totalPrice,
                onButtonClick = onUpdateClick,
                onStartDateChange = onStartDateChange,
                onEndDateChange = onEndDateChange,
                onGuestsDataChange = onGuestsChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            // BOTÓN DE CANCELAR
            Button(
                onClick = onCancelClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar reserva")
            }

            // POPUP DE PAGO
            PaymentPopup(
                show = showPopup,
                message = popupMessage,
                onDismiss = onPopupDismiss
            )
        }
    }
}

/**
 * Versión de [MyBookingDetailsScreen] con estado y conexión al ViewModel
 *
 * Funciones:
 * 1. Recolectar estados del [MyBookingDetailsViewModel] usando [collectAsStateWithLifecycle]
 * 2. Pasar los estados a [MyBookingDetailsScreen]
 *
 * @author Axel Zaragoci
 *
 * @param viewModel - Instancia de [MyBookingDetailsViewModel] de la que sacar los datos
 */
@Composable
fun MyBookingDetailsState(
    viewModel: MyBookingDetailsViewModel
) {
    val loading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()
    val room by viewModel.room.collectAsStateWithLifecycle()
    val booking by viewModel.booking.collectAsStateWithLifecycle()
    val showPopup by viewModel.showPopup.collectAsStateWithLifecycle()
    val popupMessage by viewModel.popupMessage.collectAsStateWithLifecycle()

    MyBookingDetailsScreen(
        loading = loading,
        error = error,
        room = room,
        status = booking?.status,
        booking = booking,
        showPopup = showPopup,
        popupMessage = popupMessage,
        onPopupDismiss = {},
        startDate = viewModel.checkInDateToMilliseconds(),
        endDate = viewModel.checkOutDateToMilliseconds(),
        onUpdateClick = viewModel::updateBooking,
        onCancelClick = viewModel::cancelBooking,
        onStartDateChange = viewModel::onStartDateChange,
        onEndDateChange = viewModel::onEndDateChange,
        onGuestsChange = viewModel::onGuestsChange
    )
}