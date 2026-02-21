package com.example.intermodular.models

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Modelo de datos para un elemento de la navegación de [com.example.intermodular.views.scaffold.NavigationBarState]
 *
 * @author Axel Zaragoci
 *
 * @property name - Texto descriptivo del elemento (visible como etiqueta)
 * @property icon - Icono vectorial que representa visualmente la sección
 * @property route - Ruta de navegación asociada
 */
data class NavItem(
    val name: String,
    val icon : ImageVector,
    val route : String
)