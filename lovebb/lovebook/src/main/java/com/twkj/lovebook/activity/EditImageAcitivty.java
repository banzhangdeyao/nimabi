package com.twkj.lovebook.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.twkj.lovebook.R;
import com.twkj.lovebook.utils.ImageToneLayer;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by wht on 2016/10/27.
 */

public class EditImageAcitivty extends Activity implements SeekBar.OnSeekBarChangeListener{
    private ImageToneLayer imageToneLayer;
    private ImageView imageView;
    private Bitmap bitmap;
    private Uri uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editimage);
        uri=Uri.parse(getIntent().getStringExtra("uri"));
        init();
    }

    private void init()
    {
        imageToneLayer = new ImageToneLayer(this);

        try {
            bitmap =BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView = (ImageView) findViewById(R.id.edit_img);
        imageView.setImageBitmap(bitmap);
        ((LinearLayout) findViewById(R.id.bot)).addView(imageToneLayer.getParentView());
        ArrayList<SeekBar> seekBars = imageToneLayer.getSeekBars();
        for (int i = 0, size = seekBars.size(); i < size; i++)
        {
            seekBars.get(i).setOnSeekBarChangeListener(this);
        }
    }

    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        int flag = (Integer) seekBar.getTag();
        switch (flag)
        {
            case ImageToneLayer.FLAG_SATURATION:
                imageToneLayer.setSaturation(progress);
                break;
            case ImageToneLayer.FLAG_LUM:
                imageToneLayer.setLum(progress);
                break;
            case ImageToneLayer.FLAG_HUE:
                imageToneLayer.setHue(progress);
                break;
        }

        imageView.setImageBitmap(imageToneLayer.handleImage(bitmap, flag));
    }

    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
