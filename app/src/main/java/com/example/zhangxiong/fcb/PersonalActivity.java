package com.example.zhangxiong.fcb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalActivity extends AppCompatActivity implements View.OnClickListener{

    boolean bphone,bname,boldpass,bnewpass,bsurepass;
    RelativeLayout updata_name,updata_account,updata_pass,exit;
    TextView tv_nichen,tv_phone,tv_money;
    ImageButton myback;
    static ImageView phead;
    User myuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        init();
        settextview(LoginActivity.Account);
    }

    public void settextview(String account){
        myuser=MySqlHandle.getuesr(account);
        tv_nichen.setText(myuser.getName());
        tv_phone.setText(myuser.getAccount());
        tv_money.setText(String.valueOf(myuser.getMoney()));
    }
    private void init() {
        bphone=bname=boldpass=bnewpass=bsurepass=false;
        phead= (ImageView) findViewById(R.id.phead);
        phead.setOnClickListener(this);
//        byte[] imgData = myuser.getHead();
//        if (imgData != null) {
//            //将字节数组转化为位图
//            Bitmap imagebitmap = null;
//            try {
//                imagebitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
//            } catch (OutOfMemoryError e) {
//                e.printStackTrace();
//            }
//            //将位图显示为图片
//            phead.setImageBitmap(imagebitmap);
//        } else {
//            phead.setBackgroundResource(R.mipmap.headw);
//        }
        updata_name= (RelativeLayout) findViewById(R.id.updata_name);
        updata_name.setOnClickListener(this);
        updata_account= (RelativeLayout) findViewById(R.id.updata_account);
        updata_account.setOnClickListener(this);
        updata_pass= (RelativeLayout) findViewById(R.id.updata_pass);
        updata_pass.setOnClickListener(this);
        exit= (RelativeLayout) findViewById(R.id.exit);
        exit.setOnClickListener(this);

        tv_nichen= (TextView) findViewById(R.id.tv_nichen);
        tv_phone=(TextView) findViewById(R.id.tv_phone);
        tv_money=(TextView) findViewById(R.id.tv_money);

        myback= (ImageButton) findViewById(R.id.myback);
        myback.setOnClickListener(this);
    }

