package com.example.exchangecurrencies.main.data.di

import com.example.exchangecurrencies.main.data.network.MainApi
import javax.inject.Inject

class MainApiProvider @Inject constructor(
  private val mainApi: MainApi,
) {
  suspend fun getCurrencies(base: String) = mainApi.getCurrencies(base)
}
