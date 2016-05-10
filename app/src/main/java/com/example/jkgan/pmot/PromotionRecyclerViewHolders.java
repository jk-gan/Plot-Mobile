package com.example.jkgan.pmot;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jkgan.pmot.Shops.Promotion;

import java.util.List;

public class PromotionRecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView promotionTitle;
    public TextView promotionDescription;
    public ImageView promotionImage;
    private List<Promotion> promotionList;

    public PromotionRecyclerViewHolders(View itemView, List<Promotion> itemList) {
        super(itemView);
        promotionTitle = (TextView)itemView.findViewById(R.id.promotion_title);
        promotionDescription = (TextView)itemView.findViewById(R.id.promotion_description);
        promotionImage = (ImageView)itemView.findViewById(R.id.thumbnail);
        this.promotionList = itemList;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        int position = getLayoutPosition(); // gets item position
        final Promotion promotion = promotionList.get(position);
        // We can access the data within the views
        new Thread() {
            public void run() {

                final Intent intent = new Intent(view.getContext(), PromotionActivity.class);
                intent.putExtra("NAME", promotion.getName());
                intent.putExtra("SHOP_ID", promotion.getId());
                intent.putExtra("IMAGE", promotion.getImage());
                intent.putExtra("DESCRIPTION", promotion.getDescription());
                intent.putExtra("TNC", promotion.getTnc());
                intent.putExtra("SHOP_NAME", promotion.getShop().getName());
                intent.putExtra("ADDRESS", promotion.getShop().getAddress());
                intent.putExtra("START", promotion.getStarts_at());
                intent.putExtra("EXPIRE", promotion.getExpires_at());
                intent.putExtra("PHONE", promotion.getShop().getPhone());
                intent.putExtra("SUBSCRIBED", true);
                view.getContext(). startActivity(intent);

//                new Thread() {
//                    public void run() {
//                        ((Activity) view.getContext()).runOnUiThread(new Runnable() {
//                            public void run() {
//                                ActivityOptionsCompat options =
//                                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)view.getContext(),
//                                                view.findViewById(R.id.circleView),   // The view which starts the transition
//                                                "test"    // The transitionName of the view we’re transitioning to
//                                        );
//                                ActivityCompat.startActivity((Activity) view.getContext(), intent, options.toBundle());
//                            }
//                        });
//                    }
//                }.start();
            }
        }.start();
    }

}