package com.example.jkgan.pmot;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView personName;
    public TextView personAddress;
    public ImageView personPhoto;
    private List<Shop> shopList;

    public RecyclerViewHolders(View itemView, List<Shop> itemList) {
        super(itemView);
        personName = (TextView)itemView.findViewById(R.id.person_name);
        personAddress = (TextView)itemView.findViewById(R.id.person_address);
        personPhoto = (ImageView)itemView.findViewById(R.id.circleView);
        this.shopList = itemList;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        int position = getLayoutPosition(); // gets item position
        final Shop shop = shopList.get(position);
        // We can access the data within the views
        new Thread() {
            public void run() {

                final Intent intent = new Intent(view.getContext(), ShopActivity.class);
                intent.putExtra("NAME", shop.getName());
                intent.putExtra("SHOP_ID", shop.getId());
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