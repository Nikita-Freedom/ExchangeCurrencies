package com.example.exchangecurrencies.main.domain.di

import com.example.exchangecurrencies.main.domain.MainModel
import com.example.exchangecurrencies.main.domain.MainModelImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainDomainModule {
  @Binds
  abstract fun bindMainModel(mainModelImpl: MainModelImpl): MainModel
}