package com.example.intermodular.views.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.intermodular.models.Booking

@Composable
fun BookingCard(
    booking: Booking,
    onViewDetailsClick: (String) -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = MaterialTheme.shapes.small
            ),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            InformationComponent(
                title = "Fecha de inicio: ",
                value = booking.checkInDate.toString()
            )

            InformationComponent(
                title = "Fecha de fin: ",
                value = booking.checkOutDate.toString()
            )

            InformationComponent(
                title = "Precio total: ",
                value = booking.totalPrice.toString()
            )

            Button(
                onClick = { onViewDetailsClick(booking.id) }
            ) {
                Text(
                    text = "Ver detalles"
                )
            }
        }
    }
}