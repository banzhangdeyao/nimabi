package com.twkj.lovebook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;
import com.twkj.lovebook.activity.UpBookInfoActivity;

import java.util.List;

/**
 * Created by tiantao on 2016/11/17.
 */

public class BookshelfUpBookAdapter extends RecyclerView.Adapter<BookshelfUpBookAdapter.BookshelfUpBookViewHolder>{

    private Context context;
    private List<String> listdata;
    private LayoutInflater inflater;

    public BookshelfUpBookAdapter(Context context, List<String> listdata) {
        this.context = context;
        this.listdata = listdata;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return listdata != null ? listdata.size() : 0;
    }

    @Override
    public void onBindViewHolder(BookshelfUpBookViewHolder holder, int position) {
        Glide.with(context)
                .load(listdata.get(position))
                .placeholder(R.drawable.img_bookshelf_cover)
                .error(R.drawable.img_bookshelf_cover)
                .into(holder.iv_covers);
    }

    @Override
    public BookshelfUpBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BookshelfUpBookViewHolder holder = new BookshelfUpBookViewHolder(inflater.inflate(R.layout.item_bookshelf_up_book , parent , false));
        return holder;
    }

    class BookshelfUpBookViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_covers;
        private TextView tv_flag;

        public BookshelfUpBookViewHolder(View itemView) {
            super(itemView);
            iv_covers = (ImageView) itemView.findViewById(R.id.item_bookshelf_up_book_iv_covers);
            iv_covers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context , UpBookInfoActivity.class));
                }
            });
            tv_flag = (TextView) itemView.findViewById(R.id.item_bookshelf_up_book_tv_flag);
        }
    }
}
