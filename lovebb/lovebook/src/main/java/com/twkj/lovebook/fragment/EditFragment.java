package com.twkj.lovebook.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;
import com.twkj.lovebook.activity.CheckActivity;
import com.twkj.lovebook.activity.ReviseImageActivity;
import com.twkj.lovebook.activity.WriteABookActivity;
import com.twkj.lovebook.activity.WriteABookQuicklyActivity;
import com.twkj.lovebook.bean.DraftBookContent;
import com.twkj.lovebook.bean.DraftBookPage;
import com.twkj.lovebook.dao.DraftBookContentDao;
import com.twkj.lovebook.dao.DraftBookPageDao;
import com.twkj.lovebook.selectimages.Photo;
import com.twkj.lovebook.view.EditPageView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by wht on 2016/10/10.
 */

public class EditFragment extends Fragment {
    ViewGroup viewGroup;
    private EditPageView editPageView;
    //    List<DraftBookContent> beanList;
    private DraftBookPage beanList;
    private ImageView viewmodel, viewleft, viewright;//前三个模板背景 左背景 ， 右北京你特么咋不弄一百层背景
    View view1, view2, view3, view4, view5, view6, view7, view8;
    View[] childview = new View[]{view1, view2, view3, view4, view5, view6, view7, view8};
    public static final int TAKE_PHOTO = 2345;
    public static final int TAKE_REVISE = 3000;
    Dialog alertDialog;
    private DraftBookContentDao draftBookContentDao = new DraftBookContentDao();
    private DraftBookPageDao draftBookPageDao = new DraftBookPageDao();
    final String[] arrayFruit = new String[]{"相册", "照相", "编辑"};


    //修改左背景还是右背景的flag
    private String leftOrright = "1";

    //判断是否需要生成缩略图
    private boolean littelBookImageNeedChange;


    public EditFragment() {
    }

    public EditPageView getEditPageView() {
        return editPageView;
    }

    public void setViewGroup(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_edti, null);

        return viewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editPageView = (EditPageView) viewGroup.findViewById(R.id.editPageView);
        TextView goCheck= (TextView) viewGroup.findViewById(R.id.go_check);
        goCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                //传递区分那本书的本地数据库bookid
                Log.i("fragment==========",getActivity().getIntent().getStringExtra("AutoOrNot")+"是手动还是自动编辑");
//                intent.putExtra("aimBookid",getActivity().getIntent().getIntExtra("aimBookid",0));
                if (getActivity().getIntent().getStringExtra("AutoOrNot").equals("auto")){
                    intent=new Intent(getActivity(), CheckActivity.class);
                    intent.putExtra("aimBookid",((WriteABookQuicklyActivity)getActivity()).getAimBookid());
                }else{
                    intent=new Intent(getActivity(), CheckActivity.class);
                    intent.putExtra("aimBookid",((WriteABookActivity)getActivity()).getAimBookid());
                }

