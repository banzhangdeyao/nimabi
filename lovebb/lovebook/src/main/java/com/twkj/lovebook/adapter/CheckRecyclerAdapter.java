package com.twkj.lovebook.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;
import com.twkj.lovebook.bean.DraftBookPage;

import java.io.IOException;
import java.util.List;

/**
 * Created by wht on 2016/11/4.
 */

public class CheckRecyclerAdapter extends RecyclerView.Adapter<CheckRecyclerAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<DraftBookPage> title;
    private ItemTouchHelper itemTouchHelper;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public CheckRecyclerAdapter(Context context, List mtitles){
        this.inflater=LayoutInflater.from(context);
        this.context=context;
        title=mtitles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_check,parent,false);

        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            Log.i("aaa","缩略图的uri===="+title.get(position).littleBookImage);
        Glide.with(context).load(title.get(position).littleBookImage).into(holder.item_tv);
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper =  itemTouchHelper;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public ImageView item_tv;
            public ViewHolder(View view){
                super(view);
                item_tv = (ImageView)view.findViewById(R.id.item);
                view.setOnClickListener(this);
            }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
