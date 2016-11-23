package com.twkj.lovebook.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tiantao on 2016/10/14.
 * 每页书的信息表
 * 书的每页信息表
 */

@Table(name = "draft_book_page")
public class DraftBookPage implements Serializable{

    @Column(name = "id" , isId = true ,autoGen = true)
    public  int id;

    @Column(name = "book_page")
    public int  bookPage;//页码

    @Column(name = "draft_book_id")
    public int draftBookID;//草稿ID 哪一本书

    @Column(name = "left_back_image")
    public String leftBackImage;//左侧的背景的名字

    @Column(name = "little_book_image")
    public String littleBookImage;//小的预览的缩略图

    @Column(name = "little_image_cos_url")
    public String littleImageCosUrl;//缩略图在腾讯云上的链接

    @Column(name = "personality_id")
    public int personality_id;//模板的id（目前用这个）

    @Column(name = "right_back_image")
    public String rightBackImage;//右侧的背景图片

    @Column(name = "user_id")
    public String userid;//用户的id

    @Column(name = "which_model")
    public String whichModel;//哪个模板

    @Column(name = "which_book")
    public String whichBook;//哪本书

    @Column(name = "transfor_status")
    public boolean transforStatus;//上传状态 是否上传到服务器端了

    @Column(name = "model_background_pic")
    public String modelBackgroundPic;//整个背景

    @Column(name = "is_cannot_edit")
    public boolean isCannotEdit;//是否能被编辑 可编辑true 不可编辑false


    public List<DraftBookContent> listDraftBookContent;//页中的所有内容


    public DraftBookPage(){

    }

    public int getBookPage() {
        return bookPage;
    }

    public void setBookPage(int bookPage) {
        this.bookPage = bookPage;
    }

    public int getDraftBookID() {
        return draftBookID;
    }

    public void setDraftBookID(int draftBookID) {
        this.draftBookID = draftBookID;
    }

    public String getLeftBackImage() {
        return leftBackImage;
    }

    public void setLeftBackImage(String leftBackImage) {
        this.leftBackImage = leftBackImage;
    }

    public String getLittleBookImage() {
        return littleBookImage;
    }

    public void setLittleBookImage(String littleBookImage) {
        this.littleBookImage = littleBookImage;
    }

    public String getLittleImageCosUrl() {
        return littleImageCosUrl;
    }

    public void setLittleImageCosUrl(String littleImageCosUrl) {
        this.littleImageCosUrl = littleImageCosUrl;
    }

    public int getPersonality_id() {
        return personality_id;
    }

    public void setPersonality_id(int personality_id) {
        this.personality_id = personality_id;
    }

    public String getRightBackImage() {
        return rightBackImage;
    }

    public void setRightBackImage(String rightBackImage) {
        this.rightBackImage = rightBackImage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getWhichBook() {
        return whichBook;
    }

    public void setWhichBook(String whichBook) {
        this.whichBook = whichBook;
    }

    public String getWhichModel() {
        return whichModel;
    }

    public void setWhichModel(String whichModel) {
        this.whichModel = whichModel;
    }

    public boolean isTransforStatus() {
        return transforStatus;
    }

    public void setTransforStatus(boolean transforStatus) {
        this.transforStatus = transforStatus;
    }

    public String getModelBackgroundPic() {
        return modelBackgroundPic;
    }

    public void setModelBackgroundPic(String modelBackgroundPic) {
        this.modelBackgroundPic = modelBackgroundPic;
    }

    public boolean isCannotEdit() {
        return isCannotEdit;
    }

    public void setCannotEdit(boolean cannotEdit) {
        isCannotEdit = cannotEdit;
    }
}
