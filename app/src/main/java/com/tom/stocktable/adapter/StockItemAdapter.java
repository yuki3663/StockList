package com.tom.stocktable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tom.stocktable.R;

import java.util.List;
import java.util.Random;


public class StockItemAdapter extends RecyclerView.Adapter<com.tom.stocktable.adapter.StockItemAdapter.ItemViewHolder> {

    private Boolean isUp;
    private List<String> detailDatas;

    private Context mContext;

    public StockItemAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void setDetailDatas(List<String> detailDatas, Boolean isUp) {
        this.isUp = isUp;
        this.detailDatas = detailDatas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_content_detail, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.mTabView.setText(detailDatas.get(position));
        if (isUp) {
            holder.mTabView.setTextColor(mContext.getResources().getColor(R.color.redColor));// 灰色
        } else {
            holder.mTabView.setTextColor(mContext.getResources().getColor(R.color.greenColor));// 绿色
        }
    }

    @Override
    public int getItemCount() {
        return detailDatas.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView mTabView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTabView = itemView.findViewById(R.id.tabView);
        }
    }
}




