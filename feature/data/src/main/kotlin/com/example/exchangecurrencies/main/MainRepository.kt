package com.example.exchangecurrencies.main

import com.example.exchangecurrencies.domain.entity.SortConfiguration
import com.example.exchangecurrencies.main.data.entity.CurrenciesNetworkModel
import com.example.exchangecurrencies.main.data.storage.entity.RateStorageModel
import kotlinx.coroutines.flow.Flow

interface MainRepository {
  suspend fun getAllData(currencyName: String): CurrenciesNetworkModel
  fun allData(currencyName: String): Flow<CurrenciesNetworkModel>
  suspend fun addOrRemoveFromFavorites(rate: RateStorageModel)
  suspend fun removeALl()
  suspend fun isExists(currencyName: String): Boolean
  fun getFavorites(sortConfiguration: SortConfiguration): Flow<List<RateStorageModel>>
}
