package com.tom.stocktable.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val STOCK_PRICE_URL = "https://quality.data.gov.tw/"

interface StockPriceApi {

    @GET("/dq_download_json.php")
    fun getAllPrice(@Query("nid") nid: String = "11549",
                    @Query("md5_url") md5_url: String = "bb878d47ffbe7b83bfc1b41d0b24946e"
    ): Call<List<StockPriceData>>

}

 /*
const val STOCK_PRICE_URL = "https://www.twse.com.tw/exchangeReport/"

interface StockPriceApi {

    @GET("/STOCK_DAY_ALL")
    fun getAllPrice(@Query("response") response: String = "json"): Call<List<StockPriceData>>

}*/