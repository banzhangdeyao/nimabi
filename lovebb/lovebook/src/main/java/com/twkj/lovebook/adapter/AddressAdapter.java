package com.twkj.lovebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;
import com.twkj.lovebook.bean.AddressBean;
import com.twkj.lovebook.bean.DraftBookPage;

import java.util.List;

/**
 * Created by wht on 2016/11/15.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<AddressBean> title;
    private ItemTouchHelper itemTouchHelper;
    private AddressAdapter.OnItemClickListener onItemClickListener;
    private Context context;

    public AddressAdapter(Context context, List mtitles){
        this.inflater=LayoutInflater.from(context);
        this.context=context;
        title=mtitles;
    }

    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_listview_address,parent,false);

        AddressAdapter.ViewHolder holder=new AddressAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AddressAdapter.ViewHolder holder, int position) {
        holder.shoujianren.setText(title.get(position).getShoujianren());
        holder.address_youbian.setText(title.get(position).getYoubian());
        holder.address_dizhi.setText(title.get(position).getDizhi());
        holder.address_phone.setText(title.get(position).getPhone());
//        Log.i("aaa","缩略图的uri===="+title.get(position).littleBookImage);
//        Glide.with(context).load(title.get(position).littleBookImage).into(holder.item_tv);
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

    public void setOnItemClickListener(AddressAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView item_tv;
        public TextView shoujianren,address_phone,address_dizhi,address_youbian;
        public ViewHolder(View view){
            super(view);
            item_tv = (ImageView)view.findViewById(R.id.address_selecet);
            shoujianren= (TextView) view.findViewById(R.id.address_shouhuoren);
            address_phone= (TextView) view.findViewById(R.id.address_phone);
            address_dizhi= (TextView) view.findViewById(R.id.address_dizhi);
            address_youbian=(TextView) view.findViewById(R.id.address_youbian);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}