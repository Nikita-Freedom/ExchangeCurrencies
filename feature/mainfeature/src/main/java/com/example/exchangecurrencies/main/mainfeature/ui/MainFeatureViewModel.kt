package com.example.exchangecurrencies.main.mainfeature.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.exchangecurrencies.core.base.BaseViewModel
import com.example.exchangecurrencies.domain.entity.SortConfiguration
import com.example.exchangecurrencies.main.domain.MainModel
import com.example.exchangecurrencies.main.domain.entity.RateDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainFeatureViewModel @Inject constructor(
  private val mainModel: MainModel,
) : BaseViewModel<MainFeatureViewModel>() {

  private var _uiState = mutableStateOf<MainFeatureUIState>(MainFeatureUIState.Initial)
  val uiState: State<MainFeatureUIState>
    get() = _uiState

  private val _uiStateFlow = MutableStateFlow(State())
  val uiStateFlow = _uiStateFlow.asStateFlow()

  init {
    viewModelScope.launch(IO) {
      mainModel.getAllData(_uiStateFlow.value.baseCurrency)
      observeData()
    }
  }

  fun observeData() {
    viewModelScope.launch(IO) {
      val rates = mainModel.rates
      val configuration = mainModel.sortConfiguration
      val baseCurrency = mainModel.baseCurrency
      val combineFlow = combine(rates, configuration, baseCurrency, ::Triple)

      combineFlow.flowOn(IO).onStart {
        withContext(Dispatchers.Main) {
          _uiState.value = MainFeatureUIState.Loading(
            State().copy(
              refreshInProgress = true,
              baseCurrency = combineFlow.first().third,
              sortConfiguration = combineFlow.first().second
            )
          )
        }

      }.flowOn(IO)
        .onCompletion {
          _uiState.value = MainFeatureUIState.Loaded(
            State().copy(
              refreshInProgress = false,
            )
          )

        }.flowOn(IO)
        .catch {
          _uiState.value = MainFeatureUIState.Error(
            State().copy(
              message = "$it"
            )
          )

        }.flowOn(IO).collect {
          _uiState.value = MainFeatureUIState.Loaded(
            State().copy(
              rates = it.first,
              refreshInProgress = false,
              baseCurrency = it.third,
              sortConfiguration = it.second
            )
          )
        }
    }
  }

  fun selectBaseCurrency(baseCurrency: String) {
    _uiStateFlow.update {
      it.copy(
        baseCurrency = baseCurrency
      )
    }
    viewModelScope.launch(IO) {
      mainModel.selectBaseCurrency(baseCurrency)
    }
  }

  fun dismissSortConfigurationDialog() {
    _uiStateFlow.update {
      it.copy(
        showSortConfigurationDialog = false
      )
    }
  }

  fun changeSorting() {
    _uiStateFlow.update {
      it.copy(
        showSortConfigurationDialog = true
      )
    }
  }

  fun applySortConfiguration(sortConfiguration: SortConfiguration) {
    viewModelScope.launch {
      _uiStateFlow.update { state ->
        state.copy(
          showSortConfigurationDialog = false,
          sortConfiguration = sortConfiguration,
        )
      }
      mainModel.changeSortConfiguration(sortConfiguration)
    }
  }

  fun addToFavorites(rateDomainModel: RateDomainModel) {
    viewModelScope.launch(IO) {
      mainModel.addOrRemoveFromFavorites(rateDomainModel)
    }
  }
}