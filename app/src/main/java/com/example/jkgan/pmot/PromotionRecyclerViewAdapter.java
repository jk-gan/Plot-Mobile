package com.example.jkgan.pmot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.jkgan.pmot.Shops.Promotion;

import java.util.List;

public class PromotionRecyclerViewAdapter extends RecyclerView.Adapter<PromotionRecyclerViewHolders> {

    private List<Promotion> itemList;
    private Context context;

    public PromotionRecyclerViewAdapter(Context context, List<Promotion> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public PromotionRecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.promotions_list, null);
        PromotionRecyclerViewHolders rcv = new PromotionRecyclerViewHolders(layoutView, itemList);
        return rcv;
    }

    @Override
    public void onBindViewHolder(PromotionRecyclerViewHolders holder, int position) {
        holder.promotionTitle.setText(itemList.get(position).getName());
        holder.promotionDescription.setText(itemList.get(position).getDescription());
        Glide.with(context).load(MyApplication.getUrl() + itemList.get(position).getImage()).into(holder.promotionImage);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}