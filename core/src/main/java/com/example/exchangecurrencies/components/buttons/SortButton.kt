package com.example.exchangecurrencies.components.buttons

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.exchangecurrencies.core.R
import com.example.exchangecurrencies.components.SecondaryButtonSmall
import com.example.exchangecurrencies.domain.entity.SortConfiguration
import com.example.exchangecurrencies.ui.theme.ExchangeCurrenciesTheme

@Composable
fun SortButton(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  text: String,
  direction: SortConfiguration.SortDirection,
) {
  SecondaryButtonSmall(
    modifier = modifier,
    onClick = onClick,
    slotPosition = ButtonSlot.AfterText,
    icon = {
      val icon = when (direction) {
        SortConfiguration.SortDirection.Ascending -> R.drawable.ic_arrow_up_round_cap_16
        SortConfiguration.SortDirection.Descending -> R.drawable.ic_arrow_down_round_cap_16
      }
      Icon(
        painter = painterResource(id = icon),
        contentDescription = "sort direction",
        tint = ExchangeCurrenciesTheme.colors.black
      )
    },
    text = text
  )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
internal fun SortButtonPreview() {
  ExchangeCurrenciesTheme {
    SortButton(
      direction = SortConfiguration.SortDirection.Ascending,
      text = "Название",
      onClick = {}
    )
  }
}
