package com.example.intermodular.views.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.intermodular.viewmodels.NewBookingViewModel

@Composable
fun NewBookingScreen() {
    Text("Funciona")
}

@Composable
fun NewBookingState(
    viewModel: NewBookingViewModel
) {
    NewBookingScreen()
}