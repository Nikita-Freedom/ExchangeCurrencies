package com.example.exchangecurrencies.main.mainfeature.ui

import androidx.compose.runtime.Immutable
import com.example.exchangecurrencies.domain.entity.SortConfiguration
import com.example.exchangecurrencies.main.domain.entity.CurrencyDomainModel
import com.example.exchangecurrencies.main.domain.entity.RateDomainModel

sealed class MainFeatureUIState {
  object Initial : MainFeatureUIState()
  data class Loading(val state: State) : MainFeatureUIState()
  data class Loaded(val state: State) : MainFeatureUIState()
  data class Error(val state: State) : MainFeatureUIState()
}

@Immutable
data class State(
  val content: CurrencyDomainModel? = null,
  val baseCurrency: String = "RUB",
  val refreshInProgress: Boolean = false,
  val sortConfiguration: SortConfiguration = SortConfiguration(
    SortConfiguration.Property.Name,
    SortConfiguration.SortDirection.Ascending
  ),
  val showSortConfigurationDialog: Boolean = false,
  val isFavorite: Boolean = false,
  val rates: List<RateDomainModel>? = null,
  val message: String? = null
)
