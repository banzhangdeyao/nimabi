package com.twkj.lovebook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.twkj.lovebook.R;
import com.twkj.lovebook.application.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by wht on 2016/11/14.
 */

public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText phone, yanzheng, pwd;
    private Button getyanzheng;
    private Button register;
    private ImageView back;
    Socket socket;
    public final static int GET_YANZHENMA = 0;
    public final static int GET_REGISTER = 1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_YANZHENMA:
                    Toast.makeText(RegisterActivity.this, "验证码已经发送请稍候", Toast.LENGTH_SHORT).show();
                    break;
                case GET_REGISTER:
                    Toast.makeText(RegisterActivity.this, "注册完成", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        socket = ((MyApplication) getApplication()).getSocket();
//        setContentView(R.layout.activiy_zhucedemotest);

        initView();

    }

    private void initView() {
        phone = (EditText) findViewById(R.id.phone_edit);
        yanzheng = (EditText) findViewById(R.id.yanzheng_edit);
        pwd = (EditText) findViewById(R.id.pwd_edit);
        getyanzheng = (Button) findViewById(R.id.yanzheng_btn);
        register = (Button) findViewById(R.id.register_register);
        back = (ImageView) findViewById(R.id.register_back);
        register.setOnClickListener(this);
        getyanzheng.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_back:
                break;
            case R.id.register_register:
                if (phone.getText().toString() != null && !phone.getText().toString().equals("")) {
                    if (yanzheng.getText().toString() != null && !yanzheng.getText().toString().equals("")) {
                        if (pwd.getText().toString() != null && !pwd.getText().toString().equals("")) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    register();
                                }
                            }.start();
                        } else {
                            Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "电话号码格式不正确", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.yanzheng_btn:
                if (phone.getText().toString() != null && !phone.getText().toString().equals("")) {
                    //如果电话号不等于空
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            getyanzhengma();
                        }
                    }.start();
                } else {
                    Toast.makeText(RegisterActivity.this, "电话号码格式不正确", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    public void getyanzhengma() {
        String account = phone.getText().toString();
        String method = "getValidCode";
        String msgtype = "0";

        JsonObject j1 = new JsonObject();
        j1.addProperty("account", account);
        j1.addProperty("appid", "2");
        j1.addProperty("msgtype", msgtype);

        JsonObject j2 = new JsonObject();
        j2.addProperty("method", method);
        j2.add("parameter", j1);
        try {
            PrintWriter out = ((MyApplication) getApplication()).getOut();
            if (out == null) {
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
            }
            out.print(j2);
            out.flush();
            BufferedReader recv = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

//            BufferedReader recv = ((MyApplication)getApplication()).getRecv();
//            if (recv==null){
//                recv=  new BufferedReader(
//                        new InputStreamReader(socket.getInputStream()));
//            }
            String recvMsg = null;
            while ((recvMsg = recv.readLine()) != null) {
                JSONObject jsonObject = new JSONObject(recvMsg);
                Log.i("bbb", "返回的信息：" + recvMsg);
                Log.i("bbb", "返回的信息11111：" + jsonObject.getString("result").equals("success") + (jsonObject.getInt("status") == 0));
                if (jsonObject.getString("result").equals("success")) {
                    if (jsonObject.getInt("status") == 0) {
                        Log.i("bbb", "走到弹提示的步骤了");
                        handler.sendEmptyMessage(GET_YANZHENMA);
                    }
                }
            }
            recv.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void register() {
        String account = phone.getText().toString();
        String vcode = yanzheng.getText().toString();
        String password = pwd.getText().toString();

        JsonObject j1 = new JsonObject();
        j1.addProperty("account", account);
        j1.addProperty("appid", "2");
        j1.addProperty("msgtype", "2");
        j1.addProperty("vcode", vcode);
        j1.addProperty("pwd", password);

        JsonObject j2 = new JsonObject();
        j2.addProperty("method", "validAndCreate");
        j2.add("parameter", j1);
        try {
            PrintWriter out = ((MyApplication) getApplication()).getOut();
            if (out == null) {
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
            }
            out.print(j2);
            out.flush();
            BufferedReader recv = recv = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
//            BufferedReader recv = ((MyApplication)getApplication()).getRecv();
//            if (recv==null){
//                recv=  new BufferedReader(
//                        new InputStreamReader(socket.getInputStream()));
//            }
            String recvMsg = null;
            while ((recvMsg = recv.readLine()) != null) {
                Log.i("bbb", "返回的信息：" + recvMsg);
                JSONObject jsonObject = new JSONObject(recvMsg);
                if (jsonObject.getString("result").equals("success")) {
                    if (jsonObject.getInt("status") == 0) {
                        handler.sendEmptyMessage(GET_REGISTER);
                    }
                }
            }
            recv.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
