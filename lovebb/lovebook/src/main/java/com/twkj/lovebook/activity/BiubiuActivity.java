package com.twkj.lovebook.activity;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.tencent.upload.Const.FileType;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.ITask;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.DirCreateTask;
import com.tencent.upload.task.impl.FileUploadTask;
import com.twkj.lovebook.R;
import com.twkj.lovebook.adapter.EditAdapter;
import com.twkj.lovebook.adapter.EditorDevelopBackgroundAdapter;
import com.twkj.lovebook.application.MyApplication;
import com.twkj.lovebook.bean.AllBookBean;
import com.twkj.lovebook.bean.DraftBook;
import com.twkj.lovebook.bean.DraftBookContent;
import com.twkj.lovebook.bean.DraftBookPage;
import com.twkj.lovebook.bean.ModelPageBean;
import com.twkj.lovebook.constants.BuildConfig;
import com.twkj.lovebook.dao.DraftBookContentDao;
import com.twkj.lovebook.dao.DraftBookDao;
import com.twkj.lovebook.dao.DraftBookPageDao;
import com.twkj.lovebook.fragment.EditFragment;
import com.twkj.lovebook.utils.BookInfoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BiubiuActivity extends FragmentActivity implements AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener {

    //屏幕编辑的页面全屏的viewpager来实现左右滑动
    private ViewPager viewPager;
    private EditAdapter editAdapter;
    private List<Fragment> editPageList;
    //    private List<EditPageBean> dataList;
//    private List<List<EditPageBean>> allDataList;
    private DraftBook allDataList;
    //一整本书的对象
    private AllBookBean allBookBeanInfo;
    //书的所有页
    private List<ModelPageBean> ModelPageList;

    private ListView lv;

    private DraftBookPageDao draftBookPageDao;

    private DraftBookDao draftBookDao;

    private DraftBookContentDao draftBookContentDao;

    private MyApplication myApplication;
    private Socket socket;

    private UploadManager mFileUploadManager = null;

    private String bookid=null;
    private String onceSign=null;
    private String sign = null;
    private String appid = null;
    private String bucket = null;
    private String persistenceId = null;
    private String videoSaveName = null;
    private FileUploadTask fileUploadTask = null;

    private Button btn_up, btn_id, btn_justtry , btn_task;


    private EditorDevelopBackgroundAdapter editorDevelopBackgroundAdapter;//编辑-扩展-背景选择adapter
    private RecyclerView rv_editordevelopbackground;//编辑-扩展-背景选择recyclerview
    private List<String> developbackgroundlistdata;

    private RadioGroup radioGroup;//菜单

    private RadioButton rb_background , rb_camera , rb_sticker , rb_clean;//背景，相机 ， 贴纸，清除

    private AlertDialog tipsAlert;//提示alert
    private List<String> listCannotEditPages;


    EditFragment fragment;
    private Handler hd = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                    fragment.editPageView.setString("shading2");
//                    fragment.editPageView.setBackneed(true);
//                    fragment.getEditPageView().invalidate();
                    new Thread() {
                        @Override
                        public void run() {
                             getSign();
                        }
                    }.start();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biubiu);
        Log.i("BiuBiuactivity" , "==================onCreate====================");
        initView();
        new Thread() {
            @Override
            public void run() {
                linkSocket();
            }
        }.start();

        viewPager = (ViewPager) findViewById(R.id.activity_biubiu_viewpager_Edit);
        //三个dao
        draftBookDao = new DraftBookDao();
        draftBookPageDao = new DraftBookPageDao();
        draftBookContentDao = new DraftBookContentDao();

        // 1，创建一个上传容器 需要1.appid 2.上传文件类型3.上传缓存（类型字符串，要全局唯一否则） 进行目录操作之前需要先实例化 UploadManager 对象。
        // 上传需要到的参数

        appid = "10062537";
        bucket = "lovebook";
