package com.example.exchangecurrencies.components

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.exchangecurrencies.core.R
import com.example.exchangecurrencies.components.buttons.Button
import com.example.exchangecurrencies.components.buttons.ButtonDefaults
import com.example.exchangecurrencies.components.buttons.ButtonSize
import com.example.exchangecurrencies.components.buttons.ButtonSlot
import com.example.exchangecurrencies.ui.theme.ExchangeCurrenciesTheme

@Composable
fun SecondaryButtonSmall(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  text: String,
  icon: @Composable (() -> Unit)? = null,
  slotPosition: ButtonSlot = ButtonSlot.None,
  isProgressBarVisible: Boolean = false,
  enabled: Boolean = true,
) {
  Button(
    modifier = modifier,
    onClick = onClick,
    text = text,
    icon = icon,
    slotPosition = slotPosition,
    isProgressBarVisible = isProgressBarVisible,
    enabled = enabled,
    size = ButtonSize.Small,
    colors = ButtonDefaults.secondaryButtonColors(),
  )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
internal fun SortButtonPreview() {
  ExchangeCurrenciesTheme {
    SecondaryButtonSmall(
      onClick = {},
      slotPosition = ButtonSlot.AfterText,
      icon = {
        Icon(
          painter = painterResource(id = R.drawable.ic_arrow_up_round_cap_16),
          contentDescription = "sort direction",
          tint = ExchangeCurrenciesTheme.colors.black
        )
      },
      text = "text"
    )
  }
}
