package com.example.exchangecurrencies.main.domain

import com.example.exchangecurrencies.domain.entity.SortConfiguration
import com.example.exchangecurrencies.main.domain.entity.CurrencyDomainModel
import com.example.exchangecurrencies.main.domain.entity.RateDomainModel
import kotlinx.coroutines.flow.Flow

interface MainModel {
  val allData: Flow<CurrencyDomainModel>
  val rates: Flow<List<RateDomainModel>>
  val baseCurrency: Flow<String>
  val sortConfiguration: Flow<SortConfiguration>

  suspend fun getAllData(currencyName: String): CurrencyDomainModel
  suspend fun selectBaseCurrency(currencyName: String)
  suspend fun changeSortConfiguration(configuration: SortConfiguration)

  fun favorites(configuration: SortConfiguration): Flow<List<RateDomainModel>>
  suspend fun addOrRemoveFromFavorites(rateDomainModel: RateDomainModel)
  suspend fun removeAll()
}