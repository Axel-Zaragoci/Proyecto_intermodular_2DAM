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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDateField(
    value: String,
    onValueSelected: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var show by remember { mutableStateOf(false) }
    val formatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val initialMillis = remember(value) {
        runCatching { formatter.parse(value)?.time }.getOrNull()
    }
    val state = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    if (show) {
        DatePickerDialog(
            onDismissRequest = { show = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { onValueSelected(formatter.format(Date(it))) }
                    show = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton(onClick = { show = false }) { Text("Cancelar") } }
        ) { DatePicker(state = state) }
    }

    TextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        enabled = enabled,
        label = { Text("Nacimiento*") },
        modifier = modifier,
        trailingIcon = {
            IconButton(onClick = { show = true }, enabled = enabled) {
                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
            }
        }
    )
}