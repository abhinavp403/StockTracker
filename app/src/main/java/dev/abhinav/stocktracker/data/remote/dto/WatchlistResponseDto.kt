package dev.abhinav.stocktracker.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WatchlistResponseDto(
    @SerializedName("changePercentage") val changePercentage: Double,
    @SerializedName("companyName") val companyName: String,
    @SerializedName("price") val price: Double,
    @SerializedName("symbol") val symbol: String,
) : Parcelable