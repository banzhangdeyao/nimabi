package com.twkj.lovebook.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twkj.lovebook.R;
import com.twkj.lovebook.adapter.AddressAdapter;
import com.twkj.lovebook.adapter.AddressRecycleViewAdapter;
import com.twkj.lovebook.bean.AddressBean;
import com.twkj.lovebook.helper.DefaultItemTouchHelpCallback;
import com.twkj.lovebook.helper.DefaultItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wht on 2016/11/15.
 */

public class CommonaddressActivity extends Activity implements AddressRecycleViewAdapter.IonSlidingViewClickListener{
    private RecyclerView list;
//    private AddressAdapter adapter;
    AddressRecycleViewAdapter adapter;
    private ImageView back;
    private TextView guanli;
    private DefaultItemTouchHelper itemTouchHelper;
    private Context context;
    List<AddressBean> addressList;
//    private AddressAdapter.OnItemClickListener clickListener = new AddressAdapter.OnItemClickListener() {
//        @Override
//        public void onItemClick(View view, int position) {
//            Toast.makeText(CommonaddressActivity.this, "第" + position + "被点击", Toast.LENGTH_SHORT).show();
//
//        }
//    };
//
//    private DefaultItemTouchHelpCallback.OnItemTouchCallbackListener onItemTouchCallbackListener = new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
//        @Override
//        public void onSwiped(int adapterPosition) {
//            if (addressList != null) {
//                addressList.remove(adapterPosition);
//                adapter.notifyItemRemoved(adapterPosition);
//            }
//
//        }
//
//        @Override
//        public boolean onMove(int srcPosition, int targetPosition) {
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonaddress);
        initView();
        initAdapter();

        list= (RecyclerView) findViewById(R.id.myaddress_list);
        list.setHasFixedSize(true);
       LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        //设置纵向的布局
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        list.setLayoutManager(mLayoutManager);
//        adapter = new AddressAdapter(this,addressList);
//        adapter.setOnItemClickListener(clickListener);
        adapter=new AddressRecycleViewAdapter(this,addressList);
        list.setAdapter(adapter);
        list.setItemAnimator(new DefaultItemAnimator());

//        // 把ItemTouchHelper和itemTouchHelper绑定
//        itemTouchHelper = new DefaultItemTouchHelper(onItemTouchCallbackListener);
//        itemTouchHelper.attachToRecyclerView(list);
//
////        adapter.setItemTouchHelper(itemTouchHelper);
//
//        itemTouchHelper.setDragEnable(true);
//        itemTouchHelper.setSwipeEnable(true);

    }
    private void initAdapter(){
        //获取数据初始化adapter
        addressList=new ArrayList<>();
        AddressBean bean1=new AddressBean();
        bean1.setShoujianren("123");
        bean1.setDizhi("123");
        bean1.setPhone("1231231455123");
        bean1.setYoubian("31214123123");
        AddressBean bean2=new AddressBean();
        bean2.setShoujianren("223");
        bean2.setDizhi("123");
        bean2.setPhone("1231231455123");
        bean2.setYoubian("31214123123");
        AddressBean bean3=new AddressBean();
        bean3.setShoujianren("323");
        bean3.setDizhi("123");
        bean3.setPhone("1231231455123");
        bean3.setYoubian("31214123123");
        addressList.add(bean1);
        addressList.add(bean2);
        addressList.add(bean3);
    }
    private void initView(){
        back= (ImageView) findViewById(R.id.myaddress_back);
        guanli= (TextView) findViewById(R.id.myaddress_guanli);
    }




//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.myaddress_back:
//
//                break;
//            case R.id.myaddress_guanli:
//
//                break;
//        }
//    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("111","点击项："+position);
    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        Log.i("111","删除项："+position);
        adapter.removeData(position);
    }
}
