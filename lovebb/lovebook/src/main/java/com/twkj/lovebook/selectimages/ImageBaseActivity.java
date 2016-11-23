package com.twkj.lovebook.selectimages;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * 图片选择Activity的基类。<br/>
 * <br/>
 * Created by yanglw on 2014/8/17.
 */
public class ImageBaseActivity extends AppCompatActivity {

    protected static ArrayList<Photo> checkList = new ArrayList<Photo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
    }
}
