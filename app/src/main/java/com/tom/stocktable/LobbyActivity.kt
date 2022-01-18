package com.tom.stocktable

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tom.stocktable.adapter.StockAdapter
import com.tom.stocktable.adapter.TabAdapter
import com.tom.stocktable.model.DataState
import com.tom.stocktable.viewModel.StockViewModel
import kotlinx.android.synthetic.main.activity_lobby.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class LobbyActivity : AppCompatActivity() , StockAdapter.OnTabScrollViewListener
{
    lateinit var mainHandler: Handler
    // Create a viewModel
    private lateinit var viewModel : StockViewModel
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


    private var nowUpdateSpeed : Long = 1000
    private var lineEffectTime : Long = 750
    private var isDataFinish = false

    private val updateStockTask = object : Runnable {
        override fun run() {
            if(isDataFinish)
            {
                viewModel.randomUpDown()
            }
            mainHandler.postDelayed(this, nowUpdateSpeed)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        mHeadRecyclerView = headRecyclerView
        mContentRecyclerView = contentRecyclerView
        headHorizontalScrollView = headScrollView
        viewModel = ViewModelProvider(this).get(StockViewModel::class.java)

        //上方Tab欄RecycleView
        // 設置RecyclerView水平顯示
        mHeadRecyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mTabAdapter = TabAdapter(this)
        // 设置ListView禁止滑动，这样使得ScrollView滑动更流畅
        mHeadRecyclerView?.isNestedScrollingEnabled = false
        mHeadRecyclerView?.adapter = mTabAdapter
        initTabData()

        //下方列表RecyclerView
        (mContentRecyclerView?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mContentRecyclerView?.layoutManager = LinearLayoutManager(this)
        mStockAdapter = StockAdapter(this)
        mContentRecyclerView?.adapter = mStockAdapter
        mStockAdapter?.setOnTabScrollViewListener(this)

        initListener()
        viewModel.initStockData()
        observeStockData()
        observeStockRandomList()
        mainHandler = Handler(Looper.getMainLooper())
    }
    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateStockTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateStockTask)
    }
    private fun initListener() {

        /**
         * 第三步：上方Tab欄HorizontalScrollView水平滑動時，遍歷所有下方RecyclerView列表，並使其跟隨滾動
         */
        headHorizontalScrollView?.setViewListener(object :
            CustomizeScrollView.OnScrollViewListener {
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
                    viewHolder.mStockScrollView.scrollTo(mStockAdapter?.offestX!!, 0)
                }
            }
        })
        refreshButton.setOnClickListener{
            val inputSpeed = ed_hopeSpeed.text.toString()
            if(inputSpeed.isEmpty())
                return@setOnClickListener
            nowUpdateSpeed = inputSpeed.toLong()
            nowSpeed.text = inputSpeed
        }
    }


    /**
     * 初始化上方Tab
     */
    private fun initTabData() {
        mTabAdapter?.setTabData(tabValues)
    }

    private fun observeStockData()
    {
        viewModel.dataStateLiveData.observe(this, androidx.lifecycle.Observer {
            var stockList = viewModel.getStockDataList()
            var stockDetailList = viewModel.getStockDetailDataList()
            when(it)
            {
                DataState.NetUpdate -> {
                    mStockAdapter?.setStockDatas(stockList, stockDetailList)
                    viewModel.dataStateLiveData.postValue(DataState.NetFinish)
                }
                DataState.NetFinish -> isDataFinish = true
            }
        })
    }
    private fun observeStockRandomList()
    {
        viewModel.stockRandomList.observe(this, androidx.lifecycle.Observer {
            var stockList = viewModel.getStockDataList()
            var stockDetailList = viewModel.getStockDetailDataList()
            for (index in it)
            {
                mStockAdapter?.updateStockDatas(
                    index,
                    stockList[index],
                    stockDetailList[index]
                )
            }
            mainHandler.postDelayed({
                viewModel.endLineEffect(it)
                for (index in it)
                {
                    mStockAdapter?.updateStockDatas(
                        index,
                        stockList[index],
                        stockDetailList[index]
                    )
                } },lineEffectTime)
        })
    }
    override fun scrollTo(l: Int, t: Int) {
        if (headHorizontalScrollView != null) {
            headHorizontalScrollView?.scrollTo(l, 0)
        }
    }
}