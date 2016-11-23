package com.twkj.lovebook.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.twkj.lovebook.R;
import com.twkj.lovebook.activity.WriteABookActivity;
import com.twkj.lovebook.activity.WriteABookQuicklyActivity;
import com.twkj.lovebook.selectimages.ImageDirActivity;


public class EditorbookFragment extends Fragment implements View.OnClickListener {


    private LinearLayout ll_autoedit , ll_manual;//自动编辑 手动编辑

    public EditorbookFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editorbook, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }
    private void initView(){
        ll_autoedit = (LinearLayout) getView().findViewById(R.id.fragment_editorbook_ll_autoedit);
        ll_manual = (LinearLayout) getView().findViewById(R.id.fragment_editorbook_ll_manualedit);

        ll_autoedit.setOnClickListener(this);
        ll_manual.setOnClickListener(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_editorbook_ll_autoedit:

                Intent it_writeABookQuickly = new Intent(getContext() , WriteABookQuicklyActivity.class);
                it_writeABookQuickly.putExtra("EditorOrBookShelf" , "editor");//书架还是从编辑页面进的标识
                it_writeABookQuickly.putExtra("AutoOrNot" , "yes");//手动还是自动编辑的标识
                startActivity(it_writeABookQuickly);
                break;
            case R.id.fragment_editorbook_ll_manualedit:
                Intent it_writeABook = new Intent(getContext() , WriteABookActivity.class);
                it_writeABook.putExtra("EditorOrBookShelf" , "editor");
                it_writeABook.putExtra("AutoOrNot" , "not");

                startActivity(it_writeABook);
                break;
            default:
                break;
        }
    }
}
