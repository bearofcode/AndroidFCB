package com.example.zhangxiong.fcb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;

public class MyMapActivity extends AppCompatActivity {

    public LocationClient mLocationClient;

    private MapView mapView;

    private BaiduMap baiduMap;

    private boolean isFirstLocate = true;
    double a,b;
    //要显示的pop
    private View pop;
    ImageView photo;
    //pop中的文本信息
    private TextView title;
    ArrayList<MarkerOptions> l_mark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_my_map);
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //处理点击 ,当点击的时候更新并且显示位置
                MapViewLayoutParams params = new MapViewLayoutParams.Builder().
                        layoutMode(MapViewLayoutParams.ELayoutMode.mapMode) //按照经纬度设置位置
                        .position(marker.getPosition()) //这个坐标无所谓的，但是不能传null
                        .width(MapViewLayoutParams.WRAP_CONTENT)  //宽度
                        .height(MapViewLayoutParams.WRAP_CONTENT)  //高度
                        .yOffset(-70)  //相距  正值往下  负值往上
                        .build();
                mapView.updateViewLayout(pop, params);
                pop.setVisibility(View.VISIBLE);
                //更新下title
                title.setText(marker.getTitle());
                int rid=marker.getExtraInfo().getInt("rid");
                byte[] imgData=MySqlHandle.getrecord(rid).getPhoto();
                Bitmap imagebitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                photo.setImageBitmap(imagebitmap);
                return true;
            }
        });
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                pop.setVisibility(View.GONE);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        baiduMap.setMyLocationEnabled(true);
        MapStatusUpdate update = MapStatusUpdateFactory.zoomTo(17f);
        baiduMap.animateMapStatus(update);
        requestLocation();
        initPop();
        ArrayList<Record> l_record=MySqlHandle.getAllrecord(LoginActivity.Account);
        l_mark=new ArrayList<MarkerOptions>(l_record.size());
        for(Record re:l_record) {
            MarkerOptions m = new MarkerOptions();
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gps); // 描述图片
           Bundle bundle=new Bundle();
            bundle.putSerializable("rid",re.getRid());
            m.position(new LatLng(re.getWeidu(),re.getJindu())) // 设置位置
                    .icon(bitmap) // 加载图片
                    .draggable(false)// 支持拖拽
                    .title(re.getCarnumber())//显示文本
                    .extraInfo(bundle);
            l_mark.add(m);
        }

    }
    //初始化pop
    private void initPop() {
        pop = View.inflate(getApplicationContext(), R.layout.mypop, null);
        //必须使用百度的params
        MapViewLayoutParams params = new MapViewLayoutParams.Builder().layoutMode(MapViewLayoutParams.ELayoutMode.mapMode) //按照经纬度设置
                .position(new LatLng(22.5422870000, 113.9804440000)) //这个坐标无所谓的，但是不能传null
                .width(MapViewLayoutParams.WRAP_CONTENT)  //宽度
                .height(MapViewLayoutParams.WRAP_CONTENT)  //高度
                .build();
        mapView.addView(pop,params);
        //先设置隐藏，点击的时候显示
        pop.setVisibility(View.INVISIBLE);
        //初始化这个title
        title = (TextView) pop.findViewById(R.id.title);
        photo=(ImageView) findViewById(R.id.photo);
    }

    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            Toast.makeText(this, "定位到" +location.getAddrStr(), Toast.LENGTH_SHORT).show();
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        a=location.getLatitude()-0.002;
        b=location.getLongitude();
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        baiduMap.setMyLocationData(locData);
        drawMark();
    }

    private void drawMark() {
            for(MarkerOptions m:l_mark){
                //把绘制的圆添加到百度地图上去
               baiduMap.addOverlay(m);
            }




    }
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            navigateTo(location);
        }
    }
}

