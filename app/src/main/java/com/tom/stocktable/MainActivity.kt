package com.tom.stocktable

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tom.stocktable.adapter.StockAdapter
import com.tom.stocktable.adapter.TabAdapter
import com.tom.stocktable.model.StockData
import com.tom.stocktable.model.StockDetailData
import com.tom.stocktable.network.NetworkManager
import com.tom.stocktable.network.StockPriceApi
import com.tom.stocktable.network.StockPriceData
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() , StockAdapter.OnTabScrollViewListener
{
    private val maxStockAmount = 50;
    //上方Tab欄ScrollView
    private var headHorizontalScrollView: CustomizeScrollView? = null

    //上方Tab欄RecyclerView
    private var mHeadRecyclerView: RecyclerView? = null

    //下方列表RecyclerView
    private var mContentRecyclerView: RecyclerView? = null

    //上方Tab欄Adapter
    private var mTabAdapter: TabAdapter? = null

    //下方列表Adapter
    private var mStockAdapter: StockAdapter? = null

    //上方Tab欄標題
    private val tabValues = listOf("成交價", "漲跌", "幅度", "更新時間")

    //下方列表RecyclerView資料
    private var stockDataList: MutableList<StockData> = mutableListOf()
    private var stockDetailDataList: MutableList<StockDetailData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mHeadRecyclerView = headRecyclerView
        mContentRecyclerView = contentRecyclerView
        headHorizontalScrollView = headScrollView

        //上方Tab欄RecycleView
        // 設置RecyclerView水平顯示
        mHeadRecyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mTabAdapter = TabAdapter(this)
        // 设置ListView禁止滑动，这样使得ScrollView滑动更流畅
        mHeadRecyclerView?.isNestedScrollingEnabled = false
        mHeadRecyclerView?.adapter = mTabAdapter
        initTabData()

        //下方列表RecyclerView
        mContentRecyclerView?.layoutManager = LinearLayoutManager(this)
        mStockAdapter = StockAdapter(this)
        mContentRecyclerView?.adapter = mStockAdapter
        mStockAdapter?.setOnTabScrollViewListener(this)
        initStockData()
        initListener()
    }

    private fun initListener() {
        /**
         * 第三步：上方Tab欄HorizontalScrollView水平滑動時，遍歷所有下方RecyclerView列表，並使其跟隨滾動
         */
        headHorizontalScrollView?.setViewListener(object : CustomizeScrollView.OnScrollViewListener {
            override fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int) {
                val viewHolders: List<StockAdapter.ViewHolder> = mStockAdapter?.recyclerViewHolder!!
                for (viewHolder in viewHolders) {
                    viewHolder.mStockScrollView.scrollTo(l, 0)
                }
            }
        })
        /**
         * 第四步：下方RecyclerView垂直滑動時，遍歷更新所有item中HorizontalScrollView的滾動位置，
         * 否則會出現item位置未發生變化狀態
         */
        mContentRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val viewHolders: List<StockAdapter.ViewHolder> = mStockAdapter?.recyclerViewHolder!!
                for (viewHolder in viewHolders) {
                    viewHolder.mStockScrollView.scrollTo(mStockAdapter?.getOffestX()!!, 0)
                }
            }
        })
    }


    /**
     * 初始化上方Tab
     */
    private fun initTabData() {
        mTabAdapter?.setTabData(tabValues)
    }

    /**
     * 初始化下方列表
     */
    private fun initStockData()
    {
        val apiService = NetworkManager.provideRetrofit(NetworkManager.provideOkHttpClient())
                .create(StockPriceApi::class.java)

        apiService.getAllPrice().enqueue(object : Callback<List<StockPriceData>> {
            override fun onResponse(
                    call: Call<List<StockPriceData>>,
                    response: Response<List<StockPriceData>>,
            ) {
                Log.d("MainActivity", "response: ${response.body().toString()}")
                response.body()?.let { convertUpdateData(it) }
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
            if(i >= maxStockAmount)
                break
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
            detailsList.add(String.format("%.2f", rangeData))
            detailsList.add(timeData)

            val detailData =  StockDetailData(closeData, upDownData, rangeData, timeData, isUp)
            stockDetailDataList.add(detailData)
            val data = StockData(netData[i].name, detailsList)
            stockDataList.add(data)
        }
        mStockAdapter?.setStockDatas(stockDataList, stockDetailDataList)
    }
    override fun scrollTo(l: Int, t: Int) {
        if (headHorizontalScrollView != null) {
            headHorizontalScrollView?.scrollTo(l, 0)
        }
    }
}