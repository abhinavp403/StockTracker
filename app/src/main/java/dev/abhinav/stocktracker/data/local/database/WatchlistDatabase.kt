package dev.abhinav.stocktracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.abhinav.stocktracker.data.local.entity.WatchlistEntity
import dev.abhinav.stocktracker.data.local.dao.WatchlistDao

@Database(entities = [WatchlistEntity::class], version = 1, exportSchema = false)
abstract class WatchlistDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao
}