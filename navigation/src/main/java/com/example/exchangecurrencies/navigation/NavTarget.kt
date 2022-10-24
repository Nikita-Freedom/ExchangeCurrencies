package com.example.exchangecurrencies.navigation

import com.example.exchangecurrencies.navigation.R.drawable

sealed class NavTarget(val route: String, val icon: Int) {
  object Main : NavTarget(ModuleRoutes.MainFeature.route, ModuleRoutes.MainFeature.icon)
  object Favorites :
    NavTarget(ModuleRoutes.FavoritesFeature.route, ModuleRoutes.FavoritesFeature.icon)

  object RootModule : NavTarget(ModuleRoutes.RootModule.route, ModuleRoutes.RootModule.icon)
}

enum class ModuleRoutes(val route: String, val icon: Int) {
  MainFeature("Валюты", drawable.ic_plot_up_40),
  FavoritesFeature("Избранное", drawable.ic_cards_stack_24),
  RootModule("rootmodule", drawable.ic_plot_up_40),
}