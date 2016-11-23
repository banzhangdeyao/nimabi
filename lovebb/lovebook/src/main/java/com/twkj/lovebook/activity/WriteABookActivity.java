package com.twkj.lovebook.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.impl.FileUploadTask;
import com.twkj.lovebook.R;
import com.twkj.lovebook.adapter.EditAdapter;
import com.twkj.lovebook.adapter.EditorDevelopBackgroundAdapter;
import com.twkj.lovebook.adapter.WriteABookModelAdapter;
import com.twkj.lovebook.application.MyApplication;
import com.twkj.lovebook.bean.AllBookBean;
import com.twkj.lovebook.bean.DraftBook;
import com.twkj.lovebook.bean.DraftBookContent;
import com.twkj.lovebook.bean.DraftBookPage;
import com.twkj.lovebook.bean.ModelPageBean;
import com.twkj.lovebook.constants.BookModelConfig;
import com.twkj.lovebook.dao.DraftBookContentDao;
import com.twkj.lovebook.dao.DraftBookDao;
import com.twkj.lovebook.dao.DraftBookPageDao;
import com.twkj.lovebook.fragment.EditFragment;
import com.twkj.lovebook.utils.BookInfoUtils;
import com.twkj.lovebook.utils.UUIDFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 手动编辑
 * 写书的activity
 */
public class WriteABookActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {


    private ImageView iv_back;
    //屏幕编辑的页面全屏的viewpager来实现左右滑动
    private ViewPager viewPager;
    private EditAdapter editAdapter;
    private List<Fragment> editPageList;

    private DraftBook allDataList;
    //一整本书的对象
    private AllBookBean allBookBeanInfo;
    //书的所有页
    private List<ModelPageBean> ModelPageList;

    private RecyclerView rv_model;//模板listview

    private WriteABookModelAdapter modelAdapter;

    //三个dao
    private DraftBookPageDao draftBookPageDao;

    private DraftBookDao draftBookDao;

    private DraftBookContentDao draftBookContentDao;

    //操作book的bookID
    private int aimBookid;

//    private MyApplication myApplication;
//    private Socket socket;
//
//    private UploadManager mFileUploadManager = null;
//
//    private String bookid=null;
//    private String onceSign=null;
//    private String sign = null;
//    private String appid = null;
//    private String bucket = null;
//    private String persistenceId = null;
//    private String videoSaveName = null;
//    private FileUploadTask fileUploadTask = null;
//
//    private Button btn_up, btn_id, btn_justtry , btn_task;


    private EditorDevelopBackgroundAdapter editorDevelopBackgroundAdapter;//编辑-扩展-背景选择adapter
    private RecyclerView rv_editordevelopbackground;//编辑-扩展-背景选择recyclerview
    private List<String> developbackgroundlistdata;

    private RadioGroup radioGroup_menu;//菜单

    private RadioButton rb_background , rb_camera , rb_sticker , rb_clean;//背景，相机 ， 贴纸，清除

    private AlertDialog tipsAlert;//提示alert
    private List<String> listCannotEditPages;

    private boolean moveRight = false, moveLeft = false;
    private int lastValue = 0;
    private int position;

    public int getAimBookid(){
        return aimBookid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_write_abook);


//        Log.i("生命周期" , "走了oncreat方法");
        initView();
        createFragment();
        String EditorOrBookShelf = getIntent().getStringExtra("EditorOrBookShelf");
        Log.i("WriteABookActivity" , "===========EditorOrBookShelf=============="+EditorOrBookShelf);
        if(TextUtils.equals(EditorOrBookShelf , "editor")){
            autoEditsetData();
        }else if ("bookShelf".equals(EditorOrBookShelf)){
            bookShelfSetData();
        }
        getDraftBook();
        getData();

        editAdapter = new EditAdapter(getSupportFragmentManager(), editPageList);
        viewPager.setAdapter(editAdapter);
        //如果是从校稿页面跳过来的话 直接跳转到点击的那个条目页数
        viewPager.setCurrentItem(getIntent().getIntExtra("checkPosition",0));



