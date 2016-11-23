package com.twkj.lovebook.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.tencent.upload.Const;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.ITask;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.FileUploadTask;
import com.twkj.lovebook.R;
import com.twkj.lovebook.activity.HomeActivity;
import com.twkj.lovebook.activity.ShoppingCartActivity;
import com.twkj.lovebook.activity.WriteABookActivity;
import com.twkj.lovebook.activity.WriteABookQuicklyActivity;
import com.twkj.lovebook.adapter.BookshelfDraftBookAdapter;
import com.twkj.lovebook.adapter.BookshelfUpBookAdapter;
import com.twkj.lovebook.application.MyApplication;
import com.twkj.lovebook.bean.DraftBook;
import com.twkj.lovebook.bean.DraftBookContent;
import com.twkj.lovebook.bean.DraftBookPage;
import com.twkj.lovebook.constants.BuildConfig;
import com.twkj.lovebook.dao.DraftBookContentDao;
import com.twkj.lovebook.dao.DraftBookDao;
import com.twkj.lovebook.dao.DraftBookPageDao;
import com.twkj.lovebook.manager.FullyGridLayoutManager;
import com.twkj.lovebook.view.XScrollView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class BookshelfFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, XScrollView.IXScrollViewListener {

    private ImageView iv_shopping_cart;

    private TextView tv_bookid , tv_zouni , tv_status1;
    private List<DraftBook> draftBookList;

    private UploadManager mFileUploadManager;
    private MyApplication myApplication;
    private Socket socket;

    private DraftBookDao draftBookDao;
    private DraftBookPageDao draftBookPageDao;
    private DraftBookContentDao draftBookContentDao;

    private List<DraftBookPage> needUpBookPage;

    private int needPageCount = 0;
    //上传前请求到的bookID
    private String bookid=null;
    //腾讯云签名
    private String sign = null;

    private FileUploadTask fileUploadTask = null;

    private ProgressDialog waitingPageUpDialog;

    private XScrollView xsv;

    private View content;//xsv 填充的布局

    private RecyclerView rv_upbook , rv_drafbook;

    private BookshelfUpBookAdapter upBookAdapter;
    private BookshelfDraftBookAdapter draftBookAdapter;

    //查看更多的草稿书 更多的上传书
    private LinearLayout ll_draft_book_more , ll_up_book_more;

    //草稿书删除图片
    private ImageView iv_draft_book_delete;

    private boolean flag_draft_delete;

    private Handler hd = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    new Thread() {
                        @Override
                        public void run() {
                            getSign();
                        }
                    }.start();
                    break;
                case 1001:
                    waitingPageUpDialog.setMessage(needPageCount + "/" + needUpBookPage.size());
                    break;
                case 1002:
                    waitingPageUpDialog.dismiss();
                    break;

            }
        }
    };

    public BookshelfFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookshelf, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        readyupload();
    }

    public void initView(){
        draftBookDao = new DraftBookDao();
        draftBookPageDao = new DraftBookPageDao();
        draftBookContentDao = new DraftBookContentDao();

        iv_shopping_cart = (ImageView) getView().findViewById(R.id.fragment_bookshelf_iv_shopping_cart);
        iv_shopping_cart.setOnClickListener(this);

        tv_bookid = (TextView) getView().findViewById(R.id.fragment_bookshelf_tv_bookid);
        tv_zouni = (TextView) getView().findViewById(R.id.fragment_bookshelf_tv_zouni);
        tv_status1 = (TextView) getView().findViewById(R.id.fragment_bookshelf_tv_status1);

        xsv = (XScrollView) getView().findViewById(R.id.fragment_bookshelf_xsv);
        if(content == null){

            content = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bookshelf_content_xsv , null);
            xsv.setView(content);
        }

        ll_draft_book_more = (LinearLayout) content.findViewById(R.id.fragment_bookshelf_content_xsv_ll_draftBookMore);
        ll_up_book_more = (LinearLayout) content.findViewById(R.id.fragment_bookshelf_content_xsv_ll_upBookMore);

        iv_draft_book_delete = (ImageView) content.findViewById(R.id.fragment_bookshelf_content_xsv_iv_draft_delete);
        waitingPageUpDialog = new ProgressDialog(this.getContext());
        waitingPageUpDialog.setTitle("上传书页...");
        waitingPageUpDialog.setMessage("");
        waitingPageUpDialog.setIndeterminate(true);
        waitingPageUpDialog.setCancelable(false);

        draftBookList = new ArrayList<DraftBook>();
        draftBookList = draftBookDao.selectAll();
        if (draftBookList != null && draftBookList.size() > 0){


            tv_bookid.setOnClickListener(this);
            tv_zouni.setOnClickListener(this);
            tv_status1.setOnClickListener(this);
            ll_up_book_more.setOnClickListener(this);
            ll_draft_book_more.setOnClickListener(this);
            iv_draft_book_delete.setOnClickListener(this);
            if(draftBookList.size() > 3){
                ll_draft_book_more.setVisibility(View.VISIBLE);
            }else{
                ll_draft_book_more.setVisibility(View.GONE);
            }
        }

        rv_upbook = (RecyclerView) content.findViewById(R.id.fragment_bookshelf_content_xsv_rv_upBook);
        rv_upbook.setLayoutManager(new FullyGridLayoutManager(getContext() , 3));
        List<String> upBookList = new ArrayList<String>();
        for(int i = 0 ; i < 3 ; i++){
            upBookList.add(i+"");
        }
        upBookAdapter = new BookshelfUpBookAdapter(getContext() , upBookList);
        rv_upbook.setAdapter(upBookAdapter);
        rv_upbook.setNestedScrollingEnabled(false);


        rv_drafbook = (RecyclerView)content.findViewById(R.id.fragment_bookshelf_content_xsv_rv_draftBook);
        rv_drafbook.setLayoutManager(new FullyGridLayoutManager(getContext() , 4));
        draftBookAdapter = new BookshelfDraftBookAdapter(getContext() , draftBookList , this);
        rv_drafbook.setAdapter(draftBookAdapter);
        rv_drafbook.setNestedScrollingEnabled(false);

        xsv.setPullRefreshEnable(true);
        xsv.setPullLoadEnable(false);
        xsv.setAutoLoadEnable(false);
        xsv.setIXScrollViewListener(this);
        xsv.setRefreshTime(getTime());

    }

    /**
     * 当前时间
     * @return
     */
    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    /**
     * 上传前的准备
     */
    private void readyupload(){
        // 1，创建一个上传容器 需要1.appid 2.上传文件类型3.上传缓存（类型字符串，要全局唯一否则） 进行目录操作之前需要先实例化 UploadManager 对象。
        // 上传需要到的参数
        mFileUploadManager = new UploadManager(this.getContext() , BuildConfig.TENCENT_UPLOAD_APPID , Const.FileType.File , null);

        new Thread() {
            @Override
            public void run() {
                linkSocket();
            }
        }.start();

    }

    private void linkSocket() {
        try {
            myApplication = (MyApplication) getActivity().getApplication();
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
            BufferedReader recv = new BufferedReader( new InputStreamReader(socket.getInputStream()));
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


            BufferedReader recv = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            result = recv.readLine();
//            while ((result = recv.readLine()) != null) {
            Log.i("bbb", "==================我的signsign：=========" + result);
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
     * 妈的比 烦死了 报错
     */
    private void getBookByUserId(){
//        String result = null;
//        JSONObject jsonResult = null;
//        try {
//            PrintWriter out = new PrintWriter(new BufferedWriter(
//                    new OutputStreamWriter(socket.getOutputStream())),
//                    true);
//            JsonObject jsonobject = new JsonObject();
//            jsonobject.addProperty("offset", "4");
//            jsonobject.addProperty("limmit" , "4");
//            jsonobject.addProperty("sessionid", myApplication.getSession());
//            jsonobject.addProperty("userid", myApplication.getUserid());
//
//            JsonObject js = new JsonObject();
//            js.addProperty("method", "getBookByUserid");
//            js.add("parameter", jsonobject);
//
//            out.println(js);
//
//
//            BufferedReader recv = new BufferedReader(
//                    new InputStreamReader(socket.getInputStream()));
//            result = recv.readLine();
//
//            Log.i("BF", "============getBookByUserId==result=============" + result);
//            jsonResult = new JSONObject(result);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 获取bookID
     */
    public void getBookId() {

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
                    jsonobject.addProperty("moduleid", "bookpc");
                    jsonobject.addProperty("pagenum", "66");


                    JsonObject js = new JsonObject();
                    js.addProperty("method", "saveBook");
                    js.add("parameter", jsonobject);

                    out.println(js);

                    BufferedReader recv = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

                    result = recv.readLine();
//            while ((result = recv.readLine()) != null) {
                    Log.i("bbb", "========================boooooooooooooooooookid：=============" + result);
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

    /**
     * 上传封面封底到腾讯云
     * coverOrEnd = cover
     * coverOrEnd = endPage
     * @param coverOrEnd
     */
    private void upEndPageToTencent(final String coverOrEnd){
        DraftBookContent draftBookContent = draftBookContentDao.selectEndPageByBookId(1);
        //如果找到第一张图片
        if(draftBookContent != null && !TextUtils.isEmpty(draftBookContent.imageMarkName)){
            File uploadFile = new File(draftBookContent.imageMarkName);
            String videoSaveName = coverOrEnd + ".jpg";
            String taskLoveBook = "/LoveBook";
            String taskUserid = "/1";
            String taskBookid = "/" + bookid;
            String taskPREVIEW= "/COVER";
            String destPath = taskLoveBook + taskUserid + taskBookid + taskPREVIEW + "/" + videoSaveName;
            fileUploadTask = new FileUploadTask(BuildConfig.TENCENT_UPLOAD_BUCKET, uploadFile.getAbsolutePath(), destPath, "", true, new IUploadTaskListener() {
                @Override
                public void onUploadSucceed(FileInfo fileInfo) {
                    Log.i("bb", "=============onUploadSucceed==coverOrEnd============================================================="+coverOrEnd);


                    DraftBook draftBook = draftBookDao.selectById(1);
                    draftBook.setEndPage(fileInfo.url);
                    draftBookDao.updata(draftBook);
                    shangchuanfuwuqifengdifengmian(coverOrEnd);

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
            mFileUploadManager.upload(fileUploadTask); // 开始上传
        }else{
            //如果没找到第一张图片
            shangchuanfuwuqifengdifengmian(coverOrEnd);
        }
    }

    /**
     * 上传封面封底到服务器
     * coverOrEnd = cover
     * coverOrEnd = endPage
     * @param coverOrEnd
     */
    private void shangchuanfuwuqifengdifengmian(final String coverOrEnd){
        final DraftBook draftBook = draftBookDao.selectById(1);

        new Thread() {
            @Override
            public void run() {
                super.run();
                JsonObject js = null;


                js = new JsonObject();
                js.addProperty("bookid", bookid);//书的id
                if("cover".equals(coverOrEnd)){
                    js.addProperty("componentid", "0");//0封面，1封底，2正文必须
                }else if ("endPage".equals(coverOrEnd)){
                    js.addProperty("componentid", "1");//0封面，1封底，2正文必须
                }
                js.addProperty("moduleid", "");
                js.addProperty("appid", "2");
                js.addProperty("sessionid", myApplication.getSession());
                js.addProperty("userid", myApplication.getUserid());




                if(!TextUtils.isEmpty(draftBook.endPage)){
                    js.addProperty("previewurl", draftBook.endPage);
                }else{
                    js.addProperty("previewurl", "");
                }


                JsonObject jsonAll = new JsonObject();
                jsonAll.addProperty("method", "savePage");
                jsonAll.add("parameter", js);


                PrintWriter out = null;
                try {
                    out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),
                            true);
                    out.println(jsonAll);

                    Log.i("bbb", "=======================上传的我拼的json：====================" + jsonAll);
                    BufferedReader recv = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    String result;
                    result = recv.readLine();
//            while ((result = recv.readLine()) != null) {
                    Log.i("bbb", "===================上传书页的内容了：==========" + result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 上传缩略图到腾讯云
     */
    private void upLittleImageToTencent(List<DraftBookPage> needUpBookPage){
        for(int i = 0 ; i < needUpBookPage.size() ; i++){
            upPageLittleImage(needUpBookPage.get(i));
        }
    }

    private void upPageLittleImage(final DraftBookPage draftBookPage){
        Log.i("123123","哪一个没有:"+ draftBookPage.bookPage);
        File uploadFile = new File(draftBookPage.littleBookImage);

        String videoSaveName = "previewImage" + "_" + draftBookPage.bookPage + ".jpg";
        String taskLoveBook = "/LoveBook";
        String taskUserid = "/1";
        String taskBookid = "/" + bookid;
        String taskPREVIEW= "/PAGE/PREVIEW";
        String destPath = taskLoveBook + taskUserid + taskBookid + taskPREVIEW + "/" + videoSaveName;
        fileUploadTask = new FileUploadTask(BuildConfig.TENCENT_UPLOAD_BUCKET, uploadFile.getAbsolutePath(), destPath, "", true, new IUploadTaskListener() {
            @Override
            public void onUploadSucceed(FileInfo fileInfo) {
                Log.i("bb", "=============upPageLittleImage=onUploadSucceed=fileInfo.url=============================" + fileInfo.url);
                draftBookPage.setLittleImageCosUrl(fileInfo.url);

                draftBookPageDao.updatePage(draftBookPage);
                upPage(draftBookPage);


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
        mFileUploadManager.upload(fileUploadTask); // 开始上传
    }

    /**
     * 上传图片到腾讯云
     */
    private void upImageToTencent(List<DraftBookPage> needUpBookPage) {


        for(int i = 0 ; i < needUpBookPage.size() ; i++){
            upPage(needUpBookPage.get(i));
        }



    }

    private void upPage(DraftBookPage draftBookPage){



        List<DraftBookContent> listOnePageImageContent = new ArrayList<>();
        listOnePageImageContent = draftBookContentDao.selectorImgByBookIdAndBookPage(1 , draftBookPage.bookPage);
        if (listOnePageImageContent.size() == 0 ){
            shangchufuwuqiyiye(draftBookPage);
        }else{
            //数据库查出来的图片不为0
            upPageImage(draftBookPage ,listOnePageImageContent);
        }



    }


    private int currentCount;

    private void upPageImage(final DraftBookPage draftBookPage , List<DraftBookContent> listOnePageImageCount) {
        currentCount = 0;
        final int bookImageCount = listOnePageImageCount.size();
        for (int i = 0; i < bookImageCount; i++) {
            final DraftBookContent finalDraftBookContent = listOnePageImageCount.get(i);
            File uploadFile = new File(finalDraftBookContent.getImageMarkName());
            String videoSaveName = finalDraftBookContent.getIsTextOrImage() + "_" + finalDraftBookContent.tag + ".jpg";
            String taskLoveBook = "/LoveBook";
            String taskUserid = "/1";
            String taskBookid = "/" + bookid;
            String taskPAGE = "/PAGE";
            int countPAGE = finalDraftBookContent.bookPage + 1;
            String taskPAGECount = "/PAGE_" + countPAGE;
            String destPath = taskLoveBook + taskUserid + taskBookid + taskPAGE + taskPAGECount + "/" + videoSaveName;

            fileUploadTask = new FileUploadTask(BuildConfig.TENCENT_UPLOAD_BUCKET, uploadFile.getAbsolutePath(), destPath, "", true, new IUploadTaskListener() {
                @Override
                public void onUploadSucceed(FileInfo fileInfo) {
                    Log.i("bb", "=============justTry=onUploadSucceed=fileInfo.url=============================" + fileInfo.url);
                    finalDraftBookContent.setImageCosUrl(fileInfo.url);
                    DraftBookContentDao dddd = new DraftBookContentDao();
                    dddd.update(finalDraftBookContent);
                    currentCount++;
                    if (currentCount == bookImageCount) {
                        //上传服务器
                        Log.i("bbb" , "===================currentCount == bookImageCount====================" + (currentCount == bookImageCount));
                        shangchufuwuqiyiye(draftBookPage);
                        currentCount = 0;
                    }

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
            mFileUploadManager.upload(fileUploadTask); // 开始上传
        }
    }

    private void shangchufuwuqiyiye(final DraftBookPage draftBookPage){
        //将一页上的所有红块块放到该页中DraftBookPage.listDraftBookContent 属性中去
        List<DraftBookContent> listUpDeaftBookContent = new ArrayList<DraftBookContent>();
        listUpDeaftBookContent = draftBookContentDao.selectorByBookIdAndBookPage(1, draftBookPage.bookPage);
        draftBookPage.listDraftBookContent = listUpDeaftBookContent;

        new Thread() {
            @Override
            public void run() {
                super.run();
        JsonObject js = null;
        JsonObject content = new JsonObject();

        //这个for循环是把一页里面的view块 转成json
        for (int j = 0; j < draftBookPage.listDraftBookContent.size(); j++) {
            if ("text".equals(draftBookPage.listDraftBookContent.get(j).getIsTextOrImage())) {
                //各单位注意了                            这里↓ 1的话变成01 2的话变成02 这不你麻痹瞎折腾 神经病
                content.addProperty("text" + String.format("%02d",draftBookPage.listDraftBookContent.get(j).tag), draftBookPage.listDraftBookContent.get(j).textString);

            } else {
                content.addProperty("img" + draftBookPage.listDraftBookContent.get(j).tag, draftBookPage.listDraftBookContent.get(j).imageCosUrl);
            }
        }
                content.addProperty("bg_left" , draftBookPage.leftBackImage);//左背景
                content.addProperty("bg_right" , draftBookPage.rightBackImage);//右背景



        js = new JsonObject();
        js.addProperty("appid", "2");
        js.addProperty("sessionid", myApplication.getSession());
        js.addProperty("userid", myApplication.getUserid());
        js.addProperty("bookid", bookid);//书的id
        js.addProperty("componentid", "2");//表明正文部分
        js.addProperty("moduleid", draftBookPage.whichModel);//用的是那个模板类型
        js.addProperty("pagenum", draftBookPage.bookPage);//这是第几页
        js.add("pagecontent", content);
        js.addProperty("previewurl", draftBookPage.littleImageCosUrl);

        JsonObject jsonAll = new JsonObject();
        jsonAll.addProperty("method", "savePage");
        jsonAll.add("parameter", js);


        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(jsonAll);

            Log.i("bbb", "=======================上传的我拼的json：====================" + jsonAll);
            BufferedReader recv = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String result;
            result = recv.readLine();
//            while ((result = recv.readLine()) != null) {
            Log.i("bbb", "===================上传书页的内容了：==========" + result);
            needPageCount++;
            if(needPageCount >= needUpBookPage.size()){
                Message message = new Message();
                message.what = 1002;

                hd.sendMessage(message);
            }else{
                Message message = new Message();
                message.what = 1001;

                hd.sendMessage(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
            }
        }.start();
    }


    private void shangchuanfuwuqistatus(){
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
                    jsonobject.addProperty("bookid", bookid);
                    jsonobject.addProperty("status", "1");
                    jsonobject.addProperty("appid", "2");
                    JsonObject js = new JsonObject();
                    js.addProperty("method", "changeBookStatus");
                    js.add("parameter", jsonobject);

                    out.println(js);

                    Log.i("bbb", "=======================上传书状态拼的json：====================" + js);
                    BufferedReader recv = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

                    result = recv.readLine();


                    jsonResult = new JSONObject(result);
                    String strResult = jsonResult.getString("result");
                    Log.i("bbb", "===================上传书状态信息：==========" + result);
//            }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void uploadTry(int imageCount , final DraftBookContent draftBookContent) {

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
        String videoSaveName = draftBookContent.getIsTextOrImage() + "_"+draftBookContent.tag + ".jpg";
        String taskLoveBook = "/LoveBook";
        String taskUserid = "/1";
        String taskBookid = "/"+ bookid;
        String taskPAGE = "/PAGE";
        int countPAGE = draftBookContent.bookPage + 1;
        String taskPAGECount = "/PAGE_"+ countPAGE;
        String destPath = taskLoveBook + taskUserid + taskBookid + taskPAGE + taskPAGECount + "/" + videoSaveName;

        fileUploadTask = new FileUploadTask(BuildConfig.TENCENT_UPLOAD_BUCKET, uploadFile.getAbsolutePath(), destPath, "", true, new IUploadTaskListener() {
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
        mFileUploadManager.upload(fileUploadTask); // 开始上传
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    public void startActivityWrite(int position){
        //editType;编辑类型（0是手动1是自动）
        if(0 == draftBookList.get(position).editType){
            Intent it_writeABook = new Intent(getContext() , WriteABookActivity.class);
            it_writeABook.putExtra("EditorOrBookShelf" , "bookShelf");
            it_writeABook.putExtra("aimBookid" , draftBookList.get(position).bookID);
            it_writeABook.putExtra("AutoOrNot","not");
            Log.i("BookshelfFragment" , "=======================draftBookList.get(position).bookID========================="+draftBookList.get(position).bookID);
            startActivity(it_writeABook);
        }else if(1 == draftBookList.get(position).editType){
            Intent it_writeABookQuickly = new Intent(getContext() , WriteABookQuicklyActivity.class);
            it_writeABookQuickly.putExtra("EditorOrBookShelf" , "bookShelf");
            it_writeABookQuickly.putExtra("aimBookid" , draftBookList.get(position).bookID);
            it_writeABookQuickly.putExtra("AutoOrNot","yes");
            startActivity(it_writeABookQuickly);
        }else{
            Toast.makeText(getContext() , "mei you!" , Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 从recyclerView上删除一本草稿书
     * @param pos
     */
    public void deleteDraftBook(int pos){
        //数据库中删除
        draftBookDao.deleteById(draftBookList.get(pos).id);
        //ui上面移除
        draftBookList.remove(pos);
        draftBookAdapter.notifyDataSetChanged();
    }
    public void jumpEditorFragment(){
        ((HomeActivity)getActivity()).setTabSelection(2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_bookshelf_iv_shopping_cart:
                startActivity(new Intent(getContext() , ShoppingCartActivity.class));
                break;

            case R.id.fragment_bookshelf_tv_bookid:
                getBookId();
                Toast.makeText(this.getContext() , "bookid = "+ bookid , Toast.LENGTH_SHORT).show();
                break;

            case R.id.fragment_bookshelf_tv_zouni:
                needUpBookPage = new ArrayList<DraftBookPage>();
                needUpBookPage = draftBookPageDao.selectorByBookId(1);
                needPageCount = 0;
                waitingPageUpDialog.setMessage("0/"+needUpBookPage.size());
                waitingPageUpDialog.show();
                Log.i("bbb" , "===================zouni needUpBookPage.size()=========================="+needUpBookPage.size());
                upEndPageToTencent("cover");
                upEndPageToTencent("endPage");
                upLittleImageToTencent(needUpBookPage);



                break;
            case R.id.fragment_bookshelf_tv_status1:
                shangchuanfuwuqistatus();
                break;
            case R.id.fragment_bookshelf_content_xsv_iv_draft_delete:

                flag_draft_delete = !flag_draft_delete;
                draftBookAdapter.deleteOrNot(flag_draft_delete);
                break;
            default:

                break;
        }
    }


    @Override
    public void onRefresh() {
        xsv.stopRefresh();
        getBookByUserId();
    }

    @Override
    public void onLoadMore() {

    }
}
