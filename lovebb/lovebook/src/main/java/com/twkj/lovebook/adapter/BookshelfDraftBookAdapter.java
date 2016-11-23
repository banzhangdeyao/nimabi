package com.twkj.lovebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;
import com.twkj.lovebook.bean.DraftBook;
import com.twkj.lovebook.fragment.BookshelfFragment;

import java.util.List;

/**
 * Created by tiantao on 2016/11/14.
 * 书架 草稿书的adapter
 */

public class BookshelfDraftBookAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<DraftBook> listdata;
    private BookshelfFragment bookshelfFragment;
    private LayoutInflater inflater;
    public static final int TYPE_NORMAL = 1000;
    public static final int TYPE_LAST = 1001;
    private int lastViewCount = 1;//最后添加view个数
    private boolean deleteOrNot = false;

    public BookshelfDraftBookAdapter(Context context, List<DraftBook> listdata , BookshelfFragment bookshelfFragment) {
        this.context = context;
        this.listdata = listdata;
        this.bookshelfFragment = bookshelfFragment;
        this.inflater = LayoutInflater.from(context);
    }


    /**
     * 判断是不是最后布局
     * @param position
     * @return
     */
    public boolean isLastView(int position){
        return position >=listdata.size();
    }

    public void deleteOrNot(boolean flag){
        this.deleteOrNot = flag;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return listdata != null ? listdata.size() + lastViewCount : lastViewCount;
    }





    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case TYPE_NORMAL:
                ((BookshelfDraftBookViewHolder)holder).tv_bookname.setText(listdata.get(position).bookName);
                Glide.with(context)
                        .load(listdata.get(position).coverImageName)
                        .placeholder(R.drawable.img_bookshelf_cover)
                        .error(R.drawable.img_bookshelf_cover)
                        .into(((BookshelfDraftBookViewHolder)holder).iv_covers);
                if(deleteOrNot){
                    ((BookshelfDraftBookViewHolder)holder).iv_delete.setVisibility(View.VISIBLE);
                }else{
                    ((BookshelfDraftBookViewHolder)holder).iv_delete.setVisibility(View.GONE);
                }
                break;
            case TYPE_LAST:
                Glide.with(context)
                        .load(R.drawable.bookshelf_img_add)
                        .into(((BookshelfDraftBookAddViewHolder)holder).iv);
                break;
            default:
                break;
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case TYPE_NORMAL:
                holder = new BookshelfDraftBookViewHolder(inflater.inflate(R.layout.item_bookshelf_draft_book , parent , false));
                break;
            case TYPE_LAST:
                holder = new BookshelfDraftBookAddViewHolder(inflater.inflate(R.layout.item_bookshelf_draft_book_add , parent , false));
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//        return listdata.get(position) != null ? TYPE_NORMAL : TYPE_LAST;
        if (position < getItemCount()-1){
            return TYPE_NORMAL;
        }else{
            return TYPE_LAST;
        }
    }


    //    @Override
//    public BookshelfDraftBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        BookshelfDraftBookViewHolder holder = new BookshelfDraftBookViewHolder(inflater.inflate(R.layout.item_bookshelf_draft_book , parent , false));
//        return holder;
//    }

    class BookshelfDraftBookViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_covers;
        private ImageView iv_delete;
        private TextView tv_bookname;

        public BookshelfDraftBookViewHolder(final View itemView) {
            super(itemView);
            iv_covers = (ImageView) itemView.findViewById(R.id.item_bookshelf_draft_book_iv_covers);
            iv_delete = (ImageView) itemView.findViewById(R.id.item_bookshelf_draft_book_iv_delete);
            tv_bookname = (TextView) itemView.findViewById(R.id.item_bookshelf_draft_book_tv_bookname);
            iv_covers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookshelfFragment.startActivityWrite(getLayoutPosition());
                }
            });
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookshelfFragment.deleteDraftBook(getLayoutPosition());
                }
            });
        }
    }
    class BookshelfDraftBookAddViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv;
        public BookshelfDraftBookAddViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.item_bookshelf_draft_book_add_iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookshelfFragment.jumpEditorFragment();
                }
            });
        }
    }
}
