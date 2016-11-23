package com.twkj.lovebook.application;

import android.app.Application;
import android.util.Log;

import org.xutils.x;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by tiantao on 2016/10/11.
 */

public class MyApplication extends Application {
    private Socket socket;
    private String session;
    private String userid;
    private BufferedReader recv;
    private PrintWriter out ;

    public BufferedReader getRecv() {
        return recv;
    }

    public void setRecv(BufferedReader recv) {
        this.recv = recv;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private void init() {
        socket = null;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSession(String str) {
        this.session = str;
    }

    public String getSession() {
        return session;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);//Xutils 初始化
        //x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        init();

    }


}
