package com.example.exchangecurrencies.main.domain

import com.example.exchangecurrencies.main.domain.mappers.toDomainMapRates
import com.example.exchangecurrencies.main.domain.mappers.toDomainModel
import com.example.exchangecurrencies.main.domain.mappers.toDomainRates
import com.example.exchangecurrencies.main.domain.mappers.toStorageRate
import com.example.exchangecurrencies.main.domain.mappers.toStorageRates
import com.example.exchangecurrencies.domain.entity.SortConfiguration
import com.example.exchangecurrencies.domain.entity.mapDistinctNotNullChanges
import com.example.exchangecurrencies.main.MainRepository
import com.example.exchangecurrencies.main.data.storage.entity.RateStorageModel
import com.example.exchangecurrencies.main.domain.entity.CurrencyDomainModel
import com.example.exchangecurrencies.main.domain.entity.RateDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MainModelImpl @Inject constructor(
  private val repository: MainRepository,
) : MainModel {

  private val stateFlow = MutableStateFlow(State())

  data class State(
    val allData: CurrencyDomainModel? = null,
    val baseCurrency: String? = null,
    val sortConfiguration: SortConfiguration = createDefaultSortConfiguration(),
    val rates: List<RateStorageModel> = listOf(),
    val isFavorite: Boolean = false,
  )

  override val rates: Flow<List<RateDomainModel>>
    get() = stateFlow.mapDistinctNotNullChanges { it.rates.toDomainRates() }.flowOn(Dispatchers.IO)

  override val baseCurrency: Flow<String>
    get() = stateFlow.mapDistinctNotNullChanges { it.baseCurrency }.flowOn(Dispatchers.IO)

  override val allData: Flow<CurrencyDomainModel>
    get() = stateFlow.mapDistinctNotNullChanges { it.allData }.flowOn(Dispatchers.IO)

  override val sortConfiguration: Flow<SortConfiguration>
    get() = stateFlow.mapDistinctNotNullChanges { it.sortConfiguration }.flowOn(Dispatchers.IO)

  override suspend fun getAllData(currencyName: String): CurrencyDomainModel {
    val data = repository.getAllData(currencyName).toDomainModel()
    stateFlow.update { state ->
      state.copy(
        allData = data,
        rates = data.rateDomainModels.toStorageRates(),
        baseCurrency = currencyName,
      )
    }
    return data
  }

  override suspend fun selectBaseCurrency(currencyName: String) {
    stateFlow.update { state ->
      state.copy(
        rates = repository.getAllData(currencyName).rates.toDomainMapRates().toStorageRates(),
        baseCurrency = currencyName
      )
    }
  }

  override suspend fun changeSortConfiguration(configuration: SortConfiguration) {
    stateFlow.update { state ->
      state.copy(
        sortConfiguration = configuration,
        rates = state.rates.toDomainRates().sort(configuration),
      )
    }
  }

  override suspend fun addOrRemoveFromFavorites(rateDomainModel: RateDomainModel) {
    repository.addOrRemoveFromFavorites(rateDomainModel.toStorageRate())
  }

  override suspend fun removeAll() {
    repository.removeALl()
  }

  override fun favorites(configuration: SortConfiguration): Flow<List<RateDomainModel>> {
    val data = repository.getFavorites(configuration).mapDistinctNotNullChanges {
      it.toDomainRates()
    }
    return data
  }
}

private fun List<RateDomainModel>.sort(sortConfiguration: SortConfiguration): List<RateStorageModel> {
  return if (sortConfiguration.direction == SortConfiguration.SortDirection.Ascending &&
    sortConfiguration.property == SortConfiguration.Property.Name
  ) {
    this.sortedBy { it.currencyName }.toStorageRates()
  } else if (sortConfiguration.direction == SortConfiguration.SortDirection.Descending &&
    sortConfiguration.property == SortConfiguration.Property.Name
  ) {
    this.sortedByDescending { it.currencyName }.toStorageRates()
  } else if (sortConfiguration.direction == SortConfiguration.SortDirection.Ascending &&
    sortConfiguration.property == SortConfiguration.Property.Value
  ) {
    this.sortedBy { it.currencyValue }.toStorageRates()
  } else if (sortConfiguration.direction == SortConfiguration.SortDirection.Descending &&
    sortConfiguration.property == SortConfiguration.Property.Value
  ) {
    this.sortedByDescending { it.currencyValue }.toStorageRates()
  } else {
    this.toStorageRates()
  }
}

private fun createDefaultSortConfiguration(): SortConfiguration {
  return SortConfiguration(
    property = SortConfiguration.Property.Name,
    direction = SortConfiguration.SortDirection.Ascending,
  )
}