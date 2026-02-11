package dev.abhinav.stocktracker.data.repository

import dev.abhinav.stocktracker.data.remote.api.ApiService
import dev.abhinav.stocktracker.data.remote.dto.StockPriceResponseDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class StockRepository(
    private val api: ApiService
) {
    fun getStockHistory(symbol: String): Flow<Result<List<StockPriceResponseDto>>> =
        flow {
            val response = api.getStockHistoryResponse(symbol)

            if (response.isSuccessful) {
                val body = response.body()
                if (!body.isNullOrEmpty()) {
                    emit(Result.success(body))
                } else {
                    emit(Result.failure(Exception("Empty response")))
                }
            } else {
                emit(Result.failure(Exception(response.message())))
            }
        }
        .catch { e ->
            emit(Result.failure(e))
        }
        .flowOn(Dispatchers.IO)
}