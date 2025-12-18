package dev.abhinav.stocktracker.components.stock

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.abhinav.stocktracker.model.StockUiState
import dev.abhinav.stocktracker.viewmodel.StockPriceHistoryViewModel

@Composable
fun StockPriceContent(
    uiState: StockUiState,
    viewModel: StockPriceHistoryViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header Section
        Text(
            text = uiState.symbol,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = uiState.currentPrice,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(12.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (uiState.isPositive) Color(0xFFE8F741) else Color(0xFFFFCDD2)
            ) {
                Text(
                    text = uiState.changePercent,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Text(
            text = "${uiState.companyName} • ${uiState.exchange}",
            fontSize = 14.sp,
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
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF7D8B6B),
            letterSpacing = 1.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

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
        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFE0E0E0)) else null,
        onClick = onClick
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) Color.White else Color.Gray,
            modifier = Modifier.padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun StockPriceContentPreview() {
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