package com.twkj.lovebook.dao;

import com.twkj.lovebook.bean.BookProducts;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiantao on 2016/11/21.
 * 购物车的书数据库操作
 */

public class BookProductsDao {

    private DbManager.DaoConfig daoConfig;
    private DbManager db;

    public BookProductsDao() {
        daoConfig = XutilsDaoConfig.getDaoConfig();
        db = x.getDb(daoConfig);
    }

    /**
     * 插入一条数据
     * @param bookProducts
     */
    public void insert(BookProducts bookProducts){
        try {
            db.save(bookProducts);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于设置自增长bookid
     * 找到目前数据库中最大的bookid
     * @return
     */
    public int findmax_id(){

        List<BookProducts> bookProductsList = new ArrayList<BookProducts>();
        try {
            bookProductsList = db.findAll(BookProducts.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (bookProductsList != null && bookProductsList.size() > 0){
            return bookProductsList.get(bookProductsList.size() - 1).bookProductsId;
        }else{
            return 0;
        }
    }


    /**
     * 查询所有购物车
     * @return
     */
    public List<BookProducts> selectAll(){
        List<BookProducts> bookProductsesList = new ArrayList<BookProducts>();
        try {
            bookProductsesList = db.findAll(BookProducts.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return bookProductsesList;
    }

    /**
     * 根据id删除一条数据
     * @param book_products_id 可控制的自增长id
     */
    public void deleteByBookProductsId(int book_products_id){
        WhereBuilder b = WhereBuilder.b();
        b.and("book_products_id" , "=" , book_products_id);
        try {
            db.delete(BookProducts.class , b);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
