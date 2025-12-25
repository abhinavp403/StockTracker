package dev.abhinav.stocktracker.components.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.abhinav.stocktracker.util.SortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    currentSortOption: SortOption,
    onDismiss: () -> Unit,
    onSortSelected: (SortOption) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Sort By",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Symbol Section
            Text(
                text = "SYMBOL",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            SortOption(
                text = "A → Z",
                isSelected = currentSortOption == SortOption.SYMBOL_ASC,
                onClick = { onSortSelected(SortOption.SYMBOL_ASC) }
            )

            SortOption(
                text = "Z → A",
                isSelected = currentSortOption == SortOption.SYMBOL_DESC,
                onClick = { onSortSelected(SortOption.SYMBOL_DESC) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Company Name Section
            Text(
                text = "COMPANY NAME",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            SortOption(
                text = "A → Z",
                isSelected = currentSortOption == SortOption.COMPANY_NAME_ASC,
                onClick = { onSortSelected(SortOption.COMPANY_NAME_ASC) }
            )

            SortOption(
                text = "Z → A",
                isSelected = currentSortOption == SortOption.COMPANY_NAME_DESC,
                onClick = { onSortSelected(SortOption.COMPANY_NAME_DESC) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Price Section
            Text(
                text = "PRICE",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            SortOption(
                text = "High → Low",
                isSelected = currentSortOption == SortOption.PRICE_HIGH_TO_LOW,
                onClick = { onSortSelected(SortOption.PRICE_HIGH_TO_LOW) }
            )

            SortOption(
                text = "Low → High",
                isSelected = currentSortOption == SortOption.PRICE_LOW_TO_HIGH,
                onClick = { onSortSelected(SortOption.PRICE_LOW_TO_HIGH) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Change % Section
            Text(
                text = "CHANGE %",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            SortOption(
                text = "High → Low",
                isSelected = currentSortOption == SortOption.CHANGE_HIGH_TO_LOW,
                onClick = { onSortSelected(SortOption.CHANGE_HIGH_TO_LOW) }
            )

            SortOption(
                text = "Low → High",
                isSelected = currentSortOption == SortOption.CHANGE_LOW_TO_HIGH,
                onClick = { onSortSelected(SortOption.CHANGE_LOW_TO_HIGH) }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SortOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color(0xFFE8F5E9) else Color.Transparent,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) Color(0xFF10B981) else Color.Black
            )

            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF10B981),
                    modifier = Modifier.size(20.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}