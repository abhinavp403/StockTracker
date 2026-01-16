package dev.abhinav.stocktracker.components.stock

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.abhinav.stocktracker.model.DayPrice
import dev.abhinav.stocktracker.ui.theme.StockTrackerTheme

@Composable
fun StockPriceCard(dayPrice: DayPrice) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Day Header with Daily Change
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = dayPrice.day,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = dayPrice.label,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (dayPrice.isPositiveChange) "▲" else "▼",
                            style = MaterialTheme.typography.titleSmall,
                            color = if (dayPrice.isPositiveChange) Color(0xFF4CAF50) else Color(0xFFE53935),
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            text = dayPrice.dailyChange,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (dayPrice.isPositiveChange) Color(0xFF4CAF50) else Color(0xFFE53935)
                        )
                    }
                    Text(
                        text = "Daily Change",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Price Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StockPriceItem("OPEN", dayPrice.open)
                StockPriceItem("CLOSE", dayPrice.close)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StockPriceItem("HIGH", dayPrice.high)
                StockPriceItem("LOW", dayPrice.low)
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun StockPriceCardPreview() {
    StockTrackerTheme {
        Surface {
            val sampleDayPrice = DayPrice(
                day = "Monday",
                label = "Today",
                open = "$1,200.00",
                close = "$1,250.00",
                high = "$1,260.00",
                low = "$1,190.00",
                date = "2024-06-10",
                dailyChange = "+4.17%",
                isPositiveChange = true
            )
            StockPriceCard(dayPrice = sampleDayPrice)
        }
    }
}