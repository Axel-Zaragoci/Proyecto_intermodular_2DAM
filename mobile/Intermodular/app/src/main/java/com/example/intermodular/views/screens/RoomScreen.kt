package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.intermodular.viewmodels.RoomViewModel
import com.example.intermodular.views.components.RoomCard
import com.example.intermodular.views.components.RoomFilterList

/**
 * Pantalla principal del catálogo de habitaciones.
 *
 * Muestra el listado de habitaciones disponibles de forma paginada/flexible mediante un `LazyColumn`.
 * Permite desplegar un panel de filtros avanzados ([RoomFilterList]) y observar
 * en tiempo real el estado de carga y error provistos por el [RoomViewModel].
 *
 * @param roomViewModel Instancia del ViewModel inyectada que gestiona la lógica de pantalla.
 * @param onRoomClick Callback invocado cuando el usuario pulsa sobre la tarjeta de una habitación específica, 
 * pasando como argumento el ID único (`String`) de la habitación seleccionada para la navegación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    roomViewModel: RoomViewModel,
    onRoomClick: (String) -> Unit = {}
) {
    val rooms by roomViewModel.filteredRooms.collectAsStateWithLifecycle()
    val isLoading by roomViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by roomViewModel.errorMessage.collectAsStateWithLifecycle()
    
    val showFilters by roomViewModel.showFilters.collectAsStateWithLifecycle()
    val type by roomViewModel.type.collectAsStateWithLifecycle()
    val minPrice by roomViewModel.minPrice.collectAsStateWithLifecycle()
    val maxPrice by roomViewModel.maxPrice.collectAsStateWithLifecycle()
    val guests by roomViewModel.guests.collectAsStateWithLifecycle()
    val isAvailable by roomViewModel.isAvailable.collectAsStateWithLifecycle()
    val hasExtraBed by roomViewModel.hasExtraBed.collectAsStateWithLifecycle()
    val hasCrib by roomViewModel.hasCrib.collectAsStateWithLifecycle()
    val hasOffer by roomViewModel.hasOffer.collectAsStateWithLifecycle()
    val sortBy by roomViewModel.sortBy.collectAsStateWithLifecycle()
    val sortOrder by roomViewModel.sortOrder.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Habitaciones",
                style = MaterialTheme.typography.titleMedium
            )
            Row {
                IconButton(onClick = { roomViewModel.changeFilterVisibility() }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filtros")
                }
                IconButton(onClick = { roomViewModel.loadRooms() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                }
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage ?: "Unknown error")
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                RoomFilterList(
                    showFilters = showFilters,
                    changeVisibility = roomViewModel::changeFilterVisibility,
                    type = type,
                    onTypeChanged = roomViewModel::onTypeChanged,
                    minPrice = minPrice,
                    onMinPriceChanged = roomViewModel::onMinPriceChanged,
                    maxPrice = maxPrice,
                    onMaxPriceChanged = roomViewModel::onMaxPriceChanged,
                    guests = guests,
                    onGuestsChanged = roomViewModel::onGuestsChanged,
                    isAvailable = isAvailable,
                    onIsAvailableChanged = roomViewModel::onIsAvailableChanged,
                    hasExtraBed = hasExtraBed,
                    onExtraBedChanged = roomViewModel::onExtraBedChanged,
                    hasCrib = hasCrib,
                    onCribChanged = roomViewModel::onCribChanged,
                    hasOffer = hasOffer,
                    onOfferChanged = roomViewModel::onOfferChanged,
                    sortBy = sortBy,
                    onSortByChanged = roomViewModel::onSortByChanged,
                    sortOrder = sortOrder,
                    onSortOrderChanged = roomViewModel::onSortOrderChanged,
                    filter = roomViewModel::filter,
                    clearFilters = roomViewModel::clearFilters
                )
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(rooms) { room ->
                        RoomCard(
                            room = room,
                            onButtonClick = { onRoomClick(room.id) }
                        )
                    }
                }
                
                if (rooms.isEmpty() && !showFilters) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron habitaciones",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
