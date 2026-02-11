package dev.abhinav.stocktracker.data.remote.api

import dev.abhinav.stocktracker.BuildConfig
import dev.abhinav.stocktracker.data.remote.dto.StockPriceResponseDto
import dev.abhinav.stocktracker.data.remote.dto.WatchlistResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("stable/historical-price-eod/full")
    suspend fun getStockHistoryResponse(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ) : Response<List<StockPriceResponseDto>>

    @GET("stable/profile")
    suspend fun getStockProfileResponse(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ) : Response<List<WatchlistResponseDto>>

}