                startActivity(intent);
                getActivity().finish();
            }
        });
        littelBookImageNeedChange = false;
        if (getArguments() != null) {
            beanList = (DraftBookPage) getArguments().get("data");
        }
        if (beanList != null ) {


//            Log.d("ttt", "beanlist .size:" + beanList.listDraftBookContent.size());

            addChildView();
        }
    }


    private void addChildView() {


        viewmodel = new ImageView(getContext());
        if (!TextUtils.isEmpty(beanList.modelBackgroundPic)) {



            Glide.with(getContext())
                    .load(getResId(beanList.modelBackgroundPic, R.drawable.class))
                    .into(viewmodel);
        }
        editPageView.addView(viewmodel);
        viewleft = new ImageView(getContext());

        viewleft.setOnClickListener(new LeftIVListener());
        if (!TextUtils.isEmpty(beanList.leftBackImage)) {



            Glide.with(getContext())
                    .load(getResId(beanList.leftBackImage, R.drawable.class))
                    .into(viewleft);
        }
        editPageView.addView(viewleft);
        viewright = new ImageView(getContext());

        viewright.setOnClickListener(new RightIVListener());
        if (!TextUtils.isEmpty(beanList.rightBackImage)) {



            Glide.with(getContext())
                    .load(getResId(beanList.rightBackImage, R.drawable.class))
                    .into(viewright);
        }
        editPageView.addView(viewright);
        if(beanList.listDraftBookContent != null && beanList.listDraftBookContent.size() > 0) {


            for (int i = 0; i < beanList.listDraftBookContent.size(); i++) {

                //在这里区分imageview和textview
                if (beanList.listDraftBookContent.get(i).isTextOrImage.equals("image")) {
                    //如果是imageview
                    childview[i] = new ImageView(getContext());
                    ((ImageView) childview[i]).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    childview[i].setTag(R.string.app_name,beanList.listDraftBookContent.get(i));
                    editPageView.addView(childview[i]);
                    if (beanList.listDraftBookContent.get(i).getImageMarkName() != null) {
                        Glide.with(getActivity())
                                .load(beanList.listDraftBookContent.get(i).getImageMarkName())
                                .into((ImageView) childview[i]);
//                        x.image().bind((ImageView) childview[i], beanList.listDraftBookContent.get(i).getImageMarkName(), new ImageOptions.Builder().build());
//                    Log.i("EditF", "==============beanList.get(i).getImageMarkName()==================" + beanList.listDraftBookContent.get(i).getImageMarkName());
//                    Uri uri = Uri.parse("file://"+beanList.get(i).getImageMarkName());
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri));
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    ((ImageView) childview[i]).setImageBitmap(bitmap);
                    }

                    childview[i].setOnClickListener(new GetViewPos(i));
                } else if (beanList.listDraftBookContent.get(i).getIsTextOrImage().equals("text")) {
                    //如果是textview
                    childview[i] = new EditText(getContext());
                    childview[i].setTag(R.string.app_name,beanList.listDraftBookContent.get(i));
                    ((EditText) childview[i]).setTextSize(TypedValue.COMPLEX_UNIT_SP, 5);

                    (childview[i]).setPadding(1, 0, 1, 0);
                    //行间距
                    ((EditText) childview[i]).setLineSpacing(1, 1.52f);
                    //字间距
//                    ((EditText) childview[i]).setMaxEms(9);

                    //算出他的最大行数
                    float f=Float.parseFloat(beanList.listDraftBookContent.get(i).getTextOrImageHeight());
                    f=f/(0.8314f/5.036f);
                        ((EditText) childview[i]).setMaxLines((int)(f));

                    Log.d("ddd", "最大行高:" + (int) (f));


                    Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/汉仪丫丫体简_0.ttf");
                    ((EditText) childview[i]).setTypeface(face);
                    //使Edit不能滑动
                    ((EditText) childview[i]).setMovementMethod(null);
                    ((EditText) childview[i]).setSelection(0);


                    if (beanList.listDraftBookContent.get(i).getTextString() != null) {
                        ((TextView) childview[i]).setText(beanList.listDraftBookContent.get(i).getTextString());
                    }
                    editPageView.addView(childview[i]);

                }


            }
        }

    }


    public class GetViewPos implements View.OnClickListener {
        private int pos;

        public GetViewPos(int pos) {
            this.pos = pos;
        }

        public void onClick(final View v) {
            alertDialog = new AlertDialog.Builder(getContext()).
                    setItems(arrayFruit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getContext(), arrayFruit[which], Toast.LENGTH_SHORT).show();
                            Intent intent = null;
                            switch (which) {
                                case 0:
                                    //去相册找图
                                    intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(intent, pos);
                                    break;
                                case 1:
                                    //调用手机拍照
                                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
                                    if (!appDir.exists()) {
                                        appDir.mkdir();
                                    }
//                        String fileName = System.currentTimeMillis() + ".jpg";
                        File file = new File(appDir,"take_photo.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(intent, TAKE_PHOTO+pos);
                        break;
                    case 2:
                        if(!TextUtils.isEmpty(beanList.listDraftBookContent.get(pos).imageMarkName)){

                        Intent it_reviseimageactivity = new Intent(EditFragment.this.getActivity() , ReviseImageActivity.class);
                            it_reviseimageactivity.putExtra("viewX",v.getWidth());
                            it_reviseimageactivity.putExtra("viewY",v.getHeight());

                        Log.i("EF" , "===================beanList.listDraftBookContent.get(pos).imageMarkName========================"+beanList.listDraftBookContent.get(pos).imageMarkName);
                        it_reviseimageactivity.putExtra("imageFilePath" , beanList.listDraftBookContent.get(pos).imageMarkName);
                        startActivityForResult(it_reviseimageactivity , TAKE_REVISE);
                        }
                        break;
                }
            }
        }).
                            create();

            alertDialog.show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        littelBookImageNeedChange = true;
        ImageView imageView = null;
        //如果是去相册找图片
        if (requestCode<1000) {
//            如果没有选择图片返回判断空处理
        if (data == null) {
            return;
        }

            imageView = ((ImageView) childview[requestCode]);
            Uri uri = data.getData();
        Log.e("ddd", uri.toString());
            ContentResolver cr = getActivity().getContentResolver();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                Log.d("bbb","bitmap的大小1==="+bitmap.getByteCount());
//                Log.d("bbb","bitmap的大小1===hhhh"+bitmap.getHeight());
//                Log.d("bbb","bitmap的大小1===wwww"+bitmap.getWidth());
                //压缩图片
                bitmap = comp(bitmap);
//                bitmap=transImage(bitmap,828);

//                Log.d("bbb","bitmap的大小2==="+bitmap.getByteCount());
//                Log.d("bbb","bitmap的大小2===hhhh"+bitmap.getHeight());
//                Log.d("bbb","bitmap的大小2===wwww"+bitmap.getWidth());

                imageView.setImageBitmap(bitmap);
                //裁切图片
                bitmap=cutViewToBitmap(imageView, bitmap);
                File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee22222");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
//                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                    bmp = null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    bitmap = null;

                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                将bookcontent更新到数据库
                DraftBookContent draftBookContent = (DraftBookContent) imageView.getTag(R.string.app_name);
                Log.i("ef" , "=====================================draftBookContent.isTextOrImage = "+draftBookContent.isTextOrImage+",draftBookContent.bookPage = "+draftBookContent.bookPage);
                draftBookContent.setImageMarkName(file.getAbsolutePath());
                draftBookContentDao.update(draftBookContent);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode - TAKE_PHOTO >= 0) {
            //调用系统摄像头的方法
            imageView = ((ImageView) childview[requestCode - TAKE_PHOTO]);
            File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");

            Log.e("ddd", "调用系统摄像头的方法");
//            Uri u=Uri.parse("file:///storage/emulated/0/Boohee/333.jpg");
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/Boohee/take_photo.jpg");
            try {

//                 Log.d("ddd",Uri.fromFile(file)+"");
                Bitmap bit = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(Uri.fromFile(file)));

                imageView.setImageBitmap(bit);

                bit = comp(cutViewToBitmap(imageView, bit));
                String fileName = System.currentTimeMillis() + ".jpg";
                File file2 = new File(appDir, fileName);
                FileOutputStream fos = new FileOutputStream(file2);
                bit.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                //将bookcontent更新到数据库
                DraftBookContent draftBookContent = (DraftBookContent) imageView.getTag(R.string.app_name);
                draftBookContent.setImageMarkName(file2.getAbsolutePath());
                draftBookContentDao.update(draftBookContent);

                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //从编辑返回的
        }else if(requestCode == TAKE_REVISE){
            Glide.with(getActivity())
                    .load(data.getStringExtra("imagePath"))
                    .into(imageView);


        }
    }

    //获得缩略图的方法
    public static Bitmap getViewBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        bitmap.eraseColor(Color.WHITE);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    //切图的方法
    public static Bitmap cutViewToBitmap(View view, Bitmap bitmap) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getWidth(), view.getHeight());

        view.buildDrawingCache();
        view.setDrawingCacheEnabled(true);
