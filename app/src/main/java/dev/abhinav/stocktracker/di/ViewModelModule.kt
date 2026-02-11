package dev.abhinav.stocktracker.di

import dev.abhinav.stocktracker.ui.screens.stock.StockPriceViewModel
import dev.abhinav.stocktracker.ui.screens.watchlist.StockWatchlistViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { StockWatchlistViewModel(repository = get()) }
    viewModel { StockPriceViewModel(repository = get()) }
}