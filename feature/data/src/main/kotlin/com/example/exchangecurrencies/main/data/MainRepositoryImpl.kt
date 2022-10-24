package com.example.exchangecurrencies.main.data

import com.example.exchangecurrencies.domain.entity.SortConfiguration
import com.example.exchangecurrencies.main.MainRepository
import com.example.exchangecurrencies.main.data.di.MainApiProvider
import com.example.exchangecurrencies.main.data.entity.CurrenciesNetworkModel
import com.example.exchangecurrencies.main.data.storage.FavoriteCurrenciesDatabase
import com.example.exchangecurrencies.main.data.storage.entity.RateStorageModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
  private val mainApiProvider: MainApiProvider,
  private val database: FavoriteCurrenciesDatabase,
) : MainRepository {

  override suspend fun getAllData(currencyName: String): CurrenciesNetworkModel {
    return mainApiProvider.getCurrencies(currencyName)
  }

  override fun allData(currencyName: String): Flow<CurrenciesNetworkModel> = flow {
    val data = mainApiProvider.getCurrencies(currencyName)
    emit(data)
  }.flowOn(IO)

  override suspend fun addOrRemoveFromFavorites(rate: RateStorageModel) {
    val isExists = database.favoriteRatesDao().exists(rate.currencyName)
    if (!isExists) {
      database.favoriteRatesDao().addToFavorites(rate)
    } else {
      rate.currencyName?.let { database.favoriteRatesDao().deleteByName(it) }
    }
  }

  override suspend fun removeALl() {
    database.favoriteRatesDao().deleteAll()
  }

  override suspend fun isExists(currencyName: String): Boolean {
    return database.favoriteRatesDao().exists(currencyName)
  }

  override fun getFavorites(sortConfiguration: SortConfiguration): Flow<List<RateStorageModel>> {
    return database.favoriteRatesDao().getRates(sortConfiguration).flowOn(IO)
  }

}
