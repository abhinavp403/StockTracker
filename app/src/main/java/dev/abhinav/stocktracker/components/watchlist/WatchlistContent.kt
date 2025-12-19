package dev.abhinav.stocktracker.components.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.abhinav.stocktracker.model.WatchlistStock

@Composable
fun WatchlistContent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    watchlistStocks: List<WatchlistStock>,
    lastUpdated: String?,
    onRemoveStock: (String) -> Unit,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Watchlist Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Your Watchlist",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                if (lastUpdated != null) {
                    Text(
                        text = lastUpdated,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
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
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFD4F5E9)
                ) {
                    Text(
                        text = "${watchlistStocks.size} Assets",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF10B981),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Watchlist Items
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(watchlistStocks, key = { it.symbol }) { stock ->
                WatchlistItem(
                    stock = stock,
                    onRemoveClick = { onRemoveStock(stock.symbol) }
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
        onRefresh = {}
    )
}