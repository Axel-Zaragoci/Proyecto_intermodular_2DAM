package com.example.intermodular.views.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Componente que sirve como DatePicker personalizado y con los estilos acordes al resto del programa
 *
 * Permite mostrar una fecha seleccionada por defecto y cambiar el texto de label
 *
 * @author Axel Zaragoci
 *
 * @param selectedDateMillis - Fecha seleccionada en milisegundos
 * @param onDateSelected - Callback a ejecutar al seleccionar una fecha
 * @param label - Texto a mostrar para el DatePicker con el texto por defecto "Selecciona fecha"
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDatePicker(
    selectedDateMillis: Long?,
    onDateSelected: (Long?) -> Unit,
    label: String = "Selecciona fecha"
) {
    var showDialog by remember { mutableStateOf(false) }

    val formattedDate = selectedDateMillis?.let {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(Date(it))
    } ?: ""

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
    ){
        OutlinedTextField(
            value = formattedDate,
            onValueChange = {},
            enabled = false,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,

                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,

                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
        )
    }

    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis
        )

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateSelected(datePickerState.selectedDateMillis)
                        showDialog = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}