package com.twkj.lovebook.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by tiantao on 2016/11/19.
 * 购物车里的书
 */
@Table(name = "book_products" , onCreated = "")
public class BookProducts {

    @Column(name = "id" , isId = true , autoGen = true)
    public int id;

    @Column(name = "book_products_id")
    public int bookProductsId;//自定义可控制自增长id

    @Column(name = "book_id")
    public int bookId;//书的id

    @Column(name = "book_infor")
    public String bookInfor;//书的信息

    @Column(name = "book_name")
    public String bookName;//书的名字

    @Column(name = "contacter_address")
    public String contacterAddress;//联系人地址

    @Column(name = "contacter_name")
    public String contacterName;//联系人姓名

    @Column(name = "contacter_phone")
    public String contacterPhone;//联系人电话

    @Column(name = "count")
    public int count;//数量

    @Column(name = "cover_image")
    public String coverImage;//封面图片

    @Column(name = "is_selected")
    public boolean isSelected;//是否选择

    public boolean isCheck;//是不是选中了

    @Column(name = "major_key")
    public int majorKey;//购物车中书的id

    @Column(name = "price")
    public String price;//价格

    @Column(name = "total_price")
    public String totalPrice;//总价

    @Column(name = "user_id")
    public int userId;//用户id

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBookProductsId() {
        return bookProductsId;
    }

    public void setBookProductsId(int bookProductsId) {
        this.bookProductsId = bookProductsId;
    }

    public String getBookInfor() {
        return bookInfor;
    }

    public void setBookInfor(String bookInfor) {
        this.bookInfor = bookInfor;
    }

    public String getContacterAddress() {
        return contacterAddress;
    }

    public void setContacterAddress(String contacterAddress) {
        this.contacterAddress = contacterAddress;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getContacterPhone() {
        return contacterPhone;
    }

    public void setContacterPhone(String contacterPhone) {
        this.contacterPhone = contacterPhone;
    }

    public String getContacterName() {
        return contacterName;
    }

    public void setContacterName(String contacterName) {
        this.contacterName = contacterName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getMajorKey() {
        return majorKey;
    }

    public void setMajorKey(int majorKey) {
        this.majorKey = majorKey;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
