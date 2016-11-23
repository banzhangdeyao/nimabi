package com.twkj.lovebook.utils;

import com.twkj.lovebook.bean.BookContentBean;

/**
 * Created by tiantao on 2016/10/10.
 * 像素转化工具类
 */

public class BookcontentBeanUtils {

//    public static BookContentBean changeToPX(BookContentBean bcb){
//        bcb.textOrImageWidth = (Float.valueOf(bcb.getTextOrImageWidth()) * 139) +"";
//        bcb.textOrImageHeight = (Float.valueOf(bcb.getTextOrImageHeight()) * 139) + "";
//
//        return bcb;
//    }

    public static float changeToPx(String str){
        return Float.valueOf(str) * 139;
    }

    public static int changeToPxOfInt(String str){
        return (int)(Float.valueOf(str) * 139);

    }
}
