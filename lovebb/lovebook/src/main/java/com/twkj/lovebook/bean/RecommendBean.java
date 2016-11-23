package com.twkj.lovebook.bean;

import android.widget.ImageView;

/**
 * Created by tiantao on 2016/11/21.
 * 推荐fragment 数据bean
 */

public class RecommendBean {

    public String lable;

    public String imageResource;

    public RecommendBean(String lable, String imageResource) {
        this.lable = lable;
        this.imageResource = imageResource;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
}
