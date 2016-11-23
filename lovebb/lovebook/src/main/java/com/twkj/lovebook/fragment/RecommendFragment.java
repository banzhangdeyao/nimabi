package com.twkj.lovebook.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.twkj.lovebook.R;
import com.twkj.lovebook.adapter.BannerAdapter;
import com.twkj.lovebook.adapter.RecommendAdapter;
import com.twkj.lovebook.bean.BannerBean;
import com.twkj.lovebook.bean.RecommendBean;
import com.twkj.lovebook.constants.BookModelConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 推荐fragment
 */
public class RecommendFragment extends Fragment {

    private RecyclerView recyclerView;

    private RecommendAdapter adapter;

    //轮播图
    private ViewPager bannerViewPager;

    private BannerAdapter bannerAdapter;

    private ImageView[] mIndicator;

    private ImageView iv_dotOne , iv_dotTwo , iv_dotThree  ;

    private Timer mTimer = new Timer();

    private int mBannerPosition = 0;

    private final int FAKE_BANNER_SIZE = 100;

    private final int DEFAULT_BANNER_SIZE = 3;

    private boolean mIsUserTouched = false;

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (!mIsUserTouched){
                mBannerPosition = (mBannerPosition + 1) % FAKE_BANNER_SIZE;
                /**
                 * Android在子线程更新UI的几种方法
                 * Handler，AsyncTask,view.post,runOnUiThread
                 */
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mBannerPosition == FAKE_BANNER_SIZE - 1){
                            bannerViewPager.setCurrentItem(DEFAULT_BANNER_SIZE - 1,false);
                        }else {
                            bannerViewPager.setCurrentItem(mBannerPosition);
                        }
                    }
                });
            }
        }
    };

    public RecommendFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView(){
        iv_dotOne = (ImageView) getView().findViewById(R.id.fragment_recommend_banner_dot_indicator_one);
        iv_dotTwo = (ImageView) getView().findViewById(R.id.fragment_recommend_banner_dot_indicator_two);
        iv_dotThree = (ImageView) getView().findViewById(R.id.fragment_recommend_banner_dot_indicator_three);
//        iv_dotFour = (ImageView) getView().findViewById(R.id.fragment_recommend_banner_dot_indicator_four);
//        iv_dotFive = (ImageView) getView().findViewById(R.id.fragment_recommend_banner_dot_indicator_five);
        //dot
        mIndicator = new ImageView[]{
                iv_dotOne,
                iv_dotTwo,
                iv_dotThree,
//                iv_dotFour,
//                iv_dotFive,
        };
        //view
        bannerViewPager = (ViewPager) getView().findViewById(R.id.fragment_recommend_banner_viewPager);
        //loadData
        bannerAdapter = new BannerAdapter(getContext() , BookModelConfig.bannerBeanList , bannerViewPager);
        bannerViewPager.setAdapter(bannerAdapter);
        //Touch
        bannerViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE){
                    mIsUserTouched = true;
                }else if (action == MotionEvent.ACTION_UP){
                    mIsUserTouched = false;
                }
                return false;
            }
        });
        bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBannerPosition = position;
                setIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        recyclerView = (RecyclerView) getView().findViewById(R.id.fragment_recommend_recyclerView);

        adapter = new RecommendAdapter(getContext() , BookModelConfig.recommendBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        //第一个5000表示从调用schedule()方法到第一次执行mTimerTask的run()方法的时间间隔
        //第二个5000表示以后每隔5000毫秒执行一次mTimerTask的run()方法
        mTimer.schedule(mTimerTask,5000,5000);


    }
    private void setIndicator(int position) {
        position %= DEFAULT_BANNER_SIZE;
        //遍历mIndicator重置src为normal
        for (ImageView indicator : mIndicator){
            indicator.setImageResource(R.drawable.banner_dot_normal);
        }
        mIndicator[position].setImageResource(R.drawable.banner_dot_focused);
//        mTitle.setText(news.getTop_stories().get(position).getTitle());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
