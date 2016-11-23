package com.twkj.lovebook;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.twkj.lovebook.adapter.EditAdapter;
import com.twkj.lovebook.application.MyApplication;
import com.twkj.lovebook.bean.AllBookBean;
import com.twkj.lovebook.bean.BookContentBean;
import com.twkj.lovebook.bean.DraftBook;
import com.twkj.lovebook.bean.DraftBookContent;
import com.twkj.lovebook.bean.DraftBookPage;
import com.twkj.lovebook.bean.EditPageBean;
import com.twkj.lovebook.bean.ModelPageBean;
import com.twkj.lovebook.constants.BuildConfig;
import com.twkj.lovebook.dao.DraftBookContentDao;
import com.twkj.lovebook.dao.DraftBookDao;
import com.twkj.lovebook.dao.DraftBookPageDao;
import com.twkj.lovebook.fragment.EditFragment;
import com.twkj.lovebook.utils.BookInfoUtils;
import com.twkj.lovebook.utils.BookcontentBeanUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    //屏幕编辑的页面全屏的viewpager来实现左右滑动
    private ViewPager viewPager;
    private EditAdapter editAdapter;
    private List<Fragment> editPageList;
//    private List<EditPageBean> dataList;
    private List<List<EditPageBean>> allDataList;
    //一整本书的对象
    private AllBookBean allBookBeanInfo;
    //书的所有页
    private List<ModelPageBean> ModelPageList;
    private Button bt_insert , bt_look;

    private MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置页面无标题模式
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //初始化数据
        Log.d("ttt","走过链接的方法没" );
        init();
        myApplication= (MyApplication) getApplication();
        linkSocket();

    }


    private void init(){
        viewPager= (ViewPager) findViewById(R.id.viewpager_Edit);
        test();
        setData();
        getData();
        editAdapter=new EditAdapter(getSupportFragmentManager(), editPageList);
        viewPager.setAdapter(editAdapter);
    }

    private void test(){
        Fragment fragment1 = new EditFragment();
        Fragment fragment2 = new EditFragment();
//        Fragment fragment3 = new EditFragment();
//        Fragment fragment4 = new EditFragment();
        editPageList=new ArrayList<>();
        editPageList.add(fragment1);
        editPageList.add(fragment2);
//        editPageList.add(fragment3);
//        editPageList.add(fragment4);
        //书有多少页生成多少个fragment
        allBookBeanInfo =  BookInfoUtils.getAllBookBean(this);
        ModelPageList = new ArrayList<ModelPageBean>();

        ModelPageList = allBookBeanInfo.modelPage;
        for(int i = 0 ; i < ModelPageList.size() ; i++){
            Fragment fragment = new EditFragment();
            editPageList.add(fragment);
        }

    }
    private void getData(){
        Bundle bundle=null;

        for (int i=0;i<editPageList.size();i++){
            bundle=new Bundle();
            bundle.putSerializable("data", (Serializable) allDataList.get(i));
            editPageList.get(i).setArguments(bundle);
        }
    }
    private void setData(){
//        dataList=new ArrayList<>();
        allDataList=new ArrayList<>();
        List<EditPageBean> dataList=new ArrayList<>();
        EditPageBean bean1=new EditPageBean("image01","image",50,36,50+183,36+249);
        EditPageBean bean2=new EditPageBean("image02","image",345,36,345+183,36+249);
        dataList.add(bean1);
        dataList.add(bean2);

        List<EditPageBean> dataList2=new ArrayList<>();
        EditPageBean bean3=new EditPageBean("image01","image",50,100,50+183,100+249);
        EditPageBean bean4=new EditPageBean("image02","image",345,100,345+183,100+249);
        dataList2.add(bean3);
        dataList2.add(bean4);
        allDataList.add(dataList);
        allDataList.add(dataList2);
        //有多少页
        for (int i = 0 ; i < ModelPageList.size() ; i++){
            List<BookContentBean> bookContentBeanList = ModelPageList.get(i).bookContent;
            List<EditPageBean> datalist = new ArrayList<EditPageBean>();
            //page里面有多少内容
            for (int j = 0 ; j < bookContentBeanList.size() ; j++ ){
                BookContentBean bcb = bookContentBeanList.get(j);
                EditPageBean editPageBean = new EditPageBean(bcb.contentName , bcb.isTextOrImage , BookcontentBeanUtils.changeToPxOfInt(bcb.textOrImageX) , BookcontentBeanUtils.changeToPxOfInt(bcb.textOrImageY) ,BookcontentBeanUtils.changeToPxOfInt(bcb.textOrImageWidth) , BookcontentBeanUtils.changeToPxOfInt(bcb.textOrImageHeight));
                datalist.add(editPageBean);
            }
            allDataList.add(datalist);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getThisDP(){
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

    }





    //尝试数据库添加一条数据
//    public void insert(){
//        //数据库新增一本草稿书
//        DraftBookDao draftBookDao = new DraftBookDao();
//        DraftBook draftBook = new DraftBook();
//        draftBook.setBookName("nothing");
//        draftBookDao.insert(draftBook);
//        //草稿书新增第三页 增加第三页的同时把第三页的模板放入
//        DraftBookPage draftBookPage = new DraftBookPage();
//        draftBookPage.setBookPage(3);
//        draftBookPage.setDraftBookID(1);
//        DraftBookPageDao draftBookPageDao = new DraftBookPageDao();
//        draftBookPageDao.insert(draftBookPage);
//
//        DraftBookContentDao draftBookContentDao = new DraftBookContentDao();
//        ModelPageBean mpb = ModelPageList.get(2);
//        for (int i = 0 ; i < mpb.bookContent.size() ; i++){
//            DraftBookContent dbc = new DraftBookContent();
//            dbc.setBookID(1);
//            dbc.setBookPage(3);
//            dbc.setContentName(mpb.bookContent.get(i).contentName);
//            dbc.setIsTextOrImage(mpb.bookContent.get(i).isTextOrImage);
//            dbc.setTextOrImageX(mpb.bookContent.get(i).textOrImageX);
//            dbc.setTextOrImageY(mpb.bookContent.get(i).textOrImageY);
//            dbc.setTextOrImageWidth(mpb.bookContent.get(i).textOrImageWidth);
//            dbc.setTextOrImageHeight(mpb.bookContent.get(i).textOrImageHeight);
//            dbc.setTag(i);
//            draftBookContentDao.insert(dbc);
//        }
//        //第三页放了一张图片
//
//
//        DraftBookContent dddbc = new DraftBookContent();
//        dddbc = draftBookContentDao.selectorByBookIdAndBookPageAndTag(1, 3, 0);
//        dddbc.setImageMarkName("nmh");
//        draftBookContentDao.update(dddbc);
//
//        Log.i("MainActivity" , "=====insert=============over");
//    }
    public void findnmh(){
        //查找出草稿书
        DraftBookDao draftBookDao = new DraftBookDao();
        DraftBook draftBook = draftBookDao.selectById(1);

        //查找出草稿书所有页
        DraftBookPageDao draftBookPageDao = new DraftBookPageDao();
        List<DraftBookPage> listDraftBookPage = draftBookPageDao.selectorByBookId(1);




        //页上面的内容
        DraftBookContentDao draftBookContentDao = new DraftBookContentDao();
        List<DraftBookContent> listDraftBookContent = draftBookContentDao.selectorByBookIdAndBookPage(1,3);
        listDraftBookPage.get(0).listDraftBookContent = listDraftBookContent;
        draftBook.listDraftBookPage = listDraftBookPage;
        Log.i("MainActivity" , "=========nmh========="+draftBook.listDraftBookPage.get(0).listDraftBookContent.get(0).imageMarkName);
        Toast.makeText(this , draftBook.listDraftBookPage.get(0).listDraftBookContent.get(0).imageMarkName , Toast.LENGTH_SHORT).show();

    }

    private void linkSocket()  {
        try {
            Log.d("bbb","走进这个方法没：");

            myApplication.setSocket(new Socket(BuildConfig.SOCKET_BINDING,BuildConfig.SOCKET_PORT_SHORT));
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("account","13335382871");
            jsonObject.put("pwd","e10adc3949ba59abbe56e057f20f883e");
            jsonObject.put("appid","2");

            JSONObject json= new JSONObject();
            json.put("method","login");
            json.put("parameter",jsonObject);
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(myApplication.getSocket().getOutputStream())),
                    true);
            // 向外发送
            out.println(json);
            // 通过输入流，获取返回的信息
            BufferedReader recv = new BufferedReader(
                    new InputStreamReader(myApplication.getSocket().getInputStream()));
            String recvMsg = recv.readLine();
            Log.d("bbb","联通没用："+recvMsg);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
