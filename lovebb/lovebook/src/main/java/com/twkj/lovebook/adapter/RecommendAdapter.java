package com.twkj.lovebook.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;
import com.twkj.lovebook.bean.RecommendBean;
import com.twkj.lovebook.utils.ControlBitmapUtils;

import java.util.List;

/**
 * Created by tiantao on 2016/11/22.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder>{

    private Context context;

    private List<RecommendBean> listdata;

    private LayoutInflater inflater;

    public RecommendAdapter(Context context, List<RecommendBean> listdata) {
        this.context = context;
        this.listdata = listdata;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return listdata != null ? listdata.size() : 0;
    }

    @Override
    public void onBindViewHolder(RecommendViewHolder holder, int position) {
        Glide.with(context)
                .load(ControlBitmapUtils.getResId(listdata.get(position).imageResource , R.drawable.class))
                .into(holder.image);
    }

    @Override
    public RecommendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecommendViewHolder holder = new RecommendViewHolder(inflater.inflate(R.layout.item_recommend , parent , false));
        return holder;
    }

    class RecommendViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;

        public RecommendViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.item_recommend_image);
        }
    }

}
