package com.example.exchangecurrencies.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.exchangecurrencies.navigation.NavTarget
import com.example.exchangecurrencies.ui.theme.ExchangeCurrenciesTheme

@Composable
fun BottomNavigationBar(navController: NavController) {
  val items = listOf(
    NavTarget.Main,
    NavTarget.Favorites,
  )
  BottomNavigation(
    backgroundColor = ExchangeCurrenciesTheme.colors.contendAccentTertiary,
    contentColor = ExchangeCurrenciesTheme.colors.contendPrimary
  ) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    items.forEach { item ->
      BottomNavigationItem(
        icon = { Icon(painterResource(id = item.icon), contentDescription = item.route) },
        label = { Text(text = item.route) },
        selectedContentColor = Color.White,
        unselectedContentColor = Color.White.copy(0.4f),
        alwaysShowLabel = true,
        selected = currentRoute == item.route,
        onClick = {
          navController.navigate(item.route) {
            navController.graph.startDestinationRoute?.let { route ->
              popUpTo(route) {
                saveState = true
              }
            }
            launchSingleTop = true
            restoreState = true
          }
        }
      )
    }
  }
}
