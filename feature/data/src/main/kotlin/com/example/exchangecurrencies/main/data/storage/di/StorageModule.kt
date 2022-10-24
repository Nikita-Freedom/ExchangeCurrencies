package com.example.exchangecurrencies.main.data.storage.di

import android.content.Context
import androidx.room.Room
import com.example.exchangecurrencies.main.data.storage.FavoriteCurrenciesDatabase
import com.example.exchangecurrencies.main.data.storage.dao.FavoriteRatesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

  @Provides
  @Singleton
  fun provideFavoriteCurrenciesDao(favoriteCurrenciesDatabase: FavoriteCurrenciesDatabase): FavoriteRatesDao {
    return favoriteCurrenciesDatabase.favoriteRatesDao()
  }

  @Provides
  @Singleton
  fun provideCurrenciesDatabase(@ApplicationContext appContext: Context): FavoriteCurrenciesDatabase {
    return Room.databaseBuilder(
      appContext,
      FavoriteCurrenciesDatabase::class.java,
      "currencies_db"
    )
      .fallbackToDestructiveMigration()
      .build()
  }
}