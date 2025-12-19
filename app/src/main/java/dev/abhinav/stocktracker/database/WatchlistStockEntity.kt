package dev.abhinav.stocktracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist_stocks")
data class WatchlistStockEntity(
    @PrimaryKey
    val symbol: String,
    val companyName: String,
    val price: Double,
    val changePercentage: Double,
    val lastUpdated: Long
)
