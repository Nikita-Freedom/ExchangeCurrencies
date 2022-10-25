package com.example.exchangecurrencies.main.favorites.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.example.exchangecurrencies.components.SwipeRefresh
import com.example.exchangecurrencies.components.TopAppBar
import com.example.exchangecurrencies.domain.entity.SortConfiguration
import com.example.exchangecurrencies.main.component.SortOptionSelector
import com.example.exchangecurrencies.main.domain.entity.RateDomainModel
import com.example.exchangecurrencies.mainfeature.R
import com.example.exchangecurrencies.ui.theme.ExchangeCurrenciesTheme
import com.example.exchangecurrencies.ui.theme.Shapes

@Composable
fun FavoritesFeatureView(viewModel: FavoritesFeatureViewModel) {
  FavoritesScreen(viewModel)
}


@Composable
fun FavoritesScreen(viewModel: FavoritesFeatureViewModel) {

  val uiStateFlow by viewModel.uiStateFlow.collectAsState()
  val uiState by viewModel.uiState

  MainContent(
    uiState = uiState,
    uiStateFlow = uiStateFlow,
    onRefresh = {
      viewModel.observeData()
    },
    applySortConfiguration = { sortConfiguration ->
      viewModel.applySortConfiguration(sortConfiguration)
    },
    dismissSortConfigurationDialog = {
      viewModel.dismissSortConfigurationDialog()
    },
    changeSorting = {
      viewModel.changeSorting()
    },
    removeAll = {
      viewModel.removeAll()
    },
    onFavoriteClick = { rate ->
      viewModel.removeFromFavoritesByName(rate)
    }
  )
}

@Composable
private fun MainContent(
  uiState: FavoritesFeatureUIState,
  uiStateFlow: State,
  onRefresh: () -> Unit,
  applySortConfiguration: (SortConfiguration) -> Unit,
  dismissSortConfigurationDialog: () -> Unit,
  changeSorting: () -> Unit,
  removeAll: () -> Unit,
  onFavoriteClick: (RateDomainModel) -> Unit,
) {
  when (uiState) {
    is FavoritesFeatureUIState.Error -> {
      uiState.state.message?.let {
        EmptyContentMessage(
          imgRes = com.example.exchangecurrencies.core.R.drawable.img_status_disclaimer_170,
          title = "Ошибка",
          description = it,
        )
      }
    }

    FavoritesFeatureUIState.Initial -> {
      ContentLoadingState()
    }

    is FavoritesFeatureUIState.Loaded -> {
      ScreenSlot(
        changeSorting = changeSorting,
        sortConfiguration = uiStateFlow.sortConfiguration,
        removeAll = removeAll
      ) {
        if (uiState.state.favoriteRates != null) {
          ContentStateReady(
            state = uiStateFlow,
            onRefresh = onRefresh,
            applySortConfiguration = applySortConfiguration,
            dismissSortConfigurationDialog = dismissSortConfigurationDialog,
            rates = uiState.state.favoriteRates,
            isRefreshing = uiState.state.refreshInProgress,
            onFavoriteClick = onFavoriteClick,
            isFavorite = uiStateFlow.isFavorite
          )
        } else {
          EmptyContentMessage(
            imgRes = com.example.exchangecurrencies.core.R.drawable.img_status_disclaimer_170,
            title = "Избранное",
            description = "Список пуст",
          )
        }
      }
    }

    is FavoritesFeatureUIState.Loading -> {
      ScreenSlot(
        changeSorting = changeSorting,
        removeAll = removeAll
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
  applySortConfiguration: (SortConfiguration) -> Unit,
  dismissSortConfigurationDialog: () -> Unit,
  isRefreshing: Boolean,
  onFavoriteClick: (RateDomainModel) -> Unit,
  isFavorite: Boolean
) {
  ModalBottomSheetScaffold(
    state = state,
    content = {
      ContentMain(
        onRefresh = onRefresh,
        rates = rates,
        isRefreshing = isRefreshing,
        onFavoriteClick = onFavoriteClick,
        isFavorite = isFavorite
      )
    },
    applySortConfiguration = applySortConfiguration,
    dismissSortConfigurationDialog = dismissSortConfigurationDialog,
  )
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
            SortConfiguration.SortDirection.Ascending -> SortConfiguration.SortDirection.Descending
            SortConfiguration.SortDirection.Descending -> SortConfiguration.SortDirection.Ascending
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
private fun ContentMain(
  modifier: Modifier = Modifier,
  rates: List<RateDomainModel>,
  onRefresh: () -> Unit,
  isRefreshing: Boolean,
  onFavoriteClick: (RateDomainModel) -> Unit,
  isFavorite: Boolean,
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.TopCenter
  ) {
    SwipeRefresh(
      isRefreshing = isRefreshing,
      onRefresh = onRefresh,
    ) {
      if (rates.isNotEmpty()) {
        FavoritesList(
          rates = rates,
          onFavoriteClick = onFavoriteClick,
          isFavorite = isFavorite
        )
      } else {
        EmptyContentMessage(
          imgRes = com.example.exchangecurrencies.core.R.drawable.img_status_disclaimer_170,
          title = "Избранное",
          description = "Список пуст",
        )
      }
    }
  }
}

@Composable
private fun ScreenSlot(
  changeSorting: () -> Unit,
  removeAll: () -> Unit,
  sortConfiguration: SortConfiguration? = null,
  content: @Composable () -> Unit,
) {
  Column(
    modifier = Modifier
      .statusBarsPadding()
  ) {
    TopAppBar(
      leftContent = {
        IconButton(
          modifier = Modifier.padding(horizontal = 6.dp),
          onClick = removeAll
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "delete all"
          )
        }
      },
      centerContent = {
        Text(
          modifier = Modifier.padding(start = 46.dp),
          text = "Избранное",
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
private fun FavoritesList(
  modifier: Modifier = Modifier,
  rates: List<RateDomainModel>,
  onFavoriteClick: (RateDomainModel) -> Unit,
  isFavorite: Boolean
) {
  val listState = rememberLazyListState()

  LazyColumn(
    modifier = modifier.padding(horizontal = 16.dp),
    state = listState
  ) {
    items(
      items = rates,
    ) { rate ->
      rate.currencyName?.let {
        RateItem(
          currency = it,
          value = rate.currencyValue.toString(),
          isFavorite = isFavorite,
          onFavoriteClick = { onFavoriteClick(rate) },
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
      color = ExchangeCurrenciesTheme.colors.contendTertiary
    )
  }
}
