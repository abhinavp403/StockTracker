package dev.abhinav.stocktracker.components.watchlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
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
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Sort By",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Symbol Section
            SortRadioRow("SYMBOL (A → Z)", currentSortOption == SortOption.SYMBOL_ASC) {
                onSortSelected(SortOption.SYMBOL_ASC)
            }
            SortRadioRow("SYMBOL (Z → A)", currentSortOption == SortOption.SYMBOL_DESC) {
                onSortSelected(SortOption.SYMBOL_DESC)
            }

            // Company Name Section
            SortRadioRow("COMPANY NAME (A → Z)", currentSortOption == SortOption.COMPANY_NAME_ASC) {
                onSortSelected(SortOption.COMPANY_NAME_ASC)
            }
            SortRadioRow("COMPANY NAME (Z → A)", currentSortOption == SortOption.COMPANY_NAME_DESC) {
                onSortSelected(SortOption.COMPANY_NAME_DESC)
            }

            // Price Section
            SortRadioRow("PRICE (High → Low)", currentSortOption == SortOption.PRICE_HIGH_TO_LOW) {
                onSortSelected(SortOption.PRICE_HIGH_TO_LOW)
            }
            SortRadioRow("PRICE (Low → High)", currentSortOption == SortOption.PRICE_LOW_TO_HIGH) {
                onSortSelected(SortOption.PRICE_LOW_TO_HIGH)
            }

            // Change % Section
            SortRadioRow("CHANGE % (High → Low)", currentSortOption == SortOption.CHANGE_HIGH_TO_LOW) {
                onSortSelected(SortOption.CHANGE_HIGH_TO_LOW)
            }
            SortRadioRow("CHANGE % (Low → High)", currentSortOption == SortOption.CHANGE_LOW_TO_HIGH) {
                onSortSelected(SortOption.CHANGE_LOW_TO_HIGH)
            }
        }
    }
}

@Composable
private fun SortRadioRow(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null
        )
        Text(
            text = text,
            color = Color.Black,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}