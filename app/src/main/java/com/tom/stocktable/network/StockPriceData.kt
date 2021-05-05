package com.tom.stocktable.network

import com.google.gson.annotations.SerializedName

data class StockPriceData(

        @SerializedName("證券代號")
        val stockId: String,
        @SerializedName("證券名稱")
        val name: String,
        @SerializedName("收盤價")
        val closePrice: String,
        @SerializedName("漲跌價差")
        val upDown: String
)
