package com.example.zhangxiong.fcb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_username;
    Button bt_personal, bt_carup, bt_introduce, bt_cartransport, bt_aboutus, bt_carhand, bt_idea;
    ImageButton myback;
    static ImageView head;
    User myuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        myuser = MySqlHandle.getuesr(LoginActivity.Account);
        head= (ImageView) findViewById(R.id.head);
        head.setImageResource(R.mipmap.headw);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_username.setText(myuser.getName());
        tv_username.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        bt_personal = (Button) findViewById(R.id.bt_personal);
        bt_personal.setOnClickListener(this);

        bt_carup = (Button) findViewById(R.id.bt_carup);
        bt_carup.setOnClickListener(this);

        bt_introduce = (Button) findViewById(R.id.bt_introduce);
        bt_introduce.setOnClickListener(this);

        bt_cartransport = (Button) findViewById(R.id.bt_cartransport);
        bt_cartransport.setOnClickListener(this);

        bt_aboutus = (Button) findViewById(R.id.bt_aboutus);
        bt_aboutus.setOnClickListener(this);

        bt_carhand = (Button) findViewById(R.id.bt_carhand);
        bt_carhand.setOnClickListener(this);

        bt_idea = (Button) findViewById(R.id.bt_idea);
        bt_idea.setOnClickListener(this);

        myback = (ImageButton) findViewById(R.id.myback);
        myback.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_personal) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, PersonalActivity.class);
            startActivityForResult(intent,1);
        }
        //上传信息
        if (v.getId() == R.id.bt_carup) {
            LocationManager locationManager
                    = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
            boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (gps == false && network == false) {
                Toast.makeText(MainActivity.this, "该功能需开启GPS,请开启GPS定位功能", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, UpCarActivity.class);
                startActivity(intent);
            }
        }
        //介绍推广
        if (v.getId() == R.id.bt_introduce) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, IntroduceActivity.class);
            startActivity(intent);
        }
        //车辆运输信息
        if (v.getId() == R.id.bt_cartransport) {
            LocationManager locationManager
                    = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
            boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (gps == false && network == false) {
                Toast.makeText(MainActivity.this, "该功能需开启GPS,请开启GPS定位功能", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MyMapActivity.class);
                startActivity(intent);
            }
        }
        //关于我们
        if (v.getId() == R.id.bt_aboutus) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AboutusActivity.class);
            startActivity(intent);
        }
        //车辆处理信息
        if (v.getId() == R.id.bt_carhand) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, RecordActivity.class);
            startActivity(intent);
        }
        //意见反馈
        if (v.getId() == R.id.bt_idea) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, IdearActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.myback) {
            onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:if(resultCode==RESULT_OK){
                tv_username.setText(data.getStringExtra("name"));
            }
            break;
            default:break;
        }
    }

    //监听手机返回键
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_hinit, null);
        TextView tv_name = (TextView) dialogView.findViewById(R.id.tv_name);
        tv_name.setText("确定退出程序？");
        final TextView bt_sure = (TextView) dialogView.findViewById(R.id.bt_sure);
        final TextView bt_cancel = (TextView) dialogView.findViewById(R.id.bt_cancel);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                finish();
                MySqlHandle.closeconnect();
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
