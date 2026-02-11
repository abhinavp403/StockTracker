package dev.abhinav.stocktracker.ui.screens.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.abhinav.stocktracker.data.model.WatchlistStock
import dev.abhinav.stocktracker.data.repository.WatchlistRepository
import dev.abhinav.stocktracker.util.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StockWatchlistViewModel(
    private val repository: WatchlistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistUiState())
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    private val symbols = listOf("NVDA", "TSLA", "AMZN", "AAPL", "MSFT")

    init {
        observeWatchlist()
        checkAndRefreshData()
    }

    private fun observeWatchlist() {
        viewModelScope.launch {
            repository.watchlistStocks.collect { entities ->
                val stocks = entities.map { entity ->
                    WatchlistStock(
                        symbol = entity.symbol,
                        companyName = entity.companyName,
                        price = formatPrice(entity.price),
                        changePercent = formatPercent(entity.changePercentage),
                        isPositive = entity.changePercentage >= 0
                    )
                }

                val lastUpdateTime = entities.maxOfOrNull { it.lastUpdated }
                val lastUpdateString = lastUpdateTime?.let {
                    val sdf = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
                    "Last updated: ${sdf.format(Date(it))}"
                }

                _uiState.update {
                    it.copy(
                        watchlistStocks = sortStocks(stocks, it.sortOption),
                        lastUpdated = lastUpdateString
                    )
                }
            }
        }
    }

    fun setSortOption(option: SortOption) {
        _uiState.update {
            it.copy(
                sortOption = option,
                watchlistStocks = sortStocks(it.watchlistStocks, option)
            )
        }
    }

    private fun sortStocks(stocks: List<WatchlistStock>, sortOption: SortOption): List<WatchlistStock> {
        return when (sortOption) {
            SortOption.SYMBOL_ASC -> stocks.sortedBy { it.symbol }
            SortOption.SYMBOL_DESC -> stocks.sortedByDescending { it.symbol }
            SortOption.COMPANY_NAME_ASC -> stocks.sortedBy { it.companyName }
            SortOption.COMPANY_NAME_DESC -> stocks.sortedByDescending { it.companyName }
            SortOption.PRICE_HIGH_TO_LOW -> stocks.sortedByDescending { it.price }
            SortOption.PRICE_LOW_TO_HIGH -> stocks.sortedBy { it.price }
            SortOption.CHANGE_HIGH_TO_LOW -> stocks.sortedByDescending { it.changePercent }
            SortOption.CHANGE_LOW_TO_HIGH -> stocks.sortedBy { it.changePercent }
        }
    }

    private fun checkAndRefreshData() {
        viewModelScope.launch {
            val shouldRefresh = repository.shouldRefreshData()

            if (shouldRefresh) {
                refreshAllStocks()
            }
        }
    }

    fun refreshAllStocks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.refreshAllStocks(symbols).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                    println("Successfully refreshed all stocks")
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to refresh stocks"
                        )
                    }
                }
            )
        }
    }

    fun addStock(symbol: String) {
        viewModelScope.launch {
            // Check if stock already exists
            val exists = _uiState.value.watchlistStocks.any { it.symbol == symbol }
            if (exists) {
                _uiState.update {
                    it.copy(error = "$symbol is already in your watchlist")
                }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.refreshStockProfile(symbol).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Failed to add stock"
                        )
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun removeStock(symbol: String) {
        viewModelScope.launch {
            repository.removeStock(symbol)
        }
    }

    private fun formatPrice(price: Double): String {
        return "$${String.format("%.2f", price)}"
    }

    private fun formatPercent(percent: Double): String {
        val sign = if (percent >= 0) "+" else ""
        return "$sign${String.format("%.2f", percent)}%"
    }
}

data class WatchlistUiState(
    val watchlistStocks: List<WatchlistStock> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val lastUpdated: String? = null,
    val sortOption: SortOption = SortOption.SYMBOL_ASC
)