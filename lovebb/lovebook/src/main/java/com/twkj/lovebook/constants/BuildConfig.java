package com.twkj.lovebook.constants;

import android.os.Environment;

import java.io.File;

/**
 * Created by tiantao on 2016/10/17.
 * 常量类
 * 包含 文件夹路径
 */

public class BuildConfig {

    //-----------------------------------文件夹路径star-----------------------------------------
    /**
     * app SD卡总路径
     */
    public static final String DATA_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "lovebook";

    /**
     * 存储bitmap文件路径
     */
    public static final String DATA_FILE_PATH_BITMAP = DATA_FILE_PATH + File.separator + "bitmap";

    //-----------------------------------文件夹路径end-----------------------------------------


    //-----------------------------------腾讯云需要的参数star-----------------------------------------
    /**
     * 腾讯云appid
     */
    public static final String TENCENT_UPLOAD_APPID = "10062537";

    /**
     * 腾讯云bucket
     */
    public static final String TENCENT_UPLOAD_BUCKET = "lovebook";

    /**
     * 腾讯云persistenceId
     */
    public static final String TENCENT_UPLOAD_PERSISTENCEID = "filePersistenceId";

    //-----------------------------------腾讯云需要的参数end------------------------------------------
    //socket链接地址
    public static final String SOCKET_BINDING="bookbank.cc";

    //socket短连接端口
    public static final int SOCKET_PORT_SHORT=9016;

    //socket长连接端口
    public static final int SOCKET_PORT_LONG=9018;

}
