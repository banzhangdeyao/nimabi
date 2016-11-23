package com.twkj.lovebook.selectimages;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class PreviewActivity extends ImageBaseActivity implements  View.OnClickListener {

    private ViewPager mViewPager;
    private ArrayList<Photo> mCancelList;

    private boolean mIsCheck;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_selectimages_preview);
        iv_back = (ImageView) findViewById(R.id.activity_selectimages_preview_iv_back);
        iv_back.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.activity_selectimages_preview_viewpager);
        PreviewPagerAdapter adapter = new PreviewPagerAdapter(checkList);
        Log.i("PA" , "======================checkList.size()====================="+checkList.size());
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mCancelList = new ArrayList<Photo>();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_selectimages_preview_iv_back:

                this.finish();

                break;

            default:

                break;
        }
    }

    static class PreviewPagerAdapter extends PagerAdapter{
        private List<Photo> mList;

        public PreviewPagerAdapter( List<Photo> mList) {
            this.mList = mList;

        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(container.getContext())
                    .load("file:///"+mList.get(position).path)
                    .into(photoView);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
         return photoView;
//            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
//            return false;
            return view == object;
        }
    }
}
