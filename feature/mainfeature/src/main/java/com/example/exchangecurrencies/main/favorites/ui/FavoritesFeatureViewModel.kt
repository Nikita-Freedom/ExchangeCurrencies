package com.example.exchangecurrencies.main.favorites.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.exchangecurrencies.core.base.BaseViewModel
import com.example.exchangecurrencies.domain.entity.SortConfiguration
import com.example.exchangecurrencies.main.domain.MainModel
import com.example.exchangecurrencies.main.domain.entity.RateDomainModel
import com.example.exchangecurrencies.main.favorites.ui.FavoritesFeatureUIState.Initial
import com.example.exchangecurrencies.main.favorites.ui.FavoritesFeatureUIState.Loaded
import com.example.exchangecurrencies.main.favorites.ui.FavoritesFeatureUIState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesFeatureViewModel @Inject constructor(
  private val mainModel: MainModel,
) : BaseViewModel<FavoritesFeatureViewModel>() {

  private var _uiState = mutableStateOf<FavoritesFeatureUIState>(Initial)
  val uiState: State<FavoritesFeatureUIState>
    get() = _uiState

  private val _uiStateFlow = MutableStateFlow(State())
  val uiStateFlow = _uiStateFlow.asStateFlow()


  init {
    viewModelScope.launch {
      observeData()
    }
  }

  fun observeData() {
    viewModelScope.launch {
      mainModel.favorites(_uiStateFlow.value.sortConfiguration).onStart {
        _uiState.value = Loading(
          State().copy(
            refreshInProgress = true,
          )
        )
      }.onCompletion {
        _uiStateFlow.update { state ->
          state.copy(
            refreshInProgress = false,
          )
        }
        _uiState.value = Loaded(
          State().copy(
            refreshInProgress = false,
          )
        )
      }.catch {
        _uiStateFlow.update { state ->
          state.copy(
            message = "$it"
          )
        }
      }.collect {
        _uiState.value = Loaded(
          State().copy(
            refreshInProgress = false,
            favoriteRates = it
          )
        )
//        _uiStateFlow.update { state ->
//          state.copy(
//            refreshInProgress = false,
//            favoriteRates = it
//          )
//        }
      }
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
      mainModel.favorites(sortConfiguration).flowOn(IO).collect {
        _uiStateFlow.update { state ->
          state.copy(
            showSortConfigurationDialog = false,
            sortConfiguration = sortConfiguration,
            favoriteRates = it
          )
        }
        observeData()
      }
    }
  }

  fun removeFromFavoritesByName(rate: RateDomainModel) {
    viewModelScope.launch(IO) {
      mainModel.addOrRemoveFromFavorites(rate)
    }
  }

  fun removeAll() {
    viewModelScope.launch(IO) {
      mainModel.removeAll()
    }
  }
}