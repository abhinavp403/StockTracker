package dev.abhinav.stocktracker.model

data class WatchlistStock(
    val symbol: String,
    val companyName: String,
    val price: String,
    val changePercent: String,
    val isPositive: Boolean
)