//        persistenceId = "filePersistenceId";


        mFileUploadManager = new UploadManager(this, appid, FileType.File,
                null);

        //从plist取出样本书
        allBookBeanInfo = BookInfoUtils.getAllBookBean(this);
        lv = (ListView) findViewById(R.id.activity_biubiu_lv);
        List<String> arrlist = new ArrayList<String>();
        for (int i = 0; i < allBookBeanInfo.modelPage.size(); i++) {
            arrlist.add(i + "");
        }
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrlist));
        lv.setOnItemClickListener(this);

        btn_task = (Button) findViewById(R.id.activity_biubiu_button_task);
        btn_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createbookidTask();
            }
        });


        btn_justtry = (Button) findViewById(R.id.activity_biubiu_button_justtry);
        btn_justtry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                justTry();
            }
        });
        btn_id = (Button) findViewById(R.id.activity_biubiu_button_id);
        btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getBookId();


            }
        });
        btn_up = (Button) findViewById(R.id.activity_biubiu_button_up);
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从数据库中取出所有的page
                List<DraftBookPage> listUpDeaftBookPage = new ArrayList<DraftBookPage>();
                listUpDeaftBookPage = draftBookPageDao.selectorByBookId(1);
                List<DraftBookContent> listUpDeaftBookContent;
                for (int i = 0; i < listUpDeaftBookPage.size(); i++) {
                    //将每一页上的所有红块块放到该页中DraftBookPage.listDraftBookContent 属性中去
                    listUpDeaftBookContent = new ArrayList<DraftBookContent>();
                    listUpDeaftBookContent = draftBookContentDao.selectorByBookIdAndBookPage(1, listUpDeaftBookPage.get(i).bookPage);
                    listUpDeaftBookPage.get(i).listDraftBookContent = listUpDeaftBookContent;

                }
                upLoadPageContent(listUpDeaftBookPage);

                //所有的bookpage
