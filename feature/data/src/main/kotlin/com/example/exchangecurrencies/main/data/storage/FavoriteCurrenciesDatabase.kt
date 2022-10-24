package com.example.exchangecurrencies.main.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.exchangecurrencies.main.data.storage.entity.CurrenciesStorageModel
import com.example.exchangecurrencies.main.data.converters.Converters
import com.example.exchangecurrencies.main.data.storage.dao.FavoriteRatesDao
import com.example.exchangecurrencies.main.data.storage.entity.RateStorageModel

@Database(entities = [CurrenciesStorageModel::class, RateStorageModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class FavoriteCurrenciesDatabase : RoomDatabase() {
  abstract fun favoriteRatesDao(): FavoriteRatesDao
}