package dev.abhinav.stocktracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist_stocks")
    fun getAllStocks(): Flow<List<WatchlistStockEntity>>

    @Query("SELECT * FROM watchlist_stocks WHERE symbol = :symbol")
    suspend fun getStock(symbol: String): WatchlistStockEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: WatchlistStockEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStocks(stocks: List<WatchlistStockEntity>)

    @Delete
    suspend fun deleteStock(stock: WatchlistStockEntity)

    @Query("DELETE FROM watchlist_stocks WHERE symbol = :symbol")
    suspend fun deleteBySymbol(symbol: String)

    @Query("SELECT lastUpdated FROM watchlist_stocks ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLastUpdateTime(): Long?
}