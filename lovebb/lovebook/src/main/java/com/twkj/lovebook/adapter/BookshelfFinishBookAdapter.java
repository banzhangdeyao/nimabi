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
import com.twkj.lovebook.bean.DraftBook;

import java.util.List;

/**
 * Created by tiantao on 2016/11/14.
 */

public class BookshelfFinishBookAdapter extends RecyclerView.Adapter<BookshelfFinishBookAdapter.FinishBookViewHolder>{

    private Context context;
    private List<DraftBook> listdata;
    private LayoutInflater inflater;

    public BookshelfFinishBookAdapter(Context context, List<DraftBook> listdata) {
        this.context = context;
        this.listdata = listdata;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    @Override
    public void onBindViewHolder(FinishBookViewHolder holder, int position) {
        holder.tv_bookname.setText(listdata.get(position).bookName);
        Glide.with(context)
                .load(listdata.get(position).coverImageName)
                .into(holder.iv_covers);

    }

    @Override
    public FinishBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FinishBookViewHolder holder = new FinishBookViewHolder(inflater.inflate(R.layout.item_bookshelf_finish_book , parent , false));

        return holder;
    }

    class FinishBookViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_covers;
        private TextView tv_bookname;

        public FinishBookViewHolder(View itemView) {
            super(itemView);
            iv_covers = (ImageView) itemView.findViewById(R.id.item_bookshelf_finish_book_iv_covers);
            tv_bookname = (TextView) itemView.findViewById(R.id.item_bookshelf_finish_book_tv_bookname);
        }
    }
}
