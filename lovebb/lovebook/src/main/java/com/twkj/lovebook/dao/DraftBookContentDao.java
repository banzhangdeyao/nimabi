package com.twkj.lovebook.dao;

import com.twkj.lovebook.bean.DraftBookContent;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiantao on 2016/10/14.
 * 一页书上的具体内容数据库操作
 */

public class DraftBookContentDao {

    private DbManager.DaoConfig daoConfig;
    private DbManager db;

    public DraftBookContentDao(){
        daoConfig = XutilsDaoConfig.getDaoConfig();
        db = x.getDb(daoConfig);
    }

    /**
     *
     * @param draftBookContent
     */
    public void insert(DraftBookContent draftBookContent){
        try {
            db.save(draftBookContent);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入数据集合
     * @param draftBookContentList
     */
    public void insertList(List<DraftBookContent> draftBookContentList){
        try {
            db.save(draftBookContentList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据book_id 和 book_page 删除一页的content
     * @param book_id
     * @param book_page
     */
    public void deleteByBookIdAndBookPage(int book_id , int book_page){
        WhereBuilder b = WhereBuilder.b();
        b.and("book_id" , "=" , book_id);
        b.and("book_page" , "=" , book_page);
        try {
            db.delete(DraftBookContent.class , b);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按照bookpage id 和Draftbook id查询所有content数据
     *
     */
    public List<DraftBookContent> selectorByBookIdAndBookPage(int book_id , int book_page){
        List<DraftBookContent> listDraftBookContent = new ArrayList<DraftBookContent>();
        WhereBuilder b = WhereBuilder.b();
        b.and("book_id" , "=" , book_id);//构造修改条件
        b.and("book_page" , "=" , book_page);
        try {
            listDraftBookContent = db.selector(DraftBookContent.class).where(b).findAll();

        } catch (DbException e) {
            e.printStackTrace();
        }
        return  listDraftBookContent;
    }

    /**
     * 按照Draftbook id查询所有content中image数据
     * @param book_id
     * @return
     */
    public List<DraftBookContent> selectorImgByBookIdAndBookPage(int book_id , int book_page){
        List<DraftBookContent> listDraftBookContent = new ArrayList<DraftBookContent>();
        WhereBuilder b = WhereBuilder.b();
        b.and("book_id" , "=" , book_id);//构造修改条件
        b.and("book_page" , "=" , book_page);
        b.and("is_text_or_image" , "=" , "image");
        b.and("image_mark_name" , "!=" , "");
        try {
            listDraftBookContent = db.selector(DraftBookContent.class).where(b).findAll();

        } catch (DbException e) {
            e.printStackTrace();
        }
        return  listDraftBookContent;
    }

    public DraftBookContent selectorByBookIdAndBookPageAndTag(int book_id , int book_page , int tag){
        DraftBookContent draftBookContent = new DraftBookContent();
        WhereBuilder b = WhereBuilder.b();
        b.and("book_id" , "=" , book_id);
        b.and("book_page" , "=" , book_page);
        b.and("tag" , "=" , tag);
        try {
            draftBookContent = db.selector(DraftBookContent.class).where(b).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return draftBookContent;
    }

    /**
     * 根据bookid找出封面封底的方法
     * @param book_id
     * @return
     */
    public DraftBookContent selectEndPageByBookId(int book_id){
        DraftBookContent draftBookContent = new DraftBookContent();
        WhereBuilder b = WhereBuilder.b();
        b.and("book_id" , "=" , book_id);
        b.and("is_text_or_image" , "=" , "image");
        b.and("image_mark_name" , "!=" , "");
        try {
            draftBookContent = db.selector(DraftBookContent.class).where(b).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return draftBookContent;
    }

    /**
     * 更改数据
     * @param draftBookContent
     */
    public void update(DraftBookContent draftBookContent){
        try {
            db.update(draftBookContent);

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据book_id book_page 清空一页的content
     * @param book_id
     * @param book_page
     */
    public void updatePageCleanAllContentByBookIdAndBookPage(int book_id , int book_page){
        List<DraftBookContent> contents = selectorByBookIdAndBookPage(book_id , book_page);
        for (DraftBookContent content : contents){
            content.setImageMarkName("");
            content.setIsChangeSize("");
            content.setTextString("");
            try {
                db.update(content);
            } catch (DbException e) {
                e.printStackTrace();
            }

        }

    }

}
