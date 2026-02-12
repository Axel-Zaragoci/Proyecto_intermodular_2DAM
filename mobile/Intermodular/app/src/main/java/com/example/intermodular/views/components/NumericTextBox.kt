package com.example.intermodular.views.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun NumericTextBox(
    number : String,
    onValueChanged : (String) -> Unit,
    label : String
) {
    OutlinedTextField(
        value = number,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() }) {
                onValueChanged(newValue)
            }
        },
        label = {
            Text(
                text = label
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,

            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,

            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,

            cursorColor = MaterialTheme.colorScheme.primary
        ),
    )
}