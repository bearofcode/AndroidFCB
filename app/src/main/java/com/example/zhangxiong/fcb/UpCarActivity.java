package com.example.zhangxiong.fcb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpCarActivity extends AppCompatActivity implements View.OnClickListener{

    public LocationClient locationClient;
    EditText et_carnumber;
    static ImageView iv_photo;
    TextView tv_address,tv_jindu,tv_weidu;
    Button bt_locate,bt_up;
    static Uri imageUri;
    static boolean up_photo_success=false;
    boolean bcarnumber;
    ImageButton bt_back;
    CheckBox check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient=new LocationClient(getApplicationContext());

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                StringBuilder currentPosition = new StringBuilder();
                currentPosition.append(location.getCountry());
                currentPosition.append(location.getProvince());
                currentPosition.append(location.getCity());
                currentPosition.append(location.getDistrict());
                currentPosition.append(location.getStreet());
                tv_address.setText(currentPosition);
                tv_jindu.setText(String.valueOf(location.getLongitude()));
                tv_weidu.setText(String.valueOf(location.getLatitude()));
            }
        });
        setContentView(R.layout.activity_upcar);
        init();
    }
    private void requestLocation() {
        initLocation();
        locationClient.start();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        locationClient.setLocOption(option);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stop();
    }
    private void init() {
        check= (CheckBox) findViewById(R.id.check);
        check.setOnClickListener(this);
        bcarnumber=false;
        et_carnumber= (EditText) findViewById(R.id.et_carnumber);
        et_carnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!check.isChecked()) {
                    if(!et_carnumber.getText().toString().equals("无牌车")) {
                        Pattern pattern = Pattern.compile("^[冀豫云辽黑湘皖鲁苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼渝京津沪新京军空海北沈兰济南广成使领A-Z]{1}[a-zA-Z0-9]{5}[a-zA-Z0-9挂学警港澳]{1}$");
                        String carnu = et_carnumber.getText().toString();
                        Matcher m = pattern.matcher(carnu);
                        if (TextUtils.isEmpty(carnu) || m.matches() == false) {
                            et_carnumber.setError("格式错误");
                            bcarnumber = false;
                        } else if (MySqlHandle.valicarnum(carnu)) {
                            et_carnumber.setError("该车牌号已被上传!");
                            bcarnumber = false;
                        } else {
                            bcarnumber = true;
                        }
                    }else{
                        bcarnumber = true;
                    }
                }
            }
        });

        iv_photo= (ImageView) findViewById(R.id.iv_photo);
        iv_photo.setImageResource(R.mipmap.plussmall);
        iv_photo.setOnClickListener(this);

        tv_address= (TextView) findViewById(R.id.tv_address);
        tv_jindu= (TextView) findViewById(R.id.tv_jindu);
        tv_weidu= (TextView) findViewById(R.id.tv_weidu);

        bt_locate= (Button) findViewById(R.id.bt_locate);
        bt_locate.setOnClickListener(this);

        bt_up= (Button) findViewById(R.id.bt_up);
        bt_up.setOnClickListener(this);
        bt_back= (ImageButton) findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private byte[] getIconData(Bitmap bitmap){
        int size = bitmap.getWidth()*bitmap.getHeight()*4;
        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
    private void up_all(){
        Record record=new Record();
        record.setCarnumber(et_carnumber.getText().toString());
        record.setAccount(LoginActivity.Account);
        record.setAddress(tv_address.getText().toString());
        record.setJindu(Double.valueOf(tv_jindu.getText().toString()));
        record.setWeidu(Double.valueOf(tv_weidu.getText().toString()));
        record.setState(0);
        iv_photo.setDrawingCacheEnabled(true);
        Bitmap bm = iv_photo.getDrawingCache();
        record.setPhoto(getIconData(bm));
        Calendar calendar = Calendar.getInstance();
        StringBuffer date = new StringBuffer();
        int myear = calendar.get(Calendar.YEAR);
        int mmonth = calendar.get(Calendar.MONTH) + 1;
        int mday = calendar.get(Calendar.DAY_OF_MONTH);
        date.append(String.valueOf(myear)).append("年").append(String.valueOf(mmonth)).append("月").append(mday).append("日");
        record.setDate(String.valueOf(date));
        MySqlHandle.addRecord(record);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_photo){
            LoginActivity.state=1;
            startActivity(new Intent(UpCarActivity.this,SelectPicPopupWindow.class));
        }
        if(v.getId()==R.id.bt_locate){
            requestLocation();
        }
        if(v.getId()==R.id.check){
            if(check.isChecked()){
                et_carnumber.setEnabled(false);
                bcarnumber=true;
                et_carnumber.setText("无牌车");
            }else{
                et_carnumber.setEnabled(true);
            }
        }
        if(v.getId()==R.id.bt_up){
            if(bcarnumber==false||tv_address.getText().toString().equals("")
                    ||tv_jindu.getText().toString().equals("")
                    ||tv_weidu.getText().toString().equals("")||up_photo_success==false){
                Toast.makeText(this,"请完善信息后上传",Toast.LENGTH_SHORT).show();
            }else if(MySqlHandle.valicarnum(et_carnumber.getText().toString())&&!et_carnumber.getText().toString().equals("无牌车")){
                Toast.makeText(this,"该车牌号已上传",Toast.LENGTH_SHORT).show();
            }
            else{
                up_all();
                Toast.makeText(this,"上传成功,等待审核",Toast.LENGTH_SHORT).show();
            }
        }
    }
}