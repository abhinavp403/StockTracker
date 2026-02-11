package dev.abhinav.stocktracker.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockPriceResponseDto(
    @SerializedName("change") val change: Double,
    @SerializedName("changePercent") val changePercent: Double,
    @SerializedName("close") val close: Double,
    @SerializedName("date") val date: String,
    @SerializedName("high") val high: Double,
    @SerializedName("low") val low: Double,
    @SerializedName("open") val open: Double,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("volume") val volume: Int,
    @SerializedName("vwap") val vwap: Double
) : Parcelable