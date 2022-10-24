package com.example.exchangecurrencies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.exchangecurrencies.main.navigation.addMainFeatureGraph
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun NavigationComponent(
  navController: NavHostController,
  navigator: Navigator,
) {

  LaunchedEffect("navigation") {
    navigator.sharedFlow.onEach {
      navController.navigate(it.route) {
        popUpTo(it.route)
      }
    }.launchIn(this)
  }

  NavHost(
    navController = navController,
    startDestination = NavTarget.RootModule.route
  ) {
    addMainFeatureGraph() { navController.popBackStack() }
  }
}