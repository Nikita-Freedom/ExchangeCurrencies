package com.example.exchangecurrencies.main.data.entity

import com.squareup.moshi.Json

data class CurrenciesNetworkModel(

  @Json(name = "base")
  val base: String,

  @Json(name = "date")
  val date: String,

  @Json(name = "rates")
  val rates: Map<String, Double>,

  @Json(name = "success")
  val success: Boolean,

  @Json(name = "timestamp")
  val timestamp: Long
)
