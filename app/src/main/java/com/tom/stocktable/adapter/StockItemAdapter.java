package com.tom.stocktable.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.tom.stocktable.R;

import java.util.List;


public class StockItemAdapter extends RecyclerView.Adapter<com.tom.stocktable.adapter.StockItemAdapter.ItemViewHolder>
{
    private int redColor;
    private int greenColor;
    private int backColor;
    private Boolean isUp;
    private Boolean isSetup;
    private List<String> detailDatas;
    private ItemViewHolder itemViewHolder;

    private Context mContext;
    private LayerDrawable layerDrawable;
    private GradientDrawable gradientDrawable;
    private Handler handler;


    public StockItemAdapter(Context mContext) {
        this.mContext = mContext;
        redColor = ContextCompat.getColor(mContext, R.color.redColor);
        greenColor = ContextCompat.getColor(mContext, R.color.greenColor);
        String backHexColor = "#18181A";
        backColor = Color.parseColor(backHexColor);
        layerDrawable = (LayerDrawable) ContextCompat
                .getDrawable(mContext, R.drawable.border_bottom);
        gradientDrawable = (GradientDrawable) layerDrawable
                .findDrawableByLayerId(R.id.border_bottomLine);
        handler = new Handler(Looper.getMainLooper());
    }


    public void setDetailDatas(List<String> detailDatas, Boolean isUp, Boolean isSetup) {
        this.isUp = isUp;
        this.isSetup = isSetup;
        this.detailDatas = detailDatas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_content_detail, parent, false);
        itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.mTabView.setText(detailDatas.get(position));
        gradientDrawable.setColor(backColor);
        holder.mTabView.setBackground(layerDrawable);
        if (isUp)
        {
            holder.mTabView.setTextColor(redColor);// 紅色
            if(!isSetup)
            {
                gradientDrawable.setColor(redColor);
                holder.mTabView.setBackground(layerDrawable);
            }
        }
        else
        {
            holder.mTabView.setTextColor(greenColor);// 綠色
            if(!isSetup)
            {
                gradientDrawable.setColor(greenColor);
                holder.mTabView.setBackground(layerDrawable);
            }
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




