package com.twkj.lovebook.bean;

import java.util.List;

/**
 * Created by tiantao on 2016/11/22.
 * 轮播图bean
 */

public class BannerBean {

    private int type;
    private int id;
    private String title;
    private String ga_prefix;
    private String image;
    private List<String> images;

    public BannerBean(int type, int id, String title, String ga_prefix, String image, List<String> images) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.ga_prefix = ga_prefix;
        this.image = image;
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
