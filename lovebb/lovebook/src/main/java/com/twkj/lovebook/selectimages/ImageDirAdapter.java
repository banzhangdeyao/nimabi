package com.twkj.lovebook.selectimages;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twkj.lovebook.R;

import java.util.List;

/**
 * Created by tiantao on 2016/11/8.
 */

public class ImageDirAdapter extends BaseAdapter {

    private Context context;
    private List<Dir> listdata;
    private LayoutInflater inflater;

    public ImageDirAdapter(Context context, List<Dir> listdata) {
        this.context = context;
        this.listdata = listdata;
        this.inflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_selectimages_dir, parent, false);
            holder = new Holder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.item_selectimages_dir_iv);
            holder.mTextView = (TextView) convertView.findViewById(R.id.item_selectimages_dir_path);
            holder.mNameTextView = (TextView) convertView.findViewById(R.id.item_selectimages_dir_name);

            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        Dir dir = listdata.get(position);
        holder.mTextView.setText(dir.text);
        holder.mNameTextView.setText(Html.fromHtml(dir.name + " <font color=\"#999999\">(" + dir.length + ")</font>"));

        Glide.with(context)
                .load("file:///" + dir.path)
                .into(holder.mImageView);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class Holder
    {
        public TextView mTextView, mNameTextView;
        public ImageView mImageView;
    }
}
