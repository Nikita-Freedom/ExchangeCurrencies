package com.example.exchangecurrencies.main.data.network

import com.example.exchangecurrencies.main.data.entity.CurrenciesNetworkModel
import retrofit2.http.GET
import retrofit2.http.Query

interface MainApi {
  @GET("latest?symbols=")
  suspend fun getCurrencies(@Query("base") base: String): CurrenciesNetworkModel
}