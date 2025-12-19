package dev.abhinav.stocktracker.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WatchlistStockEntity::class], version = 1, exportSchema = false)
abstract class WatchlistDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao
}