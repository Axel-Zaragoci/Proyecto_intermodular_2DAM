package com.example.intermodular.views.scaffold

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.intermodular.ui.theme.BluePrimary
import com.example.intermodular.ui.theme.BluePrimaryDark

/**
 * Componente de barra superior personalizada
 *
 * @param darkMode - Estado actual del tema (true = modo oscuro, false = modo claro)
 * @param onChangeModeClick - Callback que se ejecuta al hacer clic en el botón de cambio de tema
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarView(
    darkMode : Boolean,
    onChangeModeClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Hotel Pere María",
                color = Color.White
            )
        },
        actions = {
            IconButton(
                onClick = onChangeModeClick
            ) {
                Icon(
                    imageVector =  if (darkMode) Icons.Filled.LightMode
                    else Icons.Filled.DarkMode,
                    contentDescription = "Cambiar tema",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .background(
                Brush.horizontalGradient(
                    listOf(BluePrimary, BluePrimaryDark)
                )
            )
    )
}

/**
 * Versión del [TopAppBarView] con estado
 * Funciona de separación entre el estado y la vista
 *
 * @author Axel Zaragoci
 *
 * @param darkMode - Estado actual del tema
 * @param onToggleDarkMode - Función para cambiar el tema
 */
@Composable
fun TopAppBarState(
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    TopAppBarView(
        darkMode,
        onChangeModeClick = { onToggleDarkMode() }
    )
}