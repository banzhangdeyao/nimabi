package com.twkj.lovebook.bean;

import java.io.Serializable;

/**
 * Created by wht on 2016/10/11.
 */

    public class EditPageBean implements Serializable {
        public String contentName;
        public String isTextOrImage;
        public int textOrImageX;
        public int textOrImageY;
        public int textOrImageWidth;
        public int textOrImageHeight;

        public EditPageBean() {
        }

    public EditPageBean(String contentName, String isTextOrImage, int textOrImageX, int textOrImageY, int textOrImageWidth, int textOrImageHeight) {
        this.contentName = contentName;
        this.isTextOrImage = isTextOrImage;
        this.textOrImageX = textOrImageX;
        this.textOrImageY = textOrImageY;
        this.textOrImageWidth = textOrImageWidth;
        this.textOrImageHeight = textOrImageHeight;
    }

    public String getContentName() {
        return contentName;
    }

    public String getIsTextOrImage() {
        return isTextOrImage;
    }

    public int getTextOrImageX() {
        return textOrImageX;
    }

    public int getTextOrImageY() {
        return textOrImageY;
    }

    public int getTextOrImageWidth() {
        return textOrImageWidth;
    }

    public int getTextOrImageHeight() {
        return textOrImageHeight;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public void setIsTextOrImage(String isTextOrImage) {
        this.isTextOrImage = isTextOrImage;
    }

    public void setTextOrImageX(int textOrImageX) {
        this.textOrImageX = textOrImageX;
    }

    public void setTextOrImageY(int textOrImageY) {
        this.textOrImageY = textOrImageY;
    }

    public void setTextOrImageWidth(int textOrImageWidth) {
        this.textOrImageWidth = textOrImageWidth;
    }

    public void setTextOrImageHeight(int textOrImageHeight) {
        this.textOrImageHeight = textOrImageHeight;
    }
}
