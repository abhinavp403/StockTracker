package dev.abhinav.stocktracker.di

import dev.abhinav.stocktracker.viewmodel.StockPriceHistoryViewModel
import dev.abhinav.stocktracker.viewmodel.StockWatchlistViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { StockWatchlistViewModel(repository = get()) }
    viewModel { StockPriceHistoryViewModel(repository = get()) }
}