        //防止滑动出现白屏 ↓ 迷之方法
        viewPager.setOffscreenPageLimit(editPageList.size());
        viewPager.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (lastValue > positionOffsetPixels) {
            moveRight = true;
            moveLeft = false;
        } else if (lastValue < positionOffsetPixels) {
            moveRight = false;
            moveLeft = true;
        }
        lastValue = positionOffsetPixels;
    }

    private boolean isMovedFromPrevious() {
        return moveLeft;
    }

    private boolean isMovedFromNext() {
        return moveRight;
    }

    @Override
    public void onPageSelected(int position) {
        Log.i("wba" , "=================================moveLeft = "+moveLeft + ", moveRight = "+moveRight);
        if(moveLeft){
            //从左边position-1
            ((EditFragment)editPageList.get(position - 1)).slideUpData();
        }else{
            //position+1
            ((EditFragment)editPageList.get(position + 1)).slideUpData();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 初始化界面
     */
    private void initView(){
        iv_back = (ImageView) findViewById(R.id.activity_write_abook_iv_back);
        iv_back.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.activity_write_abook_viewpager);
        rv_model = (RecyclerView) findViewById(R.id.activity_write_abook_rv_model);
        rv_editordevelopbackground = (RecyclerView) findViewById(R.id.activity_write_abook_rv_background);
        radioGroup_menu = (RadioGroup) findViewById(R.id.activity_write_abook_radiogroup_menu);
        rb_background = (RadioButton) findViewById(R.id.activity_write_abook_rb_background);
        rb_camera = (RadioButton) findViewById(R.id.activity_write_abook_rb_camera);
        rb_sticker = (RadioButton) findViewById(R.id.activity_write_abook_rb_sticker);
        rb_clean = (RadioButton) findViewById(R.id.activity_write_abook_rb_clean);
        radioGroup_menu.setOnCheckedChangeListener(new RadioButtonListener());
        //三个dao
        draftBookDao = new DraftBookDao();
        draftBookPageDao = new DraftBookPageDao();
        draftBookContentDao = new DraftBookContentDao();

        //从plist取出样本书
        allBookBeanInfo = BookInfoUtils.getAllBookBean(this);

        modelAdapter = new WriteABookModelAdapter(this , BookModelConfig.bookModelList);
        rv_model.setLayoutManager(new LinearLayoutManager(this));
        rv_model.setAdapter(modelAdapter);
        modelAdapter.setOnItemClickListener(new ModelClickListener());
//        lv_model.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrlist));
//        lv_model.setOnItemClickListener(this);

        listCannotEditPages = new ArrayList<String>();
        for (int i = 0 ; i < allBookBeanInfo.book.whichPageCannotEdit.size() ; i++){
            listCannotEditPages.add(allBookBeanInfo.book.whichPageCannotEdit.get(i).pageNumber);
        }
        //---------------------------------menu-----------------------------------
        developbackgroundlistdata = new ArrayList<String>();
        developbackgroundlistdata.add("shading1");
        developbackgroundlistdata.add("shading2");
        developbackgroundlistdata.add("shading3");
        developbackgroundlistdata.add("shading4");
        editorDevelopBackgroundAdapter = new EditorDevelopBackgroundAdapter(this , developbackgroundlistdata);
        rv_editordevelopbackground.setAdapter(editorDevelopBackgroundAdapter);
        rv_editordevelopbackground.setLayoutManager(new LinearLayoutManager(this));
        editorDevelopBackgroundAdapter.setOnItemClickListener(new EditorDevelopBackgroundAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ((EditFragment)editPageList.get(viewPager.getCurrentItem())).changeBackground(developbackgroundlistdata.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        //--------------------------alert------------------------------------
        tipsAlert = new AlertDialog.Builder(this)

                .setTitle("提示")
                .setMessage("试试别的操作")

                .setNegativeButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tipsAlert.dismiss();
                    }
                }).create();
        tipsAlert.setCanceledOnTouchOutside(true);
    }

    private void createFragment() {

        editPageList = new ArrayList<>();
        ModelPageList = new ArrayList<ModelPageBean>();

        ModelPageList = allBookBeanInfo.modelPage;
        for (int i = 0; i < ModelPageList.size(); i++) {
            Fragment fragment = new EditFragment();
            editPageList.add(fragment);
        }
    }

    /**
     * 从编辑-->手动编辑进入 设置数据
     */
    private void autoEditsetData(){

        //添加一本draftbook 到数据库
        DraftBook putDraftBook = new DraftBook();
        aimBookid = draftBookDao.findmax_id() + 1;
        putDraftBook.setBookID(aimBookid);
        putDraftBook.setBookName("bbb");
        //editType;编辑类型（0是手动1是自动）
        putDraftBook.setEditType(0);
        putDraftBook.setBookPages(allBookBeanInfo.modelPage.size());

        draftBookDao.insert(putDraftBook);
        //添加封面等不能编辑的页面
        for (int i = 0 ; i < allBookBeanInfo.book.whichPageCannotEdit.size() ; i++){
            int pagenum = Integer.valueOf(allBookBeanInfo.book.whichPageCannotEdit.get(i).pageNumber);
            if(draftBookPageDao.selectorByBookIdAndPage(aimBookid , pagenum) == null){
                DraftBookPage putDraftBookPage = new DraftBookPage();
                putDraftBookPage.setDraftBookID(aimBookid);
                putDraftBookPage.setBookPage(pagenum);

                putDraftBookPage.setModelBackgroundPic(allBookBeanInfo.modelPage.get(pagenum).whichPage.modelBackgroundPic);
                //这一页是哪个模板 这个可能到时候改的时候就不要了
                putDraftBookPage.setWhichModel(allBookBeanInfo.modelPage.get(pagenum).whichPage.whichModelPage);

                try {
                    File appDir = new File(Environment.getExternalStorageDirectory(), "BooheeLittle");
                    if (!appDir.exists()) {
                        appDir.mkdir();
                    }
                    File file = new File(appDir, UUIDFactory.getUUIDStr() + ".jpg");
                    FileOutputStream fos = new FileOutputStream(file);

                   Bitmap bit = readBitMap(this , getResId(allBookBeanInfo.modelPage.get(pagenum).whichPage.modelBackgroundPic , R.drawable.class));


                    bit.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    bit.recycle();
                    putDraftBookPage.setLittleBookImage(file.getAbsolutePath());
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                draftBookPageDao.insert(putDraftBookPage);
            }

        }



    }

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
            return R.drawable.changbackpc_0;
        }
    }

    /**
     * 从书架 进入设置数据
     */
    private void bookShelfSetData(){
        aimBookid = getIntent().getIntExtra("aimBookid" , 0);
        Log.i("WriteABookActivity" , "=======================aimBookid========================="+aimBookid);

    }

    /**
     * 根据艾玛aimBookid 从数据库中取出draftbook
     */
    private void getDraftBook(){
        // 取出一本draftbook
//        aimBookid=1;
        DraftBook getDraftBook = new DraftBook();
        getDraftBook = draftBookDao.selectById(aimBookid);

        //初始化64页page
        getDraftBook.listDraftBookPage = new ArrayList<>();
        for (int i = 0; i < allBookBeanInfo.modelPage.size(); i++) {
            DraftBookPage dbp = new DraftBookPage();

            getDraftBook.listDraftBookPage.add(dbp);
        }

        //取出所有page
        List<DraftBookPage> draftBookPageList = new ArrayList<DraftBookPage>();
        draftBookPageList = draftBookPageDao.selectorByBookId(aimBookid);
        //放入draftbook
        if (draftBookPageList != null) {
            for (int i = 0; i < draftBookPageList.size(); i++) {

                //取出所有content
                List<DraftBookContent> draftBookContentList = new ArrayList<DraftBookContent>();
                draftBookContentList = draftBookContentDao.selectorByBookIdAndBookPage(aimBookid, draftBookPageList.get(i).bookPage);
                draftBookPageList.get(i).listDraftBookContent = draftBookContentList;
                //第几页的放到draftbookpagetwo的第几个位置
                getDraftBook.listDraftBookPage.set(draftBookPageList.get(i).bookPage, draftBookPageList.get(i));

            }
        }
        allDataList = getDraftBook;
    }

    private void getData() {
        Bundle bundle = null;

        for (int i = 0; i < editPageList.size(); i++) {
            bundle = new Bundle();
//            bundle.putSerializable("data", (Serializable) allDataList.listDraftBookPage.get(i).listDraftBookContent);
            bundle.putSerializable("data", allDataList.listDraftBookPage.get(i));

            editPageList.get(i).setArguments(bundle);
        }
    }

    /**
     * 点击右侧模板选择的监听
     */
    class ModelClickListener implements WriteABookModelAdapter.OnItemClickListener{
        @Override
        public void onItemClick(View view, int position) {
            //第几页
            int pageNumber = viewPager.getCurrentItem();
            Toast.makeText(WriteABookActivity.this , "pageNumber = " + pageNumber , Toast.LENGTH_SHORT).show();
            //不能编辑的页面
            if(listCannotEditPages.contains(pageNumber+"")) {
                tipsAlert.show();
                return;
            }
            //不能操作的item
            if(listCannotEditPages.contains(position+"")) {
                tipsAlert.show();
                return;
            }



            //点击模板时 如果这个bookpage存在 删除这个bookpage 还有这个bookpage上的content
            if (draftBookPageDao.selectorByBookIdAndPage(aimBookid, pageNumber) != null) {
                draftBookPageDao.deleteByBookIdAndBookPage(aimBookid, pageNumber);
                draftBookContentDao.deleteByBookIdAndBookPage(aimBookid, pageNumber);
            }
            //点击模板时 bookpage添加一条数据
            DraftBookPage draftBookPage = new DraftBookPage();
            draftBookPage.setDraftBookID(aimBookid);
            draftBookPage.setBookPage(pageNumber);
            draftBookPage.setWhichModel(allBookBeanInfo.modelPage.get(position).whichPage.whichModelPage);
            draftBookPage.setModelBackgroundPic(allBookBeanInfo.modelPage.get(position).whichPage.modelBackgroundPic);
            draftBookPage.setCannotEdit(true);
            draftBookPageDao.insert(draftBookPage);
            //同时添加该页的所有content
            //set 每个content的tag，set时区分img和text是第几个
            List<DraftBookContent> draftBookContentList = new ArrayList<DraftBookContent>();
            int imgCount = 0, textCount = 0;
            for (int i = 0; i < allBookBeanInfo.modelPage.get(position).bookContent.size(); i++) {
                DraftBookContent dbc = new DraftBookContent();
                dbc.setBookID(aimBookid);
                dbc.setBookPage(pageNumber);
                dbc.setContentName(allBookBeanInfo.modelPage.get(position).bookContent.get(i).contentName);
                dbc.setIsTextOrImage(allBookBeanInfo.modelPage.get(position).bookContent.get(i).isTextOrImage);
                dbc.setTextOrImageX(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textOrImageX);
                dbc.setTextOrImageY(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textOrImageY);
                dbc.setTextOrImageWidth(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textOrImageWidth);
                dbc.setTextOrImageHeight(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textOrImageHeight);
                if(!TextUtils.isEmpty(allBookBeanInfo.modelPage.get(position).bookContent.get(i).rotationAngle)){
                    dbc.setRotationAngle(allBookBeanInfo.modelPage.get(position).bookContent.get(i).rotationAngle);
                }
                if(!TextUtils.isEmpty(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textNumber)){
                    dbc.setTextNumber(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textNumber);
                }


                String textOrImage = allBookBeanInfo.modelPage.get(position).bookContent.get(i).getIsTextOrImage();
                //是image就+1存储到tag
                if ("image".equals(textOrImage)) {
                    dbc.setTag(imgCount += 1);
                }
                //是text就+1存储到tag
                if ("text".equals(textOrImage)) {
                    dbc.setTag(textCount += 1);
                }
                draftBookContentList.add(dbc);
                draftBookContentDao.insert(dbc);
            }
            draftBookPage.listDraftBookContent = draftBookContentList;
            //动态改变视图
            //存进去在取出来
            DraftBookPage beanlist = draftBookPageDao.selectorByBookIdAndPage(aimBookid , pageNumber);
            beanlist.listDraftBookContent = draftBookContentDao.selectorByBookIdAndBookPage(aimBookid , pageNumber);
            ((EditFragment)editPageList.get(viewPager.getCurrentItem())).notifyDataSetChange(beanlist);
        }
    }

