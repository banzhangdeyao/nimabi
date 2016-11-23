package com.twkj.lovebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText user_phone,user_password;
    private ImageView go_password,go_register;
    private Button login;
    SharedPreferences share;
    Socket socket;
    MyApplication app;
    Thread thread;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case GET_YANZHENMA:
//                    Toast.makeText(RegisterActivity.this, "验证码已经发送请稍候", Toast.LENGTH_SHORT).show();
//                    break;
//                case GET_REGISTER:
//                    Toast.makeText(RegisterActivity.this, "注册完成", Toast.LENGTH_SHORT).show();
//                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app= (MyApplication) getApplication();
//        SharedPreferences.Editor editor=share.edit();
        initView();

    }

    private void initView(){
        user_phone= (EditText) findViewById(R.id.user_phone);
        user_password= (EditText) findViewById(R.id.user_password);
        go_password= (ImageView) findViewById(R.id.go_password);
        go_register= (ImageView) findViewById(R.id.go_register);
        login= (Button) findViewById(R.id.login);
        user_phone.setOnClickListener(this);
        user_password.setOnClickListener(this);
        go_password.setOnClickListener(this);
        go_register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
                //
            case R.id.login:
                Log.i("bbb","登录的账号"+user_phone.getText().toString());
                Log.i("bbb",(user_phone.getText().toString() != null)+"");
                Log.i("bbb","登录的密码"+user_password.getText().toString());
                Log.i("bbb",(!user_password.getText().toString() .equals("") )+"");
                if (user_phone.getText().toString() != null && !user_phone.getText().toString().equals("") ) {
                    if (user_password.getText().toString() != null && !user_password.getText().toString().equals("")){
                      thread= new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                login();
                            }
                        };
                        thread.start();
                    }else{
                        Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "电话号码格式不正确", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.go_password:

                break;
            case R.id.go_register:
                intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;

        }
    }

    public void login(){
        socket=app.getSocket();
        Log.i("bbb", "socket的信息：" + socket.isConnected());
        String phone=user_phone.getText().toString();
        String pwd=user_password.getText().toString();
        JsonObject j1 = new JsonObject();
        j1.addProperty("account", phone);
        j1.addProperty("appid", "2");
        j1.addProperty("pwd", pwd);

        JsonObject j2 = new JsonObject();
        j2.addProperty("method", "login");
        j2.add("parameter", j1);
        try {
//            PrintWriter out = new PrintWriter(new BufferedWriter(
//                    new OutputStreamWriter(socket.getOutputStream())),
//                    true);
            PrintWriter out=((MyApplication)getApplication()).getOut();
            out.print(j2);
            out.flush();
            Log.i("bbb", "发送的信息：" + j2);
//            BufferedReader recv = ((MyApplication)getApplication()).getRecv();
            BufferedReader recv = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
//            Log.i("bbb", "recv是不是空：" + recv==null);
            String recvMsg =null;
            while ((recvMsg = recv.readLine()) != null) {
                Log.i("bbb", "返回的信息：" + recvMsg);
            }
            recv.close();

//            JSONObject jsonObject = new JSONObject(recvMsg);
//            if (jsonObject.getJSONObject("result").getString("sessionid")!=null) {
//                if (jsonObject.getInt("status") == 0) {
//                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}
