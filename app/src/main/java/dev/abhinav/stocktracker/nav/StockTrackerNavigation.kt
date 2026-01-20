package dev.abhinav.stocktracker.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.abhinav.stocktracker.components.stock.StockPriceScreen
import dev.abhinav.stocktracker.components.watchlist.WatchlistScreen

@Composable
fun StockAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavScreen.Watchlist.route
    ) {
        // Watchlist Screen
        composable(NavScreen.Watchlist.route) {
            WatchlistScreen(
                onStockClick = { symbol ->
                    navController.navigate(NavScreen.StockDetail.createRoute(symbol))
                }
            )
        }

        // Stock Detail Screen
        composable(
            route = NavScreen.StockDetail.route,
            arguments = listOf(navArgument("symbol") { type = NavType.StringType })
        ) { backStackEntry ->
            val symbol = backStackEntry.arguments?.getString("symbol") ?: "AAPL"

            StockPriceScreen(
                symbol = symbol,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
