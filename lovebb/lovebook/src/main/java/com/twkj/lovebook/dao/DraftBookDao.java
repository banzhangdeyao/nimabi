package com.twkj.lovebook.dao;

import android.database.Cursor;
import android.util.Log;

import com.twkj.lovebook.bean.DraftBook;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiantao on 2016/10/14.
 * 草稿书数据库操作
 */

public class DraftBookDao {

    private DbManager.DaoConfig daoConfig;
    private DbManager db;

    public DraftBookDao(){
        daoConfig = XutilsDaoConfig.getDaoConfig();
        db = x.getDb(daoConfig);

    }

    /**
     * 插入一条数据
     * @param draftBook
     */
    public void insert(DraftBook draftBook){
        try {

            db.save(draftBook);
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

        List<DraftBook> draftBookList = new ArrayList<DraftBook>();
        try {
            draftBookList = db.findAll(DraftBook.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (draftBookList != null && draftBookList.size() > 0){
            return draftBookList.get(draftBookList.size() - 1).bookID;
        }else{
            return 0;
        }
	}

    /**
     * 按照id查询一条草稿书数据
     * @param id
     * @return
     */
    public DraftBook selectById(int id){
        DraftBook draftBook = null;
        WhereBuilder b = WhereBuilder.b();
        b.and("book_id" , "=" , id);//构造修改条件
        try {

            draftBook = db.selector(DraftBook.class).where(b).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return draftBook;
    }

    /**
     * 查询所有草稿书
     * @return
     */
    public List<DraftBook> selectAll(){
        List<DraftBook> draftBookList = new ArrayList<DraftBook>();
        try {
            draftBookList = db.findAll(DraftBook.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return draftBookList;
    }

    public void updata(DraftBook draftBook){
        try {
            db.update(draftBook);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id删除一条草稿书数据
     * @param id
     */
    public void deleteById(int id){
        WhereBuilder b = WhereBuilder.b();
        b.and("book_id" , "=" , id);
        try {
            int t = db.delete(DraftBook.class , b);
            Log.i("DraftBookDao" , "========================t========================="+t);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除表 应该用不到
     */
    public void delTable(){
        try {
            db.dropTable(DraftBook.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