//        Bitmap bitmap = view.getDrawingCache();
//        Bitmap bit = null;

        int x1 = view.getWidth();
//        int x2 = view.getMeasuredWidth();
        int y1 = view.getHeight();
//        int y2 = view.getMeasuredHeight();

        int x2=bitmap.getWidth();
        int y2=bitmap.getHeight();
        double x = 0;
        double y = 0;
        double fx = 0;
        double fy = 0;
        fx = (double) x1 / x2;
        fy = (double) y1 / y2;
//        Log.d("bbb", "view宽" + view.getWidth() + "  view高" + view.getHeight());
//        Log.d("bbb", "实际宽" + view.getMeasuredWidth() + "  实际高" + view.getMeasuredHeight());
//        Log.d("bbb", "x1" + x1 +"  y1" + y1);
//        Log.d("bbb", "x2" + x2+ "  y2" + y2);
        //控件宽大于长
        if ((x1 / y1)+0.5f > 1) {
            Log.d("bbb", "走到这了么");
            //图片和控件 高的比值 较大的时候即控件比例放大到高相等
            if (fx < fy) {
                x = ((float)y2 / (float)y1) * x1;//控件按比例放大的宽
                //(x2/2-x/2)图像起始点的横坐标 中心点坐标减去宽的一半
                bitmap = Bitmap.createBitmap(bitmap, (int) ((float)x2 / 2 - (float)x / 2), 0, (int) x, y2);
                Log.d("bbb", "走到1");
            } else {
                //当图片和控件 宽的比值 较大的时候 即控件比例放大到宽相等
                y = ((float)x2 /(float) x1) * y1;
                //(y2/2-y/2)图像起始点的纵坐标 中心点坐标减去长的一半
                Log.i("aaa", "x1====="+x1);
                Log.i("aaa","y1====="+y1);
                Log.i("aaa", "x2====="+x2);
                Log.i("aaa", "y2====="+y2);
                Log.i("aaa", "yy====="+y);

//                bitmap = Bitmap.createBitmap(bitmap, 0, 0, 780, 1040);
                bitmap = Bitmap.createBitmap(bitmap, 0, (int) (((float)y2 / 2 - (float)y / 2)), x2, (int) y);
                Log.d("bbb", "走到2");
            }
        } else {
            //如果控件长大于宽
            Log.d("bbb", "走到这了么2");
            if (fx < fy) {
                //图片和控件 高的比值 较大的时候即控件比例放大到高相等
                x = ((float)y2 / (float)y1) * x1;//控件按比例放大的宽
                //(x2/2-x/2)图像起始点的横坐标 中心点坐标减去宽的一半
                bitmap = Bitmap.createBitmap(bitmap, (int) ((float)x2 / 2 - (float)x / 2), 0, (int) x, y2);
                Log.d("bbb", "走到3");
            } else {
                Log.i("aaa", "x1====="+x1);
                Log.i("aaa","y1====="+y1);
                Log.i("aaa", "x2====="+x2);
                Log.i("aaa", "y2====="+y2);
                //当图片和控件 宽的比值 较大的时候 即控件比例放大到宽相等
                y = ((float)x2 / (float)x1) * y1;
                Log.i("aaa", "yy====="+y);
                //(y2/2-y/2)图像起始点的纵坐标 中心点坐标减去长的一半
//                bit = bitmap.createBitmap(bitmap, ((y2  - y )/2), 0, x2, y2);
                bitmap = Bitmap.createBitmap(bitmap, 0, (int) (((float)y2 - (float)y) / 2), x2, (int) y);

                Log.d("bbb", "走到4..............");
            }

        }

        return bitmap;
    }

    //尺寸压缩的方法
    private Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
