package dev.abhinav.stocktracker.di

import androidx.room.Room
import dev.abhinav.stocktracker.data.local.dao.WatchlistDao
import dev.abhinav.stocktracker.data.local.database.WatchlistDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            WatchlistDatabase::class.java,
            "watchlist_database"
        ).build()
    }

    single<WatchlistDao> {
        get<WatchlistDatabase>().watchlistDao()
    }
}