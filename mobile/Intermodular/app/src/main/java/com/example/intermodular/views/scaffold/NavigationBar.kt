package com.example.intermodular.views.scaffold


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.intermodular.ui.theme.BluePrimary
import com.example.intermodular.ui.theme.BluePrimaryDark
import com.example.intermodular.views.navigation.Routes

@Composable
fun NavigationBarView(
    items: List<NavItem>,
    currentRoute: String,
    onItemSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = BluePrimary
    ) {
        for (item in items) {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.name
                    )
                },
                label =  {
                    Text(
                        text = item.name
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    onItemSelected(item.route)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    indicatorColor = BluePrimaryDark
                )
            )
        }
    }
}

@Composable
fun NavigationBarState(
    navController: NavController
) {
    val items = listOf(
        NavItem(
            name = "Habitaciones",
            icon = Icons.Filled.Bed,
            route = Routes.Rooms.route
        ),
        NavItem(
            name = "Reservas",
            icon = Icons.Filled.CalendarMonth,
            route = Routes.Bookings.route
        ),
        NavItem(
            name = "Usuario",
            icon = Icons.Filled.ManageAccounts,
            route = Routes.User.route
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: items[1].route

    fun onItemSelected(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavigationBarView(
        items = items,
        currentRoute = currentRoute,
        onItemSelected = {
            route: String -> onItemSelected(route)
        }
    )
}