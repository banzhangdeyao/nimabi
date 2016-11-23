package com.twkj.lovebook.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.twkj.lovebook.R;
import com.twkj.lovebook.adapter.ReviseImageTexiaoAdapter;
import com.twkj.lovebook.adapter.ReviseImageTiezhiAdapter;
import com.twkj.lovebook.bean.DraftBookContent;
import com.twkj.lovebook.dao.DraftBookContentDao;
import com.twkj.lovebook.utils.ControlBitmapUtils;
import com.twkj.lovebook.utils.ImageToneLayer;
import com.twkj.lovebook.utils.ReviseImageUtils;
import com.twkj.lovebook.view.StickerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 写书的fragment 针对图片编辑的页面
 */
public class ReviseImageActivity extends Activity implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {


    private ImageView iv_back;
    private SeekBar sb_lum, sb_saturation, sb_hum;
    private LinearLayout ll_lum, ll_saturation, ll_hum;
    private CheckBox cb_lum, cb_saturation, cb_hum;
    private TextView tv_ok;
    private TextView tv_adjust , tv_cut;//调节， 裁切
    private RadioGroup radioGroup;
    private RadioButton rb_tuya, rb_texiao;


    private RelativeLayout rl_container;//贴图容器
    // 定义贴图素材集合
    private ArrayList<StickerView> materialList;
    private ImageView iv;
    private ImageToneLayer imageToneLayer;

    //初始化的bitmap
    private Bitmap bit = null;
    private Bitmap bitmapChange;
    private String imageFilePath;

    private RecyclerView rv_texiao, rv_tiezhi;//特效recyclerView 贴纸recyclerView
    private ReviseImageTexiaoAdapter texiaoAdapter;//特效adapter
    private ReviseImageTiezhiAdapter tiezhiAdapter;//贴纸adapter
    private List<String> teizhiDataList;
    private List<Bitmap> texiaoDataList = null;
    private DraftBookContent draftBookContent;
    private DraftBookContentDao draftBookContentDao = new DraftBookContentDao();

    /**
     * SeekBar的中间值
     */
    private static final int MIDDLE_VALUE = 127;

    /**
     * SeekBar的最大值
     */
    private static final int MAX_VALUE = 255;


    /**
     * 饱和度标识
     */
    public static final int FLAG_SATURATION = 0x0;

    /**
     * 亮度标识
     */
    public static final int FLAG_LUM = 0x1;

