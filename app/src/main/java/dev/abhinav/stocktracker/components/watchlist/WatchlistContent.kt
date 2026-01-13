package dev.abhinav.stocktracker.components.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.abhinav.stocktracker.model.WatchlistStock
import dev.abhinav.stocktracker.ui.theme.GrayNeutral
import dev.abhinav.stocktracker.ui.theme.GreenPositive

@Composable
fun WatchlistContent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    watchlistStocks: List<WatchlistStock>,
    lastUpdated: String?,
    onRemoveStock: (String) -> Unit,
    onRefresh: () -> Unit,
    onAddStock: (String) -> Unit,
    onStockClick: (String) -> Unit,
    onSortClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            onSearch = onAddStock
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Watchlist Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Watchlist",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(
                        onClick = onSortClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Sort",
                            tint = GrayNeutral
                        )
                    }
                }

                if (lastUpdated != null) {
                    Text(
                        text = lastUpdated,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = GreenPositive,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFD4F5E9)
                ) {
                    Text(
                        text = "${watchlistStocks.size} Assets",
                        style = MaterialTheme.typography.bodySmall,
                        color = GreenPositive,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Watchlist Items
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(watchlistStocks, key = { it.symbol }) { stock ->
                WatchlistItem(
                    stock = stock,
                    onRemoveClick = { onRemoveStock(stock.symbol) },
                    onItemClick = { onStockClick(stock.symbol) }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewWatchlistContent() {
    val sampleStocks = listOf(
        WatchlistStock("AAPL", "Apple Inc.", "$150.00", "+1.25%", true),
        WatchlistStock("GOOGL", "Alphabet Inc.", "$2800.00", "-0.75%", false),
        WatchlistStock("AMZN", "Amazon.com Inc.", "$3400.00", "+0.50%", true)
    )

    WatchlistContent(
        searchQuery = "",
        onSearchQueryChange = {},
        watchlistStocks = sampleStocks,
        lastUpdated = "",
        onRemoveStock = {},
        onRefresh = {},
        onAddStock = {},
        onStockClick = {},
        onSortClick = {}
    )
}