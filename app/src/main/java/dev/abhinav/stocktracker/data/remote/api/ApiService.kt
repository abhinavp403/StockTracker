package dev.abhinav.stocktracker.data.remote.api

import dev.abhinav.stocktracker.BuildConfig
import dev.abhinav.stocktracker.data.remote.dto.StockPriceResponse
import dev.abhinav.stocktracker.data.remote.dto.WatchlistResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("stable/historical-price-eod/full")
    suspend fun getStockHistoryResponse(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ) : StockPriceResponse

    @GET("stable/profile")
    suspend fun getStockProfileResponse(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ) : WatchlistResponse

}