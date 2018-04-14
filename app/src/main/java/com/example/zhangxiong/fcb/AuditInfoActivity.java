package com.example.zhangxiong.fcb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AuditInfoActivity extends AppCompatActivity {

    ImageView iv_pictrue;
    TextView tv_address, tv_jindu, tv_weidu, tv_date, tv_number, tv_carnumber, tv_look;
    ImageButton bt_back;
    Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_info);
        record = MySqlHandle.getrecord(getIntent().getIntExtra("rid",0));
        LoginActivity.p.dismiss();
        init();
    }

    private void init() {
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_jindu = (TextView) findViewById(R.id.tv_jindu);
        tv_weidu = (TextView) findViewById(R.id.tv_weidu);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_carnumber = (TextView) findViewById(R.id.tv_carnumber);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_look = (TextView) findViewById(R.id.tv_look);
        tv_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AuditInfoActivity.this, StateActivity.class);
                intent.putExtra("rid",record.getRid());
                startActivity(intent);
                //finish();
            }
        });
        iv_pictrue = (ImageView) findViewById(R.id.iv_photo);
        bt_back = (ImageButton) findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_address.setText(record.getAddress());
        tv_jindu.setText(String.valueOf(record.getJindu()));
        tv_weidu.setText(String.valueOf(record.getWeidu()));
        tv_carnumber.setText(record.getCarnumber());
        tv_date.setText(record.getDate());
        byte[] imgData = record.getPhoto();
        if (imgData != null) {
            //将字节数组转化为位图
            Bitmap imagebitmap = null;
            try {
                imagebitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            //将位图显示为图片
            iv_pictrue.setImageBitmap(imagebitmap);
        } else {
            iv_pictrue.setBackgroundResource(R.mipmap.plussmall);
        }
    }
}
