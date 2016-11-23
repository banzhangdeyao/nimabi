package com.twkj.lovebook.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;

import java.util.List;

/**
 * Created by tiantao on 2016/11/3.
 */

public class ReviseImageTexiaoAdapter extends RecyclerView.Adapter<ReviseImageTexiaoAdapter.TexiaoViewHolder> {

    private Context context;
    private List<Bitmap> listdata;
    private OnItemClickListener onItemClickListener;

    public ReviseImageTexiaoAdapter(Context context , List<Bitmap> listdata) {
        this.context = context;
        this.listdata = listdata;
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    @Override
    public void onBindViewHolder(final TexiaoViewHolder holder, int position) {

        holder.iv.setImageBitmap(listdata.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                onItemClickListener.onItemClick(holder.itemView , pos);
            }
        });

    }

    public interface OnItemClickListener{
        void onItemClick(View view , int pos);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public TexiaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TexiaoViewHolder holder = new TexiaoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_editor_develop_background , parent , false));
        return holder;
    }

    class TexiaoViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv;

        public TexiaoViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.item_editor_develop_background_iv);
        }
    }
}
