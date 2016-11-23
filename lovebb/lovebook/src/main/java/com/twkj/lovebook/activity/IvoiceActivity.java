package com.twkj.lovebook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.twkj.lovebook.R;

/**
 * Created by wht on 2016/11/19.
 */

public class IvoiceActivity extends Activity implements RadioGroup.OnCheckedChangeListener {
    private RadioButton companies, individuals, address;
    private EditText companiesEdit, individualsEdit, addressEdit;
    RadioGroup radioGroup, radioGroup2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        initView();
    }

    private void initView() {
        companies= (RadioButton) findViewById(R.id.radio_companies);
        individuals= (RadioButton) findViewById(R.id.radio_individuals);
        companiesEdit= (EditText) findViewById(R.id.radio_companies_edit);
        individualsEdit= (EditText) findViewById(R.id.radio_individuals_edit);

        address = (RadioButton) findViewById(R.id.radio_address);
        addressEdit = (EditText) findViewById(R.id.radio_address_edit);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

//        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radioGroup.setOnCheckedChangeListener(this);
//        radioGroup2.setOnCheckedChangeListener(this);

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radio_companies:
                companiesEdit.setEnabled(true);
                individualsEdit.setEnabled(false);
                break;
            case R.id.radio_individuals:
                individualsEdit.setEnabled(true);
                companiesEdit.setEnabled(false);
                break;
        }
    }
}
