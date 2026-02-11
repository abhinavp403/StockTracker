package dev.abhinav.stocktracker.di

import dev.abhinav.stocktracker.data.repository.StockRepository
import dev.abhinav.stocktracker.data.repository.WatchlistRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { StockRepository(api = get()) }
    single { WatchlistRepository(api = get(), dao = get()) }
}