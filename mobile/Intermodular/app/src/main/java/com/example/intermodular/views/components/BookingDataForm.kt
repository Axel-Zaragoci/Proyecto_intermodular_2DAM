package com.example.intermodular.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BookingDataForm(
    create : Boolean,
    startDate: Long,
    endDate: Long,
    guests: String,
    totalPrice: Int?,
    onButtonClick: () -> Unit,
    onStartDateChange: (Long?) -> Unit,
    onEndDateChange: (Long?) -> Unit,
    onGuestsDataChange: (String) -> Unit
) {
    Column(

    ) {
        Row( )
        {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
            ) {
                BookingDatePicker(
                    onDateSelected = onStartDateChange,
                    selectedDateMillis = startDate,
                    label = "Fecha de inicio:"
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            ) {
                BookingDatePicker(
                    onDateSelected = onEndDateChange,
                    selectedDateMillis = endDate,
                    label = "Fecha de inicio:"
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            NumericTextBox(
                number = guests,
                onValueChanged = onGuestsDataChange,
                label = "Cantidad de huéspedes:",
                numbers = listOf("1", "2", "3", "4", "5")
            )
        }

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Precio total:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "$totalPrice €",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            onClick = onButtonClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (create) "Reservar" else "Actualizar reserva"
            )
        }
    }
}