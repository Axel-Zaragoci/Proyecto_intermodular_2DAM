package com.example.intermodular.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
* Diálogo para cambiar la contraseña del usuario.
*
* Este componente muestra un [AlertDialog] con:
* - Campo para contraseña actual
* - Campo para nueva contraseña
* - Campo para repetir la nueva contraseña
* - Opción para mostrar/ocultar todas las contraseñas
* - Botón de guardar (habilitado solo si todos los campos están rellenos)
* - Botón de cancelar
*
* Comportamiento:
* 1. Permite alternar entre mostrar y ocultar las contraseñas.
* 2. Solo permite guardar cuando los tres campos no están vacíos.
* 3. Devuelve los valores introducidos mediante el callback [onSave].
*
* La validación completa (coincidencia de contraseñas, reglas de seguridad, etc.)
* debe realizarse en el ViewModel o capa lógica correspondiente.
*
* @author Ian Rodriguez
*
* @param onBack - Callback ejecutado al cerrar o cancelar el diálogo
* @param onSave - Callback ejecutado al pulsar "Guardar" con:
*                 - oldPass: contraseña actual
*                 - newPass: nueva contraseña
*                 - repeatNewPass: repetición de la nueva contraseña
*/
@Composable
fun ChangePasswordDialog(
    onBack: () -> Unit,
    onSave: (oldPass: String, newPass: String, repeatNewPass: String) -> Unit
) {
    // Estados locales para almacenar las contraseñas introducidas
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var newPassR by remember { mutableStateOf("") }

    // Controla si las contraseñas se muestran en texto plano
    var showAll by remember { mutableStateOf(false) }

    // Transformación visual dependiendo del estado de visibilidad
    val transformation =
        if (showAll) VisualTransformation.None else PasswordVisualTransformation()

    AlertDialog(
        onDismissRequest = onBack,
        title = { Text("Cambiar contraseña") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                // Botón para alternar visibilidad de contraseñas
                TextButton(onClick = { showAll = !showAll }) {
                    Text(if (showAll) "Ocultar contraseñas" else "Ver contraseñas")
                }

                // Campo contraseña antigua
                OutlinedTextField(
                    value = oldPass,
                    onValueChange = { oldPass = it },
                    label = { Text("Contraseña antigua") },
                    singleLine = true,
                    visualTransformation = transformation,
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo nueva contraseña
                OutlinedTextField(
                    value = newPass,
                    onValueChange = { newPass = it },
                    label = { Text("Nueva contraseña") },
                    singleLine = true,
                    visualTransformation = transformation,
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo repetir nueva contraseña
                OutlinedTextField(
                    value = newPassR,
                    onValueChange = { newPassR = it },
                    label = { Text("Repetir nueva contraseña") },
                    singleLine = true,
                    visualTransformation = transformation,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },

        // Botón confirmar
        confirmButton = {
            Button(
                onClick = { onSave(oldPass, newPass, newPassR) },
                enabled = oldPass.isNotBlank() &&
                        newPass.isNotBlank() &&
                        newPassR.isNotBlank()
            ) {
                Text("Guardar")
            }
        },

        // Botón cancelar
        dismissButton = {
            TextButton(onClick = onBack) {
                Text("Cancelar")
            }
        }
    )
}