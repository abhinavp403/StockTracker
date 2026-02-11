package dev.abhinav.stocktracker.data.repository

import dev.abhinav.stocktracker.data.remote.dto.StockPriceResponse
import dev.abhinav.stocktracker.data.remote.api.ApiService

class StockRepository(
    private val api: ApiService
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