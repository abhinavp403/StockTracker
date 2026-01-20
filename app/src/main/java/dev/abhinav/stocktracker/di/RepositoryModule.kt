package dev.abhinav.stocktracker.di

import dev.abhinav.stocktracker.repository.StockRepository
import dev.abhinav.stocktracker.repository.StockWatchlistRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { StockRepository(api = get()) }
    single { StockWatchlistRepository(api = get(), dao = get()) }
}