public void resetname(){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    final AlertDialog dialog = builder.create();
    View dialogView = View.inflate(this, R.layout.dialog_updata, null);
    TextView tv_title= (TextView) dialogView.findViewById(R.id.tv_title);
    tv_title.setText("修改昵称");
    final TextView bt_sure=(TextView)dialogView.findViewById(R.id.bt_sure);
    final TextView bt_cancel=(TextView)dialogView.findViewById(R.id.bt_cancel);
    final EditText et_name= (EditText) dialogView.findViewById(R.id.et_name);
    bt_sure.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(bname) {
               MySqlHandle.updateuser(LoginActivity.Account, "name", et_name.getText().toString());
                Toast.makeText(PersonalActivity.this, "修改昵称成功", Toast.LENGTH_SHORT).show();
                tv_nichen.setText(et_name.getText().toString());
                Intent intent=new Intent();
                intent.putExtra("name",et_name.getText().toString());
                setResult(RESULT_OK,intent);
                dialog.dismiss();
            }
            else {
                Toast.makeText(PersonalActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                et_name.setText("");
            }

        }
    });
    bt_cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    });
    et_name.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @Override
        public void afterTextChanged(Editable s) {
            Pattern pattern= Pattern.compile("[\\u4e00-\\u9fa5A-Za-z0-9]{1,12}");
            String name=et_name.getText().toString();
            Matcher m=pattern.matcher(name);
            if(TextUtils.isEmpty(name))
            {
                et_name.setError("不能为空!");
                bname=false;
            }
            else if(!m.matches())
            {
                et_name.setError("1-12位汉字、字母、数字组合!");
                bname=false;
            }
            else {
                bname=true;
            }
        }
    });
    dialog.setView(dialogView);
    dialog.show();
}

    private void resetaccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_updata, null);
        TextView tv_title= (TextView) dialogView.findViewById(R.id.tv_title);
        tv_title.setText("修改手机号码");
        final TextView bt_sure=(TextView)dialogView.findViewById(R.id.bt_sure);
        final TextView bt_cancel=(TextView)dialogView.findViewById(R.id.bt_cancel);
        final EditText et_name= (EditText) dialogView.findViewById(R.id.et_name);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bphone) {
                    MySqlHandle.updateuser(LoginActivity.Account, "account", et_name.getText().toString());
                    MySqlHandle.updateaccount(LoginActivity.Account,et_name.getText().toString());
                    Toast.makeText(PersonalActivity.this, "修改手机号码成功\n     请重新登陆", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(PersonalActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(PersonalActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    et_name.setText("");
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                Pattern pattern= Pattern.compile("[1]\\d{10}");
                String name=et_name.getText().toString();
                Matcher m=pattern.matcher(name);
                if(TextUtils.isEmpty(name))
                {
                    et_name.setError("不能为空!");
                    bphone=false;
                }
                else if(!m.matches())
                {
                    et_name.setError("手机号码错误!");
                    bphone=false;
                }
                else if(MySqlHandle.valiuser(name)) {
                    et_name.setError("该手机号码已被注册!");
                    bphone=false;
                }
                else{
                    bphone=true;
                }
            }
        });
        dialog.setView(dialogView);
        dialog.show();
    }
    private void resetpass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_updata_pass, null);
        final TextView bt_sure=(TextView)dialogView.findViewById(R.id.bt_sure);
        final TextView bt_cancel=(TextView)dialogView.findViewById(R.id.bt_cancel);
        final EditText et_oldpass= (EditText) dialogView.findViewById(R.id.et_oldpass);
        final EditText et_newpass= (EditText) dialogView.findViewById(R.id.et_newpass);
        final EditText et_surepass= (EditText) dialogView.findViewById(R.id.et_surepass);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bnewpass==true&&bsurepass==true&&boldpass==true) {
                    MySqlHandle.updateuser(LoginActivity.Account, "pass", et_surepass.getText().toString());
                    Toast.makeText(PersonalActivity.this, "修改密码成功\n 请重新登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(PersonalActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(PersonalActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                    et_oldpass.setText("");
                    et_surepass.setText("");
                    et_newpass.setText("");
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        et_oldpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!MySqlHandle.login(LoginActivity.Account,et_oldpass.getText().toString()))
                {
                    et_oldpass.setError("密码错误!");
                    boldpass=false;
                }
                else {
                    boldpass=true;
                }
            }
        });
        et_newpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern pattern=Pattern.compile("[A-Za-z0-9]{6,15}");
                String pass=et_newpass.getText().toString();
                Matcher m=pattern.matcher(pass);
                et_surepass.setText("");
                if(TextUtils.isEmpty(pass))
                {
                    et_newpass.setError("不能为空!");
                    bnewpass=false;
                }
                else if(!m.matches())
                {
                    et_newpass.setError("6~15位字母数字组合!");
                    bnewpass=false;
                }
                else
                {
                    bnewpass=true;
                }
            }
        });
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
                String pass=et_surepass.getText().toString();
                Matcher m=pattern.matcher(pass);
                if(TextUtils.isEmpty(pass))
                {
                    et_surepass.setError("不能为空!");
                    bsurepass=false;
                }
                else if(!m.matches())
                {
                    et_surepass.setError("6~15位字母数字组合!");
                    bsurepass=false;
                }
                else if(!pass.equals(et_newpass.getText().toString())){
                    et_surepass.setError("两次密码不一致!");
                    bsurepass=false;
                }else
                {
                    bsurepass=true;
                }
            }
        });
        dialog.setView(dialogView);
        dialog.show();
    }
    private void myexit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_hinit, null);
        TextView tv_name= (TextView) dialogView.findViewById(R.id.tv_name);
        tv_name.setText("确定注销当前账号？");
        final TextView bt_sure=(TextView)dialogView.findViewById(R.id.bt_sure);
        final TextView bt_cancel=(TextView)dialogView.findViewById(R.id.bt_cancel);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(PersonalActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(dialogView);
        dialog.show();

    }

    @Override
    public void onClick(View v) {
//        if(v.getId()==R.id.phead){
//            LoginActivity.state=0;
//            startActivity(new Intent(PersonalActivity.this,SelectPicPopupWindow.class));
//        }
        if(v.getId()==R.id.updata_name){
            resetname();
        }
        if(v.getId()==R.id.updata_account){
            resetaccount();
        }
        if(v.getId()==R.id.updata_pass){
            resetpass();
        }
        if(v.getId()==R.id.exit){
            myexit();
        }
        if(v.getId()==R.id.myback){
            onBackPressed();
        }
    }
}
