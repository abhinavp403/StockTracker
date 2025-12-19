package dev.abhinav.stocktracker.remote

import dev.abhinav.stocktracker.BuildConfig
import dev.abhinav.stocktracker.model.StockPriceResponse
import dev.abhinav.stocktracker.model.StockProfileResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {

    @GET("stable/historical-price-eod/full")
    suspend fun getStockHistoryResponse(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ) : StockPriceResponse

    @GET("stable/profile")
    suspend fun getStockProfileResponse(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ) : StockProfileResponse

}