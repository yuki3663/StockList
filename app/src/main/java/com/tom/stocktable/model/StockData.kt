package com.tom.stocktable.model

data class StockData(val stockName:String,
                     val details: List<String>)
data class StockDetailData(val price:Float,
                           val upDown:Float,
                           val range:Float,
                           val time:String,
                           val isUp:Boolean)