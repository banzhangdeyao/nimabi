package com.twkj.lovebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;
import com.twkj.lovebook.utils.ControlBitmapUtils;

import java.util.List;

/**
 * Created by tiantao on 2016/11/16.
 * 写一本书左侧模板的adapter
 */

public class WriteABookModelAdapter extends RecyclerView.Adapter<WriteABookModelAdapter.WriteABookModelViewHolder>{

    private Context context;
    private List<String> listdata;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public WriteABookModelAdapter(Context context, List<String> listdata) {
        this.context = context;
        this.listdata = listdata;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    @Override
    public void onBindViewHolder(final WriteABookModelViewHolder holder, int position) {
        Glide.with(context)
                .load(ControlBitmapUtils.getResId(listdata.get(position) , R.drawable.class))
                .into(holder.iv_model);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                onItemClickListener.onItemClick(holder.itemView , pos);
            }
        });
    }

    @Override
    public WriteABookModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WriteABookModelViewHolder holder = new WriteABookModelViewHolder(inflater.inflate(R.layout.item_writeabook_model , parent , false));
        return holder;
    }

    public interface OnItemClickListener{
        void onItemClick(View view , int position);
    }

    class WriteABookModelViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_model;

        public WriteABookModelViewHolder(View itemView) {
            super(itemView);
            iv_model = (ImageView) itemView.findViewById(R.id.item_writeabook_model_iv_model);
        }
    }

}
