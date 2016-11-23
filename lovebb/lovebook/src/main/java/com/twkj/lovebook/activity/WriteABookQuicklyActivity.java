package com.twkj.lovebook.activity;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.twkj.lovebook.R;
import com.twkj.lovebook.adapter.EditAdapter;
import com.twkj.lovebook.adapter.EditorDevelopBackgroundAdapter;
import com.twkj.lovebook.adapter.WriteABookModelAdapter;
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
import com.twkj.lovebook.selectimages.Constan;
import com.twkj.lovebook.selectimages.ImageDirActivity;
import com.twkj.lovebook.selectimages.Photo;
import com.twkj.lovebook.utils.BookInfoUtils;
import com.twkj.lovebook.utils.ControlBitmapUtils;
import com.twkj.lovebook.utils.UUIDFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动编辑
 * 写书的activity
 */
public class WriteABookQuicklyActivity extends FragmentActivity implements View.OnClickListener {

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

    //从数据库取出来 要操作的草稿书
    private DraftBook getDraftBook;

    private EditorDevelopBackgroundAdapter editorDevelopBackgroundAdapter;//编辑-扩展-背景选择adapter
    private RecyclerView rv_editordevelopbackground;//编辑-扩展-背景选择recyclerview
    private List<String> developbackgroundlistdata;

    private RadioGroup radioGroup_menu;//菜单

    private RadioButton rb_background , rb_camera , rb_sticker , rb_clean;//背景，相机 ， 贴纸，清除

    private AlertDialog tipsAlert;//提示alert
    private ProgressDialog waitDialog;//提示等待dialog
    private List<String> listCannotEditPages;

    public int getAimBookid(){
        return aimBookid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_write_abook_quickly);

        long start0 = System.currentTimeMillis();
        initView();
        createFragment();
        Log.i("WABQA" , "===================== initView(); +  createFragment();=================================="+(System.currentTimeMillis() - start0)+"ms");

        String EditorOrBookShelf = getIntent().getStringExtra("EditorOrBookShelf");

        if(TextUtils.equals(EditorOrBookShelf , "editor")){

            editsetData();
        }else if ("bookShelf".equals(EditorOrBookShelf)){
            bookShelfSetData();
        }

        getDraftBook();
        getData();

