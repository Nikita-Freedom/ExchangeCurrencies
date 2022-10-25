package com.example.exchangecurrencies.main.favorites.ui

import androidx.compose.runtime.Immutable
import com.example.exchangecurrencies.domain.TransientComponentState
import com.example.exchangecurrencies.domain.entity.SortConfiguration
import com.example.exchangecurrencies.main.domain.entity.RateDomainModel

sealed class FavoritesFeatureUIState() {
  object Initial : FavoritesFeatureUIState()
  data class Loading(val state: State) : FavoritesFeatureUIState()
  data class Loaded(val state: State) : FavoritesFeatureUIState()
  data class Error(val state: State) : FavoritesFeatureUIState()
}

@Immutable
data class State(
  val message: String? = null,
  val favoriteRates: List<RateDomainModel>? = null,
  val sortConfiguration: SortConfiguration = SortConfiguration(
    SortConfiguration.Property.Name,
    SortConfiguration.SortDirection.Ascending
  ),
  val showSortConfigurationDialog: Boolean = false,
  val content: List<RateDomainModel>? = null,
  val refreshInProgress: Boolean = false,
  val isNextPageLoading: Boolean = false,
  val confirmCancelDialog: TransientComponentState<Long> = TransientComponentState.None,
  val isFavorite: Boolean = false,
)
