package com.example.zhangxiong.fcb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {

    ListView lv_show;
    ImageButton myback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        myback= (ImageButton) findViewById(R.id.myback);
        myback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lv_show= (ListView) findViewById(R.id.lv_show);
        ArrayList<Record> l_record=MySqlHandle.getAllrecord(LoginActivity.Account);
        ListviwAdapter adapter=new ListviwAdapter(RecordActivity.this,R.layout.list_view,l_record);
        lv_show.setAdapter(adapter);
    }
}