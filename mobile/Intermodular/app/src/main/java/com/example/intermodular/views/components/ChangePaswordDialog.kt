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

@Composable
fun ChangePasswordDialog(
    onBack: () -> Unit,
    onSave: (oldPass: String, newPass: String, repeatNewPass: String) -> Unit
) {
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var newPassR by remember { mutableStateOf("") }

    var showAll by remember { mutableStateOf(false) }

    val transformation = if (showAll) VisualTransformation.None else PasswordVisualTransformation()

    AlertDialog(
        onDismissRequest = onBack,
        title = { Text("Cambiar contraseña") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                TextButton(onClick = { showAll = !showAll }) {
                    Text(if (showAll) "Ocultar contraseñas" else "Ver contraseñas")
                }

                OutlinedTextField(
                    value = oldPass,
                    onValueChange = { oldPass = it },
                    label = { Text("Contraseña antigua") },
                    singleLine = true,
                    visualTransformation = transformation,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = newPass,
                    onValueChange = { newPass = it },
                    label = { Text("Nueva contraseña") },
                    singleLine = true,
                    visualTransformation = transformation,
                    modifier = Modifier.fillMaxWidth()
                )

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
        confirmButton = {
            Button(
                onClick = { onSave(oldPass, newPass, newPassR) },
                enabled = oldPass.isNotBlank() && newPass.isNotBlank() && newPassR.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onBack) { Text("Cancelar") }
        }
    )
}