    /**
     * 色相标识
     */
    public static final int FLAG_HUE = 0x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_revise_image);

        initView();
    }

    private void initView() {

        iv_back = (ImageView) findViewById(R.id.activity_revise_image_iv_back);
        iv_back.setOnClickListener(this);
        tv_ok = (TextView) findViewById(R.id.activity_revise_image_tv_ok);
        tv_ok.setOnClickListener(this);
        tv_adjust = (TextView) findViewById(R.id.activity_revise_image_tv_adjust);
        tv_cut = (TextView) findViewById(R.id.activity_revise_image_tv_cut);
        tv_adjust.setOnClickListener(this);
        tv_cut.setOnClickListener(this);
        imageToneLayer = new ImageToneLayer(this);

        rl_container = (RelativeLayout) findViewById(R.id.activity_revise_image_rl_container);
        iv = (ImageView) findViewById(R.id.activity_revise_image_iv);
        rv_texiao = (RecyclerView) findViewById(R.id.activity_revise_image_rv_texiao);
        rv_tiezhi = (RecyclerView) findViewById(R.id.activity_revise_image_rv_tiezhi);
        materialList = new ArrayList<StickerView>();
        radioGroup = (RadioGroup) findViewById(R.id.activity_revise_image_radiogroup);
        rb_tuya = (RadioButton) findViewById(R.id.activity_revise_image_rb_tuya);
        rb_texiao = (RadioButton) findViewById(R.id.activity_revise_image_rb_texiao);
        radioGroup.setOnCheckedChangeListener(new RadioButtonListener());

        ll_lum = (LinearLayout) findViewById(R.id.activity_revise_image_ll_lum);
        ll_saturation = (LinearLayout) findViewById(R.id.activity_revise_image_ll_saturation);
        ll_hum = (LinearLayout) findViewById(R.id.activity_revise_image_ll_hum);

        cb_lum = (CheckBox) findViewById(R.id.activity_revise_image_cb_lum);
        cb_saturation = (CheckBox) findViewById(R.id.activity_revise_image_cb_saturation);
        cb_hum = (CheckBox) findViewById(R.id.activity_revise_image_cb_hum);
        cb_lum.setOnCheckedChangeListener(this);
        cb_saturation.setOnCheckedChangeListener(this);
        cb_hum.setOnCheckedChangeListener(this);

        sb_lum = (SeekBar) findViewById(R.id.activity_revise_image_sb_lum);
        sb_lum.setMax(MAX_VALUE);
        sb_lum.setProgress(MIDDLE_VALUE);
        sb_lum.setOnSeekBarChangeListener(this);

        sb_saturation = (SeekBar) findViewById(R.id.activity_revise_image_sb_saturation);
        sb_saturation.setMax(MAX_VALUE);
        sb_saturation.setProgress(MIDDLE_VALUE);
        sb_saturation.setOnSeekBarChangeListener(this);

        sb_hum = (SeekBar) findViewById(R.id.activity_revise_image_sb_hum);
        sb_hum.setMax(MAX_VALUE);
        sb_hum.setProgress(MIDDLE_VALUE);
        sb_hum.setOnSeekBarChangeListener(this);

        imageFilePath = getIntent().getStringExtra("imageFilePath");
        draftBookContent = (DraftBookContent) getIntent().getSerializableExtra("draftBookContent");

        Glide.with(this).load(imageFilePath).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                iv.setImageBitmap(resource);
                bit = resource;
                bitmapChange = bit;
                if(texiaoDataList == null){
                    texiaoDataList = new ArrayList<Bitmap>();
                    texiaoDataList.add(ReviseImageUtils.oldRemeber(bit));
                    texiaoDataList.add(ReviseImageUtils.blurImage(bit));
                    texiaoDataList.add(ReviseImageUtils.blurImageAmeliorate(bit));
                    texiaoDataList.add(ReviseImageUtils.emboss(bit));
                    texiaoDataList.add(ReviseImageUtils.sharpenImageAmeliorate(bit));
                    texiaoDataList.add(ReviseImageUtils.film(bit));
                    texiaoDataList.add(ReviseImageUtils.sunshine(bit, bit.getWidth() / 2, bit.getHeight() / 2));
                    texiaoAdapter = new ReviseImageTexiaoAdapter(ReviseImageActivity.this, texiaoDataList);
                    texiaoAdapter.setOnItemClickListener(new TeXiaoAdapterListener());
                    rv_texiao.setLayoutManager(new LinearLayoutManager(ReviseImageActivity.this));
                    rv_texiao.setAdapter(texiaoAdapter);
                }


            }
        });
        teizhiDataList = new ArrayList<String>();
        teizhiDataList.add("reviseimage_tiezhi001");
        teizhiDataList.add("reviseimage_tiezhi002");
        teizhiDataList.add("reviseimage_tiezhi003");
        teizhiDataList.add("reviseimage_tiezhi004");
        teizhiDataList.add("reviseimage_tiezhi005");
        teizhiDataList.add("reviseimage_tiezhi006");
        teizhiDataList.add("reviseimage_tiezhi007");
        teizhiDataList.add("reviseimage_tiezhi008");
        tiezhiAdapter = new ReviseImageTiezhiAdapter(ReviseImageActivity.this, teizhiDataList);
        tiezhiAdapter.setOnItemClickListener(new TiezhiAdapterListener());
        rv_tiezhi.setLayoutManager(new LinearLayoutManager(this));
        rv_tiezhi.setAdapter(tiezhiAdapter);

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int flag = 0;
        switch (seekBar.getId()) {
            case R.id.activity_revise_image_sb_lum:
                imageToneLayer.setLum(progress);
                flag = FLAG_LUM;
                break;

            case R.id.activity_revise_image_sb_saturation:
                imageToneLayer.setSaturation(progress);
                flag = FLAG_SATURATION;
                break;

            case R.id.activity_revise_image_sb_hum:
                imageToneLayer.setHue(progress);
                flag = FLAG_HUE;
                break;
            default:

                break;
        }


        iv.setImageBitmap(imageToneLayer.handleImage(bit, flag));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.activity_revise_image_cb_lum:
                if (isChecked) {
                    ll_lum.setVisibility(View.VISIBLE);
                } else {
                    ll_lum.setVisibility(View.GONE);
                }
                break;

            case R.id.activity_revise_image_cb_saturation:
                if (isChecked) {
                    ll_saturation.setVisibility(View.VISIBLE);
                } else {
                    ll_saturation.setVisibility(View.GONE);
                }
                break;

            case R.id.activity_revise_image_cb_hum:
                if (isChecked) {
                    ll_hum.setVisibility(View.VISIBLE);
                } else {
                    ll_hum.setVisibility(View.GONE);
                }
                break;
            default:

                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.activity_revise_image_iv_back:
                this.finish();
                break;
            case R.id.activity_revise_image_tv_ok:


                for (StickerView effectView : materialList){
                        if (effectView.getIsActive()) {
                            bitmapChange = createBitmap(bitmapChange,
                                    ControlBitmapUtils.readBitMap(ReviseImageActivity.this , getResId(effectView.getImgPath() , R.drawable.class)),
                                    effectView.getCenterPoint(), effectView.getDegree(), effectView.getScaleValue());
                        }
                }
