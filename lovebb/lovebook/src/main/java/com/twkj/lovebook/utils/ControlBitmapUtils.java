package com.twkj.lovebook.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

/**
 * Created by tiantao on 2016/11/2.
 */

public class ControlBitmapUtils {
    /**
     * 保存照相图片到系统默认相册
     *
     * @param name
     *            :图片名称
     * @param bitmap
     *            :图片对象
     * @return
     */
    public static String saveCommentImage(String name, Bitmap bitmap) {
        String path = "";
        try {

            String status = Environment.getExternalStorageState();
            if (status.equals(Environment.MEDIA_MOUNTED)) {
                // 将图片保存到系统默认相册
//				File picture = new File(Environment.getExternalStorageDirectory()
//						+ "/temp.jpg");
                String filePath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/DCIM/Camera/" + name;
                path = filePath;
                Log.d("URL", "**" + path + "**");
                File myFile = new File(filePath);
                myFile.createNewFile();
                FileOutputStream fOut = null;
                fOut = new FileOutputStream(myFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
                Log.i("URL", "path>>>" + path + "***");
            }

            return path;

        } catch (Exception e) {
            Log.e("save Bitmap failed", e.getMessage());
        }
        return path;
    }

    public static int getResourceId(Context context, String name, String type, String packageName){

        Resources themeResources=null;
        PackageManager pm=context.getPackageManager();
        try {
            themeResources=pm.getResourcesForApplication(packageName);
            return themeResources.getIdentifier(name, type, packageName);
        } catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中

        Log.d("image size = ", "~~~~~~~"+baos.toByteArray().length / 1024);

        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片

        return bitmap;
    }





    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * <a href="\"http://www.eoeandroid.com/home.php?mod=space&uid=309376\"" target="\"_blank\"">@return</a> degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static boolean  saveBitmap2file(Bitmap bmp,String path){
        Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

    /**
     * BitmapFactory.decodeStream()方式编译bitmap

     *  @param context

     *  @param resId

     *  @return

     */

    public static Bitmap readBitMap(Context  context, int resId){

        BitmapFactory.Options opt = new  BitmapFactory.Options();

        opt.inPreferredConfig =  Bitmap.Config.RGB_565;

        opt.inPurgeable = true;

        opt.inInputShareable = true;

        //  获取资源图片

        InputStream is =  context.getResources().openRawResource(resId);

        return  BitmapFactory.decodeStream(is, null, opt);

    }

    /**
     * 通过资源文件名 获取资源文件id
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName , Class<?> c){
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
