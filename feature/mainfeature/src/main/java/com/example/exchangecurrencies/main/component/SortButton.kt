package com.example.exchangecurrencies.main.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.exchangecurrencies.core.R.string
import com.example.exchangecurrencies.domain.entity.SortConfiguration
import com.example.exchangecurrencies.components.buttons.SortButton as SortButtonCore

@Composable
internal fun SortButton(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  configuration: SortConfiguration,
) {
  val text = when (configuration.property) {
    SortConfiguration.Property.Name -> stringResource(id = string.rates_sort_option_name)
    SortConfiguration.Property.Value -> stringResource(id = string.rates_sort_option_price)
  }
  SortButtonCore(
    modifier = modifier,
    onClick = onClick,
    text = text,
    direction = configuration.direction
  )
}
