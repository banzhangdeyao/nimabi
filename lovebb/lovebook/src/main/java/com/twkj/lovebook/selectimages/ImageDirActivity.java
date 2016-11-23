package com.twkj.lovebook.selectimages;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twkj.lovebook.R;
import java.util.ArrayList;

/**
 * 查看所有含有图片的目录。<br/>
 * <br/>
 * Created by yanglw on 2014/8/17.
 */
public class ImageDirActivity extends ImageBaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private ListView listView;
    private ImageView iv_back;
    private TextView tv_list , tv_preview , tv_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_image_dir);

        listView = (ListView) findViewById(R.id.activity_image_dir_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Dir dir = (Dir) parent.getItemAtPosition(position);
                if (dir != null)
                {
                    Intent intent = new Intent(ImageDirActivity.this, ImagesActivity.class);
                    intent.putExtra(ImagesActivity.ARG_DIR_ID, dir.id);
                    intent.putExtra(ImagesActivity.ARG_DIR_NAME, dir.name);
                    intent.putExtra(Constan.ARG_PHOTO_LIST, checkList);

                    startActivityForResult(intent, 1);
                }
            }
        });

        iv_back = (ImageView) findViewById(R.id.activity_selectimages_dir_iv_back);
        tv_list = (TextView) findViewById(R.id.activity_selectimages_dir_tv_list);
        tv_preview = (TextView) findViewById(R.id.activity_selectimages_dir_tv_preview);
        tv_ok = (TextView) findViewById(R.id.activity_selectimages_dir_tv_ok);
        iv_back.setOnClickListener(this);
        tv_list.setOnClickListener(this);
        tv_preview.setOnClickListener(this);
        tv_ok.setOnClickListener(this);


        if (savedInstanceState == null)
        {
            ArrayList<Photo> list = getIntent().getParcelableArrayListExtra(Constan.ARG_PHOTO_LIST);
            if (list != null)
            {
                checkList.addAll(list);
            }
        }
        getSupportLoaderManager().initLoader(0 , null , this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            finish();
        }
    }

    @Override
    public void finish() {

        Intent intent = new Intent();
        intent.putExtra(Constan.RES_PHOTO_LIST, checkList);
        setResult(RESULT_OK, intent);

        super.finish();
        checkList.clear();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        "count(1) length",
                        MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.DATA
                },
                "1=1) GROUP BY " + MediaStore.Images.Media.BUCKET_ID + " -- (",
                null,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC," +
                        MediaStore.Images.Media.DATE_MODIFIED + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0)
        {
            ArrayList<Dir> list = new ArrayList<Dir>();

            data.moveToPosition(-1);
            while (data.moveToNext())
            {
                int id = data.getInt(data.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                String path = data.getString(data.getColumnIndex(MediaStore.Images.Media.DATA));
                String dirPath;
                int index = path.lastIndexOf('/');
                if (index > 0)
                {
                    dirPath = path.substring(0, index);
                }
                else
                {
                    dirPath = path;
                }

                Dir dir = new Dir();
                dir.id = String.valueOf(id);
                dir.name = data.getString(data.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                dir.text = dirPath;
                dir.path = path;
                dir.length = data.getInt(data.getColumnIndex("length"));
                list.add(dir);
            }

            ImageDirAdapter adapter = new ImageDirAdapter(this, list);
            listView.setAdapter(adapter);
        }
        else
        {
            Toast.makeText(this, "没有图片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_selectimages_dir_iv_back:
                this.finish();
                break;

            case R.id.activity_selectimages_dir_tv_list:

                break;

            case R.id.activity_selectimages_dir_tv_preview:
                Intent it_preview = new Intent(this , PreviewActivity.class);
                startActivity(it_preview);
                break;

            case R.id.activity_selectimages_dir_tv_ok:
                finish();
                break;
            default:

                break;
        }
    }
}
