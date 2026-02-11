package dev.abhinav.stocktracker.data.repository

import dev.abhinav.stocktracker.data.local.dao.WatchlistDao
import dev.abhinav.stocktracker.data.local.entity.WatchlistEntity
import dev.abhinav.stocktracker.data.remote.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.Calendar

class WatchlistRepository(
    private val api: ApiService,
    private val dao: WatchlistDao
) {
    val watchlistStocks: Flow<List<WatchlistEntity>> =
        dao.getAllStocks()

    suspend fun shouldRefreshData(): Boolean {
        val lastUpdate = dao.getLastUpdateTime() ?: return true

        val now = System.currentTimeMillis()
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

    fun refreshStockProfile(symbol: String): Flow<Result<Unit>> =
        flow {
            val response = api.getStockProfileResponse(symbol)

            if (response.isSuccessful) {
                val body = response.body()
                if (!body.isNullOrEmpty()) {
                    val item = body.first()
                    val entity = WatchlistEntity(
                        symbol = item.symbol,
                        companyName = item.companyName,
                        price = item.price,
                        changePercentage = item.changePercentage,
                        lastUpdated = System.currentTimeMillis()
                    )
                    dao.insertStock(entity)
                    emit(Result.success(Unit))
                } else {
                    emit(Result.failure(Exception("Empty response for $symbol")))
                }
            } else {
                emit(Result.failure(Exception(response.message())))
            }
        }
        .catch { e ->
            emit(Result.failure(e))
        }
        .flowOn(Dispatchers.IO)

    fun refreshAllStocks(symbols: List<String>): Flow<Result<Unit>> =
        flow {
            val entities = mutableListOf<WatchlistEntity>()
            val currentTime = System.currentTimeMillis()

            symbols.forEach { symbol ->
                try {
                    val response = api.getStockProfileResponse(symbol)

                    if (response.isSuccessful) {
                        response.body()
                            ?.firstOrNull()
                            ?.let { item ->
                                entities.add(
                                    WatchlistEntity(
                                        symbol = item.symbol,
                                        companyName = item.companyName,
                                        price = item.price,
                                        changePercentage = item.changePercentage,
                                        lastUpdated = currentTime
                                    )
                                )
                            }
                    }
                } catch (e: Exception) {
                    println("Failed to fetch $symbol: ${e.message}")
                }
            }

            if (entities.isNotEmpty()) {
                dao.insertStocks(entities)
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("Failed to fetch any stocks")))
            }
        }
        .catch { e ->
            emit(Result.failure(e))
        }
        .flowOn(Dispatchers.IO)

    suspend fun removeStock(symbol: String) {
        dao.deleteBySymbol(symbol)
    }
}