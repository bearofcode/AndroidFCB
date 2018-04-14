package com.example.zhangxiong.fcb;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText et_account,et_pass,et_surepass,et_verlidation;
    Button bt_getverli,bt_regist;
    ImageButton myback;
    TextView tv_forget;
    boolean baccount,bpass,bvali,bsurepass,bgetvalid;
    EventHandler eventHandler;
    String strPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //MobSDK.init(this, "Mob-AppSecret", "Mob-AppSecret");
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message = myHandler.obtainMessage(0x00);
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                myHandler.sendMessage(message);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
        init();
    }
    public void init(){
        baccount=false;
        bpass=false;
        bsurepass=false;
        bvali=false;
        bgetvalid=false;
        et_account= (EditText) findViewById(R.id.et_account);
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern pattern=Pattern.compile("[1]\\d{10}");
                String account=et_account.getText().toString();
                Matcher m=pattern.matcher(account);
                if(TextUtils.isEmpty(account))
                {
                    et_account.setError("不能为空!");
                    baccount=false;
                    bt_regist.setEnabled(false);
                    bt_regist.setBackgroundResource(R.drawable.btn_unable);
                }
                else if(!m.matches())
                {
                    et_account.setError("账号错误!");
                    baccount=false;
                    bt_regist.setEnabled(false);
                    bt_regist.setBackgroundResource(R.drawable.btn_unable);
                }
                else {
                    if(MySqlHandle.valiuser(et_account.getText().toString())) {
                        et_account.setError("帐号已注册!");

                    }
                    else {
                        baccount = true;
                        if (bvali==true&&baccount==true&&bpass==true&&bsurepass==true) {
                            bt_regist.setEnabled(true);
                            bt_regist.setBackgroundResource(R.drawable.btn_regist);
                        }
                    }
                }
            }
        });
        et_pass= (EditText) findViewById(R.id.et_pass);
        et_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern pattern=Pattern.compile("[A-Za-z0-9]{6,15}");
                String pass=et_pass.getText().toString();
                Matcher m=pattern.matcher(pass);
                et_surepass.setText("");
                if(TextUtils.isEmpty(pass))
                {
                    et_pass.setError("不能为空!");
                    bpass=false;
                }
                else if(!m.matches())
                {
                    et_pass.setError("6~15位字母数字组合!");
                    bpass=false;
                }
                else {
                    bpass = true;
                }
            }
        });
        et_surepass= (EditText) findViewById(R.id.et_surepass);
        et_surepass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern pattern=Pattern.compile("[A-Za-z0-9]{6,15}");
                String surepass=et_surepass.getText().toString();
                Matcher m=pattern.matcher(surepass);
                if(TextUtils.isEmpty(surepass))
                {
                    et_surepass.setError("不能为空!");
                    bsurepass=false;
                    bt_regist.setEnabled(false);
                    bt_regist.setBackgroundResource(R.drawable.btn_unable);
                }
                else if(!m.matches())
                {
                    et_surepass.setError("6~15位字母数字组合!");
                    bsurepass=false;
                    bt_regist.setEnabled(false);
                    bt_regist.setBackgroundResource(R.drawable.btn_unable);
                }
                else if(!surepass.equals(et_pass.getText().toString())){
                    et_surepass.setError("两次密码不一致!");
                    bsurepass=false;
                    bt_regist.setEnabled(false);
                    bt_regist.setBackgroundResource(R.drawable.btn_unable);
                }
                else {
                    bsurepass = true;
                    if(bvali==true&&baccount==true&&bpass==true){
                        bt_regist.setEnabled(true);
                        bt_regist.setBackgroundResource(R.drawable.btn_regist);
                    }
                }
            }
        });
        et_verlidation= (EditText) findViewById(R.id.et_verlidation);
        et_verlidation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String strCode = et_verlidation.getText().toString();
                if (null != strCode && strCode.length() == 4){
                    bvali=true;
                    if(bvali==true&&baccount==true&&bpass==true){
                        bt_regist.setEnabled(true);
                        bt_regist.setBackgroundResource(R.drawable.btn_regist);
                    }
                }
                else{
                    et_verlidation.setError("验证码格式错误");
                    bvali=false;
                    bt_regist.setEnabled(false);
                    bt_regist.setBackgroundResource(R.drawable.btn_unable);
                }
            }
        });
        bt_getverli= (Button) findViewById(R.id.bt_getverli);
        bt_getverli.setOnClickListener(this);
        bt_getverli.setEnabled(true);
        bt_getverli.setBackgroundResource(R.drawable.btn_regist);
        bt_regist= (Button) findViewById(R.id.bt_regist);
        bt_regist.setOnClickListener(this);
        bt_regist.setEnabled(false);
        bt_regist.setBackgroundResource(R.drawable.btn_unable);
        tv_forget= (TextView) findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);
        tv_forget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        myback= (ImageButton) findViewById(R.id.myback);
        myback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //需要验证自己申请的  AppKey
        strPhoneNumber="";

    }
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (result == SMSSDK.RESULT_COMPLETE) { //回调  当返回的结果是complete
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码
                            Toast.makeText(RegisterActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码
                            MySqlHandle.addUser(et_account.getText().toString(),et_pass.getText().toString());
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    } else { //进行操作出错，通过下面的信息区分析错误原因
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            //错误代码：  http://wiki.mob.com/android-api-%E9%94%99%E8%AF%AF%E7%A0%81%E5%8F%82%E8%80%83/
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 0x01:
                    bt_getverli.setText("重新发送(" + msg.arg1 + ")");
                    break;
                case 0x02:
                    bt_getverli.setText("获取验证码");
                    bt_getverli.setBackgroundResource(R.drawable.btn_regist);
                    bt_getverli.setClickable(true);
                    bgetvalid=false;
                    break;
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁
        SMSSDK.unregisterEventHandler(eventHandler);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_forget) {
            Intent intent = new Intent();
            //intent.setClass(RegisterActivity.this, ForgetActivity.class);
            intent.setClass(RegisterActivity.this,ForgetActivity.class);
            startActivity(intent);
            finish();
        }
        if(v.getId()==R.id.bt_getverli){
            strPhoneNumber = et_account.getText().toString();
            if (baccount==false) {
                Toast.makeText(RegisterActivity.this, "手机号码格式错误", Toast.LENGTH_SHORT).show();
                return;
            }
            //验证通过 将发送短信到这个号码上面
            SMSSDK.getVerificationCode("86", strPhoneNumber);
            bt_getverli.setClickable(false);
            bt_getverli.setBackgroundResource(R.drawable.btn_unable);
            bgetvalid=true;
            //开启线程去更新button的text
            new Thread() {
                @Override
                public void run() {
                    int totalTime = 60;
                    for (int i = 0; i < totalTime; i++) {
                        Message message = myHandler.obtainMessage(0x01);
                        message.arg1 = totalTime - i;
                        myHandler.sendMessage(message);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    myHandler.sendEmptyMessage(0x02);
                }
            }.start();
        }
        if(v.getId()==R.id.bt_regist){
            if(bgetvalid)
            {
                SMSSDK.submitVerificationCode("86", strPhoneNumber, et_verlidation.getText().toString());
            }
            else{
                Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //监听手机返回键
}

