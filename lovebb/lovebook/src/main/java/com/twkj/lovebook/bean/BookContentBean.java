package com.twkj.lovebook.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by tiantao on 2016/10/10.
 * 模板书每个page上面的元素
 */

public class BookContentBean {



    public String contentName;


    public String isTextOrImage;


    public String textOrImageX;


    public String textOrImageY;


    public String textOrImageWidth;


    public String textOrImageHeight;

    public String rotationAngle;

    public String textNumber;

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getIsTextOrImage() {
        return isTextOrImage;
    }

    public void setIsTextOrImage(String isTextOrImage) {
        this.isTextOrImage = isTextOrImage;
    }

    public String getTextOrImageX() {
        return textOrImageX;
    }

    public void setTextOrImageX(String textOrImageX) {
        this.textOrImageX = textOrImageX;
    }

    public String getTextOrImageY() {
        return textOrImageY;
    }

    public void setTextOrImageY(String textOrImageY) {
        this.textOrImageY = textOrImageY;
    }

    public String getTextOrImageWidth() {
        return textOrImageWidth;
    }

    public void setTextOrImageWidth(String textOrImageWidth) {
        this.textOrImageWidth = textOrImageWidth;
    }

    public String getTextOrImageHeight() {
        return textOrImageHeight;
    }

    public void setTextOrImageHeight(String textOrImageHeight) {
        this.textOrImageHeight = textOrImageHeight;
    }

    public String getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(String rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public String getTextNumber() {
        return textNumber;
    }

    public void setTextNumber(String textNumber) {
        this.textNumber = textNumber;
    }
}
