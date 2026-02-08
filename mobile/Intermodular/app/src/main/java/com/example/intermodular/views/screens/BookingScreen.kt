package com.example.intermodular.views.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.intermodular.viewmodels.BookingViewModel

@Composable
fun BookingScreen(
    viewModel: BookingViewModel
) {
    val bookings by viewModel.bookings.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    Column {
        if (loading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
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
                Text(
                    text = booking.id
                )
            }
        }
    }
}