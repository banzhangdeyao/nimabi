package com.twkj.lovebook.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.twkj.lovebook.R;
import com.twkj.lovebook.adapter.UpBookInfoModelAdapter;
import com.twkj.lovebook.constants.BookModelConfig;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 已经长传书的详情
 */
public class UpBookInfoActivity extends Activity implements View.OnClickListener {

    private ImageView iv_back , iv_share;

    private RecyclerView rv_model;

    private UpBookInfoModelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_up_book_info);

        initView();
    }

    private void initView(){
        iv_back = (ImageView) findViewById(R.id.activity_up_book_info_iv_back);
        iv_share = (ImageView) findViewById(R.id.activity_up_book_info_iv_share);
        rv_model = (RecyclerView) findViewById(R.id.activity_up_book_info_rv_model);
        adapter = new UpBookInfoModelAdapter(this , BookModelConfig.bookModelList);
        rv_model.setLayoutManager(new GridLayoutManager(this , 2));
        rv_model.setAdapter(adapter);

        iv_back.setOnClickListener(this);
        iv_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_up_book_info_iv_back:
                this.finish();
                break;
            case R.id.activity_up_book_info_iv_share:
                showShare();
                break;
        }
    }
    private void showShare() {
        ShareSDK.initSDK(this , "192882d19d768");
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("情书");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("情书一种可以将往日的情感寄托");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://pic65.nipic.com/file/20150422/18889456_142813785397_2.png");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
