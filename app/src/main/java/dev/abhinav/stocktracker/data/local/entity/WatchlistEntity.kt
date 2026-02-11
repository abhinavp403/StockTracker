package dev.abhinav.stocktracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist_stocks")
data class WatchlistEntity(
    @PrimaryKey
    val symbol: String,
    val companyName: String,
    val price: Double,
    val changePercentage: Double,
    val lastUpdated: Long
)