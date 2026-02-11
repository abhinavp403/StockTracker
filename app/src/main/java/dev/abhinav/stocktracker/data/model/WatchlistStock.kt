package dev.abhinav.stocktracker.data.model

data class WatchlistStock(
    val symbol: String,
    val companyName: String,
    val price: String,
    val changePercent: String,
    val isPositive: Boolean
)