package com.twkj.lovebook.dao;

import com.twkj.lovebook.bean.DraftBookPage;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiantao on 2016/10/14.
 * 草稿书书页数据库操作
 */

public class DraftBookPageDao {

    private DbManager.DaoConfig daoConfig;
    private DbManager db;

    public DraftBookPageDao(){
        daoConfig = XutilsDaoConfig.getDaoConfig();
        db = x.getDb(daoConfig);
    }

    public void insert(DraftBookPage draftBookPage){
        try {
            db.save(draftBookPage);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入数据集合
     * @param draftBookPageList
     */
    public void insertList(List<DraftBookPage> draftBookPageList){
        try {
            db.save(draftBookPageList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据草稿的draft_book_id 删除所有的bookpage
     * @param draft_book_id
     */
    public void deleteByBookId(int draft_book_id){
        WhereBuilder b = WhereBuilder.b();
        b.and("draft_book_id" , "=" , draft_book_id);
        try {
            db.delete(DraftBookPage.class , b);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据草稿draft_book_id 和 book_page 删除一页
     * @param draft_book_id
     * @param book_page
     */
    public void deleteByBookIdAndBookPage(int draft_book_id , int book_page){
        WhereBuilder b = WhereBuilder.b();
        b.and("draft_book_id" , "=" , draft_book_id);
        b.and("book_page" , "=" ,book_page);
        try {
            db.delete(DraftBookPage.class , b);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据草稿的draft_book_id 查询所有的draftbookpage
     * @param draft_book_id
     * @return
     */
    public List<DraftBookPage> selectorByBookId(int draft_book_id){
        List<DraftBookPage> draftBookPageList = new ArrayList<DraftBookPage>();
        WhereBuilder b = WhereBuilder.b();
        b.and("draft_book_id" , "=" , draft_book_id);
        try {
            draftBookPageList = db.selector(DraftBookPage.class).where(b).orderBy("book_page",false).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return  draftBookPageList;
    }

    /**
     * 根据草稿的draft_book_id 查询所有不可编辑的draftbookpage
     * @param draft_book_id
     * @return
     */
    public List<DraftBookPage> selectorCannotEditPageByBookId(int draft_book_id){
        List<DraftBookPage> draftBookPageList = new ArrayList<DraftBookPage>();
        WhereBuilder b = WhereBuilder.b();
        b.and("draft_book_id" , "=" , draft_book_id);
        b.and("is_cannot_edit" , "=" , false);
        try {
            draftBookPageList = db.selector(DraftBookPage.class).where(b).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return  draftBookPageList;
    }

    /**
     *查询一页page
     * @param draft_book_id
     * @param book_page
     * @return
     */
    public DraftBookPage selectorByBookIdAndPage(int draft_book_id , int book_page){
        DraftBookPage draftBookPage = null;
        WhereBuilder b = WhereBuilder.b();
        b.and("draft_book_id" , "=" , draft_book_id);
        b.and("book_page" , "=" , book_page);
        try {
            draftBookPage = db.selector(DraftBookPage.class).where(b).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return draftBookPage;
    }

    /**
     * 更新
     * @param draftBookPage
     */
    public void updatePage(DraftBookPage draftBookPage){

        try {
            db.update(draftBookPage);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据id 更新
     * @param id
     * @param book_page
     */
    public void updatePageByBookIdAndBookPage(int id , int book_page){
        try {
            db.update(DraftBookPage.class , WhereBuilder.b("id" , "=" , id) , new KeyValue("book_page" , book_page));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将一页清空
     * @param draftBookPage
     */
    public void updataPageClean(DraftBookPage draftBookPage){
        draftBookPage.setLeftBackImage("");
        draftBookPage.setRightBackImage("");
        draftBookPage.setLeftBackImage("");
        try {
            db.update(draftBookPage);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
