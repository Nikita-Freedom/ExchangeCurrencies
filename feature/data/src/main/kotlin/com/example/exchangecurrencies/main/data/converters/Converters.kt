package com.example.exchangecurrencies.main.data.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.exchangecurrencies.main.data.storage.entity.RateStorageModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.ParameterizedType

@ProvidedTypeConverter
class Converters {
  private val moshi = Moshi.Builder()
  private val listMyData: ParameterizedType =
    Types.newParameterizedType(List::class.java, RateStorageModel::class.java)
  private val adapter: JsonAdapter<List<RateStorageModel>> = moshi
    .add(KotlinJsonAdapterFactory())
    .build().adapter(listMyData)

  @TypeConverter
  fun listMyModelToJsonStr(listMyModel: List<RateStorageModel>?): String? {
    return adapter.toJson(listMyModel)
  }

  @TypeConverter
  fun jsonStrToListMyModel(jsonStr: String?): List<RateStorageModel>? {
    return jsonStr?.let { adapter.fromJson(jsonStr) }
  }

}