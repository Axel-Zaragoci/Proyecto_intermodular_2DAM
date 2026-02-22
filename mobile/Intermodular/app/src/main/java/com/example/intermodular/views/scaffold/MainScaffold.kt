package com.example.intermodular.views.scaffold

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.intermodular.views.navigation.Navigation
import com.example.intermodular.views.navigation.Routes

/**
 * Componente principal de la aplicación
 * Define el espacio para la barra superior, barra inferior y el espacio para la navegación
 *
 * @author Axel Zaragoci
 *
 * @param darkMode - Estado actual del tema, siendo `true` si está en modo oscuro y `false` en caso contrario
 * @param onToggleDarkMode - Callback para cambiar de modo oscuro a modo claro
 */
@Composable
fun MyApp(
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBars = currentRoute != Routes.Login.route && currentRoute != Routes.Register.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showBars) {
                TopAppBarState(
                    darkMode = darkMode,
                    onToggleDarkMode = onToggleDarkMode
                )
            }
        },
        bottomBar = {
            if (showBars) {
                NavigationBarState(navController = navController)
            }
        }
    ) { innerPadding ->
        Navigation(
            navigationController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
