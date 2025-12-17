package dev.abhinav.stocktracker.model

data class StockUiState(
    val symbol: String = "",
    val currentPrice: String = "",
    val changePercent: String = "",
    val isPositive: Boolean = true,
    val companyName: String = "",
    val exchange: String = "",
    val priceHistory: List<DayPrice> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedTab: Int = 0
)
