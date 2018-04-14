package com.example.zhangxiong.fcb;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class IdearActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton myback;
    TextView tv_sbmit;
    EditText et_idear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idear);
        myback= (ImageButton) findViewById(R.id.myback);
        tv_sbmit= (TextView) findViewById(R.id.tv_submit);
        et_idear= (EditText) findViewById(R.id.et_idear);
        myback.setOnClickListener(this);
        tv_sbmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_submit){
            if(TextUtils.isEmpty(et_idear.getText())){
                Toast.makeText(IdearActivity.this,"请填写反馈内容",Toast.LENGTH_SHORT).show();
            }
            else{
                et_idear.setText("");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(this, R.layout.dialog_hinit, null);
                TextView tv_name= (TextView) dialogView.findViewById(R.id.tv_name);
                tv_name.setText("提交成功，我们将立即处理您反馈的问题");
                final TextView bt_sure=(TextView)dialogView.findViewById(R.id.bt_sure);
                final TextView bt_cancel=(TextView)dialogView.findViewById(R.id.bt_cancel);
                bt_cancel.setVisibility(View.INVISIBLE);
                bt_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setView(dialogView);
                dialog.show();
            }
        }
        if (v.getId()==R.id.myback){
            onBackPressed();
        }
    }
}
