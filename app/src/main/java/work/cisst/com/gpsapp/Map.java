package work.cisst.com.gpsapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import work.cisst.com.gpsapp.Tool.BDLocationUtils;
import work.cisst.com.gpsapp.Tool.GpsUtils;
import work.cisst.com.gpsapp.Tool.LocationUtils;
import work.cisst.com.gpsapp.entity.Positions;
import work.cisst.com.gpsapp.mapper.PositionsMapper;


/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 */
public class Map extends Activity implements View.OnClickListener {

    Integer id = 1;
    // 定位相关
    LocationClient mLocClient;
   // MyLocationListenner myListener = new MyLocationListenner();
    MapView mMapView;
    BaiduMap mBaiduMap;
    List<Positions> positions;
    private TextView btnbegin,btnover;
    private Button btnover2,btnover1;//时间按钮
    private Calendar cal;
    TimerTask task = null;

    private int year,month,day;
    private String beginTime,endTime;
    private int sizePosition = 2; //每次回放便利的大小

    // UI相关
    OnCheckedChangeListener radioButtonListener;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        id = intent.getIntExtra("id",1);
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
            local = GpsUtils.gps84_To_bd09(location.getLatitude(),location.getLongitude());
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

        //获取当前日期  开始
        getbeginDate();
        btnbegin=(TextView) findViewById(R.id.btnbegin);
        btnbegin.setOnClickListener(this);

        //获取当前日期  结束
        getoverDate();
        btnover=(TextView) findViewById(R.id.btnover);
        btnover.setOnClickListener(this);

        //获取当前日期 开始
        btnover1 = (Button) findViewById(R.id.btnover1);
        btnover1.setOnClickListener(this);
        //获取当前日期 开始
        btnover2 = (Button) findViewById(R.id.btnover2);
        btnover2.setOnClickListener(this);
        findViewById(R.id.btnselect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.clear();
                beginTime = btnbegin.getText().toString() + " " + btnover1.getText().toString()+":"+"00";
                endTime = btnover.getText().toString() + " " + btnover2.getText().toString()+":"+"00";
                PositionsMapper positionsMapper = new PositionsMapper();
                List<Positions> list = positionsMapper.getListByTime(beginTime, endTime, id);
                positions = list;
                if(list.size()<=0){
                    Toast.makeText(Map.this,"这个时间段内没有定位数据",Toast.LENGTH_LONG).show();
                }else{
                    showItbyStype();
                }

            }
        });



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

    public void getDoIt() {
        LatLng p1 = new LatLng(39.97923, 116.357428);
        LatLng p2 = new LatLng(39.94923, 116.397428);
        LatLng p3 = new LatLng(39.97923, 116.437428);
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        //设置折线的属性

        OverlayOptions mOverlayOptions = new PolylineOptions()
                .width(10)
                .color(0xAAFF0000)
                .points(points);
        //在地图上绘制折线
        //mPloyline 折线对象

        Overlay mPolyline = mBaiduMap.addOverlay(mOverlayOptions);
    }

    //获取到的数据，展示出来，按照时间
    public void showItbyStype() {
        Timer  timer = new Timer();
        if(task != null){
            task.cancel();
        }
        task = new TimerTask() {
            @Override
            public synchronized void run() {
                List<LatLng> points = new ArrayList<LatLng>();
                List<Integer> indexList = new ArrayList<>();
                indexList.add(0);
                LatLng star = new LatLng(positions.get(0).getLat(), positions.get(0).getLng());
                points.add(star);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(star).zoom(18.0f);
                Long oldTime = positions.get(0).getTime().getTime();
                LatLng p1 = null;
                if(sizePosition<=positions.size()) {
                    for (int i = 1; i < sizePosition; i++) {
                        mBaiduMap.clear();
                        Positions position = positions.get(i);
                        Long time = position.getTime().getTime();
                        oldTime = time;
                        p1 = new LatLng(position.getLat(), position.getLng());
                        //添加点进去
                        points.add(p1);
                    }
                    MapStatus mapStatus = new MapStatus.Builder()
                            .target(p1)
                            .zoom(20F)
                            .build();
                    //设置地图状态变更
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                            .newMapStatus(mapStatus);
                    mBaiduMap.setMapStatus(mapStatusUpdate);
                    sizePosition++;
                    drawLine(points);
                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
                    OverlayOptions options = new MarkerOptions()
                            .position(star)
                            .icon(bitmap);
                    mBaiduMap.addOverlay(options);
                } else{
                    LatLng end = new LatLng(positions.get(positions.size() - 1).getLat(), positions.get(positions.size() - 1).getLng());
                    BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
                    OverlayOptions options2 = new MarkerOptions()
                            .position(end)
                            .icon(bitmap2);
                    mBaiduMap.addOverlay(options2);
                    this.cancel();
                }
            }
        };
        timer.schedule(task,2,3000);
    }
    //获取当前日期  开始
    private void getbeginDate() {
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
    }
    //获取当前日期  结束
    private void getoverDate() {
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
    }


    private void drawLine( List<LatLng> points) {
        //在每次画线之前，需要先清除以前画的
        OverlayOptions ooPolyline = new PolylineOptions().width(5)
                .color(0xAAFF0000).points(points);
        mBaiduMap.addOverlay(ooPolyline);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnbegin:
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        btnbegin.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(Map.this, DatePickerDialog.THEME_HOLO_LIGHT,listener,year,month,day);//主题在这里！后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();
                break;

            case R.id.btnover:
                DatePickerDialog.OnDateSetListener listener1=new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        btnover.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                    }
                };
                DatePickerDialog dialog1=new DatePickerDialog(Map.this, DatePickerDialog.THEME_HOLO_LIGHT,listener1,year,month,day);//主题在这里！后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog1.show();
                break;
            case R.id.btnover1:
                TimePickerDialog timePickerDialog = new TimePickerDialog(Map.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        String hour = String.valueOf(hourOfDay),
                                min=String.valueOf(minute);
                        if(hourOfDay<10){
                            hour = "0" + hourOfDay;
                        }
                        if(minute<10 && minute!=60){
                            min = "0" + minute;
                        }
                        btnover1.setText(hour+":"+min);
                    }
                },1,1,true);
                timePickerDialog.show();          //显示时间设置对话框
                break;
            case R.id.btnover2:
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(Map.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        String hour = String.valueOf(hourOfDay),
                                min=String.valueOf(minute);
                        if(hourOfDay<10){
                            hour = "0" + hourOfDay;
                        }
                        if(minute<10 && minute!=60){
                            min = "0" + minute;
                        }
                        btnover2.setText(hour+":"+min);
                    }
                },1,2,true);
                timePickerDialog1.show();          //显示时间设置对话框
                // break;

            default:
                break;
        }
    }

}