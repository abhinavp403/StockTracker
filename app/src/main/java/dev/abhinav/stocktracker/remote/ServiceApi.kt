package dev.abhinav.stocktracker.remote

import dev.abhinav.stocktracker.BuildConfig
import dev.abhinav.stocktracker.model.StockPriceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {

    @GET("stable/historical-price-eod/full")
    suspend fun getStockHistoryResponse(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ) : StockPriceResponse
}