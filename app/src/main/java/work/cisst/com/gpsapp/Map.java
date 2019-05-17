package work.cisst.com.gpsapp;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import work.cisst.com.gpsapp.Tool.BDLocationUtils;
import work.cisst.com.gpsapp.Tool.GpsUtils;
import work.cisst.com.gpsapp.Tool.LocationUtils;


/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 */
public class Map extends Activity {

    // 定位相关
    LocationClient mLocClient;
   // MyLocationListenner myListener = new MyLocationListenner();
    MapView mMapView;
    BaiduMap mBaiduMap;

    // UI相关
    OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09"); // 设置坐标类型
        option.setScanSpan(1000);
        Location location = LocationUtils.getInstance( Map.this ).showLocation();
        double[] local = new double[2];
        if (location != null) {
            String address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();
            local = GpsUtils.gcj02_To_Bd09(location.getLatitude(),location.getLongitude());
        }
        LatLng ll = new LatLng(local[0],local[1]);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bigtrack);
        OverlayOptions options = new MarkerOptions()
                .position(ll)
                .icon(bitmap);
        mBaiduMap.addOverlay(options);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }


    /**
     * 定位SDK监听函数
     *//*
    public class MyLocationListenner extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
        }
    }*/

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }


}
