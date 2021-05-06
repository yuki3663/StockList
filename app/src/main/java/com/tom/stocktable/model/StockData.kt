package com.tom.stocktable.model

data class StockData(val stockName:String,
                     var details: List<String>)
data class StockDetailData(var price:Float,
                           var upDown:Float,
                           var range:Float,
                           var time:String,
                           var isUp:Boolean)