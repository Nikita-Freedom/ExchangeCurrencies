package com.example.exchangecurrencies.main.data.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
  tableName = "rates_table",
)
data class RateStorageModel(
  @ColumnInfo(name = "rate_id")
  @PrimaryKey(autoGenerate = true)
  val rateId: Long? = null,

  @ColumnInfo(name = "currency_name")
  val currencyName: String? = null,

  @ColumnInfo(name = "currency_value")
  val currencyValue: Double? = null
)