//            baos.reset();//重置baos即清空baos
//            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
//        }
//        ByteArrayInputStream isBm = null;
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        image=BitmapFactory.decodeStream(isBm, null, newOpts);
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
//        image=BitmapFactory.decodeStream(isBm, null, newOpts);
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 700f;//这里设置高度
        float ww = hh / w * h;//这里设置宽度
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        Log.i("aaa","缩放的倍数="+be);
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inJustDecodeBounds = false;
        isBm = new ByteArrayInputStream(baos.toByteArray());
        image = BitmapFactory.decodeStream(isBm, null, newOpts);
        baos.reset();
        isBm.reset();
        return image;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 滑页时将文字和图片更新到数据库
     */
    public void slideUpData(){



        //滑页时文字更新到数据库
        for (int i = 0; i < childview.length; i++) {
            if (childview[i] != null && "text".equals(((DraftBookContent) childview[i].getTag(R.string.app_name)).getIsTextOrImage())) {
                //将bookcontent更新到数据库
                DraftBookContent draftBookContent = (DraftBookContent) childview[i].getTag(R.string.app_name);
                EditText et = (EditText) childview[i];
                draftBookContent.setTextString(et.getText().toString());
                draftBookContentDao.update(draftBookContent);
            }
        }

        //滑页时缩略图更新到数据库

        if(littelBookImageNeedChange){
            Log.i("ef" , "================littelBookImageNeedChange====================="+littelBookImageNeedChange);

            try {
                File appDir = new File(Environment.getExternalStorageDirectory(), "BooheeLittle");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                File file = new File(appDir, System.currentTimeMillis() + ".jpg");
                FileOutputStream fos = new FileOutputStream(file);
                Bitmap bit = getViewBitmap(editPageView);

                bit.compress(Bitmap.CompressFormat.JPEG, 10, fos);

                //将bookpage更新到数据库
                DraftBookPage draftBookPage = beanList;
                beanList.setLittleBookImage(file.getAbsolutePath());
                draftBookPageDao.updatePage(draftBookPage);

                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            littelBookImageNeedChange=false;
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getWidth(), view.getHeight());
        ImageView imageview = (ImageView) view;
        Log.d("bbb", "view宽" + view.getWidth() + "  view高" + view.getHeight());
        Log.d("bbb", "实际宽" + view.getMeasuredWidth() + "  实际高" + view.getMeasuredHeight());
        Log.d("bbb", "测试宽" + imageview.getDrawable().getIntrinsicWidth() + "  测试高" + imageview.getDrawable().getIntrinsicHeight());

        view.buildDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

    /**
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 左背景图片点击监听
     */
    public class LeftIVListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            leftOrright = "1";

        }
    }

    /**
     * 有背景图片点击监听
     */
    public class RightIVListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            leftOrright = "0";

        }
    }

    /**
     *改变背景
     * @param imgstr
     */
    public void changeBackground(String imgstr){
        littelBookImageNeedChange = true;
        if ("1".equals(leftOrright)){

            Glide.with(getContext())
                    .load(getResId(imgstr , R.drawable.class))
                    .into(viewleft);
            beanList.setLeftBackImage(imgstr);
            draftBookPageDao.updatePage(beanList);
        }else if ("0".equals(leftOrright)){

            Glide.with(getContext())
                    .load(getResId(imgstr , R.drawable.class))
                    .into(viewright);
            beanList.setRightBackImage(imgstr);
            draftBookPageDao.updatePage(beanList);
        }

    }

    public void notifyDataSetChange(DraftBookPage draftBookPage) {
        littelBookImageNeedChange = true;
        beanList = draftBookPage;
        editPageView.removeAllViews();
        viewmodel = new ImageView(getContext());
        if (!TextUtils.isEmpty(draftBookPage.modelBackgroundPic)) {
            Glide.with(getContext())
                    .load(getResId(draftBookPage.modelBackgroundPic, R.drawable.class))
                    .into(viewmodel);
        }
        editPageView.addView(viewmodel);
        viewleft = new ImageView(getContext());

        viewleft.setOnClickListener(new LeftIVListener());
        editPageView.addView(viewleft);
        viewright = new ImageView(getContext());

        viewright.setOnClickListener(new RightIVListener());
        editPageView.addView(viewright);
        if (draftBookPage.listDraftBookContent != null && draftBookPage.listDraftBookContent.size() > 0) {
            for (int i = 0; i < draftBookPage.listDraftBookContent.size(); i++) {
                //在这里区分imageview和textview
                if (draftBookPage.listDraftBookContent.get(i).isTextOrImage.equals("image")) {
                    //如果是imageview
                    childview[i] = new ImageView(getContext());
                    ((ImageView) childview[i]).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    childview[i].setTag(R.string.app_name ,draftBookPage.listDraftBookContent.get(i));
                    editPageView.addView(childview[i]);
                    if (draftBookPage.listDraftBookContent.get(i).getImageMarkName() != null) {
//                        x.image().bind((ImageView) childview[i], draftBookPage.listDraftBookContent.get(i).getImageMarkName(), new ImageOptions.Builder().build());
                        Glide.with(getActivity())
                                .load(draftBookPage.listDraftBookContent.get(i).getImageMarkName())
                                .into((ImageView) childview[i]);
                    }

                    childview[i].setOnClickListener(new GetViewPos(i));
                } else if (draftBookPage.listDraftBookContent.get(i).isTextOrImage.equals("text")) {
                    //如果是textview
                    childview[i] = new EditText(getContext());
                    childview[i].setTag(R.string.app_name ,draftBookPage.listDraftBookContent.get(i));
                    ((EditText) childview[i]).setTextSize(TypedValue.COMPLEX_UNIT_SP, 7);

                    (childview[i]).setPadding(1, 0, 1, 0);
                    //行间距
                    ((EditText) childview[i]).setLineSpacing(1, 1.52f);
                    //字间距
                    float f = Float.parseFloat(beanList.listDraftBookContent.get(i).getTextOrImageHeight());
                    f = f / (0.8314f / 5.036f);
                    if (f < 1) {
                        f = 1;
                    }
                    ((EditText) childview[i]).setMaxLines((int) (f));
                    Log.d("ddd", "最大行高:" + (int) (f));

                    Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/汉仪丫丫体简_0.ttf");
                    ((EditText) childview[i]).setTypeface(face);
                    //使Edit不能滑动
                    ((EditText) childview[i]).setMovementMethod(null);
                    ((EditText) childview[i]).setSelection(0);


                    if (draftBookPage.listDraftBookContent.get(i).getTextString() != null) {
                        ((TextView) childview[i]).setText(draftBookPage.listDraftBookContent.get(i).getTextString());
                    }
                    editPageView.addView(childview[i]);

                }


            }

        }
    }

    public Bitmap transImage(Bitmap bitmap, int width) {
//        Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        // 缩放图片的尺寸
        float scaleWidth = (float) width / bitmapWidth;
//        float scaleHeight = (float) bitmapHeight*scaleWidth/bitmapWidth;
        Matrix matrix = new Matrix();

//        matrix.postScale(scaleWidth, scaleHeight);
        matrix.setScale(scaleWidth, scaleWidth);
        Log.i("aaa","bitmapW======+"+bitmapWidth);
        Log.i("aaa","bitmapH======+"+bitmapHeight);
        Log.i("aaa","scaleWidth======+"+scaleWidth);
//        Log.i("aaa","scaleheight======+"+scaleHeight);
        // 产生缩放后的Bitmap对象
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
      return resizeBitmap;
    }

    public  void removeThisPage(){
        editPageView.removeAllViews();
    }

    /**
     *
     * @param photoList 图片的list
     * @param current   记录该从list的哪一个位置开始放图片
     */
    public int quicklyUpdate(List<Photo> photoList , int current){
        if (photoList.size() != 0){
            //遍历所有的childview
            for (int i = 0 ; i < childview.length ; i ++){
                //childview 有可能是null
                if (childview[i] != null && current < photoList.size() ){
                    //如果childview是image ((DraftBookContent)childview[i].getTag(R.string.app_name)).imageMarkName 是空
                    DraftBookContent currentDraftBookContent = (DraftBookContent)childview[i].getTag(R.string.app_name);
                    if(("image".equals(((DraftBookContent)childview[i].getTag(R.string.app_name)).isTextOrImage) && TextUtils.isEmpty(currentDraftBookContent.imageMarkName))){

                        ImageView iv = (ImageView) childview[i];
                        //图片放进去了写入数据库
                        Uri uri = Uri.parse("file:///" + photoList.get(current).path);
                        ContentResolver cr = getActivity().getContentResolver();
                        Bitmap bitmap;
                        try {
                            bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                            //压缩图片
                            bitmap = comp(bitmap);
                            iv.setImageBitmap(bitmap);
                            //裁切图片
                            bitmap=cutViewToBitmap(iv, bitmap);
                            File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee22222");
                            if (!appDir.exists()) {
                                appDir.mkdir();
                            }
                            String fileName = System.currentTimeMillis() + ".jpg";
                            File file = new File(appDir, fileName);
                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                bitmap = null;
                                fos.flush();
                                fos.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //将bookcontent更新到数据库
                            DraftBookContent draftBookContent = (DraftBookContent) iv.getTag(R.string.app_name);
                            Log.i("ef" , "=====================================draftBookContent.isTextOrImage = "+draftBookContent.isTextOrImage+",draftBookContent.bookPage = "+draftBookContent.bookPage);
                            draftBookContent.setImageMarkName(file.getAbsolutePath());
                            draftBookContentDao.update(draftBookContent);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        //记录位置的参数+1
                        current++;


                    }
                }
            }
            //更新这一页的缩略图
            littelBookImageNeedChange = true;
            slideUpData();
        }
            //把++过的传回去
            return current;
    }
}
