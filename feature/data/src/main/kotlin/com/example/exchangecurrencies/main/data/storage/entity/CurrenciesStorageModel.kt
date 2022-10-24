package com.example.exchangecurrencies.main.data.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
  tableName = "currencies_table",
  foreignKeys = [ForeignKey(
    entity = RateStorageModel::class,
    parentColumns = ["rate_id"],
    childColumns = ["rate_idPK"],
    onDelete = ForeignKey.CASCADE
  )],
)
data class CurrenciesStorageModel(
  @PrimaryKey(autoGenerate = true)
  val id: Int? = null,

  @ColumnInfo(name = "base")
  val base: String,

  @ColumnInfo(name = "date")
  val date: String,

  @ColumnInfo(name = "success")
  val success: Boolean,

  @ColumnInfo(name = "timestamp")
  val timestamp: Long,

  @ColumnInfo(name = "rates")
  val rates: List<RateStorageModel>,

  @ColumnInfo(name = "rate_idPK")
  val rateId: Int
)
