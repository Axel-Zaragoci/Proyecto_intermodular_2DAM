package com.example.intermodular.views.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PaymentPopup(
    mostrar: Boolean,
    mensaje: String,
    onDismiss: () -> Unit
) {
    if (mostrar) {
        Dialog(
            onDismissRequest = {
                if (mensaje == "Pago completado") {
                    onDismiss()
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = mensaje == "Pago completado",
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
                    AnimatedContent(
                        targetState = mensaje,
                        transitionSpec = {
                            fadeIn() with fadeOut()
                        },
                        label = "mensaje_animado"
                    ) { mensajeActual ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (mensajeActual == "Procesando pago...") {
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
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = mensajeActual,
                                fontSize = 18.sp,
                                color = Color.Black,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}