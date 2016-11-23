package com.twkj.lovebook.selectimages;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twkj.lovebook.R;

import java.util.ArrayList;

public class ImagesActivity extends ImageBaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {


    public static final String ARG_DIR_ID = "my.android.app.chooseimages.DIR_ID";
    public static final String ARG_DIR_NAME = "my.android.app.chooseimages.DIR_NAME";
    private ImageView iv_back;

    private GridView mGridView;

    private ImagesAdapter mAdapter;

    private String mDirId;

    private boolean mIsEnable;

    private TextView tv_list , tv_preview , tv_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_selectimages_images);

        iv_back = (ImageView) findViewById(R.id.activity_selectimages_images_iv_back);
        tv_list = (TextView) findViewById(R.id.activity_selectimages_images_tv_list);
        tv_preview = (TextView) findViewById(R.id.activity_selectimages_images_tv_preview);
        tv_ok = (TextView) findViewById(R.id.activity_selectimages_images_tv_ok);
        iv_back.setOnClickListener(this);
        tv_list.setOnClickListener(this);
        tv_preview.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
        mGridView = (GridView) findViewById(R.id.activity_selectimages_images_gridview);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Photo photo = (Photo) parent.getItemAtPosition(position);
                if (photo == null)
                {
                    return;
                }

                if (!checkList.contains(photo))
                {
                    if (checkList.size() >= Constan.MAX_SIZE && Constan.MAX_SIZE >= 0)
                    {
                        Toast.makeText(ImagesActivity.this,
                                "最多选取20张图片:)",
                                Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                }
                mAdapter.setCheck(position, view);

                setBtnEnable(!checkList.isEmpty());
            }
        });
//        mGridView.setOnScrollListener(ImageManager.pauseScrollListener);

        Intent intent = getIntent();
        mDirId = intent.getStringExtra(ARG_DIR_ID);
        setTitle(intent.getStringExtra(ARG_DIR_NAME));

        getSupportLoaderManager().initLoader(0, null, this);

        setBtnEnable(!checkList.isEmpty());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            setResult(RESULT_OK);
            finish();
            return;
        }
        if (resultCode == Constan.RESULT_CHANGE)
        {
            mAdapter.notifyDataSetChanged();
            setBtnEnable(!checkList.isEmpty());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Media.DATA//图片地址
                },
                mDirId == null ? null : MediaStore.Images.Media.BUCKET_ID + "=" + mDirId,
                null,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0)
        {
            ArrayList<Photo> list = new ArrayList<Photo>();

            data.moveToPosition(-1);
            while (data.moveToNext())
            {
                Photo photo = new Photo();

                photo.path = data.getString(data.getColumnIndex(MediaStore.Images.Media.DATA));
                list.add(photo);
            }
            mAdapter = new ImagesAdapter(this, list, checkList);
            mGridView.setAdapter(mAdapter);
        }
        else
        {
            Toast.makeText(this, "nothing", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    private void setBtnEnable(boolean enable)
    {
        mIsEnable = enable;
        if (Build.VERSION.SDK_INT >= 11)
        {
            invalidateOptionsMenu();
        }
        else
        {
//            supportInvalidateOptionsMenu();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_selectimages_images_iv_back:
                this.finish();
                break;
            case R.id.activity_selectimages_images_tv_list:

                break;

            case R.id.activity_selectimages_images_tv_preview:

                Intent it_preview = new Intent(this , PreviewActivity.class);
                startActivity(it_preview);
                break;

            case R.id.activity_selectimages_images_tv_ok:
                setResult(RESULT_OK);
                finish();
                break;
            default:

                break;
        }
    }
}
