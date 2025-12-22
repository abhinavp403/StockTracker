package dev.abhinav.stocktracker.components.watchlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.abhinav.stocktracker.model.WatchlistStock

@Composable
fun WatchlistItem(
    stock: WatchlistStock,
    onRemoveClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stock Name and Symbol
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stock.symbol,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = stock.companyName,
                    fontSize = 13.sp,
                    color = Color(0xFF868E96)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Price Info
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = stock.price,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (stock.isPositive) Color(0xFFD4F5E9) else Color(0xFFFFE5E5)
                ) {
                    Text(
                        text = "${if (stock.isPositive) "↗" else "↘"} ${stock.changePercent}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (stock.isPositive) Color(0xFF10B981) else Color(0xFFEF4444),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Remove Button
            IconButton(
                onClick = onRemoveClick,
                modifier = Modifier.size(32.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE9ECEF),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove",
                            tint = Color(0xFF868E96),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun WatchlistItemPreview() {
    WatchlistItem(
        stock = WatchlistStock(
            symbol = "AAPL",
            companyName = "Apple Inc.",
            price = "$150.00",
            changePercent = "1.25%",
            isPositive = true
        ),
        onRemoveClick = {},
        onItemClick = {}
    )
}