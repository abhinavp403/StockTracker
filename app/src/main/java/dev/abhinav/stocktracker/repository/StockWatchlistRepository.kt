package dev.abhinav.stocktracker.repository

import dev.abhinav.stocktracker.database.WatchlistDao
import dev.abhinav.stocktracker.database.WatchlistStockEntity
import dev.abhinav.stocktracker.remote.ServiceApi
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class StockWatchlistRepository(
    private val api: ServiceApi,
    private val dao: WatchlistDao
) {
    val watchlistStocks: Flow<List<WatchlistStockEntity>> = dao.getAllStocks()

    suspend fun shouldRefreshData(): Boolean {
        val lastUpdate = dao.getLastUpdateTime() ?: return true

        val now = System.currentTimeMillis()
        val lastUpdateCalendar = Calendar.getInstance().apply {
            timeInMillis = lastUpdate
        }
        val nowCalendar = Calendar.getInstance().apply {
            timeInMillis = now
        }

        // Check if it's past 6 PM today and we haven't updated since 6 PM
        val todayAt6PM = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // If current time is past 6 PM and last update was before today's 6 PM
        return nowCalendar.timeInMillis >= todayAt6PM.timeInMillis &&
                lastUpdate < todayAt6PM.timeInMillis
    }

    suspend fun refreshStockProfile(symbol: String): Result<Unit> {
        return try {
            val response = api.getStockProfileResponse(symbol)
            if (response.isNotEmpty()) {
                val item = response[0]
                val entity = WatchlistStockEntity(
                    symbol = item.symbol,
                    companyName = item.companyName,
                    price = item.price,
                    changePercentage = item.changePercentage,
                    lastUpdated = System.currentTimeMillis()
                )
                dao.insertStock(entity)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Empty response for $symbol"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refreshAllStocks(symbols: List<String>): Result<Unit> {
        return try {
            val entities = mutableListOf<WatchlistStockEntity>()
            val currentTime = System.currentTimeMillis()

            symbols.forEach { symbol ->
                try {
                    val response = api.getStockProfileResponse(symbol)
                    if (response.isNotEmpty()) {
                        val item = response[0]
                        entities.add(
                            WatchlistStockEntity(
                                symbol = item.symbol,
                                companyName = item.companyName,
                                price = item.price,
                                changePercentage = item.changePercentage,
                                lastUpdated = currentTime
                            )
                        )
                    }
                } catch (e: Exception) {
                    println("Failed to fetch $symbol: ${e.message}")
                }
            }

            if (entities.isNotEmpty()) {
                dao.insertStocks(entities)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to fetch any stocks"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeStock(symbol: String) {
        dao.deleteBySymbol(symbol)
    }
}