//                iv.setImageBitmap(bitmapChange);
                File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee22222");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmapChange.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    bitmapChange = null;
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //将bookcontent更新到数据库
                draftBookContent.setImageMarkName(file.getAbsolutePath());
                draftBookContentDao.update(draftBookContent);
                Intent it_result = new Intent();

                it_result.putExtra("imagePath", file.getAbsolutePath());
                setResult(1001, it_result);
                finish();

                break;
            //调节
            case R.id.activity_revise_image_tv_adjust:

                break;
            //裁切
            case R.id.activity_revise_image_tv_cut:
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(Uri.parse(getIntent().getStringExtra("imageFilePath")), "image/*");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getIntent().getStringExtra("imageFilePath"));
                intent.putExtra("aspectX", getIntent().getIntExtra("viewX",0));
                intent.putExtra("aspectY", getIntent().getIntExtra("viewX",0));
                startActivityForResult(intent, 3);
                break;
            default:

                break;
        }
    }

    public static int getResId(String variableName , Class<?> c){
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    /**
     * 左侧特效和背景两个radiobutton监听
     */
    class RadioButtonListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.activity_revise_image_rb_tuya:
                    rv_texiao.setVisibility(View.VISIBLE);
                    rv_tiezhi.setVisibility(ViewGroup.GONE);

                    break;
                case R.id.activity_revise_image_rb_texiao:
                    rv_texiao.setVisibility(View.GONE);
                    rv_tiezhi.setVisibility(ViewGroup.VISIBLE);
                    break;
            }
        }
    }

    class TeXiaoAdapterListener implements ReviseImageTexiaoAdapter.OnItemClickListener {
        @Override
        public void onItemClick(View view, int pos) {
            switch (pos) {
                case 0:
                    bitmapChange = ReviseImageUtils.oldRemeber(bit);
                    break;
                case 1:
                    bitmapChange = ReviseImageUtils.blurImage(bit);
                    break;
                case 2:
                    bitmapChange = ReviseImageUtils.blurImageAmeliorate(bit);

                    break;
                case 3:
                    bitmapChange = ReviseImageUtils.emboss(bit);

                    break;
                case 4:
                    bitmapChange = ReviseImageUtils.sharpenImageAmeliorate(bit);

                    break;
                case 5:
                    bitmapChange = ReviseImageUtils.film(bit);

                    break;
                case 6:
                    bitmapChange = ReviseImageUtils.sunshine(bit, bit.getWidth() / 2, bit.getHeight() / 2);

                    break;
                default:
                    break;
            }
            iv.setImageBitmap(bitmapChange);
        }
    }

    /**
     * 选择贴纸的监听
     */
    class TiezhiAdapterListener implements ReviseImageTiezhiAdapter.OnItemClickListener {
        @Override
        public void onItemClick(View v, int pos) {
            StickerView stickerView = new StickerView(ReviseImageActivity.this, teizhiDataList.get(pos));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            stickerView.setLayoutParams(params);
            rl_container.addView(stickerView);
            materialList.add(stickerView);

        }
    }

    // 图片合成
    private Bitmap createBitmap(Bitmap src, Bitmap dst, float[] centerPoint, float degree, float scaleValue) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        float scale = (float) w / (float) width;

        int ww = dst.getWidth();
        int wh = dst.getHeight();

        float Ltx = centerPoint[0] - iv.getLeft() - ww * scaleValue / 2;
        float Lty = centerPoint[1] - iv.getTop() - wh * scaleValue / 2;

        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);//创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(src, 0, 0, null);//在 0，0坐标开始画入src

        // 定义矩阵对象
        Matrix matrix = new Matrix();
        matrix.postScale(scaleValue, scaleValue);
//        matrix.postRotate(degree);
        cv.save();
        cv.rotate(degree, centerPoint[0], centerPoint[1]);
        Bitmap dstbmp = Bitmap.createBitmap(dst, 0, 0, dst.getWidth(), dst.getHeight(),
                matrix, true);
        cv.drawBitmap(dstbmp, Ltx * scale, Lty * scale, null);//在src画贴图
        //cv.save( Canvas.ALL_SAVE_FLAG );//保存
        cv.restore();//存储
        return newb;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 3:
                Bitmap bitmap = data.getParcelableExtra("data");

//                byte[] source = data.getByteArrayExtra();
//                icon = BitmapFactory.decodeByteArray(source,0,source.length);
//                contactIcon.setImageBitmap(icon);
//                Bitmap bitmap = decodeUriAsBitmap(newimageUri);//decode bitmap
                iv.setImageBitmap(bitmap);
                break;
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {

        Bitmap bitmap = null;
        Log.d("ttt", "是不是空啊 ：" + (uri == null));//
        try {

            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

        } catch (FileNotFoundException e) {

            e.printStackTrace();

            return null;

        }

        return bitmap;

    }
}
