package com.example.exchangecurrencies.main.mainfeature.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.exchangecurrencies.components.BottomSheetExpandHideEffect
import com.example.exchangecurrencies.components.EmptyContentMessage
import com.example.exchangecurrencies.components.HorizontalDivider
import com.example.exchangecurrencies.components.RateItem
import com.example.exchangecurrencies.components.Spinner
import com.example.exchangecurrencies.components.SwipeRefresh
import com.example.exchangecurrencies.components.TopAppBar
import com.example.exchangecurrencies.core.R.drawable
import com.example.exchangecurrencies.domain.entity.SortConfiguration
import com.example.exchangecurrencies.domain.entity.SortConfiguration.SortDirection
import com.example.exchangecurrencies.main.component.SortOptionSelector
import com.example.exchangecurrencies.main.domain.entity.RateDomainModel
import com.example.exchangecurrencies.mainfeature.R
import com.example.exchangecurrencies.ui.theme.ExchangeCurrenciesTheme
import com.example.exchangecurrencies.ui.theme.Shapes

@Composable
fun MainFeatureView(viewModel: MainFeatureViewModel) {
  MainScreen(viewModel)
}


@Composable
fun MainScreen(viewModel: MainFeatureViewModel) {

  val uiStateFlow by viewModel.uiStateFlow.collectAsState()
  val uiState by viewModel.uiState

  MainContent(
    uiState = uiState,
    uiStateFlow = uiStateFlow,
    onRefresh = {
      viewModel.observeData()
    },
    onFavoriteClick = { rate ->
      viewModel.addToFavorites(rate)
    },
    loadRatesByCurrency = { currency ->
      viewModel.selectBaseCurrency(currency)
    },
    applySortConfiguration = { sortConfiguration ->
      viewModel.applySortConfiguration(sortConfiguration)
    },
    dismissSortConfigurationDialog = {
      viewModel.dismissSortConfigurationDialog()
    },
    changeSorting = {
      viewModel.changeSorting()
    }
  )
}

@Composable
private fun MainContent(
  uiState: MainFeatureUIState,
  uiStateFlow: State,
  onRefresh: (String) -> Unit,
  onFavoriteClick: (RateDomainModel) -> Unit,
  loadRatesByCurrency: (String) -> Unit,
  applySortConfiguration: (SortConfiguration) -> Unit,
  dismissSortConfigurationDialog: () -> Unit,
  changeSorting: () -> Unit,
) {
  when (uiState) {
    is MainFeatureUIState.Error -> {
      uiState.state.message?.let { error ->
        EmptyContentMessage(
          imgRes = drawable.img_status_disclaimer_170,
          title = "Ошибка",
          description = error,
        )
      }
    }

    MainFeatureUIState.Initial -> {
      ContentLoadingState()
    }

    is MainFeatureUIState.Loaded -> {
      ScreenSlot(
        loadRatesByCurrency = loadRatesByCurrency,
        changeSorting = changeSorting,
        selectedBaseCurrency = uiState.state.baseCurrency,
        sortConfiguration = uiStateFlow.sortConfiguration,
      ) {
        if (uiState.state.rates != null) {
          ContentStateReady(
            state = uiStateFlow,
            onRefresh = { onRefresh(uiState.state.baseCurrency) },
            onFavoriteClick = onFavoriteClick,
            applySortConfiguration = applySortConfiguration,
            dismissSortConfigurationDialog = dismissSortConfigurationDialog,
            rates = uiState.state.rates
          )
        } else {
          EmptyContentMessage(
            imgRes = drawable.img_status_disclaimer_170,
            title = "Валюты",
            description = "Данных нет",
          )
        }
      }
    }

    is MainFeatureUIState.Loading -> {
      ScreenSlot(
        loadRatesByCurrency = loadRatesByCurrency,
        changeSorting = changeSorting,
        selectedBaseCurrency = uiState.state.baseCurrency
      ) {
        ContentLoadingState()
      }
    }
  }
}

@Composable
private fun ContentStateReady(
  state: State,
  rates: List<RateDomainModel>,
  onRefresh: () -> Unit,
  onFavoriteClick: (RateDomainModel) -> Unit,
  applySortConfiguration: (SortConfiguration) -> Unit,
  dismissSortConfigurationDialog: () -> Unit,
) {
  ModalBottomSheetScaffold(
    state = state,
    content = {
      ContentMain(
        onRefresh = onRefresh,
        onFavoriteClick = onFavoriteClick,
        rates = rates,
      )
    },
    applySortConfiguration = applySortConfiguration,
    dismissSortConfigurationDialog = dismissSortConfigurationDialog,
  )
}

