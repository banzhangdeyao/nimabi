package com.twkj.lovebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by tiantao on 2016/10/27.
 */

public class EditorDevelopBackgroundAdapter extends RecyclerView.Adapter<EditorDevelopBackgroundAdapter.BackgroundViewHolder> {

    private Context context;
    private List<String> listdata;
    private OnItemClickListener onItemClickListener;

    public EditorDevelopBackgroundAdapter(Context context, List<String> listdata) {
        this.context = context;
        this.listdata = listdata;
    }

    @Override
    public BackgroundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BackgroundViewHolder holder = new BackgroundViewHolder(LayoutInflater.from(context).inflate(R.layout.item_editor_develop_background , parent , false));


        return holder;
    }

    @Override
    public void onBindViewHolder(final BackgroundViewHolder holder, int position) {
                Glide.with(context)
                .load(getResId(listdata.get(position) , R.drawable.class))
                .into(holder.imageView)
                ;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.imageView , pos);
                    }
                });
    }


    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
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

    class BackgroundViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        public BackgroundViewHolder(View itemView) {

            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_editor_develop_background_iv);
        }
    }
}
