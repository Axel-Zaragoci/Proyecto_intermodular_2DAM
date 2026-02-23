package com.example.intermodular.views.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * Componente de diálogo modal para simular un proceso de pago
 * Según el contenido muestra un [CircularProgressIndicator] para indicar que está en proceso o lo oculta si se ha completado el pago simulado
 *
 * Al ejecutarse empezará mostrando que está cargando y luego pasará a mostrar que se ha completado
 *
 * @author Axel Zaragoci
 *
 * @param show - Controlador de visibilidad del diálogo
 * @param message - Mensaje a mostrar, que determina algunos comportamientos como la capacidad de cierre
 * @param onDismiss - Callback a ejecutar al intentar cerrar el diálogo
 */
@Composable
fun PaymentPopup(
    show: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    if (show) {
        Dialog(
            onDismissRequest = {
                if (message == "Pago completado") {
                    onDismiss()
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = message == "Pago completado",
                dismissOnClickOutside = false
            )
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (message == "Procesando pago...") {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .padding(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 4.dp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }


                    Text(
                        text = message,
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}