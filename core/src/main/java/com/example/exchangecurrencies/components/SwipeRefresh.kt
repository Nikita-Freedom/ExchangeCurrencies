package com.example.exchangecurrencies.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.exchangecurrencies.ui.theme.ExchangeCurrenciesTheme
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun SwipeRefresh(
  isRefreshing: Boolean,
  indicatorPadding: PaddingValues = PaddingValues(0.dp),
  onRefresh: () -> Unit,
  content: @Composable () -> Unit,
) {
  val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
  com.google.accompanist.swiperefresh.SwipeRefresh(
    state = swipeRefreshState,
    onRefresh = onRefresh,
    indicatorPadding = indicatorPadding,
    clipIndicatorToPadding = false,
    indicator = { state, refreshTrigger ->
      SwipeRefreshIndicator(
        state = state,
        scale = true,
        refreshTriggerDistance = refreshTrigger,
        backgroundColor = ExchangeCurrenciesTheme.colors.backgroundPrimary,
        contentColor = ExchangeCurrenciesTheme.colors.textPrimary
      )
    },
    content = content
  )
}