package com.tom.stocktable.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tom.stocktable.model.DataState
import com.tom.stocktable.model.StockData
import com.tom.stocktable.model.StockDetailData
import com.tom.stocktable.model.StockOpenData
import com.tom.stocktable.network.NetworkManager
import com.tom.stocktable.network.StockPriceApi
import com.tom.stocktable.network.StockPriceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class StockViewModel : ViewModel()
{
    var dataStateLiveData = MutableLiveData<DataState>()
    var stockRandomList = MutableLiveData<MutableList<Int>>()
    //下方列表RecyclerView資料
    private var stockDataList: MutableList<StockData> = mutableListOf()
    private var stockDetailDataList: MutableList<StockDetailData> = mutableListOf()
    private val perChangeStock = 10
    private val maxRange = 10

    /**
     * 初始化下方列表
     */
    fun initStockData()
    {
        dataStateLiveData.postValue(DataState.None)
        val apiService = NetworkManager.provideRetrofit(NetworkManager.provideOkHttpClient())
            .create(StockPriceApi::class.java)

        apiService.getAllPrice().enqueue(object : Callback<List<StockPriceData>> {
            override fun onResponse(
                call: Call<List<StockPriceData>>,
                response: Response<List<StockPriceData>>,
            ) {
                Log.d("MainActivity", "response: ${response.body().toString()}")
                response.body()?.let {
                    convertUpdateData(filterTopStock(it))
                }
            }

            override fun onFailure(call: Call<List<StockPriceData>>, t: Throwable) {
                Log.d("MainActivity", "error: ${t.message}")
            }
        })
    }
    private fun convertUpdateData(netData: List<StockPriceData>)
    {
        stockDataList.clear()
        for (i in netData.indices)
        {
            val detailsList: MutableList<String> = mutableListOf()
            if(netData[i].closePrice.isEmpty())
                continue
            val closeData: Float = netData[i].closePrice.toFloat()
            val upDownData: Float = netData[i].upDown.toFloat()
            val rangeData = upDownData / closeData * 100
            val timeData = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            val isUp = rangeData >= 0
            detailsList.add(netData[i].closePrice)
            detailsList.add(netData[i].upDown)
            detailsList.add(String.format("%.2f", rangeData) + "%")
            detailsList.add(timeData)

            val detailData =  StockDetailData(closeData, upDownData, rangeData, timeData, isUp, true)
            stockDetailDataList.add(detailData)
            val data = StockData(netData[i].name, detailsList)
            stockDataList.add(data)
        }
        dataStateLiveData.postValue(DataState.NetUpdate)
    }
    private fun changeStockDetail(Index: Int)
    {
        val detailsList: MutableList<String> = mutableListOf()
        val rand = Random()
        val randRange = rand.nextFloat() * (maxRange - (-maxRange)) + -maxRange
        val oldPrice = stockDetailDataList[Index].price
        val newPrice = oldPrice + oldPrice * (randRange / 100)
        val timeData = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        stockDetailDataList[Index].upDown = newPrice - oldPrice
        stockDetailDataList[Index].price = newPrice
        stockDetailDataList[Index].range = randRange
        stockDetailDataList[Index].time = timeData
        stockDetailDataList[Index].isUp = randRange >= 0
        stockDetailDataList[Index].isSetup = false
        detailsList.add(String.format("%.2f", newPrice))
        detailsList.add(String.format("%.2f", stockDetailDataList[Index].upDown))
        detailsList.add(String.format("%.2f", randRange) + "%")
        detailsList.add(timeData)
        stockDataList[Index].details = detailsList
    }
    fun randomUpDown()
    {
        var randomInxList: MutableList<Int> = mutableListOf()
        for (i in 1..perChangeStock)
        {
            var randomStockInx = (0 until stockDataList.size).random()
            while (randomInxList.contains(randomStockInx))
            {
                randomStockInx = (0 until stockDataList.size).random()
            }
            randomInxList.add(randomStockInx)
            changeStockDetail(randomStockInx)
        }
        stockRandomList.postValue(randomInxList)
        //mStockAdapter?.setStockDatas(stockDataList, stockDetailDataList)
    }
    private fun filterTopStock(netData: List<StockPriceData>) : List<StockPriceData>
    {
        val topList: MutableList<StockPriceData> = mutableListOf()
        for (i in netData.indices)
        {
            if(StockOpenData.topStockList.contains(netData[i].stockId))
            {
                topList.add(netData[i])
            }
        }
        return topList
    }
    fun endLineEffect(randomList: List<Int>)
    {
        for (index in randomList)
        {
            stockDetailDataList[index].isSetup = true
        }
    }
    fun getStockDataList() : MutableList<StockData>
    {
        return stockDataList
    }
    fun getStockDetailDataList() : MutableList<StockDetailData>
    {
        return stockDetailDataList
    }
}