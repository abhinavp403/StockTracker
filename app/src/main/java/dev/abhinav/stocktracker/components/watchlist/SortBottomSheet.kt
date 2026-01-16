package dev.abhinav.stocktracker.components.watchlist

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.abhinav.stocktracker.util.SortOption
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    currentSortOption: SortOption,
    onDismiss: () -> Unit,
    onSortSelected: (SortOption) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var isClosing by remember { mutableStateOf(false) }

    val sheetOffset by animateDpAsState(
        targetValue = if (isClosing) 50.dp else 0.dp,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearOutSlowInEasing
        ),
        label = "sheetOffset",
        finishedListener = { _ ->
            if (isClosing) {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    onDismiss()
                    isClosing = false
                }
            }
        }
    )

    fun closeWithAnimation() {
        if (isClosing) return
        isClosing = true
    }


    ModalBottomSheet(
        onDismissRequest = { closeWithAnimation() },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = sheetOffset) // apply slow slide
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Sort By",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            fun select(option: SortOption) {
                onSortSelected(option)
                closeWithAnimation()
            }

            // Symbol Section
            SortRadioRow("SYMBOL (A → Z)", currentSortOption == SortOption.SYMBOL_ASC) {
                select(SortOption.SYMBOL_ASC)
            }
            SortRadioRow("SYMBOL (Z → A)", currentSortOption == SortOption.SYMBOL_DESC) {
                select(SortOption.SYMBOL_DESC)
            }

            // Company Name Section
            SortRadioRow("COMPANY NAME (A → Z)", currentSortOption == SortOption.COMPANY_NAME_ASC) {
                select(SortOption.COMPANY_NAME_ASC)
            }
            SortRadioRow("COMPANY NAME (Z → A)", currentSortOption == SortOption.COMPANY_NAME_DESC) {
                select(SortOption.COMPANY_NAME_DESC)
            }

            // Price Section
            SortRadioRow("PRICE (High → Low)", currentSortOption == SortOption.PRICE_HIGH_TO_LOW) {
                select(SortOption.PRICE_HIGH_TO_LOW)
            }
            SortRadioRow("PRICE (Low → High)", currentSortOption == SortOption.PRICE_LOW_TO_HIGH) {
                select(SortOption.PRICE_LOW_TO_HIGH)
            }

            // Change % Section
            SortRadioRow("CHANGE % (High → Low)", currentSortOption == SortOption.CHANGE_HIGH_TO_LOW) {
                select(SortOption.CHANGE_HIGH_TO_LOW)
            }
            SortRadioRow("CHANGE % (Low → High)", currentSortOption == SortOption.CHANGE_LOW_TO_HIGH) {
                select(SortOption.CHANGE_LOW_TO_HIGH)
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
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}