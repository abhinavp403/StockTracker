package dev.abhinav.stocktracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abhinav.stocktracker.model.WatchlistStock
import dev.abhinav.stocktracker.repository.StockWatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StockWatchlistViewModel @Inject constructor(
    private val repository: StockWatchlistRepository
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
                        watchlistStocks = stocks,
                        lastUpdated = lastUpdateString
                    )
                }
            }
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
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.refreshStockProfile(symbol).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to add stock"
                        )
                    }
                }
            )
        }
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
    val lastUpdated: String? = null
)