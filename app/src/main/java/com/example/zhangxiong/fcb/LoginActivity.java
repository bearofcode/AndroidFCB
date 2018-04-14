package com.example.zhangxiong.fcb;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    static String Account=new String();
    static ProgressDialog p;
    static int state=0;//0表示个人中心更换头像，1表示上传车辆图片
    EditText et_account,et_pass;
    Button bt_login;
    TextView tv_forget;
    boolean baccount,bpass;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    CheckBox remermber;
    boolean isremember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        if(!MySqlHandle.isconnect)
        MySqlHandle.connect();
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(LoginActivity.this, permissions, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    public void init(){
        baccount=false;
        bpass=false;
        et_account= (EditText) findViewById(R.id.et_account);
        et_pass= (EditText) findViewById(R.id.et_pass);
        bt_login= (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        tv_forget= (TextView) findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);
        remermber=(CheckBox)findViewById(R.id.remember);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        isremember=pref.getBoolean("isremember",false);
        if(isremember){
            et_account.setText(pref.getString("account",""));
            et_pass.setText(pref.getString("pass",""));
            remermber.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.bt_login){
            String account=et_account.getText().toString();
            String pass=et_pass.getText().toString();
            if(MySqlHandle.login(account,pass)) {
                editor=pref.edit();
                if(remermber.isChecked()){
                    editor.putBoolean("isremember",true);
                    editor.putString("account",account);
                    editor.putString("pass",pass);
                }
                else
                {
                    editor.clear();
                }
                editor.apply();
                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                Account=account;
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                //finish();
            }else{
                Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
            }
        }
        if(v.getId()==R.id.tv_forget)
        {
            Intent intent=new Intent();
            intent.setClass(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        }
    }

    //监听手机返回键
    public void onBackPressed()
    {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog dialog = builder.create();
            View dialogView = View.inflate(this, R.layout.dialog_hinit, null);
            TextView tv_name= (TextView) dialogView.findViewById(R.id.tv_name);
            tv_name.setText("确定退出程序？");
            final TextView bt_sure=(TextView)dialogView.findViewById(R.id.bt_sure);
            final TextView bt_cancel=(TextView)dialogView.findViewById(R.id.bt_cancel);
            bt_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    home.addCategory(Intent.CATEGORY_HOME);
                    startActivity(home);
                    if(MySqlHandle.isconnect)
                       MySqlHandle.closeconnect();
                    finish();
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
}
