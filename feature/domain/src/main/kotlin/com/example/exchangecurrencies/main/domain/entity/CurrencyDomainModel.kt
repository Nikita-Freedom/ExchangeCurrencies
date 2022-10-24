package com.example.exchangecurrencies.main.domain.entity

data class CurrencyDomainModel(
  val base: String,
  val date: String,
  val rateDomainModels: List<RateDomainModel>,
  val success: Boolean,
  val timestamp: Long
)
