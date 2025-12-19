package dev.abhinav.stocktracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abhinav.stocktracker.model.WatchlistStock
import dev.abhinav.stocktracker.repository.StockRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockWatchlistViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistUiState())
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    private val symbols = listOf("NVDA", "TSLA", "AMZN", "AAPL", "MSFT")

    init {
        fetchAllStockProfiles()
    }

    private fun fetchAllStockProfiles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val stocks = mutableListOf<WatchlistStock>()

            // Fetch all stocks in parallel
            val deferredStocks = symbols.map { symbol ->
                async {
                    repository.getStockProfile(symbol).fold(
                        onSuccess = { response ->
                            if (response.isNotEmpty()) {
                                val item = response[0]
                                WatchlistStock(
                                    symbol = item.symbol,
                                    companyName = item.companyName,
                                    price = formatPrice(item.price),
                                    changePercent = formatPercent(item.changePercentage),
                                    isPositive = item.changePercentage >= 0
                                )
                            } else {
                                println("Empty response for $symbol")
                                null
                            }
                        },
                        onFailure = { exception ->
                            println("Failed to fetch $symbol: ${exception.message}")
                            null
                        }
                    )
                }
            }

            // Wait for all to complete and filter out nulls
            stocks.addAll(deferredStocks.awaitAll().filterNotNull())

            _uiState.update {
                it.copy(
                    watchlistStocks = stocks,
                    isLoading = false
                )
            }

            println("Fetched ${stocks.size} stocks successfully")
        }
    }

    fun fetchStockProfile(symbol: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.getStockProfile(symbol).fold(
                onSuccess = { response ->
                    if (response.isNotEmpty()) {
                        val item = response[0]
                        val newStock = WatchlistStock(
                            symbol = item.symbol,
                            companyName = item.companyName,
                            price = formatPrice(item.price),
                            changePercent = formatPercent(item.changePercentage),
                            isPositive = item.changePercentage >= 0
                        )

                        val updatedList = _uiState.value.watchlistStocks.toMutableList()
                        updatedList.add(newStock)

                        _uiState.update {
                            it.copy(
                                watchlistStocks = updatedList,
                                isLoading = false
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "No data found for $symbol"
                            )
                        }
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to fetch stock profile"
                        )
                    }
                }
            )
        }
    }

    fun removeStock(symbol: String) {
        val updatedList = _uiState.value.watchlistStocks.filter { it.symbol != symbol }
        _uiState.update { it.copy(watchlistStocks = updatedList) }
    }

    private fun formatPrice(price: Double): String {
        return "${String.format("%.2f", price)}"
    }

    private fun formatPercent(percent: Double): String {
        val sign = if (percent >= 0) "+" else ""
        return "$sign${String.format("%.2f", percent)}%"
    }
}

data class WatchlistUiState(
    val watchlistStocks: List<WatchlistStock> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)