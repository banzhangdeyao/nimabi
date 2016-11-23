package com.twkj.lovebook.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twkj.lovebook.R;
import com.twkj.lovebook.bean.AllBookBean;

import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Created by tiantao on 2016/10/11.
 * bookinfo工具类
 */

public class BookInfoUtils {
    //将raw文件中的bookinfo(plist)文件转化为对象返回
    public static AllBookBean getAllBookBean(Context context){
        InputStream inputStream = context.getResources().openRawResource(R.raw.bookmodel);
        String strBookInfo = InputStreamUtils.getStringFromInputStream(inputStream);
        Gson gson = new Gson();
        Type type = new TypeToken<AllBookBean>(){}.getType();
        AllBookBean abb = gson.fromJson(strBookInfo , type);
        return abb;
    }
}
