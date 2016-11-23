package com.twkj.lovebook.dao;

import android.os.Environment;

import org.xutils.DbManager;

import java.io.File;

/**
 * Created by tiantao on 2016/10/11.
 */

public class XutilsDaoConfig {

    public static DbManager.DaoConfig daoConfig;

    public static DbManager.DaoConfig getDaoConfig(){
//        File file = new File(Environment.getExternalStorageDirectory().getPath());
        if (daoConfig == null){
            daoConfig = new DbManager.DaoConfig()
                    .setDbName("book.db")
//                    .setDbDir(file)
                    .setDbVersion(1)
                    .setAllowTransaction(true)
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        }
                        //设置数据库打开的监听
                    }).setDbOpenListener(new DbManager.DbOpenListener(){
                        @Override
                        public void onDbOpened(DbManager db) {
                            //开启数据库支持多线程操作，提升性能
                            db.getDatabase().enableWriteAheadLogging();
                        }
                    });
        }
        return daoConfig;
    }
}
