package dev.abhinav.stocktracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abhinav.stocktracker.model.DayPrice
import dev.abhinav.stocktracker.model.StockPriceResponseItem
import dev.abhinav.stocktracker.model.StockUiState
import dev.abhinav.stocktracker.repository.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StockPriceHistoryViewModel @Inject constructor(
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
                        _uiState.update {
                            it.copy(
                                symbol = response[0].symbol,
                                currentPrice = formatPrice(response[0].close),
                                changePercent = formatPercent(response[0].changePercent),
                                isPositive = response[0].changePercent >= 0,
                                companyName = getCompanyName(response[0].symbol),
                                exchange = "NASDAQ",
                                priceHistory = mapToDayPrices(response.take(5)),
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
        _uiState.update { it.copy(selectedTab = tabIndex) }
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