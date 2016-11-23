package com.twkj.lovebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;
import com.twkj.lovebook.utils.ControlBitmapUtils;

import java.util.List;

/**
 * Created by tiantao on 2016/11/18.
 */

public class UpBookInfoModelAdapter extends RecyclerView.Adapter<UpBookInfoModelAdapter.UpBookInfoModelViewHolder>{

    private Context context;

    private List<String> listdata;

    private LayoutInflater inflater;

    public UpBookInfoModelAdapter(Context context, List<String> listdata) {
        this.context = context;
        this.listdata = listdata;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return listdata != null ? listdata.size() : 0;
    }

    @Override
    public void onBindViewHolder(UpBookInfoModelViewHolder holder, int position) {

        Glide.with(context)
                .load(ControlBitmapUtils.getResId(listdata.get(position) , R.drawable.class))
                .placeholder(R.drawable.img_bookshelf_cover)
                .error(R.drawable.img_bookshelf_cover)
                .into(holder.iv_model);
    }

    @Override
    public UpBookInfoModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UpBookInfoModelViewHolder holder = new UpBookInfoModelViewHolder(inflater.inflate(R.layout.item_up_book_info_model , parent , false));
        return holder;
    }

    class UpBookInfoModelViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_model;

        public UpBookInfoModelViewHolder(View itemView) {
            super(itemView);
            iv_model = (ImageView) itemView.findViewById(R.id.item_up_book_info_model_iv);
        }
    }
}
