package dev.abhinav.stocktracker.components

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
import dev.abhinav.stocktracker.model.DayPrice

@Composable
fun StockPriceScreen() {
    val priceHistory = listOf(
        DayPrice("Wednesday", "Today", "$1,200.00", "$1,240.50", "$1,250.10", "$1,190.80", "", "2.4", true),
        DayPrice("Tuesday", "Yesterday", "$1,195.50", "$1,210.00", "$1,215.75", "$1,188.20","","1.3", false),
        DayPrice("Monday", "2 Days ago", "$1,205.00", "$1,195.00", "$1,208.50", "$1,190.00", "","0.76", true)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header Section
        Text(
            text = "TSLA",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = "$1,240.50",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(12.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFE8F741)
            ) {
                Text(
                    text = "↗ +2.4%",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Text(
            text = "Tesla, Inc. • NASDAQ",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        // Tab Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TabButton(
                text = "3 Days",
                isSelected = true,
                onClick = {  },
                modifier = Modifier.weight(1f)
            )
            TabButton(
                text = "5 Days",
                isSelected = false,
                onClick = {  },
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
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Price Cards
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
fun StockPriceScreenPreview() {
    StockPriceScreen()
}