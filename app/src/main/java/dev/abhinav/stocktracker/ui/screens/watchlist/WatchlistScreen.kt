package dev.abhinav.stocktracker.ui.screens.watchlist

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    onStockClick: (String) -> Unit = {},
    viewModel: StockWatchlistViewModel = koinViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var showSortSheet by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.error) {
        uiState.error?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    // Sort Bottom Sheet
    if (showSortSheet) {
        SortBottomSheet(
            currentSortOption = uiState.sortOption,
            onDismiss = { showSortSheet = false },
            onSortSelected = { option ->
                viewModel.setSortOption(option)
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
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
                    onStockClick = onStockClick,
                    onSortClick = { showSortSheet = true }
                )
            }
        }
    }
}
