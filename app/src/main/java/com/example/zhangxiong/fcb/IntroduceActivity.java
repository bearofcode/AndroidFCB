package com.example.zhangxiong.fcb;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by ZhangXiong on 2017/12/7.
 */

public class IntroduceActivity extends Activity implements View.OnClickListener{
    ImageButton qq,weixin,weibo,baidu,lanya,email,yunbei,wifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introce);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = RelativeLayout.LayoutParams.FILL_PARENT;
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);
        init();
    }

    private void init() {
        qq= (ImageButton) findViewById(R.id.qq);
        weixin= (ImageButton) findViewById(R.id.weixin);
        baidu= (ImageButton) findViewById(R.id.baidu);
        lanya= (ImageButton) findViewById(R.id.lanya);
        weibo= (ImageButton) findViewById(R.id.weibo);
        email= (ImageButton) findViewById(R.id.email);
        yunbei= (ImageButton) findViewById(R.id.yunbei);
        wifi= (ImageButton) findViewById(R.id.wifi);
        qq.setOnClickListener(this);
        weixin.setOnClickListener(this);
        weibo.setOnClickListener(this);
        email.setOnClickListener(this);
        baidu.setOnClickListener(this);
        lanya.setOnClickListener(this);
        yunbei.setOnClickListener(this);
        wifi.setOnClickListener(this);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this,"功能更新中",Toast.LENGTH_SHORT).show();
    }
}
