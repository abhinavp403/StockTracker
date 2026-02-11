package dev.abhinav.stocktracker.ui.navigation

sealed class NavScreen(val route: String) {
    object Watchlist : NavScreen("watchlist")

    object StockDetail : NavScreen("stock_detail/{symbol}") {
        fun createRoute(symbol: String) = "stock_detail/$symbol"
    }
}