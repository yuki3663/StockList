package com.tom.stocktable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tom.stocktable.adapter.StockAdapter
import com.tom.stocktable.adapter.TabAdapter
import com.tom.stocktable.model.StockData
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() , StockAdapter.OnTabScrollViewListener {
    //上方Tab欄ScrollView
    var headHorizontalScrollView: CustomizeScrollView? = null

    //上方Tab欄RecyclerView
    private var mHeadRecyclerView: RecyclerView? = null

    //下方列表RecyclerView
    private var mContentRecyclerView: RecyclerView? = null

    //上方Tab欄Adapter
    private var mTabAdapter: TabAdapter? = null

    //下方列表Adapter
    private var mStockAdapter: StockAdapter? = null

    //上方Tab欄標題
    val tabValues = listOf("成交價", "漲跌", "幅度", "更新時間")

    //下方列表RecyclerView資料
    private var stockDataList: MutableList<StockData> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mHeadRecyclerView = headRecyclerView
        mContentRecyclerView = contentRecyclerView
        headHorizontalScrollView = headScrollView

        // TODO:上方Tab欄RecycleView
        // 設置RecyclerView水平顯示
        mHeadRecyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mTabAdapter = TabAdapter(this)
        // 设置ListView禁止滑动，这样使得ScrollView滑动更流畅
        mHeadRecyclerView?.isNestedScrollingEnabled = false
        mHeadRecyclerView?.adapter = mTabAdapter
        initTabData()

        // TODO:下方列表RecyclerView
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
    private fun initStockData() {
        val details = listOf("600", "10", "10", "12:00")
        val data1 = StockData("台積電", details)
        val data2 = StockData("台積電", details)
        val data3 = StockData("台積電", details)
        val data4 = StockData("台積電", details)
        val data5 = StockData("台積電", details)
        val data6 = StockData("台積電", details)
        stockDataList.add(data1)
        stockDataList.add(data2)
        stockDataList.add(data3)
        stockDataList.add(data4)
        stockDataList.add(data5)
        stockDataList.add(data6)
        mStockAdapter?.setStockDatas(stockDataList)
    }

    override fun scrollTo(l: Int, t: Int) {
        if (headHorizontalScrollView != null) {
            headHorizontalScrollView?.scrollTo(l, 0)
        }
    }
}