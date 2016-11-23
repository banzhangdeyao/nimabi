package com.twkj.lovebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by tiantao on 2016/11/4.
 */

public class ReviseImageTiezhiAdapter extends RecyclerView.Adapter<ReviseImageTiezhiAdapter.TiezhiViewHolder> {

    private Context context;
    private List<String> listdata;
    private OnItemClickListener onItemClickListener;

    public ReviseImageTiezhiAdapter(Context context, List<String> listdata) {
        this.context = context;
        this.listdata = listdata;
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    @Override
    public void onBindViewHolder(final TiezhiViewHolder holder, int position) {
        Glide.with(context)
                .load(getResId(listdata.get(position) , R.drawable.class))
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("RITiezhiAdapter" , "==============onBindViewHolder.onclick=================");
                int pos = holder.getLayoutPosition();
                onItemClickListener.onItemClick(holder.itemView , pos);
            }
        });
    }

    @Override
    public TiezhiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TiezhiViewHolder holder = new TiezhiViewHolder(LayoutInflater.from(context).inflate(R.layout.item_editor_develop_background , parent , false));

        return holder;
    }

    private int getResId(String variableName , Class<?> c){
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v , int pos);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        Log.i("RITiezhiAdapter" , "====================setOnItemClickListener======================");
    }

    class TiezhiViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public TiezhiViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_editor_develop_background_iv);
        }
    }
}
