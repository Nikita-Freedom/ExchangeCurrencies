package com.example.exchangecurrencies.main.data.di

import com.example.exchangecurrencies.main.data.MainRepository
import com.example.exchangecurrencies.main.data.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MainRepositoryModule {
  @Binds
  abstract fun bindsMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository
}