        editAdapter = new EditAdapter(getSupportFragmentManager(), editPageList);
        viewPager.setAdapter(editAdapter);
        //防止滑动出现白屏 ↓ 迷之方法
        viewPager.setOffscreenPageLimit(editPageList.size());
        Log.i("WABQA" , "===============viewPager over======================="+(System.currentTimeMillis() - start0)+"ms");
    }

    /**
     * 初始化界面
     */
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.activity_write_abook_quickly_iv_back);
        iv_back.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.activity_write_abook_quickly_viewpager);
        rv_model = (RecyclerView) findViewById(R.id.activity_write_abook_quickly_rv_model);
        rv_editordevelopbackground = (RecyclerView) findViewById(R.id.activity_write_abook_quickly_rv_background);
        radioGroup_menu = (RadioGroup) findViewById(R.id.activity_write_abook_quickly_radiogroup_menu);
        rb_background = (RadioButton) findViewById(R.id.activity_write_abook_quickly_rb_background);
        rb_camera = (RadioButton) findViewById(R.id.activity_write_abook_quickly_rb_camera);
        rb_sticker = (RadioButton) findViewById(R.id.activity_write_abook_quickly_rb_sticker);
        rb_clean = (RadioButton) findViewById(R.id.activity_write_abook_quickly_rb_clean);
        radioGroup_menu.setOnCheckedChangeListener(new RadioButtonListener());
        //三个dao
        draftBookDao = new DraftBookDao();
        draftBookPageDao = new DraftBookPageDao();
        draftBookContentDao = new DraftBookContentDao();


        //从plist取出样本书
        allBookBeanInfo = BookInfoUtils.getAllBookBean(this);
        //右侧 样板
        rv_model.setLayoutManager(new LinearLayoutManager(this));
        modelAdapter = new WriteABookModelAdapter(this , BookModelConfig.bookModelList);
        rv_model.setAdapter(modelAdapter);
        modelAdapter.setOnItemClickListener(new WriteABookModelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewPager.setCurrentItem(position);
            }
        });

        //不可编辑的bookpage集合
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
        waitDialog = new ProgressDialog(this);
        waitDialog.setMessage("稍等一下");
        waitDialog.setIndeterminate(true);
        waitDialog.setCancelable(false);

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

    /**
     * 创建所有fragment
     */
    private void createFragment() {

        editPageList = new ArrayList<>();
        ModelPageList = new ArrayList<ModelPageBean>();

        ModelPageList = allBookBeanInfo.modelPage;
        for (int i = 0; i < ModelPageList.size(); i++) {
            EditFragment fragment = new EditFragment();
            editPageList.add(fragment);
        }
    }

    /**
     * 从编辑-->自动编辑进入 设置数据
     */
    private void editsetData(){
        //添加一本draftbook 到数据库
        DraftBook putDraftBook = new DraftBook();
        aimBookid = draftBookDao.findmax_id() + 1;
        putDraftBook.setBookID(aimBookid);
        putDraftBook.setBookName(allBookBeanInfo.book.bookName);
        //editType;编辑类型（0是手动1是自动）
        putDraftBook.setEditType(1);
        //初始化从第0页灌版
        putDraftBook.setCurrentNum(0);
        putDraftBook.setBookPages(allBookBeanInfo.modelPage.size());
        draftBookDao.insert(putDraftBook);
        //添加所有draftpage 到数据库
        ArrayList<DraftBookPage> saveListDraftBookPage = new ArrayList<DraftBookPage>();
        ArrayList<DraftBookContent> saveListDraftBookContent = new ArrayList<DraftBookContent>();
        for(int j = 0 ; j < allBookBeanInfo.modelPage.size() ; j ++){
            DraftBookPage saveDraftBookPage = new DraftBookPage();
            saveDraftBookPage.setDraftBookID(aimBookid);
            saveDraftBookPage.setBookPage(j);
            saveDraftBookPage.setModelBackgroundPic(allBookBeanInfo.modelPage.get(j).whichPage.modelBackgroundPic);
            saveDraftBookPage.setWhichModel(allBookBeanInfo.modelPage.get(j).whichPage.whichModelPage);

            saveListDraftBookPage.add(saveDraftBookPage);
//            draftBookPageDao.insert(saveDraftBookPage);
            //添加每一页的所有content
            int imgCount = 0, textCount = 0;
            for (int h = 0 ; h < allBookBeanInfo.modelPage.get(j).bookContent.size() ; h++){
                DraftBookContent saveDraftBookContent = new DraftBookContent();
                saveDraftBookContent.setBookID(aimBookid);
                saveDraftBookContent.setBookPage(j);
                saveDraftBookContent.setContentName(allBookBeanInfo.modelPage.get(j).bookContent.get(h).contentName);
                saveDraftBookContent.setIsTextOrImage(allBookBeanInfo.modelPage.get(j).bookContent.get(h).isTextOrImage);
                saveDraftBookContent.setTextOrImageX(allBookBeanInfo.modelPage.get(j).bookContent.get(h).textOrImageX);
                saveDraftBookContent.setTextOrImageY(allBookBeanInfo.modelPage.get(j).bookContent.get(h).textOrImageY);
                saveDraftBookContent.setTextOrImageWidth(allBookBeanInfo.modelPage.get(j).bookContent.get(h).textOrImageWidth);
                saveDraftBookContent.setTextOrImageHeight(allBookBeanInfo.modelPage.get(j).bookContent.get(h).textOrImageHeight);
                if (!TextUtils.isEmpty(allBookBeanInfo.modelPage.get(j).bookContent.get(h).rotationAngle)){
                    saveDraftBookContent.setRotationAngle(allBookBeanInfo.modelPage.get(j).bookContent.get(h).rotationAngle);
                }
                if (!TextUtils.isEmpty(allBookBeanInfo.modelPage.get(j).bookContent.get(h).textNumber)){
                    saveDraftBookContent.setTextNumber(allBookBeanInfo.modelPage.get(j).bookContent.get(h).textNumber);
                }
                String textOrImage = allBookBeanInfo.modelPage.get(j).bookContent.get(h).isTextOrImage;
                //是image就+1存储到tag
                if ("image".equals(textOrImage)) {
                    saveDraftBookContent.setTag(imgCount += 1);
                }
                //是text就+1存储到tag
                if ("text".equals(textOrImage)) {
                    saveDraftBookContent.setTag(textCount += 1);
                }
//                draftBookContentDao.insert(saveDraftBookContent);
                saveListDraftBookContent.add(saveDraftBookContent);
            }
        }
        draftBookPageDao.insertList(saveListDraftBookPage);
        draftBookContentDao.insertList(saveListDraftBookContent);
        //添加封面等不能编辑的页面的缩略图
        for (int i = 0 ; i < allBookBeanInfo.book.whichPageCannotEdit.size() ; i++){
            int pagenum = Integer.valueOf(allBookBeanInfo.book.whichPageCannotEdit.get(i).pageNumber);
            if(draftBookPageDao.selectorByBookIdAndPage(aimBookid , pagenum) != null){
                DraftBookPage putCannotEditDraftBookPage = draftBookPageDao.selectorByBookIdAndPage(aimBookid , pagenum);
                //给不可编辑页面生成缩略图
                try {
                    File appDir = new File(Environment.getExternalStorageDirectory(), "BooheeLittle");
                    if (!appDir.exists()) {
                        appDir.mkdir();
                    }
                    File file = new File(appDir, UUIDFactory.getUUIDStr() + ".jpg");
                    FileOutputStream fos = new FileOutputStream(file);
                    Bitmap bit = ControlBitmapUtils.readBitMap(this , ControlBitmapUtils.getResId(allBookBeanInfo.modelPage.get(pagenum).whichPage.modelBackgroundPic , R.drawable.class));
                    bit.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    bit.recycle();
                    putCannotEditDraftBookPage.setLittleBookImage(file.getAbsolutePath());

                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //更新不可编辑页面到数据库
                draftBookPageDao.updatePage(putCannotEditDraftBookPage);
            }
        }
    }

    /**
     * 从书架 进入设置数据
     */
    private void bookShelfSetData(){
        aimBookid = getIntent().getIntExtra("aimBookid" , 0);
    }

    /**
     * 根据艾玛aimBookid 从数据库中取出draftbook
     */
    private void getDraftBook(){
        // 取出一本draftbook
        getDraftBook = new DraftBook();
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


    /**
     * 往fragment的集合里set数据
     */
    private void getData() {
        Bundle bundle = null;
        for (int i = 0; i < editPageList.size(); i++) {
            bundle = new Bundle();
            bundle.putSerializable("data", allDataList.listDraftBookPage.get(i));
            editPageList.get(i).setArguments(bundle);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_write_abook_quickly_iv_back:
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
            //不能编辑的页面 点到不能编辑的情况把所有radiobutton选中状态取消
            if(listCannotEditPages.contains(viewPager.getCurrentItem()+"")){
                tipsAlert.show();
                switch (checkedId){
                    case R.id.activity_write_abook_quickly_rb_background:

                        rb_background.setChecked(false);

                        break;
                    case R.id.activity_write_abook_quickly_rb_camera:
                        rb_camera.setChecked(false);
                        break;
                    case R.id.activity_write_abook_quickly_rb_sticker:
                        rb_sticker.setChecked(false);
                        break;
                    case R.id.activity_write_abook_quickly_rb_clean:
                        rb_clean.setChecked(false);
                        break;
                    default:
                        break;
                }
                return;
            }


            switch (checkedId){
                case R.id.activity_write_abook_quickly_rb_background:

                    rv_editordevelopbackground.setVisibility(View.VISIBLE);

                    break;
                case R.id.activity_write_abook_quickly_rb_camera:
                    rv_editordevelopbackground.setVisibility(View.GONE);
                    Intent it_imageDirActivity = new Intent(WriteABookQuicklyActivity.this , ImageDirActivity.class);
                    startActivityForResult(it_imageDirActivity , 1);
                    //跳转到别的页面 RadioButton还原了
                    rb_camera.setChecked(false);
                    break;
                case R.id.activity_write_abook_quickly_rb_sticker:
                    rv_editordevelopbackground.setVisibility(View.GONE);
                    break;
                case R.id.activity_write_abook_quickly_rb_clean:
                    rv_editordevelopbackground.setVisibility(View.GONE);
                    //从数据库清空这一页数据
                    draftBookPageDao.updataPageClean(getDraftBook.listDraftBookPage.get(viewPager.getCurrentItem()));
                    draftBookContentDao.updatePageCleanAllContentByBookIdAndBookPage(aimBookid , viewPager.getCurrentItem());
                    //ui上清空这一页
                    //动态改变视图
                    //存进去在取出来
                    DraftBookPage beanlist = draftBookPageDao.selectorByBookIdAndPage(aimBookid , viewPager.getCurrentItem());
                    beanlist.listDraftBookContent = draftBookContentDao.selectorByBookIdAndBookPage(aimBookid , viewPager.getCurrentItem());
                    ((EditFragment)editPageList.get(viewPager.getCurrentItem())).notifyDataSetChange(beanlist);
                    //清空之后 RadioButton还原了
                    rb_clean.setChecked(false);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(data != null){
                final ArrayList<Photo> list = data.getParcelableArrayListExtra(Constan.RES_PHOTO_LIST);

                int current = 0;
                long star = System.currentTimeMillis();
                waitDialog.show();
                //for一直循环 传到最后不用管了
                for(int i = viewPager.getCurrentItem() ; i < editAdapter.getCount() ; i++){
                    //第一次是个0传过去
                    current = editAdapter.quicklyUpdate(i , list ,current);
                    //current >= list.size() 说明图片放完了 跳出for循环
                    if(current >= list.size()){
                        //viewpager跳转到最后编辑到的viewpager
                        viewPager.setCurrentItem(i);
                        getDraftBook.setCurrentNum(i);
                        draftBookDao.updata(getDraftBook);
                        break;
                    }
                }
                waitDialog.dismiss();
                Log.i("WABQA" , "=================total time====================="+ (System.currentTimeMillis() - star) +"ms");
//                new AsyncTask<Integer , Integer , Integer>(){
//                    @Override
//                    protected void onPreExecute() {
//                        super.onPreExecute();
//                        waitDialog.show();
//                    }
//
//                    @Override
//                    protected Integer doInBackground(Integer... params) {
//                        int current = 0;
//                        ArrayList<Photo> list = data.getParcelableArrayListExtra(Constan.RES_PHOTO_LIST);
//                        //for一直循环 传到最后不用管了
//                        for(int i = params[0] ; i < editAdapter.getCount() ; i++){
//                            //第一次是个0传过去
//                            current = editAdapter.quicklyUpdate(i , list ,current);
//                            //current >= list.size() 说明图片放完了 跳出for循环
//                            if(current >= list.size()){
//                                //viewpager跳转到最后编辑到的viewpager
//                                //viewPager.setCurrentItem(i);
//                                //getDraftBook.setCurrentNum(i);
//                                //draftBookDao.updata(getDraftBook);
//                                return i;
//                                //break;
//                            }
//                        }
//
//                        return null;
//
//                    }
//
//                    @Override
//                    protected void onPostExecute(Integer integer) {
//                        super.onPostExecute(integer);
//                        viewPager.setCurrentItem(integer);
//                        getDraftBook.setCurrentNum(integer);
//                        draftBookDao.updata(getDraftBook);
//                        waitDialog.dismiss();
//                    }
//                };

            }
        }
    }

    /**
     * 跳转到对应viewpager
     * @param pos
     */
    public void jumpViewPager(int pos){
        viewPager.setCurrentItem(pos);
    }
}
