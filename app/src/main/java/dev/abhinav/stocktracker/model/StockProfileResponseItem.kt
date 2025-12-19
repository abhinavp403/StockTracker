package dev.abhinav.stocktracker.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockProfileResponseItem(
    @SerializedName("changePercentage") val changePercentage: Double,
    @SerializedName("companyName") val companyName: String,
    @SerializedName("price") val price: Double,
    @SerializedName("symbol") val symbol: String,
) : Parcelable