package com.twkj.lovebook.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;
import com.twkj.lovebook.bean.BannerBean;
import com.twkj.lovebook.utils.ControlBitmapUtils;

import java.util.List;

/**
 * Created by tiantao on 2016/11/22.
 */

public class BannerAdapter extends PagerAdapter {

    private Context context;
    private List<BannerBean> listdata;
    private LayoutInflater inflater;
    private ViewPager mViewPager;
    private final int DEFAULT_BANNER_SIZE = 3;
    private final int FAKE_BANNER_SIZE = 100;

    public BannerAdapter(Context context, List<BannerBean> listdata , ViewPager mViewPager) {
        this.context = context;
        this.listdata = listdata;
        this.mViewPager = mViewPager;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position %= DEFAULT_BANNER_SIZE;

        View view = inflater.inflate(R.layout.item_banner, container, false);
        ImageView image = (ImageView) view.findViewById(R.id.item_banner_image);

//        Utils.loadImage(newsList.get(position).getImage(), image);
        Glide.with(context)
                .load(ControlBitmapUtils.getResId(listdata.get(position).getImage() , R.drawable.class))
                .into(image);
        final int pos = position;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,pos+"-->"+listdata.get(pos).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
//        super.destroyItem(container, position, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        //这个有点懵逼..
        int position = mViewPager.getCurrentItem();
        if (position == 0){
            position = DEFAULT_BANNER_SIZE;
            mViewPager.setCurrentItem(position,false);
        }else if (position == FAKE_BANNER_SIZE - 1){
            position = DEFAULT_BANNER_SIZE - 1;
            mViewPager.setCurrentItem(position,false);
        }
    }

    @Override
    public int getCount() {
        return FAKE_BANNER_SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
//        return false;
        return view == object;
    }
}
