package com.example.exchangecurrencies.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exchangecurrencies.ui.theme.ExchangeCurrenciesTheme

@Composable
fun RateItem(
  currency: String,
  value: String,
  onFavoriteClick: () -> Unit = {},
  isFavorite: Boolean
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
      .clickable(onClick = onFavoriteClick),
    backgroundColor = ExchangeCurrenciesTheme.colors.backgroundPrimary,
    shape = RoundedCornerShape(8.dp),
    elevation = 3.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        modifier = Modifier.weight(1f),
        text = currency,
        style = ExchangeCurrenciesTheme.typography.caption1,
        color = ExchangeCurrenciesTheme.colors.black
      )
      Spacer(modifier = Modifier.weight(1f))
      Text(
        modifier = Modifier.weight(1f),
        text = value,
        style = ExchangeCurrenciesTheme.typography.caption1,
        color = ExchangeCurrenciesTheme.colors.black
      )
      Spacer(modifier = Modifier.weight(1f))
      IconButton(
        onClick = { onFavoriteClick() }
      ) {
        val icon = if (isFavorite) {
          Icons.Default.Favorite
        } else {
          Icons.Default.Star
        }
        Icon(
          imageVector = icon,
          contentDescription = null
        )
      }
    }
  }
}