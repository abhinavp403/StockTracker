package dev.abhinav.stocktracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.abhinav.stocktracker.data.local.entity.WatchlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist_stocks")
    fun getAllStocks(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist_stocks WHERE symbol = :symbol")
    suspend fun getStock(symbol: String): WatchlistEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertStock(stock: WatchlistEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertStocks(stocks: List<WatchlistEntity>)

    @Delete
    suspend fun deleteStock(stock: WatchlistEntity)

    @Query("DELETE FROM watchlist_stocks WHERE symbol = :symbol")
    suspend fun deleteBySymbol(symbol: String)

    @Query("SELECT lastUpdated FROM watchlist_stocks ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLastUpdateTime(): Long?
}