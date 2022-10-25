package com.example.exchangecurrencies.main.data.di

import com.example.exchangecurrencies.main.data.MainRepositoryImpl
import com.example.exchangecurrencies.main.data.network.MainApi
import com.example.exchangecurrencies.main.data.storage.FavoriteCurrenciesDatabase
import com.example.exchangecurrencies.main.data.storage.entity.RateStorageModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainDataModule {
  @Singleton
  @Provides
  internal fun provideRepository(
    mainApiProvider: MainApiProvider,
    database: FavoriteCurrenciesDatabase
  ) =
    MainRepositoryImpl(mainApiProvider, database)

  @Provides
  fun provideApi(): MainApi {
    val interceptor = Interceptor { chain ->
      val newRequest =
        chain.request().newBuilder()
          .addHeader("apikey", API_KEY)
          .build()
      chain.proceed(newRequest)
    }

    val okhttp = OkHttpClient.Builder()
      .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
      .readTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
      .writeTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
      .addInterceptor(HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BASIC)
      })
      .addInterceptor(interceptor)
      .build()


    val listMyData: ParameterizedType =
      Types.newParameterizedType(List::class.java, RateStorageModel::class.java)
    val adapter: JsonAdapter<List<RateStorageModel>> = Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build().adapter(listMyData)

    val moshi = Moshi.Builder()
      .addLast(KotlinJsonAdapterFactory())
      .add(Type::class.java, adapter)
      .build()

    val retrofit = Retrofit.Builder()
      .client(okhttp)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .baseUrl("$BASE_URL/")
      .build()

    return retrofit
      .create(MainApi::class.java)
  }
}

internal const val BASE_URL = "https://api.apilayer.com/exchangerates_data"
internal const val API_KEY = "YDG58vwWI8VjWOG5dmTnjjk9DU8LbBer"
internal const val HTTP_CONNECT_TIMEOUT = 60_000L
