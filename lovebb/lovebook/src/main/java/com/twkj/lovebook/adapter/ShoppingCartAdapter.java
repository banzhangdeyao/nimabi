package com.twkj.lovebook.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;
import com.twkj.lovebook.activity.ShoppingCartActivity;
import com.twkj.lovebook.bean.BookProducts;
import com.twkj.lovebook.dao.BookProductsDao;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiantao on 2016/11/19.
 */

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder>{

    private Context context;
    private List<BookProducts> listdata;//数据源
    private boolean isEditOrOk;//true = edit  false = ok

    private LayoutInflater inflater;
    private BookProductsDao bookProductsDao;
    private List<BookProducts> listcheck;//选中的数据
    public ShoppingCartAdapter(Context context, List<BookProducts> listdata , boolean isEditOrOk) {
        this.context = context;
        this.listdata = listdata;
        this.isEditOrOk = isEditOrOk;

        this.inflater = LayoutInflater.from(context);
        this.bookProductsDao = new BookProductsDao();
        this.listcheck = new ArrayList<BookProducts>();
    }

    @Override
    public int getItemCount() {
        return listdata != null ? listdata.size() : 0;
    }

    @Override
    public void onBindViewHolder(ShoppingCartViewHolder holder, int position) {
        Glide.with(context)
                .load(listdata.get(position).getCoverImage())
                .placeholder(R.drawable.img_bookshelf_cover)
                .error(R.drawable.img_bookshelf_cover)
                .into(holder.iv_covers);
        holder.tv_count.setText(listdata.get(position).count+"");
        holder.tv_bookName.setText(listdata.get(position).bookName + listdata.get(position).bookProductsId);
        holder.tv_contacterAddress.setText(listdata.get(position).contacterAddress);
        holder.tv_info.setText(listdata.get(position).bookInfor);
        holder.tv_price.setText(listdata.get(position).price);
        if (isEditOrOk){

            holder.tv_jianhao.setVisibility(View.INVISIBLE);
            holder.tv_count.setVisibility(View.INVISIBLE);
            holder.tv_jiahao.setVisibility(View.INVISIBLE);
        }else{
            holder.tv_jianhao.setVisibility(View.VISIBLE);
            holder.tv_count.setVisibility(View.VISIBLE);
            holder.tv_jiahao.setVisibility(View.VISIBLE);
        }
        if (listdata.get(position).isCheck){
            holder.iv_check.setImageResource(R.drawable.btn_shoppingcart_choice);
        }else{
            holder.iv_check.setImageResource(R.drawable.btn_shoppingcart_unchoice);
        }
    }

    @Override
    public ShoppingCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ShoppingCartViewHolder holder = new ShoppingCartViewHolder(inflater.inflate(R.layout.item_shopping_cart , parent , false));
        return holder;
    }

    /**
     * 是编辑状态还是完成
     * @param editOrOk true = edit  false = ok
     */
    public void isEditOrOk(boolean editOrOk){
        this.isEditOrOk = editOrOk;
        notifyDataSetChanged();


    }

    /**
     * 删除功能
     */
    public void delete(){

        for(BookProducts bp : listcheck){
            if (bp.isCheck()){
                bookProductsDao.deleteByBookProductsId(bp.bookProductsId);


            }
        }
        listdata.removeAll(listcheck);
        listcheck.clear();
        notifyDataSetChanged();
    }

    /**
     * 复制功能
     */
    public void copy(){
        for (BookProducts bp : listcheck){
            if (bp.isCheck()){
                //新建对象 避免自增长id冲突
                BookProducts bpnew = new BookProducts();
                bpnew.setBookId(bp.bookId);
                bpnew.setBookProductsId(bookProductsDao.findmax_id() + 1);
                bpnew.setBookInfor(bp.bookInfor);
                bpnew.setBookName(bp.bookName);
                bpnew.setCoverImage(bp.coverImage);
                bpnew.setContacterAddress(bp.contacterAddress);
                bpnew.setCount(bp.count);
                bpnew.setPrice(bp.price);
                bookProductsDao.insert(bpnew);
                listdata.add(0 , bpnew);
            }
        }
        notifyDataSetChanged();
    }

    class ShoppingCartViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_covers;
        private TextView tv_jiahao , tv_jianhao , tv_count;
        private TextView tv_bookName , tv_contacterAddress , tv_info , tv_price ;
        private ImageView iv_check;

        public ShoppingCartViewHolder(View itemView) {
            super(itemView);
            iv_covers = (ImageView) itemView.findViewById(R.id.item_shopping_cart_iv_covers);
            tv_jiahao = (TextView) itemView.findViewById(R.id.item_shopping_cart_tv_jiahao);
            tv_jianhao = (TextView) itemView.findViewById(R.id.item_shopping_cart_tv_jianhao);
            tv_count = (TextView) itemView.findViewById(R.id.item_shopping_cart_tv_count);
            tv_bookName = (TextView) itemView.findViewById(R.id.item_shopping_cart_tv_bookName);
            tv_contacterAddress = (TextView) itemView.findViewById(R.id.item_shopping_cart_tv_contacterAddress);
            tv_info = (TextView) itemView.findViewById(R.id.item_shopping_cart_tv_info);
            tv_price = (TextView) itemView.findViewById(R.id.item_shopping_cart_tv_price);
            iv_check = (ImageView) itemView.findViewById(R.id.item_shopping_cart_iv_check);
            /**
             * 选中了的监听
             */
            iv_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listcheck.contains(listdata.get(getLayoutPosition()))){
                        listcheck.remove(listdata.get(getLayoutPosition()));
                        listdata.get(getLayoutPosition()).setCheck(false);
                        notifyItemChanged(getLayoutPosition());
                    }else{
                        listcheck.add(listdata.get(getLayoutPosition()));
                        listdata.get(getLayoutPosition()).setCheck(true);
                        notifyItemChanged(getLayoutPosition());
                    }
                }
            });

            tv_jiahao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listdata.get(getLayoutPosition()).count++;
                    notifyItemChanged(getLayoutPosition());
                }
            });

            tv_jianhao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listdata.get(getLayoutPosition()).count > 0){

                    listdata.get(getLayoutPosition()).count--;
                    notifyItemChanged(getLayoutPosition());
                    }
                }
            });
        }
    }
}
