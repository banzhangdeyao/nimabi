package com.twkj.lovebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.twkj.lovebook.R;
import com.twkj.lovebook.bean.AddressBean;
import com.twkj.lovebook.view.SlidingButtonView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wht on 2016/11/22.
 */
public class AddressRecycleViewAdapter extends RecyclerView.Adapter<AddressRecycleViewAdapter.MyViewHolder> implements SlidingButtonView.IonSlidingButtonListener {

    private Context mContext;

    private IonSlidingViewClickListener mIDeleteBtnClickListener;

    private List<AddressBean> mDatas = new ArrayList<>();

    private SlidingButtonView mMenu = null;

    public AddressRecycleViewAdapter(Context context,List<AddressBean> list) {

        mContext = context;
        mIDeleteBtnClickListener = (IonSlidingViewClickListener) context;

       mDatas=list;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.shoujianren.setText(mDatas.get(position).getShoujianren());
        holder.address_youbian.setText(mDatas.get(position).getYoubian());
        holder.address_dizhi.setText(mDatas.get(position).getDizhi());
        holder.address_phone.setText(mDatas.get(position).getPhone());
        //设置内容布局的宽为屏幕宽度
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE );
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( outMetrics);
        holder.layout_content.getLayoutParams().width = outMetrics.widthPixels;

        holder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                    Log.i("ttt","菜单关闭了么");
                } else {
                    int n = holder.getLayoutPosition();
                    mIDeleteBtnClickListener.onItemClick(v, n);
                }

            }
        });
        holder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getLayoutPosition();
                mIDeleteBtnClickListener.onDeleteBtnCilck(v, n);
            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_listview_address, arg0,false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }



    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView btn_Delete;
        public ImageView item_tv;
        public ViewGroup layout_content;
        public TextView shoujianren,address_phone,address_dizhi,address_youbian;
        public MyViewHolder(View itemView) {
            super(itemView);
            btn_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            item_tv = (ImageView)itemView.findViewById(R.id.address_selecet);
            shoujianren= (TextView) itemView.findViewById(R.id.address_shouhuoren);
            address_phone= (TextView) itemView.findViewById(R.id.address_phone);
            address_dizhi= (TextView) itemView.findViewById(R.id.address_dizhi);
            address_youbian=(TextView) itemView.findViewById(R.id.address_youbian);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            ((SlidingButtonView) itemView).setSlidingButtonListener(AddressRecycleViewAdapter.this);
        }
    }

    public void addData(int position) {
        mDatas.add(position, null);
        notifyItemInserted(position);
    }

    public void removeData(int position){
        mDatas.remove(position);
        notifyItemRemoved(position);

    }

    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }

    /**
     * 滑动或者点击了Item监听
     * @param slidingButtonView
     */
    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if(menuIsOpen()){
            if(mMenu != slidingButtonView){
                closeMenu();
                Log.i("ttt", "触发的onDOWN ORmove 的关闭");
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }
    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        if(mMenu != null){
            return true;
        }
        Log.i("asd","mMenu为null");
        return false;
    }



    public interface IonSlidingViewClickListener {
        void onItemClick(View view,int position);
        void onDeleteBtnCilck(View view,int position);
    }
}