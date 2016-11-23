package com.twkj.lovebook.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.BreakIterator;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.twkj.lovebook.R;
import com.twkj.lovebook.activity.CommonaddressActivity;
import com.twkj.lovebook.activity.IvoiceActivity;
import com.twkj.lovebook.activity.LoginActivity;
import com.twkj.lovebook.activity.RegisterActivity;


public class MyFragment extends Fragment implements View.OnClickListener {
    SharedPreferences share;
    boolean islogin;
    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        share= getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);
        islogin =share.getBoolean("islogin", false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, null);

        //头像
        ImageView head = (ImageView) view.findViewById(R.id.my_head);
        if (islogin){
            head.setImageResource(R.drawable.minenoneheader3x);
        }else{
            head.setImageResource(R.drawable.btnlanding3x);
        }
        //我的订单
        LinearLayout my_order = (LinearLayout) view.findViewById(R.id.my_order);
        //常用地址
        LinearLayout my_commonaddress = (LinearLayout) view.findViewById(R.id.my_commonaddress);
        //发票管理
        LinearLayout my_controloverinvoices = (LinearLayout) view.findViewById(R.id.my_controloverinvoices);
        //联系我们
        LinearLayout my_contactus = (LinearLayout) view.findViewById(R.id.my_contactus);
        //账户安全
        LinearLayout my_commonproblem = (LinearLayout) view.findViewById(R.id.my_commonproblem);
        //常见问题
        LinearLayout my_account = (LinearLayout) view.findViewById(R.id.my_account);
        head.setOnClickListener(this);
        my_order.setOnClickListener(this);
        my_commonaddress.setOnClickListener(this);
        my_controloverinvoices.setOnClickListener(this);
        my_contactus.setOnClickListener(this);
        my_commonproblem.setOnClickListener(this);
        my_account.setOnClickListener(this);


//        return inflater.inflate(R.layout.fragment_my, container, false);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        islogin =share.getBoolean("islogin", false);
        switch (v.getId()) {
            case R.id.my_head:
                if (islogin) {

                } else {
                   intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.my_order:
                break;
            case R.id.my_commonaddress:
                intent=new Intent(getActivity(), CommonaddressActivity.class);
                startActivity(intent);
                break;
            case R.id.my_controloverinvoices:
                intent = new Intent(getActivity(), IvoiceActivity.class);
                startActivity(intent);
                break;
            case R.id.my_contactus:
                break;
            case R.id.my_commonproblem:
                break;
            case R.id.my_account:
                break;
        }
    }
}
