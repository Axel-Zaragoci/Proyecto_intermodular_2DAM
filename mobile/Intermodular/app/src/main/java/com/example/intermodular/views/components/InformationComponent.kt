package com.example.intermodular.views.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Componente utilizado para desplegar información en 2 líneas
 * La primera línea sirve de titulo indicando que información se va a mostrar
 * La segunda línea muestra la información
 *
 * @author Axel Zaragoci
 */
@Composable
fun InformationComponent(
    title: String,
    value: String?
) {
    Column (
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}