package com.example.intermodular.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.math.roundToInt
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment

@Composable
fun FilterList(
    showFilters: Boolean,
    changeVisibility: () -> Unit,
    selectedStartDate : Long?,
    selectedEndDate : Long?,
    onStartDateSelected : (Long?) -> Unit,
    onEndDateSelected : (Long?)-> Unit,
    maxPrice : Int,
    onMaxPriceChanged : (Int) -> Unit,
    guests : String,
    onGuestsChanged : (String) -> Unit,
    extraBed : Boolean,
    onExtraBedCheckChanged : (Boolean) -> Unit,
    cradle : Boolean,
    onCradleCheckChanged : (Boolean) -> Unit,
    filter : () -> Unit,
    filterOffer : () -> Unit
) {
    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 30.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.large
            )
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row (
            modifier = Modifier
                .clickable { changeVisibility() }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = if (showFilters) "Ocultar filtros" else "Ver filtros",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(0.dp, 8.dp, 10.dp, 8.dp)
            )
            Icon(
                imageVector = if (showFilters) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                contentDescription = "Icono mostrar / ocultar",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ),
            )
        }

        if (showFilters) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    BookingDatePicker(
                        selectedDateMillis = selectedStartDate,
                        onDateSelected = onStartDateSelected,
                        label = "Fecha de inicio"
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    BookingDatePicker(
                        selectedDateMillis = selectedEndDate,
                        onDateSelected = onEndDateSelected,
                        label = "Fecha de fin"
                    )
                }
            }

            Column (
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Precio máximo: ",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Slider(
                    value = maxPrice.toFloat(),
                    onValueChange = { floatValue ->
                        onMaxPriceChanged(floatValue.roundToInt())
                    },
                    valueRange = 1f..1000f,
                    steps = 999,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline,
                        activeTickColor = Color.Transparent,
                        inactiveTickColor = Color.Transparent,
                    ),
                )
                Text(
                    text = "Valor seleccionado: $maxPrice€",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                NumericTextBox(
                    number = guests,
                    onValueChanged = onGuestsChanged,
                    label = "Cantidad de huéspedes",
                    numbers = listOf("1", "2", "3", "4", "5")
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Checkbox(
                        checked = extraBed,
                        onCheckedChange = onExtraBedCheckChanged
                    )

                    Text(
                        text = "Cama extra"
                    )
                }

                Row (
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Checkbox(
                        checked = cradle,
                        onCheckedChange = onCradleCheckChanged
                    )

                    Text(
                        text = "Cuna"
                    )
                }
            }

            Row (
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Button(
                    onClick = { filter() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Filtrar"
                    )
                }

                Button(
                    onClick = { filterOffer() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Ver ofertas"
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FilterListPreview() {
    FilterList(
        showFilters = true,
        changeVisibility = {

        },
        selectedStartDate = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
        selectedEndDate = LocalDate.now().plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
        onStartDateSelected = {

        },
        onEndDateSelected = {

        },
        maxPrice = 1000,
        onMaxPriceChanged = {

        },
        guests = "0",
        onGuestsChanged = {

        },
        extraBed = false,
        onExtraBedCheckChanged = {

        },
        cradle = false,
        onCradleCheckChanged = {

        },
        filter = {

        },
        filterOffer = {

        }
    )
}