package dev.abhinav.stocktracker.components.stock

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.abhinav.stocktracker.viewmodel.StockPriceHistoryViewModel

@Composable
fun StockPriceScreen(
    viewModel: StockPriceHistoryViewModel,
    symbol: String
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(symbol) {
        viewModel.fetchStockData(symbol)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            uiState.error != null -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.fetchStockData(symbol) }) {
                        Text("Retry")
                    }
                }
            }
            else -> {
                StockPriceContent(uiState)
            }
        }
    }
}