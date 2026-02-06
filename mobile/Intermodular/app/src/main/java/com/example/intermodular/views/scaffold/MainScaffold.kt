package com.example.intermodular.views.scaffold

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.intermodular.views.navigation.Navigation

@Composable
fun MyApp(
    darkMode : Boolean,
    onToggleDarkMode: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarState(
                darkMode = darkMode,
                onToggleDarkMode = onToggleDarkMode
            )
        },
        bottomBar = { NavigationBarState(
            navController = navController
        ) }
    ) {
        innerPadding ->
        Navigation(
            navigationController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}