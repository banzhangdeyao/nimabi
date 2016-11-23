package com.twkj.lovebook.activity;

import android.app.Activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.twkj.lovebook.R;
import com.twkj.lovebook.adapter.EditorEditBookAutoAdapter;

import java.util.ArrayList;
import java.util.List;

public class EditorAutoActivity extends Activity {

    private RecyclerView recyclerView;
    private EditorEditBookAutoAdapter adapter;
    private List<String> listdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_editor_auto);

        initView();
    }

    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.activity_editor_auto_rv);
        listdata = new ArrayList<String>(5);

        adapter = new EditorEditBookAutoAdapter(this , listdata);
        adapter.setOnItemClickListener(new EditorEditBookAutoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this ,4));
    }
}
