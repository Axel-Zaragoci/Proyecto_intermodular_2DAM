package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.RangeSlider
import com.example.intermodular.models.RoomFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomFilterScreen(
    currentFilter: RoomFilter,
    onApplyParams: (RoomFilter) -> Unit,
    onResetParams: () -> Unit,
    onDismiss: () -> Unit
) {
    var type by remember { mutableStateOf(currentFilter.type ?: "") }
    var minPrice by remember { mutableStateOf(currentFilter.minPrice?.toString() ?: "") }
    var maxPrice by remember { mutableStateOf(currentFilter.maxPrice?.toString() ?: "") }
    var guests by remember { mutableStateOf(currentFilter.guests?.toString() ?: "") }
    var isAvailable by remember { mutableStateOf(currentFilter.isAvailable ?: false) }
    var hasExtraBed by remember { mutableStateOf(currentFilter.hasExtraBed ?: false) }
    var hasCrib by remember { mutableStateOf(currentFilter.hasCrib ?: false) }
    var hasOffer by remember { mutableStateOf(currentFilter.hasOffer ?: false) }
    var sortBy by remember { mutableStateOf(currentFilter.sortBy ?: "roomNumber") }
    var sortOrder by remember { mutableStateOf(currentFilter.sortOrder ?: "asc") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filtros de Habitación") },
                actions = {
                    TextButton(onClick = {
                        onResetParams()
                        onDismiss()
                    }) {
                        Text("Reset")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize() // Fill entire sheet height
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Type Dropdown
            var expanded by remember { mutableStateOf(false) }
            val types = listOf("Simple", "Doble", "Suite", "Family")
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = type,
                    onValueChange = { },
                    label = { Text("Tipo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Todos") },
                        onClick = {
                            type = ""
                            expanded = false
                        }
                    )
                    types.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                type = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Price Range Slider
            val rangeStart = minPrice.toFloatOrNull() ?: 0f
            val rangeEnd = maxPrice.toFloatOrNull() ?: 300f
            var sliderPosition by remember { mutableStateOf(rangeStart..rangeEnd) }
            
            Text("Rango de Precio: ${sliderPosition.start.toInt()}€ - ${sliderPosition.endInclusive.toInt()}€")
            RangeSlider(
                value = sliderPosition,
                onValueChange = { range ->
                    sliderPosition = range
                    minPrice = range.start.toInt().toString()
                    maxPrice = range.endInclusive.toInt().toString()
                },
                valueRange = 0f..300f,
                steps = 19, // Steps of 50 (300/20)
                modifier = Modifier.fillMaxWidth()
            )

            // Guests
            OutlinedTextField(
                value = guests,
                onValueChange = { guests = it },
                label = { Text("Huéspedes") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Checkboxes
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isAvailable, onCheckedChange = { isAvailable = it })
                Text("Solo Disponible")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = hasExtraBed, onCheckedChange = { hasExtraBed = it })
                Text("Cama Extra")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = hasCrib, onCheckedChange = { hasCrib = it })
                Text("Cuna")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = hasOffer, onCheckedChange = { hasOffer = it })
                Text("Oferta")
            }

            // Sort
             Text("Ordenar por:", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = sortBy == "roomNumber",
                    onClick = { sortBy = "roomNumber" },
                    label = { Text("Número") }
                )
                FilterChip(
                    selected = sortBy == "pricePerNight",
                    onClick = { sortBy = "pricePerNight" },
                    label = { Text("Precio") }
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = sortOrder == "asc",
                    onClick = { sortOrder = "asc" },
                    label = { Text("ASC") }
                )
                FilterChip(
                    selected = sortOrder == "desc",
                    onClick = { sortOrder = "desc" },
                    label = { Text("DESC") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val filter = RoomFilter(
                        type = type.ifBlank { null },
                        minPrice = minPrice.toDoubleOrNull(),
                        maxPrice = maxPrice.toDoubleOrNull(),
                        guests = guests.toIntOrNull(),
                        isAvailable = if (isAvailable) true else null,
                        hasExtraBed = if (hasExtraBed) true else null,
                        hasCrib = if (hasCrib) true else null,
                        hasOffer = if (hasOffer) true else null,
                        sortBy = sortBy,
                        sortOrder = sortOrder
                    )
                    onApplyParams(filter)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aplicar Filtros")
            }
        }
    }
}
