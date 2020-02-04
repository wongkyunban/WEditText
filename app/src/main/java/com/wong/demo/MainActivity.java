package com.wong.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.wong.widget.ClearEditText;
import com.wong.widget.SimpleSpinnerEditText;
import com.wong.widget.SpinnerEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpinnerEditText spinnerEditText = findViewById(R.id.set_select_input);
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("No." + i + "号");
        }
        spinnerEditText.setOptions(list);

        ClearEditText cet = findViewById(R.id.cet);
//        cet.setShakeAnimation();
//        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher,null);
//        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
//        cet.setCompoundDrawablesRelative(null,null,drawable,null);

        /*SimpleSpinnerEditText*/
        String[] strings = new String[50];
        for (int i = 0; i < 50; i++) {
            strings[i] = "No." + i + "号";
        }
        SimpleSpinnerEditText simpleSpinnerEditText = findViewById(R.id.sset);
        BaseAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings);
        simpleSpinnerEditText.setAdapter(adapter);
//        simpleSpinnerEditText.setPopupDivider(getDrawable(R.drawable.divider_bg));
//        simpleSpinnerEditText.setPopupDividerHeight(80);

    }
}