//    /**
//     * 点击右侧模板选择的监听
//     * @param parent
//     * @param view
//     * @param position
//     * @param id
//     */
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        //第几页
//        int pageNumber = viewPager.getCurrentItem();
//        Toast.makeText(this , "pageNumber = " + pageNumber , Toast.LENGTH_SHORT).show();
//        //不能编辑的页面
//        if(listCannotEditPages.contains(pageNumber+"")) {
//            tipsAlert.show();
//            return;
//        }
//        //不能操作的item
//        if(listCannotEditPages.contains(position+"")) {
//            tipsAlert.show();
//            return;
//        }
//
//
//
//        //点击模板时 如果这个bookpage存在 删除这个bookpage 还有这个bookpage上的content
//        if (draftBookPageDao.selectorByBookIdAndPage(aimBookid, pageNumber) != null) {
//            draftBookPageDao.deleteByBookIdAndBookPage(aimBookid, pageNumber);
//            draftBookContentDao.deleteByBookIdAndBookPage(aimBookid, pageNumber);
//        }
//        //点击模板时 bookpage添加一条数据
//        DraftBookPage draftBookPage = new DraftBookPage();
//        draftBookPage.setDraftBookID(aimBookid);
//        draftBookPage.setBookPage(pageNumber);
//        draftBookPage.setWhichModel(allBookBeanInfo.modelPage.get(position).whichPage.whichModelPage);
//        draftBookPage.setModelBackgroundPic(allBookBeanInfo.modelPage.get(position).whichPage.modelBackgroundPic);
//        draftBookPage.setCannotEdit(true);
//        draftBookPageDao.insert(draftBookPage);
//        //同时添加该页的所有content
//        //set 每个content的tag，set时区分img和text是第几个
//        List<DraftBookContent> draftBookContentList = new ArrayList<DraftBookContent>();
//        int imgCount = 0, textCount = 0;
//        for (int i = 0; i < allBookBeanInfo.modelPage.get(position).bookContent.size(); i++) {
//                    DraftBookContent dbc = new DraftBookContent();
//                    dbc.setBookID(aimBookid);
//                    dbc.setBookPage(pageNumber);
//                    dbc.setContentName(allBookBeanInfo.modelPage.get(position).bookContent.get(i).contentName);
//                    dbc.setIsTextOrImage(allBookBeanInfo.modelPage.get(position).bookContent.get(i).isTextOrImage);
//                    dbc.setTextOrImageX(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textOrImageX);
//                    dbc.setTextOrImageY(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textOrImageY);
//                    dbc.setTextOrImageWidth(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textOrImageWidth);
//                    dbc.setTextOrImageHeight(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textOrImageHeight);
//                    if(!TextUtils.isEmpty(allBookBeanInfo.modelPage.get(position).bookContent.get(i).rotationAngle)){
//                        dbc.setRotationAngle(allBookBeanInfo.modelPage.get(position).bookContent.get(i).rotationAngle);
//                    }
//                    if(!TextUtils.isEmpty(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textNumber)){
//                        dbc.setTextNumber(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textNumber);
//            }
//
//
//            String textOrImage = allBookBeanInfo.modelPage.get(position).bookContent.get(i).getIsTextOrImage();
//            //是image就+1存储到tag
//            if ("image".equals(textOrImage)) {
//                dbc.setTag(imgCount += 1);
//            }
//            //是text就+1存储到tag
//            if ("text".equals(textOrImage)) {
//                dbc.setTag(textCount += 1);
//            }
//            draftBookContentList.add(dbc);
//            draftBookContentDao.insert(dbc);
//        }
//        draftBookPage.listDraftBookContent = draftBookContentList;
//        //动态改变视图
//        //存进去在取出来
//        DraftBookPage beanlist = draftBookPageDao.selectorByBookIdAndPage(aimBookid , pageNumber);
//        beanlist.listDraftBookContent = draftBookContentDao.selectorByBookIdAndBookPage(aimBookid , pageNumber);
//        ((EditFragment)editPageList.get(viewPager.getCurrentItem())).notifyDataSetChange(beanlist);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_write_abook_iv_back:
                this.finish();
                break;
            default:

                break;
        }
    }

    /**
     * 菜单radiogroup监听
     */
    public class RadioButtonListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            //不能编辑的页面
            if(listCannotEditPages.contains(viewPager.getCurrentItem()+"")){
                tipsAlert.show();
                switch (checkedId){
                    case R.id.activity_write_abook_rb_background:

                        rb_background.setChecked(false);

                        break;
                    case R.id.activity_write_abook_rb_camera:
                        rb_camera.setChecked(false);
                        break;
                    case R.id.activity_write_abook_rb_sticker:
                        rb_sticker.setChecked(false);
                        break;
                    case R.id.activity_write_abook_rb_clean:
                        rb_clean.setChecked(false);
                        break;
                    default:
                        break;
                }
                return;
            }


            switch (checkedId){
                case R.id.activity_write_abook_rb_background:

                    rv_editordevelopbackground.setVisibility(View.VISIBLE);

                    break;
                case R.id.activity_write_abook_rb_camera:
                    rv_editordevelopbackground.setVisibility(View.GONE);
                    break;
                case R.id.activity_write_abook_rb_sticker:
                    rv_editordevelopbackground.setVisibility(View.GONE);
                    break;
                case R.id.activity_write_abook_rb_clean:
                    if (draftBookPageDao.selectorByBookIdAndPage(aimBookid, viewPager.getChildCount()) != null) {
                        draftBookPageDao.deleteByBookIdAndBookPage(aimBookid, viewPager.getChildCount());
                        draftBookContentDao.deleteByBookIdAndBookPage(aimBookid, viewPager.getChildCount());
                    }
                    Log.i("aim",aimBookid+"这个是本地区分书本的id");
//                    DraftBookPage beanlist = draftBookPageDao.selectorByBookIdAndPage(aimBookid , viewPager.getChildCount());
//                    beanlist.listDraftBookContent = draftBookContentDao.selectorByBookIdAndBookPage(aimBookid , viewPager.getChildCount());
                    ((EditFragment)editPageList.get(viewPager.getCurrentItem())).removeThisPage();
                    rv_editordevelopbackground.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

}
