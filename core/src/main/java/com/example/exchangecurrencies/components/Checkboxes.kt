package com.example.exchangecurrencies.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exchangecurrencies.ui.theme.ExchangeCurrenciesTheme

@Composable
fun Checkbox(
  modifier: Modifier = Modifier,
  checked: Boolean,
  onCheckedChange: ((Boolean) -> Unit)?,
) {
  androidx.compose.material.Checkbox(
    modifier = modifier
      .background(ExchangeCurrenciesTheme.colors.contendSecondary, CheckboxShape),
    checked = checked,
    onCheckedChange = onCheckedChange,
    colors = CheckboxDefaults.colors(
      checkedColor = Color.Transparent,
      uncheckedColor = Color.Transparent,
      disabledColor = Color.Transparent,
      checkmarkColor = ExchangeCurrenciesTheme.colors.textPrimary,
    )
  )
}

private val CheckboxShape = RoundedCornerShape(8.dp)

@Preview(showBackground = true, widthDp = 360)
@Composable
internal fun CheckboxPreview() {
  ExchangeCurrenciesTheme {
    Row {
      Checkbox(
        checked = false,
        onCheckedChange = { }
      )
      Spacer(modifier = Modifier.width(16.dp))
      Checkbox(
        checked = true,
        onCheckedChange = { }
      )
    }
  }
}
