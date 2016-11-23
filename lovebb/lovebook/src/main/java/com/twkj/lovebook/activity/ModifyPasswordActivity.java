package com.twkj.lovebook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.twkj.lovebook.R;

/**
 * Created by wht on 2016/11/16.
 */

public class ModifyPasswordActivity extends Activity implements View.OnClickListener{
    private ImageView back;
    private Button getyanzhengma,commit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypwd);

    }
    public void initView(){
        back= (ImageView) findViewById(R.id.modify_back);
        getyanzhengma= (Button) findViewById(R.id.modify_yanzheng_btn);
        commit= (Button) findViewById(R.id.modify_commit);
    }

    @Override
    public void onClick(View v) {

    }
}
