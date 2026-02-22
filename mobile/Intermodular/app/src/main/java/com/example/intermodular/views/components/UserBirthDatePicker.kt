package com.example.intermodular.views.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
 * Campo personalizado para seleccionar la fecha de nacimiento.
 *
 * Este componente:
 * - Muestra la fecha en formato ISO (yyyy-MM-dd).
 * - Abre un [DatePickerDialog] al pulsar el icono de calendario.
 * - Devuelve la fecha seleccionada formateada en ISO_LOCAL_DATE.
 * - Puede deshabilitarse cuando la pantalla está en estado de carga/guardado.
 *
 * Flujo:
 * 1. Se muestra un [TextField] en modo solo lectura.
 * 2. Al pulsar el icono, se abre el selector de fecha.
 * 3. Al confirmar, se transforma la fecha seleccionada a String y se envía mediante [onValueSelected].
 *
 * @author Ian Rodriguez
 *
 * @param value - Fecha actual en formato String (yyyy-MM-dd)
 * @param onValueSelected - Callback que devuelve la nueva fecha seleccionada en formato ISO
 * @param enabled - Indica si el campo está habilitado para interactuar
 * @param modifier - Modifier para personalización externa del componente
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDateField(
    value: String,
    onValueSelected: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    // Controla la visibilidad del DatePickerDialog
    var show by remember { mutableStateOf(false) }

    // Formateador para convertir entre Date y String ISO
    val formatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    // Convertir el valor actual (String) a milisegundos para inicializar el DatePicker
    val initialMillis = remember(value) {
        runCatching { formatter.parse(value)?.time }.getOrNull()
    }

    // Estado interno del DatePicker
    val state = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    // Diálogo del selector de fecha
    if (show) {
        DatePickerDialog(
            onDismissRequest = { show = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        onValueSelected(formatter.format(Date(it)))
                    }
                    show = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { show = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = state)
        }
    }

    // Campo visual que muestra la fecha seleccionada
    TextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        enabled = enabled,
        label = { Text("Nacimiento*") },
        modifier = modifier,
        trailingIcon = {
            IconButton(onClick = { show = true }, enabled = enabled) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha"
                )
            }
        }
    )
}