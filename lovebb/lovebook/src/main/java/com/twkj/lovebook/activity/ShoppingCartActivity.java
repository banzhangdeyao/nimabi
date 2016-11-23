package com.twkj.lovebook.activity;

import android.app.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twkj.lovebook.R;
import com.twkj.lovebook.adapter.ShoppingCartAdapter;
import com.twkj.lovebook.bean.BookProducts;
import com.twkj.lovebook.dao.BookProductsDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车 2016年11月19日10:21:37 加你麻痹的班！
 */
public class ShoppingCartActivity extends Activity implements View.OnClickListener {

    private ImageView iv_back;

    private TextView tv_ok , tv_edit ,  tv_title; // 完成 ， 标题 身在北京cheng城

    private LinearLayout ll_ok , ll_edit;

    private Button btn_ok , btn_copy , btn_delete;

    private RecyclerView rv_shoppingcart;

    private ShoppingCartAdapter adapter;

    private BookProductsDao bookProductsDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        initView();
    }

    private void initView(){
        bookProductsDao = new BookProductsDao();
        iv_back = (ImageView) findViewById(R.id.activity_shopping_cart_iv_back);
        tv_title = (TextView) findViewById(R.id.activity_shopping_cart_tv_title);
        tv_ok = (TextView) findViewById(R.id.activity_shopping_cart_tv_ok);
        tv_edit = (TextView) findViewById(R.id.activity_shopping_cart_tv_edit);
        rv_shoppingcart = (RecyclerView) findViewById(R.id.activity_shopping_cart_rv_shoppingcart);
        btn_copy = (Button) findViewById(R.id.activity_shopping_cart_btn_copy);
        btn_delete = (Button) findViewById(R.id.activity_shopping_cart_btn_delete);
        btn_ok = (Button) findViewById(R.id.activity_shopping_cart_btn_ok);
        ll_ok = (LinearLayout) findViewById(R.id.activity_shopping_cart_ll_ok);
        ll_edit = (LinearLayout) findViewById(R.id.activity_shopping_cart_ll_edit);

        tv_edit.setVisibility(View.GONE);
        tv_ok.setVisibility(View.VISIBLE);
        ll_edit.setVisibility(View.VISIBLE);
        ll_ok.setVisibility(View.GONE);

        iv_back.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        btn_copy.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

        rv_shoppingcart.setLayoutManager(new LinearLayoutManager(this));

//        for (int i = 0 ; i < 2 ; i++){
//            BookProducts bp = new BookProducts();
//            bp.setBookId(i);
//            bp.setBookProductsId(bookProductsDao.findmax_id() + 1);
//            bp.setBookInfor("规格：二营长拉出了他娘的意大利炮");
//            bp.setBookName("行走平安县");
//            bp.setCoverImage("http://imgsrc.baidu.com/forum/w=580/sign=0212a984b519ebc4c0787691b227cf79/01d33b87e950352abd7fb3e95543fbf2b3118bd1.jpg");
//            bp.setContacterAddress("平安县");
//            bp.setCount(1);
//            bp.setPrice("￥23");
//            bookProductsDao.insert(bp);
//        }
        List<BookProducts> bookProductsesList = bookProductsDao.selectAll();
        adapter = new ShoppingCartAdapter(this , bookProductsesList , false);
        rv_shoppingcart.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_shopping_cart_iv_back:
                this.finish();
                break;

            case R.id.activity_shopping_cart_tv_ok:
                tv_ok.setVisibility(View.GONE);
                tv_edit.setVisibility(View.VISIBLE);
                ll_edit.setVisibility(View.GONE);
                ll_ok.setVisibility(View.VISIBLE);
                adapter.isEditOrOk(true);
                break;

            case R.id.activity_shopping_cart_tv_edit:
                tv_edit.setVisibility(View.GONE);
                tv_ok.setVisibility(View.VISIBLE);
                ll_edit.setVisibility(View.VISIBLE);
                ll_ok.setVisibility(View.GONE);
                adapter.isEditOrOk(false);
                break;
            case R.id.activity_shopping_cart_btn_copy:
                adapter.copy();
                break;

            case R.id.activity_shopping_cart_btn_delete:
                adapter.delete();
                break;

            case R.id.activity_shopping_cart_btn_ok:

                break;
            default:

                break;
        }
    }


}