//                Log.i("Biubiua", "==============imageCosUrl===============" + listUpDeaftBookPage.get(2).listDraftBookContent.get(0).imageCosUrl);
            }
        });


        createFragment();
        setData();
        getData();

        editAdapter = new EditAdapter(getSupportFragmentManager(), editPageList);
        viewPager.setAdapter(editAdapter);
        //防止滑动出现白屏 ↓ 迷之方法
        viewPager.setOffscreenPageLimit(editPageList.size());
        viewPager.addOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    protected void onStart() {
        super.onStart();

    }



    /**
     * 更换viewpage
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void initView(){
        rv_editordevelopbackground = (RecyclerView) findViewById(R.id.activity_biubiu_rv_background);
        radioGroup = (RadioGroup) findViewById(R.id.activity_biubiu_radiogroup);
        rb_background = (RadioButton) findViewById(R.id.activity_biubiu_rb_background);
        rb_camera = (RadioButton) findViewById(R.id.activity_biubiu_rb_camera);
        rb_sticker = (RadioButton) findViewById(R.id.activity_biubiu_rb_sticker);
        rb_clean = (RadioButton) findViewById(R.id.activity_biubiu_rb_clean);
        radioGroup.setOnCheckedChangeListener(new RadioButtonListener());
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
        tipsAlert = new AlertDialog.Builder(this)

                .setTitle("提示")
                .setMessage("此页面不能进行其他操作")

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

    private void setData() {
        //添加一本draftbook
        DraftBook putDraftBook = new DraftBook();
        putDraftBook.setBookName("nmh");
        putDraftBook.setBookPages(allBookBeanInfo.modelPage.size());
        draftBookDao.insert(putDraftBook);
        //添加封面等不能编辑的页面 给王海涛个狗币都说不听 添添添添你吗个圆猪猪的卵子蛋
        for (int i = 0 ; i < allBookBeanInfo.book.whichPageCannotEdit.size() ; i++){
            int pagenum = Integer.valueOf(allBookBeanInfo.book.whichPageCannotEdit.get(i).pageNumber);
            if(draftBookPageDao.selectorByBookIdAndPage(1 , pagenum) == null){
                DraftBookPage putDraftBookPage = new DraftBookPage();
                putDraftBookPage.setDraftBookID(1);
                putDraftBookPage.setBookPage(pagenum);
                putDraftBookPage.setModelBackgroundPic(allBookBeanInfo.modelPage.get(pagenum).whichPage.modelBackgroundPic);
                draftBookPageDao.insert(putDraftBookPage);
            }

        }
        // 取出一本draftbook
        DraftBook getDraftBook = new DraftBook();
        getDraftBook = draftBookDao.selectById(1);

        listCannotEditPages = new ArrayList<String>();
        for (int i = 0 ; i < allBookBeanInfo.book.whichPageCannotEdit.size() ; i++){
            listCannotEditPages.add(allBookBeanInfo.book.whichPageCannotEdit.get(i).pageNumber);
        }
        //取出所有page
        List<DraftBookPage> draftBookPageList = new ArrayList<DraftBookPage>();

        draftBookPageList = draftBookPageDao.selectorByBookId(1);
        //初始化64页page
        getDraftBook.listDraftBookPage = new ArrayList<>();
        for (int i = 0; i < allBookBeanInfo.modelPage.size(); i++) {
            DraftBookPage dbp = new DraftBookPage();

            getDraftBook.listDraftBookPage.add(dbp);
        }
        //放入draftbook

        if (draftBookPageList != null) {
            Log.i("BiuBiuact", "====================draftBookPageList.size()===========" + draftBookPageList.size());
            for (int i = 0; i < draftBookPageList.size(); i++) {

                //取出所有content
                List<DraftBookContent> draftBookContentList = new ArrayList<DraftBookContent>();
                draftBookContentList = draftBookContentDao.selectorByBookIdAndBookPage(1, draftBookPageList.get(i).bookPage);
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
            bundle.putSerializable("data", (Serializable) allDataList.listDraftBookPage.get(i));

            editPageList.get(i).setArguments(bundle);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //不能编辑的页面
        if(listCannotEditPages.contains(viewPager.getCurrentItem()+"")) {
            tipsAlert.show();
            return;
        }
        //不能操作的item
        if(listCannotEditPages.contains(position)) {
            tipsAlert.show();
            return;
        }

        //第几页
        int pageNumber = viewPager.getCurrentItem();

        //点击模板时 如果这个bookpage存在 删除这个bookpage 还有这个bookpage上的content
        if (draftBookPageDao.selectorByBookIdAndPage(1, pageNumber) != null) {
            draftBookPageDao.deleteByBookIdAndBookPage(1, pageNumber);
            draftBookContentDao.deleteByBookIdAndBookPage(1, pageNumber);
        }
        //点击模板时 bookpage添加一条数据
        DraftBookPage draftBookPage = new DraftBookPage();
        draftBookPage.setDraftBookID(1);
        draftBookPage.setBookPage(pageNumber);
        draftBookPage.setWhichModel(allBookBeanInfo.modelPage.get(position).whichPage.whichModelPage);
        draftBookPage.setModelBackgroundPic(allBookBeanInfo.modelPage.get(position).whichPage.modelBackgroundPic);
        draftBookPageDao.insert(draftBookPage);
        //同时添加该页的所有content
        //set 每个content的tag，set时区分img和text是第几个
        List<DraftBookContent> draftBookContentList = new ArrayList<DraftBookContent>();
        int imgCount = 0, textCount = 0;
        for (int i = 0; i < allBookBeanInfo.modelPage.get(position).bookContent.size(); i++) {
            DraftBookContent dbc = new DraftBookContent();
            dbc.setBookID(1);
            dbc.setBookPage(pageNumber);
            dbc.setContentName(allBookBeanInfo.modelPage.get(position).bookContent.get(i).contentName);
            dbc.setIsTextOrImage(allBookBeanInfo.modelPage.get(position).bookContent.get(i).isTextOrImage);
            dbc.setTextOrImageX(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textOrImageX);
            dbc.setTextOrImageY(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textOrImageY);
            dbc.setTextOrImageWidth(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textOrImageWidth);
            dbc.setTextOrImageHeight(allBookBeanInfo.modelPage.get(position).bookContent.get(i).textOrImageHeight);
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
        ((EditFragment)editPageList.get(viewPager.getCurrentItem())).notifyDataSetChange(draftBookPage);

    }

    private void linkSocket() {
        try {
            myApplication = (MyApplication) getApplication();
            myApplication.setSocket(new Socket(BuildConfig.SOCKET_BINDING, BuildConfig.SOCKET_PORT_SHORT));
            socket = myApplication.getSocket();

            JsonObject j1 = new JsonObject();
            j1.addProperty("account", "13335382871");
            j1.addProperty("pwd", "e10adc3949ba59abbe56e057f20f883e");
            j1.addProperty("appid", "2");

            JsonObject j2 = new JsonObject();
            j2.addProperty("method", "login");
            j2.add("parameter", j1);

            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
//            Log.d("bbb","连上没有："+json.toString());

            // 向外发送
            out.println(j2);
            // 通过输入流，获取返回的信息
            BufferedReader recv = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String recvMsg = recv.readLine();
//            while ((recvMsg = recv.readLine()) != null) {
            Log.d("bbb", "返回的信息：" + recvMsg);
            JSONObject j3 = new JSONObject(recvMsg);
            myApplication.setSession(j3.getJSONObject("result").getString("sessionid"));
            myApplication.setUserid(j3.getJSONObject("result").getString("userid"));
            //通知去取信息
            hd.sendEmptyMessage(0);

//            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getSign() {
        Log.d("bbb", "走到getsign了么");
        String result = null;
        JSONObject jsonResult = null;
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            JsonObject jsonobject = new JsonObject();//创建一个总的对象，这个对象对整个json串
            jsonobject.addProperty("appid", "2");
            jsonobject.addProperty("sessionid", myApplication.getSession());
            jsonobject.addProperty("userid", myApplication.getUserid());

            JsonObject js = new JsonObject();
            js.addProperty("method", "sign");
            js.add("parameter", jsonobject);

            out.println(js);

            Log.d("bbb", "发送请求sign 的数据了");
            BufferedReader recv = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            result = recv.readLine();
//            while ((result = recv.readLine()) != null) {
            Log.d("bbb", "我的signsign：" + result);
            jsonResult = new JSONObject(result);
            jsonResult = jsonResult.getJSONObject("result");
            result = jsonResult.getString("cos");

            sign = result;

//            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传前还要你麻痹的创建腾讯云尼玛的卵子蛋路径
     * bookid 的路径 真尼玛逼难伺候
     */
    private void createbookidTask(){
        DirCreateTask filetask = null;

        String taskLoveBook = "/" + "LoveBook";
        String taskUserid = "/" + "1";
        filetask = new DirCreateTask(FileType.File, bucket, taskLoveBook + taskUserid + "/" + bookid + "/PAGE", null, new DirCreateTask.IListener() {
            @Override
            public void onSuccess(final DirCreateTask.CmdTaskRsp result) {
                // 创建成功
                Log.i("bb" , "==============createUseridTask==onSuccess=============="+result.path);
            }
            @Override
            public void onFailure(final int ret, final String msg) {
                // 创建失败
                Log.i("bb" , "==============createUseridTask==onFailure=============="+"ret="+ret+" msg="+msg);
            }
        });

        filetask.setAuth(sign);
        mFileUploadManager.sendCommand(filetask);
    }



    /**
     *
     */
    private void createAllPage(String count){

        DirCreateTask filetask = null;

        String taskLoveBook = "/" + "LoveBook";
        String taskUserid = "/" + "1";
        filetask = new DirCreateTask(FileType.File, bucket, taskLoveBook + taskUserid + "/" + bookid, null, new DirCreateTask.IListener() {
            @Override
            public void onSuccess(final DirCreateTask.CmdTaskRsp result) {
                // 创建成功
                Log.i("bb" , "==============createUseridTask==onSuccess=============="+result.path);
            }
            @Override
            public void onFailure(final int ret, final String msg) {
                // 创建失败
                Log.i("bb" , "==============createUseridTask==onFailure=============="+"ret="+ret+" msg="+msg);
            }
        });

        filetask.setAuth(sign);
        mFileUploadManager.sendCommand(filetask);
    }

    /**
     * 上传所有图片到腾讯云
     */
    private void justTry() {

        List<DraftBookContent> listAllImageContent = new ArrayList<>();

        listAllImageContent = draftBookContentDao.selectorImgByBookIdAndBookPage(1 , 1);
        Log.i("Biu", "============justTry=listAllImageContent.size()============" + listAllImageContent.size());
        for (int i = 0; i < listAllImageContent.size(); i++) {
            uploadTry(listAllImageContent.get(i));

        }
    }

    private void uploadTry(final DraftBookContent draftBookContent) {

        if (TextUtils.isEmpty(draftBookContent.getImageMarkName())) {
            Log.i("bb", "===========不需要上传================");
            return;
        }
        File uploadFile = new File(draftBookContent.getImageMarkName());

        if (uploadFile == null || !uploadFile.exists()) {
            Log.i("bb", "===========文件不存在================");
            return;
        }
        Log.i("bb", "===========文件存在================");
        videoSaveName = draftBookContent.getIsTextOrImage() + "_"+draftBookContent.tag + ".jpg";
        String taskLoveBook = "/LoveBook";
        String taskUserid = "/1";
        String taskBookid = "/"+bookid;
        String taskPAGE = "/PAGE";
        int countPAGE = draftBookContent.bookPage + 1;
        String taskPAGECount = "/PAGE_"+ countPAGE;
        String destPath = taskLoveBook + taskUserid + taskBookid + taskPAGE + taskPAGECount + "/" + videoSaveName;

        fileUploadTask = new FileUploadTask(bucket, uploadFile.getAbsolutePath(), destPath, "", true, new IUploadTaskListener() {
            @Override
            public void onUploadSucceed(FileInfo fileInfo) {
                Log.i("bb", "=============justTry=onUploadSucceed=fileInfo.url=============================" + fileInfo.url);
                draftBookContent.setImageCosUrl(fileInfo.url);
                DraftBookContentDao dddd = new DraftBookContentDao();
                dddd.update(draftBookContent);
            }

            @Override
            public void onUploadFailed(int i, String s) {
                Log.i("bb", "=============justTry.onUploadFailed()================================================================" + "上传结果:失败! ret:" + i + " msg:" + s);

            }

            @Override
            public void onUploadProgress(long l, long l1) {

            }

            @Override
            public void onUploadStateChange(ITask.TaskState taskState) {

            }
        });
        //上传任务开始
        fileUploadTask.setAuth(sign);
//        Log.i("bbb", "singnnnnnnnnn" + sign);
        mFileUploadManager.upload(fileUploadTask); // 开始上传


    }


    /**
     * 上传文件
     */
    private void doUploadFile(String destPath, boolean to_over_write) {


        File uploadFile = new File("/storage/emulated/0/099.jpg");
        if (uploadFile == null || !uploadFile.exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        //为了测试覆盖上传
        videoSaveName = uploadFile.getName();
        if (destPath == null) destPath = "/" + videoSaveName;
        fileUploadTask = new FileUploadTask(bucket, uploadFile.getAbsolutePath(), destPath, "", to_over_write, new IUploadTaskListener() {
            @Override
            public void onUploadSucceed(FileInfo fileInfo) {
                Log.i("BiubiuActivity", "===============onUploadSucceed================");
                Log.i("BiubiuActivity", "===============fileInfo================" + fileInfo.toString());
                Log.i("BiubiuActivity", "===============fileInfo.url================" + fileInfo.url);


            }

            @Override
            public void onUploadFailed(int i, String s) {
                Log.i("BiubiuActivity", "===============onUploadFailed================");
            }

            @Override
            public void onUploadProgress(long l, long l1) {

            }

            @Override
            public void onUploadStateChange(ITask.TaskState taskState) {

            }
        });
        //上传任务开始
        fileUploadTask.setAuth(sign);
        mFileUploadManager.upload(fileUploadTask); // 开始上传

    }

    public void getBookId() {
        Log.d("bbb", "走到获取bookid的方法了");
        new Thread() {
            @Override
            public void run() {
                String result = null;
                JSONObject jsonResult = null;
                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),
                            true);
                    JsonObject jsonobject = new JsonObject();//创建一个总的对象，这个对象对整个json串
                    jsonobject.addProperty("appid", "2");
                    jsonobject.addProperty("sessionid", myApplication.getSession());
                    jsonobject.addProperty("userid", myApplication.getUserid());
                    jsonobject.addProperty("title", "andorid test");
                    jsonobject.addProperty("moduleid", "book1");
                    jsonobject.addProperty("pagenum", "64");


                    JsonObject js = new JsonObject();
                    js.addProperty("method", "saveBook");
                    js.add("parameter", jsonobject);

                    out.println(js);

                    BufferedReader recv = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

                    result = recv.readLine();
//            while ((result = recv.readLine()) != null) {
                    Log.d("bbb", "boooooooooooooooooookid：" + result);
                    jsonResult = new JSONObject(result);
                    jsonResult = jsonResult.getJSONObject("result");
                    result = jsonResult.getString("bookid");
                    bookid = result;
//            }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void getSocketMsg() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    BufferedReader recv = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    String recvMsg = null;
                    while ((recvMsg = recv.readLine()) != null) {
                        Log.d("bbb", "返回的信息：" + recvMsg);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void upLoadPageContent(final List<DraftBookPage> listUpDeaftBookPage) {

        new Thread() {
            @Override
            public void run() {
                super.run();

                JsonObject js = null;
                //pagecontent是个小红块的jsonarray
                for (int i = 0; i < listUpDeaftBookPage.size(); i++) {
                    JsonObject content = new JsonObject();
                    Log.d("bbb","");
                    //这个for循环是把一页里面的view块 转成json
                    for (int j = 0; j < listUpDeaftBookPage.get(i).listDraftBookContent.size(); j++) {
                        if ("text".equals(listUpDeaftBookPage.get(i).listDraftBookContent.get(j).getIsTextOrImage())) {
                            //各单位注意了                            这里↓ 1的话变成01 2的话变成02 这不你麻痹瞎折腾 神经病
                            content.addProperty("text" + String.format("%02d",listUpDeaftBookPage.get(i).listDraftBookContent.get(j).tag), listUpDeaftBookPage.get(i).listDraftBookContent.get(j).textString);

                        } else {
                            content.addProperty("img" + listUpDeaftBookPage.get(i).listDraftBookContent.get(j).tag, listUpDeaftBookPage.get(i).listDraftBookContent.get(j).imageCosUrl);
                        }
                    }


                    js = new JsonObject();
                    js.addProperty("appid", "2");
                    js.addProperty("sessionid", myApplication.getSession());
                    js.addProperty("userid", myApplication.getUserid());
                    js.addProperty("bookid", bookid);//书的id
                    js.addProperty("componentid", "2");//表明正文部分
                    js.addProperty("moduleid", listUpDeaftBookPage.get(i).whichModel);//用的是那个模板类型
                    js.addProperty("pagenum", listUpDeaftBookPage.get(i).bookPage);//这是第几页
                    js.add("pagecontent", content);
                    js.addProperty("previewurl", "");

                    JsonObject jsonAll = new JsonObject();
                    jsonAll.addProperty("method", "savePage");
                    jsonAll.add("parameter", js);


                    PrintWriter out = null;
                    try {
                        out = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())),
                                true);
                        out.println(jsonAll);

                        Log.d("bbb", "上传的我拼的json：" + jsonAll);
                        BufferedReader recv = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                        String result;
                        result = recv.readLine();
//            while ((result = recv.readLine()) != null) {
                        Log.d("bbb", "上传书页的内容了：" + result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
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
                    case R.id.activity_biubiu_rb_background:

                        rb_background.setChecked(false);

                        break;
                    case R.id.activity_biubiu_rb_camera:
                        rb_camera.setChecked(false);
                        break;
                    case R.id.activity_biubiu_rb_sticker:
                       rb_sticker.setChecked(false);
                        break;
                    case R.id.activity_biubiu_rb_clean:
                        rb_clean.setChecked(false);
                        break;
                    default:
                        break;
                }
                return;
            }


            switch (checkedId){
                case R.id.activity_biubiu_rb_background:

                    rv_editordevelopbackground.setVisibility(View.VISIBLE);

                    break;
                case R.id.activity_biubiu_rb_camera:
                    rv_editordevelopbackground.setVisibility(View.GONE);
                    break;
                case R.id.activity_biubiu_rb_sticker:
                    rv_editordevelopbackground.setVisibility(View.GONE);
                    break;
                case R.id.activity_biubiu_rb_clean:
                    rv_editordevelopbackground.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }
}
