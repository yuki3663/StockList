package com.tom.stocktable.model

data class StockData(val stockName:String,
                     var details: List<String>)
data class StockDetailData(var price:Float,
                           var upDown:Float,
                           var range:Float,
                           var time:String,
                           var isUp:Boolean,
                           var isSetup:Boolean)
class StockOpenData
{
    companion object{
        val topStockList = listOf(
            "00677U","2609","2603","3481" ,"00715L","2303","2610","2353","2317","3576",
            "2883","00632R","2409","2888","2344","2457","2890","00637L","2489","2337",
            "2356","2330","2882","00642U","2891","6116","3704","2618","2601","3231","3094",
            "00881","2881","2641","8054","2324","4961","6443","2884","2338","2886","2002",
            "2363","2014","2892","2331","5608","2605","3037","1314","8150","1568","2402",
            "2617","2371","2328","4956","2885","2340","3711","6147","3058","5880","6283",
            "1301","2887","2106","8936","2357","8358","6244","3707","4960","1101","5285",
            "3346","4938","8183","8069"
        )
    }
}
enum class DataState
{
    None, NetUpdate, NetFinish
}