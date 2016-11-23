package com.twkj.lovebook.activity;

import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.JsonObject;
import com.twkj.lovebook.R;
import com.twkj.lovebook.application.MyApplication;
import com.twkj.lovebook.constants.BuildConfig;
import com.twkj.lovebook.fragment.BookshelfFragment;
import com.twkj.lovebook.fragment.EditorbookFragment;
import com.twkj.lovebook.fragment.MyFragment;
import com.twkj.lovebook.fragment.RecommendFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class HomeActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private FrameLayout fl;
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    private RadioButton rb_recommend , rb_bookshelf, rb_edit , rb_my;
    private RecommendFragment recommendFragment;//推荐
    private BookshelfFragment bookshelfFragment;//书架
    private EditorbookFragment editorbookFragment; //编辑
    private MyFragment myFragment;//我的

    private Socket socket;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        new Thread(){
            @Override
            public void run() {
                super.run();
                linkSocket();
            }
        }.start();

        initView();

    }

    private void initView(){
        fl = (FrameLayout) findViewById(R.id.activity_home_fl);
        radioGroup = (RadioGroup) findViewById(R.id.activity_home_radiogroup);
        rb_recommend = (RadioButton) findViewById(R.id.activity_home_rb_recommen);
        rb_bookshelf = (RadioButton) findViewById(R.id.activity_home_rb_bookshelf);
        rb_edit = (RadioButton) findViewById(R.id.activity_home_rb_edit);
        rb_my = (RadioButton) findViewById(R.id.activity_home_rb_my);

        radioGroup.setOnCheckedChangeListener(this);

        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.activity_home_rb_recommen:
                setTabSelection(0);
                break;

            case R.id.activity_home_rb_bookshelf:
                setTabSelection(1);
                break;
            case R.id.activity_home_rb_edit:
                Log.i("HA" , "===============activity_home_rb_edit====================");
                setTabSelection(2);
                break;
            case R.id.activity_home_rb_my:
                setTabSelection(3);
                break;


        }
    }

    public void setTabSelection(int index){
        //先清除上次的选中状态
        clearSelection();
        //开启fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //隐藏所有fragment
        hideFragment(transaction);

        switch (index){
            case 0:
                rb_recommend.setTextColor(Color.BLACK);
                rb_recommend.setBackgroundColor(Color.WHITE);
                if(recommendFragment == null){
                    recommendFragment = new RecommendFragment();
                    transaction.add(R.id.activity_home_fl , recommendFragment);
                }else{
                    transaction.show(recommendFragment);
                }
                break;
            case 1:
                rb_bookshelf.setTextColor(Color.BLACK);
                rb_bookshelf.setBackgroundColor(Color.WHITE);
                if(bookshelfFragment == null){
                    bookshelfFragment = new BookshelfFragment();
                    transaction.add(R.id.activity_home_fl , bookshelfFragment);
                }else{
                    bookshelfFragment.initView();
                    transaction.show(bookshelfFragment);
                }
                break;

            case 2:
                //bookshelfframgnet
                rb_edit.setChecked(true);
                rb_edit.setTextColor(Color.BLACK);
                rb_edit.setBackgroundColor(Color.WHITE);
                if(editorbookFragment == null){
                    editorbookFragment = new EditorbookFragment();
                    transaction.add(R.id.activity_home_fl , editorbookFragment);
                }else{
                    transaction.show(editorbookFragment);
                }
                break;

            case 3:
                rb_my.setTextColor(Color.BLACK);
                rb_my.setBackgroundColor(Color.WHITE);
                if(myFragment == null){
                    myFragment = new MyFragment();
                    transaction.add(R.id.activity_home_fl , myFragment);
                }else{
                    transaction.show(myFragment);
                }
                break;
        }
        transaction.commit();
    }
    /**
     * 清除上次的选中状态
     */
    public void clearSelection(){
        rb_recommend.setTextColor(Color.WHITE);
        rb_recommend.setBackgroundColor(Color.BLACK);
        rb_bookshelf.setTextColor(Color.WHITE);
        rb_bookshelf.setBackgroundColor(Color.BLACK);
        rb_edit.setTextColor(Color.WHITE);
        rb_edit.setBackgroundColor(Color.BLACK);
        rb_my.setTextColor(Color.WHITE);
        rb_my.setBackgroundColor(Color.BLACK);
    }

    /**
     * 将所有fragment都设置为隐藏状态
     * @param transaction
     */
    public void hideFragment(FragmentTransaction transaction){
        if (recommendFragment != null){
            transaction.hide(recommendFragment);
        }
        if(bookshelfFragment != null){
            transaction.hide(bookshelfFragment);
        }
        if (editorbookFragment != null){
            transaction.hide(editorbookFragment);
        }
        if (myFragment != null){
            transaction.hide(myFragment);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        将super.onSaveInstanceState(outState);注释掉，让其不再保存Fragment的状态，达到fragment随Activity一起销毁的目的
//        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void linkSocket() {
        try {
            myApplication = (MyApplication) getApplication();
            Socket mysocket=new Socket(BuildConfig.SOCKET_BINDING, BuildConfig.SOCKET_PORT_SHORT);
//            myApplication.setSocket(new Socket(BuildConfig.SOCKET_BINDING, BuildConfig.SOCKET_PORT_SHORT));
            myApplication.setSocket(mysocket);
            socket = myApplication.getSocket();

            JsonObject j1 = new JsonObject();
            j1.addProperty("account", "13335382871");
            j1.addProperty("pwd", "e10adc3949ba59abbe56e057f20f883e");
            j1.addProperty("appid", "2");

            JsonObject j2 = new JsonObject();
            j2.addProperty("method", "login");
            j2.add("parameter", j1);
//
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            myApplication.setOut(out);
////            Log.d("bbb","连上没有："+json.toString());
//
//            // 向外发送
            out.println(j2);
            out.flush();
//            // 通过输入流，获取返回的信息

//            getmsg();
//            BufferedReader recv = new BufferedReader(
//                    new InputStreamReader(socket.getInputStream()));
////            myApplication.setRecv(recv);
//            String recvMsg = null;
//            while ((recvMsg = recv.readLine()) != null) {
//                Log.i("bbb", "返回的信息+home：" + recvMsg);
//            }
//            recv.close();
//            JSONObject j3 = new JSONObject(recvMsg);
//            myApplication.setSession(j3.getJSONObject("result").getString("sessionid"));
//            myApplication.setUserid(j3.getJSONObject("result").getString("userid"));

//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
    private void getmsg(){
        new Thread(){
            @Override
            public void run() {
                super.run();
               JSONObject myjson;
                while (true){
                    try {
//                        socket.setSoTimeout(1000 * 20);
                        BufferedReader recv2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String recvMsg = null;
                        while ((recvMsg = recv2.readLine()) != null) {
                            myjson = new JSONObject(recvMsg);
                            Log.i("bbb", recvMsg + "<这个是get接受的信息>");
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }
}
