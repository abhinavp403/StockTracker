package dev.abhinav.stocktracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.abhinav.stocktracker.model.DayPrice
import dev.abhinav.stocktracker.model.Ohlc
import dev.abhinav.stocktracker.model.StockPriceResponseItem
import dev.abhinav.stocktracker.repository.StockRepository
import dev.abhinav.stocktracker.util.Trend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

class StockPriceHistoryViewModel(
    private val repository: StockRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StockUiState())
    val uiState: StateFlow<StockUiState> = _uiState.asStateFlow()

    fun fetchStockData(symbol: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.getStockHistory(symbol).fold(
                onSuccess = { response ->
                    if (response.isNotEmpty()) {
                        val ohlcList = response.map {
                            Ohlc(
                                open = it.open,
                                high = it.high,
                                low = it.low,
                                close = it.close
                            )
                        }

                        val (bestBuy, bestSell, trend) =
                            calculateBestBuySell(ohlcList, _uiState.value.selectedTab)

                        _uiState.update {
                            it.copy(
                                symbol = response[0].symbol,
                                currentPrice = formatPrice(response[0].close),
                                changePercent = formatPercent(response[0].changePercent),
                                isPositive = response[0].changePercent >= 0,
                                companyName = getCompanyName(response[0].symbol),
                                exchange = "NASDAQ",
                                priceHistory = mapToDayPrices(response.take(5)),
                                bestBuyPrice = formatPrice(bestBuy),
                                bestSellPrice = formatPrice(bestSell),
                                trend = trend,
                                isLoading = false
                            )
                        }
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    }
                }
            )
        }
    }

    private fun calculateBestBuySell(
        ohlcList: List<Ohlc>,
        selectedTab: Int
    ): Triple<Double, Double, Trend> {

        val requiredDays = when (selectedTab) {
            1 -> 5   // 5 Days tab
            else -> 3 // Default: 3 Days tab
        }

        if (ohlcList.size < requiredDays) {
            return Triple(0.0, 0.0, Trend.NEUTRAL)
        }

        val recent = ohlcList.take(requiredDays)
        val latest = recent.first()

        // --- Pivot (latest day still matters most) ---
        val pivot = (latest.high + latest.low + latest.close) / 3

        val support = (2 * pivot) - latest.high
        val resistance = (2 * pivot) - latest.low

        // --- Trend detection (scaled by selected range) ---
        val trend = when {
            recent.zipWithNext().all { it.first.close < it.second.close } ->
                Trend.BEARISH

            recent.zipWithNext().all { it.first.close > it.second.close } ->
                Trend.BULLISH

            else -> Trend.NEUTRAL
        }

        // --- Volatility over selected range ---
        val avgRange = recent
            .map { it.high - it.low }
            .average()

        val bufferMultiplier = when (selectedTab) {
            1 -> 0.20   // smoother for 5 days
            else -> 0.15 // tighter for 3 days
        }

        val buffer = avgRange * bufferMultiplier

        // --- Final Buy/Sell levels ---
        val bestBuy = when (trend) {
            Trend.BULLISH -> support + buffer
            Trend.BEARISH -> latest.low
            Trend.NEUTRAL -> support
        }

        val bestSell = when (trend) {
            Trend.BULLISH -> resistance
            Trend.BEARISH -> resistance - buffer
            Trend.NEUTRAL -> resistance
        }

        return Triple(bestBuy, bestSell, trend)
    }

    private fun mapToDayPrices(items: List<StockPriceResponseItem>): List<DayPrice> {
        val today = LocalDate.now()

        return items.mapIndexed { index, item ->
            val itemDate = LocalDate.parse(item.date)
            val daysDiff = ChronoUnit.DAYS.between(itemDate, today).toInt()

            val (dayName, label) = when (daysDiff) {
                0 -> getDayOfWeek(itemDate) to "Today"
                1 -> getDayOfWeek(itemDate) to "Yesterday"
                else -> getDayOfWeek(itemDate) to "$daysDiff Days ago"
            }

            DayPrice(
                day = dayName,
                label = label,
                open = formatPrice(item.open),
                close = formatPrice(item.close),
                high = formatPrice(item.high),
                low = formatPrice(item.low),
                date = item.date,
                dailyChange = formatPercentSimple(item.changePercent),
                isPositiveChange = item.changePercent >= 0
            )
        }
    }

    fun selectTab(tabIndex: Int) {
        viewModelScope.launch {
            val currentHistory = _uiState.value.priceHistory
            if (currentHistory.isNotEmpty()) {
                // Reconstruct OHLC list from priceHistory
                val ohlcList = currentHistory.map { dayPrice ->
                    Ohlc(
                        open = dayPrice.open.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0,
                        high = dayPrice.high.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0,
                        low = dayPrice.low.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0,
                        close = dayPrice.close.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0
                    )
                }

                val (bestBuy, bestSell, trend) = calculateBestBuySell(ohlcList, tabIndex)

                _uiState.update {
                    it.copy(
                        selectedTab = tabIndex,
                        bestBuyPrice = formatPrice(bestBuy),
                        bestSellPrice = formatPrice(bestSell),
                        trend = trend
                    )
                }
            } else {
                _uiState.update { it.copy(selectedTab = tabIndex) }
            }
        }
    }

    private fun formatPercentSimple(percent: Double): String {
        val sign = if (percent >= 0) "+" else ""
        return "$sign${String.format("%.2f", percent)}%"
    }

    private fun getDayOfWeek(date: LocalDate): String {
        return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    private fun formatPrice(price: Double): String {
        return "$${String.format("%,.2f", price)}"
    }

    private fun formatPercent(percent: Double): String {
        val arrow = if (percent >= 0) "↗" else "↘"
        val sign = if (percent >= 0) "+" else ""
        return "$arrow $sign${String.format("%.1f", percent)}%"
    }

    private fun getCompanyName(symbol: String): String {
        return when(symbol) {
            "TSLA" -> "Tesla, Inc."
            "AAPL" -> "Apple Inc."
            "GOOGL" -> "Alphabet Inc."
            "MSFT" -> "Microsoft Corporation"
            "AMZN" -> "Amazon.com Inc."
            else -> "$symbol Inc."
        }
    }
}

data class StockUiState(
    val symbol: String = "",
    val currentPrice: String = "",
    val changePercent: String = "",
    val isPositive: Boolean = true,
    val companyName: String = "",
    val exchange: String = "",
    val priceHistory: List<DayPrice> = emptyList(),
    val bestBuyPrice: String = "",
    val bestSellPrice: String = "",
    val trend: Trend = Trend.NEUTRAL,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedTab: Int = 0
)