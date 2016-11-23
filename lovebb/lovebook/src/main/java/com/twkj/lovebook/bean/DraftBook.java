package com.twkj.lovebook.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tiantao on 2016/10/14.
 * 书的草稿表
 */
//onCreated = "sql"：当第一次创建表需要插入数据时候在此写sql语句 我就不写
    @Table(name = "draft_book" , onCreated = "")
    public class DraftBook implements Serializable{
    /**
     * name = "book_id"：数据库表中的一个字段
     * isId = true：是否是主键
     * autoGen = true：是否自动增长
     * //property = "NOT NULL"：添加约束
     */

    @Column(name = "id" , isId = true , autoGen = true)
    public int id;

    @Column(name = "book_id")
    public int bookID;//草稿的id

    @Column(name = "book_name")
    public String bookName;//书的名字

    @Column(name = "book_pages")
    public int bookPages;//总的页数

    @Column(name = "cover_image_name")
    public String coverImageName;//草稿中书的封面

    @Column(name = "create_timestamp")
    public Double createTimestamp;//创建草稿的时间

    @Column(name = "edit_type")
    public int editType;//编辑类型（0是手动1是自动）

    @Column(name = "title_page")
    public String titlePage;//扉页

    @Column(name = "user_id")
    public int userID;//用户id

    @Column(name = "end_page")
    public String endPage;//封底（目前封面合一）

    @Column(name = "draft_book_status")
    public String draftBookStatus;//书的状态 草稿中的书包含两种状态，上传中未上传，uploading和unUploading

    @Column(name = "bookId_server")
    public String bookId_Server;//服务器端返回的bookid

    @Column(name = "current_num")
    public int currentNum;//自动灌版记录灌倒那一页

    public List<DraftBookPage> listDraftBookPage;//书的所有页

    //默认的构造方法必须写出，如果没有，这张表是创建不成功的 放你马屁我都不信
    public DraftBook(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getBookPages() {
        return bookPages;
    }

    public void setBookPages(int bookPages) {
        this.bookPages = bookPages;
    }

    public String getCoverImageName() {
        return coverImageName;
    }

    public void setCoverImageName(String coverImageName) {
        this.coverImageName = coverImageName;
    }

    public Double getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Double createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public int getEditType() {
        return editType;
    }

    public void setEditType(int editType) {
        this.editType = editType;
    }

    public String getTitlePage() {
        return titlePage;
    }

    public void setTitlePage(String titlePage) {
        this.titlePage = titlePage;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEndPage() {
        return endPage;
    }

    public void setEndPage(String endPage) {
        this.endPage = endPage;
    }

    public String getBookId_Server() {
        return bookId_Server;
    }

    public void setBookId_Server(String bookId_Server) {
        this.bookId_Server = bookId_Server;
    }

    public String getDraftBookStatus() {
        return draftBookStatus;
    }

    public void setDraftBookStatus(String draftBookStatus) {
        this.draftBookStatus = draftBookStatus;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }
}
