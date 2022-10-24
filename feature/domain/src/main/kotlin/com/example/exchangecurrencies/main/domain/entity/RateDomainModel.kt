package com.example.exchangecurrencies.main.domain.entity

data class RateDomainModel(
  val id: Long? = null,
  val isFavorite: Boolean = false,
  val currencyName: String? = null,
  val currencyValue: Double? = null
)
