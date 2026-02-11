package dev.abhinav.stocktracker.ui.screens.stock

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.abhinav.stocktracker.ui.theme.GrayNeutral
import dev.abhinav.stocktracker.ui.theme.GreenPositive
import dev.abhinav.stocktracker.ui.theme.PinkRed
import dev.abhinav.stocktracker.ui.theme.RedNegative
import dev.abhinav.stocktracker.ui.theme.StockTrackerTheme
import dev.abhinav.stocktracker.ui.theme.YellowGreen
import dev.abhinav.stocktracker.util.Trend
import org.koin.androidx.compose.koinViewModel

@Composable
fun StockPriceContent(
    uiState: StockUiState,
    viewModel: StockPriceViewModel = koinViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // Header Section
        Text(
            text = uiState.symbol,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = uiState.currentPrice,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(12.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (uiState.isPositive) YellowGreen else PinkRed
            ) {
                Text(
                    text = uiState.changePercent,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        Text(
            text = "${uiState.companyName} • ${uiState.exchange}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Tab Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TabButton(
                text = "3 Days",
                isSelected = uiState.selectedTab == 0,
                onClick = { viewModel.selectTab(0) },
                modifier = Modifier.weight(1f)
            )
            TabButton(
                text = "5 Days",
                isSelected = uiState.selectedTab == 1,
                onClick = { viewModel.selectTab(1) },
                modifier = Modifier.weight(1f)
            )
        }

        // Price History Header
        Text(
            text = "PRICE HISTORY",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF7D8B6B),
            letterSpacing = 1.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Trading Recommendation Card
        TradingRecommendationCard(
            bestBuyPrice = uiState.bestBuyPrice,
            bestSellPrice = uiState.bestSellPrice,
            trend = uiState.trend
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Price Cards
        val priceHistory = if (uiState.selectedTab == 0) {
            uiState.priceHistory.take(3)
        } else {
            uiState.priceHistory
        }
        priceHistory.forEach { dayPrice ->
            StockPriceCard(dayPrice)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color(0xFF2D2D2D) else Color.White,
        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
        onClick = onClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) Color.White else Color.Gray,
            modifier = Modifier.padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TradingRecommendationCard(
    bestBuyPrice: String,
    bestSellPrice: String,
    trend: Trend
) {
    val trendColor = when (trend) {
        Trend.BULLISH -> GreenPositive
        Trend.BEARISH -> RedNegative
        Trend.NEUTRAL -> GrayNeutral
    }

    val trendLabel = when (trend) {
        Trend.BULLISH -> "Bullish Trend"
        Trend.BEARISH -> "Bearish Trend"
        Trend.NEUTRAL -> "Neutral Trend"
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Trading Levels",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = trendColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = trendLabel,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = trendColor,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Prices Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                PriceBlock(
                    label = "BEST BUY",
                    price = bestBuyPrice,
                    priceColor = GreenPositive
                )

                PriceBlock(
                    label = "BEST SELL",
                    price = bestSellPrice,
                    priceColor = RedNegative
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Insight
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF8F9FA)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "💡",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = when (trend) {
                            Trend.BULLISH ->
                                "Price is trending upward. Buying near support and selling near resistance is favored."
                            Trend.BEARISH ->
                                "Downtrend detected. Caution advised, selling near resistance may be safer."
                            Trend.NEUTRAL ->
                                "Price is consolidating. Wait for a clearer breakout or rejection."
                        },
                        fontSize = 14.sp,
                        color = Color(0xFF495057),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PriceBlock(
    label: String,
    price: String,
    priceColor: Color
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = price,
            style = MaterialTheme.typography.headlineMedium,
            color = priceColor,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun StockPriceContentPreview() {
    StockTrackerTheme {
        StockPriceContent(
            uiState = StockUiState(
                symbol = "TSLA",
                currentPrice = "$650.00",
                changePercent = "+2.5%",
                isPositive = true,
                companyName = "Tesla, Inc.",
                exchange = "NASDAQ",
                priceHistory = listOf(),
                selectedTab = 0,
                isLoading = false,
                error = null
            )
        )
    }
}