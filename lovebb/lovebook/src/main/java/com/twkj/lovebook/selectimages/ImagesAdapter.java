package com.twkj.lovebook.selectimages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;

import java.util.List;

/**
 * Created by tiantao on 2016/11/8.
 */

public class ImagesAdapter extends BaseAdapter {

    private Context context;
    private List<Photo> listdata;
    private List<Photo> mCheckList;
    private LayoutInflater mInfater;

    public ImagesAdapter(Context context, List<Photo> listdata, List<Photo> mCheckList) {
        this.context = context;
        this.listdata = listdata;
        this.mCheckList = mCheckList;
        this.mInfater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return listdata == null ? 0 : listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return listdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null)
        {
            convertView = mInfater.inflate(R.layout.item_selectimages_images, parent, false);
            holder = new Holder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.item_selectimages_images_iv);
            holder.mCheckImgaeView = (ImageView) convertView.findViewById(R.id.item_selectimages_images_check);

            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        Photo photo = listdata.get(position);

        if (mCheckList != null && mCheckList.contains(photo))
        {
            holder.mCheckImgaeView.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.mCheckImgaeView.setVisibility(View.INVISIBLE);
        }
        Glide.with(context)
                .load("file:///" + photo.path)
                .into(holder.mImageView);
        return convertView;
    }
    public void setCheck(int postion, View view)
    {
        Photo photo = listdata.get(postion);

        boolean checked = mCheckList.contains(photo);

        Holder holder = (Holder) view.getTag();

        if (checked)
        {
            mCheckList.remove(photo);
            holder.mCheckImgaeView.setVisibility(View.INVISIBLE);
        }
        else
        {
            mCheckList.add(photo);
            holder.mCheckImgaeView.setVisibility(View.VISIBLE);
        }
    }


    private class Holder
    {
        public ImageView mImageView, mCheckImgaeView;
    }
}
