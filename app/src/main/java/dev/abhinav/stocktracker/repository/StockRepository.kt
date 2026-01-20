package dev.abhinav.stocktracker.repository

import dev.abhinav.stocktracker.model.StockPriceResponse
import dev.abhinav.stocktracker.remote.ServiceApi

class StockRepository(
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
}