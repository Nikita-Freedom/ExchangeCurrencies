package com.example.exchangecurrencies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.rememberNavController
import com.example.exchangecurrencies.components.BottomNavigationBar
import com.example.exchangecurrencies.navigation.NavigationComponent
import com.example.exchangecurrencies.navigation.Navigator
import com.example.exchangecurrencies.ui.theme.ExchangeCurrenciesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ExchangeCurrenciesTheme {
        MainScreenContent()
      }
    }
  }
}

@Composable
fun MainScreenContent() {
  val navController = rememberNavController()
  val navigator = Navigator()
  Scaffold(
    topBar = {
    },
    bottomBar = { BottomNavigationBar(navController) },
    content = { padding ->
      Box(modifier = Modifier.padding(padding)) {
        NavigationComponent(navController, navigator)
      }
    },
    backgroundColor = colorResource(R.color.white)
  )
}
