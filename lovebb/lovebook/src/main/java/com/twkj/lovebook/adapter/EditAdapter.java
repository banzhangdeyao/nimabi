package com.twkj.lovebook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.twkj.lovebook.fragment.EditFragment;
import com.twkj.lovebook.selectimages.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wht on 2016/10/10.
 */

public class EditAdapter extends FragmentPagerAdapter{
    private List<Fragment> list;
    //用于给每个fragment打上tag fragment需要更新时找到对应的fragment
    private List<String> tagList = new ArrayList<String>();
    private FragmentManager mFragmentManager;
    public EditAdapter(FragmentManager fm) {
        super(fm);
    }
    public EditAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list=list;
        this.mFragmentManager = fm;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //把tag存起来
        tagList.add(makeFragmentName(container.getId(),getItemId(position)));
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        //把tag删了
        tagList.remove(makeFragmentName(container.getId(), getItemId(position)));
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * //更新Fragment的内容
     * @param position 刷新那个fragment
     * @param photoList 图片的list
     * @param current 记录该从list的哪一个位置开始放图片
     */
    public int quicklyUpdate(int position , List<Photo> photoList , int current){
        //通过fragmentManager 找出对应的fragment 然后调用Editfragment里的update()
        Fragment fragment = mFragmentManager.findFragmentByTag(tagList.get(position));

        if(fragment == null){
            return current;
        }
        return ((EditFragment)fragment).quicklyUpdate(photoList , current);
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
