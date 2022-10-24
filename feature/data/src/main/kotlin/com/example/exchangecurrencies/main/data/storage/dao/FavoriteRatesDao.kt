package com.example.exchangecurrencies.main.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exchangecurrencies.main.data.storage.entity.RateStorageModel
import com.example.exchangecurrencies.domain.entity.SortConfiguration
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRatesDao {

  fun getRates(sortConfiguration: SortConfiguration): Flow<List<RateStorageModel>> {
    return if (sortConfiguration.direction == SortConfiguration.SortDirection.Ascending &&
      sortConfiguration.property == SortConfiguration.Property.Name
    ) {
      getRatesSortedByNameAsc()
    } else if (sortConfiguration.direction == SortConfiguration.SortDirection.Descending &&
      sortConfiguration.property == SortConfiguration.Property.Name
    ) {
      getRatesSortedByNameDesc()
    } else if (sortConfiguration.direction == SortConfiguration.SortDirection.Ascending &&
      sortConfiguration.property == SortConfiguration.Property.Value
    ) {
      getRatesSortedByValueAsc()
    } else if (sortConfiguration.direction == SortConfiguration.SortDirection.Descending &&
      sortConfiguration.property == SortConfiguration.Property.Value
    ) {
      getRatesSortedByValueDesc()
    } else {
      allRates()
    }
  }

  @Query("select * from rates_table order by currency_name asc")
  fun getRatesSortedByNameAsc(): Flow<List<RateStorageModel>>

  @Query("select * from rates_table order by currency_name desc")
  fun getRatesSortedByNameDesc(): Flow<List<RateStorageModel>>

  @Query("select * from rates_table order by currency_value asc")
  fun getRatesSortedByValueAsc(): Flow<List<RateStorageModel>>

  @Query("select * from rates_table order by currency_value desc")
  fun getRatesSortedByValueDesc(): Flow<List<RateStorageModel>>

  @Query("select * from rates_table")
  fun allRates(): Flow<List<RateStorageModel>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addToFavorites(rate: RateStorageModel)

  @Query("DELETE FROM rates_table WHERE currency_name = :rateName")
  suspend fun deleteByName(rateName: String)

  @Query("DELETE FROM rates_table")
  suspend fun deleteAll()

  @Query("SELECT EXISTS (SELECT 1 FROM rates_table WHERE currency_name = :name)")
  suspend fun exists(name: String?): Boolean

}