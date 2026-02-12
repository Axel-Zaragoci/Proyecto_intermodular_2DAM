package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.Tune
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.intermodular.viewmodels.RoomViewModel
import com.example.intermodular.views.components.RoomCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    roomViewModel: RoomViewModel
) {
    val rooms by roomViewModel.filteredRooms.collectAsStateWithLifecycle()
    val isLoading by roomViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by roomViewModel.errorMessage.collectAsStateWithLifecycle()
    val currentFilter by roomViewModel.currentFilter.collectAsStateWithLifecycle()

    var showFilterSheet by remember { mutableStateOf(false) }

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
                IconButton(onClick = { showFilterSheet = true }) {
                    Icon(Icons.Default.Tune, contentDescription = "Filtrar")
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
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(rooms) { room ->
                    RoomCard(
                        room = room,
                        onButtonClick = { /*TODO*/ }
                    )
                }
            }
        }
    }
    if (showFilterSheet) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { it != SheetValue.Hidden }
        )
        
        ModalBottomSheet(
            onDismissRequest = { /* Prevent dismissal by clicking outside */ },
            sheetState = sheetState,
            dragHandle = null // Optional: hide drag handle if you want a truly full-screen feel, or keep it. I'll keep default for now but skip partial.
        ) {
            RoomFilterScreen(
                currentFilter = currentFilter,
                onApplyParams = { filter ->
                    roomViewModel.updateFilter(filter)
                    showFilterSheet = false
                },
                onResetParams = {
                    roomViewModel.clearFilters()
                    showFilterSheet = false
                },
                onDismiss = { showFilterSheet = false }
            )
        }
    }
}
