package dev.abhinav.stocktracker.repository

import dev.abhinav.stocktracker.model.StockPriceResponse
import dev.abhinav.stocktracker.model.StockProfileResponse
import dev.abhinav.stocktracker.remote.ServiceApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepository @Inject constructor(
    private val api: ServiceApi
) {
    // Calls api to get response
    suspend fun getStockHistory(symbol: String) : Result<StockPriceResponse> {
        return try {
            val response = api.getStockHistoryResponse(symbol)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStockProfile(symbol: String) : Result<StockProfileResponse> {
        return try {
            val response = api.getStockProfileResponse(symbol)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}