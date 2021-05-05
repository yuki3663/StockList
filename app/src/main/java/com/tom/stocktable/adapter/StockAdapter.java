package com.tom.stocktable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.tom.stocktable.CustomizeScrollView;
import com.tom.stocktable.R;
import com.tom.stocktable.model.StockData;
import com.tom.stocktable.model.StockDetailData;

import java.util.ArrayList;
import java.util.List;


public class StockAdapter extends RecyclerView.Adapter<com.tom.stocktable.adapter.StockAdapter.ViewHolder> {

    //股票列表ViewHolder集合
    private List<ViewHolder> recyclerViewHolder = new ArrayList<>();
    
    //記錄item滑動的位置，用於RecyclerView上下滾動時更新所有列表
    private int offestX;

    private OnTabScrollViewListener onTabScrollViewListener;

    private List<StockData> StockDatas = new ArrayList<>();
    private List<StockDetailData> StockDetailDatas= new ArrayList<>();

    private Context mContext;

    public StockAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setOnTabScrollViewListener(OnTabScrollViewListener onTabScrollViewListener) {
        this.onTabScrollViewListener = onTabScrollViewListener;
    }

    public void setStockDatas(List<StockData> stockDatas, List<StockDetailData> stockDetailDatas) {
        this.StockDatas = stockDatas;
        this.StockDetailDatas = stockDetailDatas;
        notifyDataSetChanged();
    }

    public List<ViewHolder> getRecyclerViewHolder() {
        return recyclerViewHolder;
    }

    public int getOffestX() {
        return offestX;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_content_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mStockName.setText(StockDatas.get(position).getStockName());
        holder.mStockRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        holder.mStockRecyclerView.setNestedScrollingEnabled(false);

        // TODO：文本RecyclerView中具體信息的RecyclerView（RecyclerView嵌套）
        StockItemAdapter stockItemAdapter = new StockItemAdapter(mContext);
        holder.mStockRecyclerView.setAdapter(stockItemAdapter);
        stockItemAdapter.setDetailDatas(StockDatas.get(position).getDetails(), StockDetailDatas.get(position).isUp());

        if (!recyclerViewHolder.contains(holder)) {
            recyclerViewHolder.add(holder);
        }

        /**
         * 第一步：水平滑動item時，遍歷所有ViewHolder，使得整個列表的HorizontalScrollView同步滑動
         */
        holder.mStockScrollView.setViewListener(new CustomizeScrollView.OnScrollViewListener() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                for (ViewHolder viewHolder : recyclerViewHolder)
                {
                    if (viewHolder != holder) {
                        viewHolder.mStockScrollView.scrollTo(l, 0);
                    }
                }
                /**
                 * 第二步：水平滑動item時，接口回調到Tab欄的HorizontalScrollView，使得Tab欄跟隨item滾動即時更新
                 */
                if (onTabScrollViewListener != null) {
                    onTabScrollViewListener.scrollTo(l, t);
                    offestX = l;
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return StockDatas.size() == 0 ? 0 : StockDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mStockName;
        public CustomizeScrollView mStockScrollView;
        public RecyclerView mStockRecyclerView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mStockName = itemView.findViewById(R.id.stockName);
            mStockScrollView = itemView.findViewById(R.id.stockScrollView);
            mStockRecyclerView = itemView.findViewById(R.id.stockRecyclerView);
        }
    }


    public interface OnTabScrollViewListener {
        void scrollTo(int l, int t);
    }
}