@Composable
private fun ContentMain(
  modifier: Modifier = Modifier,
  rates: List<RateDomainModel>,
  onRefresh: () -> Unit,
  onFavoriteClick: (RateDomainModel) -> Unit,
) {
  Box(
    modifier = modifier
      .fillMaxSize()
  ) {
    SwipeRefresh(
      isRefreshing = false,
      onRefresh = onRefresh,
    ) {
      RatesList(
        rates = rates,
        onFavoriteClick = onFavoriteClick,
      )
    }
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ModalBottomSheetScaffold(
  state: State,
  content: @Composable () -> Unit,
  applySortConfiguration: (SortConfiguration) -> Unit,
  dismissSortConfigurationDialog: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(
    initialValue = ModalBottomSheetValue.Hidden,
    confirmStateChange = { sheetValue ->
      if (sheetValue == ModalBottomSheetValue.Hidden) {
        dismissSortConfigurationDialog()
      }
      false
    }
  )

  var showDialog by remember { mutableStateOf(value = state.showSortConfigurationDialog) }
  if (state.showSortConfigurationDialog) {
    showDialog = true
  }

  BottomSheetExpandHideEffect(state.showSortConfigurationDialog, sheetState) {
    showDialog = false
  }

  ModalBottomSheetLayout(
    sheetState = sheetState,
    sheetShape = Shapes.large.copy(
      bottomStart = CornerSize(percent = 0),
      bottomEnd = CornerSize(percent = 0)
    ),
    scrimColor = ExchangeCurrenciesTheme.colors.shadowColor.copy(alpha = 0.4f),
    sheetBackgroundColor = Color.Transparent,
    sheetContent = {
      ModalSheetContent(
        showSortConfigurationDialog = showDialog,
        sortConfiguration = state.sortConfiguration,
        applySortConfiguration = applySortConfiguration
      )
    },
    content = {
      content()
    }
  )
}

@Composable
private fun ModalSheetContent(
  sortConfiguration: SortConfiguration? = null,
  showSortConfigurationDialog: Boolean = false,
  applySortConfiguration: (SortConfiguration) -> Unit
) {
  if (showSortConfigurationDialog && sortConfiguration != null) {
    var sortConfiguration by remember(showSortConfigurationDialog) {
      mutableStateOf(sortConfiguration)
    }
    SortOptionSelector(
      sortConfiguration = sortConfiguration,
      onDirectionClick = {
        sortConfiguration = sortConfiguration.let { configuration ->
          val newDirection = when (configuration.direction) {
            SortDirection.Ascending -> SortDirection.Descending
            SortDirection.Descending -> SortDirection.Ascending
          }
          configuration.copy(direction = newDirection)
        }
      },
      onPropertyClick = { property ->
        sortConfiguration = sortConfiguration.copy(property = property)
      },
      onApplyClick = {
        applySortConfiguration(sortConfiguration)
      }
    )
  } else {
    Spacer(modifier = Modifier.height(24.dp))
  }
}


@Composable
private fun ScreenSlot(
  loadRatesByCurrency: (String) -> Unit,
  changeSorting: () -> Unit,
  selectedBaseCurrency: String,
  sortConfiguration: SortConfiguration? = null,
  content: @Composable () -> Unit,
) {
  Column(
    modifier = Modifier
      .statusBarsPadding()
  ) {
    TopAppBar(
      leftContent = {
        SelectBaseCurrencySpinner(
          modifier = Modifier.padding(start = 8.dp),
          onItemSelected = { currency ->
            loadRatesByCurrency(currency)
          },
          selectedBaseCurrency = selectedBaseCurrency,
          options = listOf("RUB", "USD", "EUR")
        )
      },
      centerContent = {
        Text(
          modifier = Modifier.padding(start = 58.dp),
          text = "Валюты",
          color = ExchangeCurrenciesTheme.colors.black,
          style = ExchangeCurrenciesTheme.typography.title1
        )
      },
      rightContent = {
        if (sortConfiguration != null) {
          AnimatedVisibility(
            visible = true,
            enter = slideInVertically { fullHeight -> fullHeight },
            exit = slideOutVertically { fullHeight -> fullHeight },
          ) {
            IconButton(
              modifier = Modifier.padding(horizontal = 26.dp),
              onClick = changeSorting
            ) {
              Icon(
                painter = painterResource(id = R.drawable.ic_filter_24),
                contentDescription = "filter Icon"
              )
            }
          }
        }
      }
    )
    HorizontalDivider()
    Box(
      modifier = Modifier
        .fillMaxSize()
        .weight(1f),
      contentAlignment = Alignment.Center,
    ) {
      content()
    }
  }
}

@Composable
fun SelectBaseCurrencySpinner(
  modifier: Modifier = Modifier,
  onItemSelected: (String) -> Unit,
  selectedBaseCurrency: String,
  options: List<String>
) {
  Spinner(
    modifier = modifier,
    options = options,
    dropDownModifier = Modifier.wrapContentSize(),
    onItemSelected = onItemSelected,
    dropdownItemFactory = { item, _ ->
      Text(
        text = item,
        color = ExchangeCurrenciesTheme.colors.contendAccentTertiary
      )
    },
    selectedText = selectedBaseCurrency,
  )
}


@Composable
private fun RatesList(
  modifier: Modifier = Modifier,
  rates: List<RateDomainModel>,
  onFavoriteClick: (RateDomainModel) -> Unit,
) {
  val listState = rememberLazyListState()
  LazyColumn(
    modifier = modifier.padding(horizontal = 8.dp),
    state = listState
  ) {
    items(
      items = rates
    ) { rate ->
      rate.currencyName?.let { currencyName ->
        RateItem(
          currency = currencyName,
          value = rate.currencyValue.toString(),
          onFavoriteClick = { onFavoriteClick(rate) },
          isFavorite = rate.isFavorite
        )
      }
    }
  }
}

@Composable
private fun ContentLoadingState() {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    CircularProgressIndicator(
      color = ExchangeCurrenciesTheme.colors.contendAccentTertiary
    )
  }
}
