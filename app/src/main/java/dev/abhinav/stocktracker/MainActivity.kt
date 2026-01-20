package dev.abhinav.stocktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.abhinav.stocktracker.nav.StockAppNavigation
import dev.abhinav.stocktracker.ui.theme.StockTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StockTrackerTheme {
                StockAppNavigation()
            }
        }
    }
}