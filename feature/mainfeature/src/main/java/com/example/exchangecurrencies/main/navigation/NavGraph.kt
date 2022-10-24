package com.example.exchangecurrencies.main.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.exchangecurrencies.main.favorites.ui.FavoritesFeatureView
import com.example.exchangecurrencies.main.favorites.ui.FavoritesFeatureViewModel
import com.example.exchangecurrencies.main.mainfeature.ui.MainFeatureView
import com.example.exchangecurrencies.main.mainfeature.ui.MainFeatureViewModel
import com.example.exchangecurrencies.navigation.NavTarget

fun NavGraphBuilder.addMainFeatureGraph(popBackStack: () -> Unit) {
  navigation(
    startDestination = NavTarget.Main.route,
    route = NavTarget.RootModule.route
  ) {
    composable(NavTarget.Main.route) {
      val viewModel: MainFeatureViewModel = hiltViewModel()
      MainFeatureView(viewModel)
    }
    composable(NavTarget.Favorites.route) {
      val viewModel: FavoritesFeatureViewModel = hiltViewModel()
      FavoritesFeatureView(viewModel)
    }
  }
}