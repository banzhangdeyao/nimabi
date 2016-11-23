package com.twkj.lovebook.bean;



import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by tiantao on 2016/10/14.
 * 就是TM的bookcontent
 */

@Table(name = "draft_book_content")
public class DraftBookContent implements Serializable{


    @Column(name = "draft_book_content_id" , isId = true , autoGen = true)
    public int draftBookContentId;//就是TM的draftBookContentId

    @Column(name = "book_id")
    public int bookID;//哪一本书

    @Column(name = "book_page")
    public int bookPage;//那一夜

    //    public String imageChange; 暂时没用
    @Column(name = "image_cos_url")
    public String imageCosUrl;//图片在腾讯云上的链接

//    public float imageHeight;//暂时没用

    @Column(name = "image_id")
    public int imageID;//图片的id排序

    @Column(name = "image_mark_name")
    public String imageMarkName;//图片存在本地的链接

//    public float imageWidth;//图片的宽（目前没用）

    @Column(name = "is_change_size")
    public String isChangeSize;//是否改变了尺寸

    @Column(name = "text_id")
    public int textId;//文字的id

//    public String textMarkName;//目前没有用

    @Column(name = "text_string")
    public String textString;//文字的内容

    @Column(name = "content_name")
    public String contentName;//内容的名字

    @Column(name = "is_text_or_image")
    public String isTextOrImage;//是图片还是文字

    @Column(name = "text_or_image_height")
    public String textOrImageHeight;//图片或文字的高

    @Column(name = "text_or_image_width")
    public String textOrImageWidth;//图片或文字的宽

    @Column(name = "text_or_image_x")
    public String textOrImageX;//图片或文字的X

    @Column(name = "text_or_image_y")
    public String textOrImageY;//图片或文字的Y

    @Column(name = "tag")
    public int tag;//一页上面有tag多个content 用tag记录是哪个content

    @Column(name = "rotation_angle")
    public String rotationAngle;

    @Column(name = "text_number")
    public String textNumber;


    public DraftBookContent(){

    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getDraftBookContentId() {
        return draftBookContentId;
    }

    public void setDraftBookContentId(int draftBookContentId) {
        this.draftBookContentId = draftBookContentId;
    }

    public String getImageCosUrl() {
        return imageCosUrl;
    }

    public void setImageCosUrl(String imageCosUrl) {
        this.imageCosUrl = imageCosUrl;
    }

    public int getBookPage() {
        return bookPage;
    }

    public void setBookPage(int bookPage) {
        this.bookPage = bookPage;
    }

    public String getImageMarkName() {
        return imageMarkName;
    }

    public void setImageMarkName(String imageMarkName) {
        this.imageMarkName = imageMarkName;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getIsChangeSize() {
        return isChangeSize;
    }

    public void setIsChangeSize(String isChangeSize) {
        this.isChangeSize = isChangeSize;
    }

    public int getTextId() {
        return textId;
    }

    public void setTextId(int textId) {
        this.textId = textId;
    }

    public String getTextString() {
        return textString;
    }

    public void setTextString(String textString) {
        this.textString = textString;
    }

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

    public String getTextOrImageHeight() {
        return textOrImageHeight;
    }

    public void setTextOrImageHeight(String textOrImageHeight) {
        this.textOrImageHeight = textOrImageHeight;
    }

    public String getTextOrImageWidth() {
        return textOrImageWidth;
    }

    public void setTextOrImageWidth(String textOrImageWidth) {
        this.textOrImageWidth = textOrImageWidth;
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

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
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
