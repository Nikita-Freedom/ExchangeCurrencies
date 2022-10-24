package com.example.exchangecurrencies.main.domain.mappers

import com.example.exchangecurrencies.main.data.entity.CurrenciesNetworkModel
import com.example.exchangecurrencies.main.data.storage.entity.RateStorageModel
import com.example.exchangecurrencies.main.domain.entity.CurrencyDomainModel
import com.example.exchangecurrencies.main.domain.entity.RateDomainModel

fun CurrenciesNetworkModel.toDomainModel(): CurrencyDomainModel {
  return CurrencyDomainModel(
    base = this.base,
    date = this.date,
    rateDomainModels = this.rates.toDomainMapRates(),
    success = this.success,
    timestamp = this.timestamp
  )
}

//fun CurrenciesStorageModel.toStorageModel(): CurrenciesDomainModel {
//  return CurrenciesDomainModel(
//    base = this.base,
//    date = this.date,
//    success = this.success,
//    timestamp = this.timestamp,
//    rateDomainModels = this.rates.toDomainRates()
//  )
//}

fun List<RateDomainModel>.toStorageRates(): List<RateStorageModel> {
  return this.map {
    RateStorageModel(
      rateId = it.id,
      currencyName = it.currencyName,
      currencyValue = it.currencyValue
    )
  }
}

fun Map<String, Double>.toRates(): List<RateStorageModel> {
  return this.map {
    RateStorageModel(
      currencyName = it.key,
      currencyValue = it.value
    )
  }
}
fun List<RateStorageModel>.toDomainRates(
//  isFavorite: Boolean
): List<RateDomainModel> {
  return this.map {
    RateDomainModel(
      id = it.rateId,
      currencyName = it.currencyName,
      currencyValue = it.currencyValue,
      isFavorite = true
    )
  }
}

fun Map<String, Double>.toDomainMapRates(): List<RateDomainModel> {
  return this.map {
    RateDomainModel(
      currencyName = it.key,
      currencyValue = it.value
    )
  }
}

fun RateDomainModel.toStorageRate(): RateStorageModel {
  return RateStorageModel(
    rateId = this.id,
    currencyName = this.currencyName,
    currencyValue = this.currencyValue
  )
}

