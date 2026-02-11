package dev.abhinav.stocktracker.data.model

data class DayPrice(
    val day: String,
    val label: String,
    val open: String,
    val close: String,
    val high: String,
    val low: String,
    val date: String,
    val dailyChange: String,
    val isPositiveChange: Boolean
)