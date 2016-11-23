package com.twkj.lovebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;

import java.util.List;

/**
 * Created by tiantao on 2016/10/28.
 */

public class EditorEditBookAutoAdapter extends RecyclerView.Adapter<EditorEditBookAutoAdapter.EditorEditBookAutoViewHolder> {


    private Context context;
    private List<String> listdata;

    private OnItemClickListener onItemClickListener;

    public EditorEditBookAutoAdapter(Context context, List<String> listdata) {
        this.context = context;
        this.listdata = listdata;
    }

    @Override
    public void onBindViewHolder(final EditorEditBookAutoViewHolder holder, int position) {
        Glide.with(context)
                .load(R.drawable.shading1)
                .into(holder.imageView);

        //如果设置了回调，则设置点击事件
        if(onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView , pos);
                }
            });
        }
    }

    @Override
    public EditorEditBookAutoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        EditorEditBookAutoViewHolder holder = new EditorEditBookAutoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_editor_editbook_auto , parent , false));


        return holder;
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class EditorEditBookAutoViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView tv_name;
        private TextView tv_model;
        private TextView tv_size;

        public EditorEditBookAutoViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_editor_editbook_auto_iv);
        }
    }
}
