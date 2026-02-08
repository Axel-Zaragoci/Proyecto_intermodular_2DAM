package com.example.intermodular.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.intermodular.models.Booking
import com.example.intermodular.viewmodels.MyBookingsViewModel
import com.example.intermodular.views.components.BookingCard
import com.example.intermodular.views.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    loading : Boolean,
    error : String?,
    bookings : List<Booking>,
    onViewDetailsClick : (String) -> Unit
) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loading) {
            Box (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        LazyColumn {
            items(bookings) { booking ->
                BookingCard(
                    booking = booking,
                    onViewDetailsClick = onViewDetailsClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreenState(
    viewModel: MyBookingsViewModel,
    navController : NavHostController
) {
    val bookings by viewModel.bookings.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    MyBookingsScreen(
        loading = loading,
        error = error,
        bookings = bookings,
        onViewDetailsClick = { id ->
            navController.navigate(
                Routes.MyBookingDetails.createRoute(id)
            )
        }
    )
}