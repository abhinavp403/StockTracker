package dev.abhinav.stocktracker.components.watchlist

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.abhinav.stocktracker.viewmodel.StockWatchlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    viewModel: StockWatchlistViewModel,
    onStockClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.error) {
        uiState.error?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        when {
            uiState.isLoading && uiState.watchlistStocks.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                WatchlistContent(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    watchlistStocks = uiState.watchlistStocks,
                    lastUpdated = uiState.lastUpdated,
                    onRemoveStock = { viewModel.removeStock(it) },
                    onRefresh = { viewModel.refreshAllStocks() },
                    onAddStock = { viewModel.addStock(it) },
                    onStockClick = onStockClick
                )
            }
        }
    }
}
