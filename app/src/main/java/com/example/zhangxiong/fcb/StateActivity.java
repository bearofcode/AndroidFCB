package com.example.zhangxiong.fcb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class StateActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton bt_back;
    TextView tv_first, tv_second, tv_third, tv_fourth, tv_fifth, tv_sixth, bt_moni;
    Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);
        int rid = getIntent().getIntExtra("rid",0);
        record = MySqlHandle.getrecordnop(rid);
        tv_first = (TextView) findViewById(R.id.tv_first);
        tv_second = (TextView) findViewById(R.id.tv_second);
        tv_third = (TextView) findViewById(R.id.tv_third);
        tv_fourth = (TextView) findViewById(R.id.tv_fourth);
        tv_fifth = (TextView) findViewById(R.id.tv_fifth);
        tv_sixth = (TextView) findViewById(R.id.tv_sixth);

        bt_back = (ImageButton) findViewById(R.id.bt_back);
        bt_back.setOnClickListener(this);

        bt_moni = (TextView) findViewById(R.id.bt_moni);
        bt_moni.setOnClickListener(this);

        show(record.getState());
    }

    public void refresh() {
        tv_first.setText("");
        tv_second.setText("");
        tv_third.setText("");
        tv_fourth.setText("");
        tv_fifth.setText("");
        tv_sixth.setText("");
    }

    public void show(int state) {
        switch (state) {
            case 0:
                refresh();
                tv_first.setText("车辆信息已上传");
                break;
            case 1:
                refresh();
                tv_first.setText("车辆信息已上传");
                tv_second.setText("系统已初步认为废旧车辆");
                break;
            case 2:
                refresh();
                tv_first.setText("车辆信息已上传");
                tv_second.setText("系统已初步认为废旧车辆");
                tv_third.setText("勘察人员已前往勘测");
                break;
            case 3:
                refresh();
                tv_first.setText("车辆信息已上传");
                tv_second.setText("系统已初步认为废旧车辆");
                tv_third.setText("勘察人员已前往勘测");
                tv_fourth.setText("已确认为需要处理的废旧车辆");
                break;
            case 4:
                refresh();
                tv_first.setText("车辆信息已上传");
                tv_second.setText("系统已初步认为废旧车辆");
                tv_third.setText("勘察人员已前往勘测");
                tv_fourth.setText("已确认为需要处理的废旧车辆");
                tv_fifth.setText("废旧车辆正在被运输");
                break;
            case 5:
                refresh();
                tv_first.setText("车辆信息已上传");
                tv_second.setText("系统已初步认为废旧车辆");
                tv_third.setText("勘察人员已前往勘测");
                tv_fourth.setText("已确认为需要处理的废旧车辆");
                tv_fifth.setText("废旧车辆正在被运输");
                tv_sixth.setText("审核通过,奖金已发放至余额");
                break;
            default:
                break;
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_back) {
            onBackPressed();
        }
        if (v.getId() == R.id.bt_moni) {
            int st = record.getState() + 1;
            if (record.getState() < 5) {
                MySqlHandle.updaterecord(record.getRid(), st);
                Intent intent = new Intent();
                intent.setClass(StateActivity.this, StateActivity.class);
                intent.putExtra("rid", record.getRid());
                startActivity(intent);
                finish();
            }
        }
    }
}
