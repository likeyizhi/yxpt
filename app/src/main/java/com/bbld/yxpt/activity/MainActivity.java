package com.bbld.yxpt.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.Login;
import com.bbld.yxpt.bean.OrderReturnInfo;
import com.bbld.yxpt.bean.ShopList;
import com.bbld.yxpt.bean.ShopListPage;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.update.UpdateService;
import com.bbld.yxpt.utils.MyToken;
import com.bbld.yxpt.zxing.android.CaptureActivity;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;
import com.wuxiaolong.androidutils.library.NetConnectUtil;
import com.wuxiaolong.androidutils.library.VersionUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends BaseActivity {
    @BindView(R.id.mapView)
    MapView mMapView;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    @BindView(R.id.rlBottomWhite)
    RelativeLayout rlBottomWhite;
    @BindView(R.id.llBottomIn)
    LinearLayout llBottomIn;
    @BindView(R.id.rlFirst)
    RelativeLayout rlFirst;
    @BindView(R.id.rlSecond)
    RelativeLayout rlSecond;
    @BindView(R.id.ivLeft01)
    ImageView ivLeft01;
    @BindView(R.id.ivRight01)
    ImageView ivRight01;
    @BindView(R.id.ivScanCode)
    ImageView ivScanCode;
    @BindView(R.id.ivScanCode02)
    ImageView ivScanCode02;
    @BindView(R.id.ivMore)
    ImageView ivMore;
    @BindView(R.id.tvShopName)
    TextView tvShopName;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvOpenTime)
    TextView tvOpenTime;
    @BindView(R.id.tvCurrentAddress)
    TextView tvCurrentAddress;
    @BindView(R.id.lvShopListPage)
    ListView lvShopListPage;
    @BindView(R.id.ivLeft02)
    ImageView ivLeft02;
    @BindView(R.id.ivRight02)
    ImageView ivRight02;
    @BindView(R.id.rlRight01)
    RelativeLayout rlRight01;
    @BindView(R.id.llSearchLoc)
    LinearLayout llSearchLoc;
    @BindView(R.id.tvNearShopCount)
    TextView tvNearShopCount;
    @BindView(R.id.tvGoHere)
    TextView tvGoHere;


    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    BaiduMap mBaiduMap;
    // UI相关
    RadioGroup.OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private InfoWindow mInfoWindow;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);
    BitmapDescriptor bdB = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_markb);
    BitmapDescriptor bdC = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_markc);
    BitmapDescriptor bdD = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_markd);
    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);
    BitmapDescriptor bdGround = BitmapDescriptorFactory
            .fromResource(R.drawable.ground_overlay);
    private ArrayList<Marker> markerList;
    private List<ShopList.ShopListShopList> shopList;
    private Marker mMarker;
    private String mCurrentAddress;
    private int pageIndex=1;
    private int pageSize=10;
    private List<ShopListPage.ShopListPageShopList> shopListPage;
    private ShopListPageAdapter adapter;
    private String shopId;
    private String shopX;
    private String shopY;
    // 获取反地理编码对象
    private GeoCoder mGeoCoder = GeoCoder.newInstance();
    private String currentAddr;
    private static final int IS_SCAN=1;
    private static final int IS_GO=2;
    private int isWhat=IS_SCAN;
    private static final int BAIDU_READ_PHONE_STATE =100;
    private static final String TOKEN=null;
    private String mCurrentProvince;
    private String mCurrentCity;
    private String mCurrentDistrict;
    private String mCurrentStreet;
    private String backKey="";
    private String backX;
    private String backY;
    private ArrayList<BitmapDescriptor> bdImgs;
    private Dialog mWeiboDialog;
    private String isBack="noback";

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
//            Log.d(LTAG, "action: " + s);
//            TextView text = (TextView) findViewById(R.id.text_Info);
//            text.setTextColor(Color.RED);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                showToast("key 验证出错! 错误码 :" + intent.getIntExtra
                        (SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                        +  " ; 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                showToast("key 验证成功! 功能可以正常使用");
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                showToast("网络出错");
            }
        }
    }

    private SDKReceiver mReceiver;


    @Override
    protected void initViewsAndEvents() {
        // 注册 SDK 广播监听者
//        IntentFilter iFilter = new IntentFilter();
//        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
//        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
//        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
//        mReceiver = new SDKReceiver();
//        registerReceiver(mReceiver, iFilter);
        //判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT>=23){
            showContacts();
        }else{
            initMap();
            setListeners();
        }
    }
    public void showContacts(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }else{
            initMap();
            setListeners();
        }
    }
    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    initMap();
                    setListeners();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    private void setListeners() {
        /**点击显示带有店铺列表的布局**/
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopListPage.size()!=0 || (!shopListPage.isEmpty())){
//                TranslateAnimation animation = new TranslateAnimation(0,0,0,500);
//                animation.setDuration(500);//设置动画持续时间
//                animation.setRepeatCount(0);//设置重复次数
//                rlFirst.setAnimation(animation);
//                animation.startNow();
                    rlFirst.setVisibility(View.GONE);

                    TranslateAnimation animation02 = new TranslateAnimation(0,0,500,0);
                    animation02.setDuration(200);//设置动画持续时间
                    animation02.setRepeatCount(0);//设置重复次数
                    rlSecond.setAnimation(animation02);
                    animation02.startNow();
                    rlSecond.setVisibility(View.VISIBLE);
                }
            }
        });
        /**触摸显示带有店铺列表的布局**/
        llBottom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (shopListPage.size()!=0 || (!shopListPage.isEmpty())){
                    if (rlBottomWhite.getVisibility()==View.VISIBLE){
//                    TranslateAnimation animation = new TranslateAnimation(0,0,0,500);
//                    animation.setDuration(500);//设置动画持续时间
//                    animation.setRepeatCount(0);//设置重复次数
//                    rlFirst.setAnimation(animation);
//                    animation.startNow();
                        rlFirst.setVisibility(View.GONE);

                        TranslateAnimation animation02 = new TranslateAnimation(0,0,500,0);
                        animation02.setDuration(200);//设置动画持续时间
                        animation02.setRepeatCount(0);//设置重复次数
                        rlSecond.setAnimation(animation02);
                        animation02.startNow();
                        rlSecond.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }
        });
        /**点击显示带有店铺列表的布局**/
        llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopListPage.size()!=0 || (!shopListPage.isEmpty())){
                    if (rlBottomWhite.getVisibility()==View.VISIBLE){
                        TranslateAnimation animation = new TranslateAnimation(0,0,0,500);
                        animation.setDuration(500);//设置动画持续时间
                        animation.setRepeatCount(0);//设置重复次数
                        rlFirst.setAnimation(animation);
                        animation.startNow();
                        rlFirst.setVisibility(View.GONE);

                        TranslateAnimation animation02 = new TranslateAnimation(0,0,500,0);
                        animation02.setDuration(200);//设置动画持续时间
                        animation02.setRepeatCount(0);//设置重复次数
                        rlSecond.setAnimation(animation02);
                        animation.startNow();
                        rlSecond.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        /**点击收起带有店铺列表的布局**/
        rlSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslateAnimation animation = new TranslateAnimation(0,0,500,0);
                animation.setDuration(200);//设置动画持续时间
                animation.setRepeatCount(0);//设置重复次数
                rlFirst.setAnimation(animation);
                animation.startNow();
                rlFirst.setVisibility(View.VISIBLE);

                TranslateAnimation animation02 = new TranslateAnimation(0,0,0,500);
                animation02.setDuration(200);//设置动画持续时间
                animation02.setRepeatCount(0);//设置重复次数
                rlSecond.setAnimation(animation02);
                animation.startNow();
                rlSecond.setVisibility(View.GONE);

            }
        });
        /**点击回到定位位置**/
        ivLeft01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location(mCurrentLat,mCurrentLon);
            }
        });
        /**滑屏触发地图状态改变监听器**/
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                if (llBottomIn.getVisibility()==View.VISIBLE){
                    llBottomIn.setVisibility(View.GONE);
                    rlBottomWhite.setVisibility(View.VISIBLE);
                    ivRight02.setVisibility(View.GONE);
                    ivScanCode.setImageResource(R.mipmap.saoma);
                    isWhat=IS_SCAN;
                }
                if (rlSecond.getVisibility()==View.VISIBLE){
                    TranslateAnimation animation = new TranslateAnimation(0,0,500,0);
                    animation.setDuration(200);//设置动画持续时间
                    animation.setRepeatCount(0);//设置重复次数
                    rlFirst.setAnimation(animation);
                    animation.startNow();
                    rlFirst.setVisibility(View.VISIBLE);

                    TranslateAnimation animation02 = new TranslateAnimation(0,0,0,500);
                    animation02.setDuration(200);//设置动画持续时间
                    animation02.setRepeatCount(0);//设置重复次数
                    rlSecond.setAnimation(animation02);
                    animation02.startNow();
                    rlSecond.setVisibility(View.GONE);
                }
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                if (isBack.equals("back")){
                    isBack = "noback";
                }else{
                    updateMapState(mapStatus);
                }
            }
        });
        rlRight01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(PersonalNewActivity.class);
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i=0;i<markerList.size();i++){
                    if (marker==markerList.get(i)){
//                        showToast(shopList.get(i).getAddress());
                        shopId=shopList.get(i).getShopID();
                        shopY =shopList.get(i).getLatitude();
                        shopX =shopList.get(i).getLongitude();
                        tvShopName.setText(shopList.get(i).getShopName()+"");
                        tvDistance.setText(shopList.get(i).getDistance()+"");
                        tvAddress.setText(shopList.get(i).getAddress()+"");
                        tvPhone.setText(shopList.get(i).getContact()+"");
                        tvOpenTime.setText("营业时间:"+shopList.get(i).getOpenTime());
                    }
                }

                TranslateAnimation animation = new TranslateAnimation(0,0,500,0);
                animation.setDuration(200);//设置动画持续时间
                animation.setRepeatCount(0);//设置重复次数
                rlFirst.setAnimation(animation);
                animation.startNow();
                rlFirst.setVisibility(View.VISIBLE);
                ivRight02.setVisibility(View.VISIBLE);


                if (llBottomIn.getVisibility()!=View.VISIBLE){
                    llBottomIn.setVisibility(View.VISIBLE);
                    rlBottomWhite.setVisibility(View.GONE);
                    ivScanCode.setImageResource(R.mipmap.gozl);
                    isWhat=IS_GO;
                }
                if (rlSecond.getVisibility()==View.VISIBLE){
                    TranslateAnimation animation02 = new TranslateAnimation(0,0,0,500);
                    animation02.setDuration(200);//设置动画持续时间
                    animation02.setRepeatCount(0);//设置重复次数
                    rlSecond.setAnimation(animation02);
                    animation02.startNow();
                    rlSecond.setVisibility(View.GONE);
                }
                return true;
            }
        });
        ivScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (isWhat){
                    case IS_SCAN:
                        String token = new MyToken(MainActivity.this).getToken();
                        if(token==null || token.equals("")){
                            showToast("请先登录");
                            readyGo(LoginActivity.class);
                        }else{
                            if (Build.VERSION.SDK_INT >= 23){
                                int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                                if (cameraPermission != PackageManager.PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 123);
                                    return;
                                }else{
                                    readyGo(CaptureActivity.class);
                                }
                            }else{
                                readyGo(CaptureActivity.class);
                            }
                        }
                        break;
                    case IS_GO:
//                        showToast("去这里");
                        Bundle bundle=new Bundle();
                        bundle.putDouble("shopX",Double.parseDouble(shopX));
                        bundle.putDouble("shopY",Double.parseDouble(shopY));
                        bundle.putDouble("mCurrentLat",mCurrentLat);
                        bundle.putDouble("mCurrentLon",mCurrentLon);
                        bundle.putString("mCurrentCity",mCurrentCity);
                        readyGo(RoutePlanActivity.class,bundle);
                        break;
                }
            }
        });
        ivRight02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = new MyToken(MainActivity.this).getToken();
                if(token==null || token.equals("")){
                    showToast("请先登录");
                    readyGo(LoginActivity.class);
                }else{
                    if (Build.VERSION.SDK_INT >= 23){
                        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                        if (cameraPermission != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 123);
                            return;
                        }else{
                            readyGo(CaptureActivity.class);
                        }
                    }else{
                        readyGo(CaptureActivity.class);
                    }
                }
            }
        });
        ivScanCode02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = new MyToken(MainActivity.this).getToken();
                if(token==null || token.equals("")){
                    showToast("请先登录");
                    readyGo(LoginActivity.class);
                }else{
                    if (Build.VERSION.SDK_INT >= 23){
                        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                        if (cameraPermission != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 123);
                            return;
                        }else{
                            readyGo(CaptureActivity.class);
                        }
                    }else{
                        readyGo(CaptureActivity.class);
                    }
                }
            }
        });
        //跳转店铺详情
        llBottomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = new MyToken(MainActivity.this).getToken();
                if (token==null || token.equals("")){
                    readyGo(LoginActivity.class);
                }else{
                    Bundle bundle=new Bundle();
                    bundle.putString("shopId",shopId);
                    bundle.putDouble("shopX",Double.parseDouble(shopX));
                    bundle.putDouble("shopY",Double.parseDouble(shopY));
                    bundle.putDouble("mCurrentLat",mCurrentLat);
                    bundle.putDouble("mCurrentLon",mCurrentLon);
                    bundle.putString("mCurrentCity",mCurrentCity);
                    readyGo(ShopDetailsActivity.class, bundle);
                }
            }
        });
        //跳转到用户订单的返还信息
        ivLeft02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = new MyToken(MainActivity.this).getToken();
                if (token==null || token.equals("")){
                    showToast("请先登录");
                    readyGo(LoginActivity.class);
                }else {
                    readyGo(MyOrder01Activity.class);
                }
//                String token = new MyToken(MainActivity.this).getToken();
//                if (token==null || token.equals("")){
//                    showToast("请先登录");
//                    readyGo(LoginActivity.class);
//                }else {
//                    Call<OrderReturnInfo> call= RetrofitService.getInstance().getOrderReturnInfo(token);
//                    call.enqueue(new Callback<OrderReturnInfo>() {
//                        @Override
//                        public void onResponse(Response<OrderReturnInfo> response, Retrofit retrofit) {
//                            if (response==null){
//                                showToast(responseFail());
//                                return;
//                            }
//                            if (response.body().getStatus()==0){
//                                readyGo(MyOrderActivity.class);
//                            }else{
//                                showToast(response.body().getMes());
//                            }
//                        }
//                        @Override
//                        public void onFailure(Throwable throwable) {
//
//                        }
//                    });
//                }
            }
        });
        lvShopListPage.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isBottom;
            private boolean isTop;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_FLING:
                        //Log.i("info", "SCROLL_STATE_FLING");
                        break;
                    case SCROLL_STATE_IDLE:
                        if (isBottom) {
                            //到最底部
//                            showToast("bottom");
                        }
                        if (isTop){
                            //到最顶部
                            TranslateAnimation animation = new TranslateAnimation(0,0,500,0);
                            animation.setDuration(200);//设置动画持续时间
                            animation.setRepeatCount(0);//设置重复次数
                            rlFirst.setAnimation(animation);
                            animation.startNow();
                            rlFirst.setVisibility(View.VISIBLE);

                            TranslateAnimation animation02 = new TranslateAnimation(0,0,0,500);
                            animation02.setDuration(200);//设置动画持续时间
                            animation02.setRepeatCount(0);//设置重复次数
                            rlSecond.setAnimation(animation02);
                            animation02.startNow();
                            rlSecond.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount){
                    //Log.i("info", "到底了....");
                    isBottom = true;
                }else{
                    isBottom = false;
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = lvShopListPage.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
//                        Log.d("ListView", "##### 滚动到顶部 ######");
                        isTop = true;
                    }else{
                        isTop = false;
                    }
                }else{
                    isTop = false;
                }
            }
        });
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("电话");
            }
        });
        tvGoHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putDouble("shopX",Double.parseDouble(shopX));
                bundle.putDouble("shopY",Double.parseDouble(shopY));
                bundle.putDouble("mCurrentLat",mCurrentLat);
                bundle.putDouble("mCurrentLon",mCurrentLon);
                bundle.putString("mCurrentCity",mCurrentCity);
                readyGo(RoutePlanActivity.class,bundle);
            }
        });
    }
    private void updateMapState(MapStatus status) {
        LatLng mCenterLatLng = status.target;
        /**获取经纬度*/
        double lat = mCenterLatLng.latitude;
        double lng = mCenterLatLng.longitude;
        loadData(lat,lng);
    }
    private void initMap() {
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(MainActivity.this, "加载中...");

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 地图初始化
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);
        //获取屏幕高度
        WindowManager wm = this.getWindowManager();
        int height = wm.getDefaultDisplay().getHeight();
        //设置比例尺位置
        mBaiduMap.setViewPadding(0, 20, 0, height/2);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(0);//重复定位
        mLocClient.setLocOption(option);
        mLocClient.start();
    }
    public void initOverlay() {
        List<LatLng> latLngs=new ArrayList<LatLng>();
        for (int i=0;i<shopList.size();i++){
            LatLng ll = new LatLng(Double.parseDouble(shopList.get(i).getLatitude()), Double.parseDouble(shopList.get(i).getLongitude()));
            latLngs.add(ll);
        }
        markerList=new ArrayList<Marker>();
        bdImgs=new ArrayList<BitmapDescriptor>();
        for (int l=0;l<latLngs.size();l++){
            BitmapDescriptor bdGcoding = BitmapDescriptorFactory
                    .fromResource(R.mipmap.yule);
            MarkerOptions oo = new MarkerOptions().position(latLngs.get(l)).icon(bdGcoding)
                    .zIndex(5);
            bdImgs.add(bdGcoding);
            // 掉下动画
//            oo.animateType(MarkerOptions.MarkerAnimateType.drop);
            mMarker = (Marker) (mBaiduMap.addOverlay(oo));
            markerList.add(mMarker);
        }
    }
    /**
     * 清除所有Overlay
     *
     * @param view
     */
    public void clearOverlay(View view) {
        mBaiduMap.clear();
    }

    /**
     * 重新添加Overlay
     *
     * @param view
     */
    public void resetOverlay(View view) {
        clearOverlay(null);
        initOverlay();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
//            showToast(location.getProvince()+","+location.getCity()+","+location.getAddress().district+","+location.getAddress().street);
            mCurrentProvince=location.getProvince();
            mCurrentCity=location.getCity();
            mCurrentDistrict=location.getDistrict();
            mCurrentStreet=location.getStreet();
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            //加载数据
            loadData(mCurrentLat, mCurrentLon);
            //设置地址信息街道等
            setLocationAddr();
            //登录验证
            getLogin();
            //定位点击监听
            setChangeLocationListener();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(14.5f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    private void setChangeLocationListener() {
        llSearchLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putDouble("x", mCurrentLon);
                bundle.putDouble("y", mCurrentLat);
                readyGoForResult(SearchMoreActivity.class, 6065, bundle);
            }
        });
        WeiboDialogUtils.closeDialog(mWeiboDialog);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 6065:
                backKey = data.getExtras().getString("key");
                isBack = data.getExtras().getString("isBack");
                loadData(mCurrentLat, mCurrentLon);
                break;
        }
    }

    private void setLocationAddr() {
        tvCurrentAddress.setText(mCurrentProvince+mCurrentCity+mCurrentDistrict+mCurrentStreet);
    }

    private void getLogin() {
        //读取帐号密码
        SharedPreferences sharedGetAP=getSharedPreferences("YXAP",MODE_PRIVATE);
        String sacc = sharedGetAP.getString("YXACC", "");
        String spwd = sharedGetAP.getString("YXPWD", "");
        if (sacc.equals("")||spwd.equals("")){
        }else{
            Call<Login> call= RetrofitService.getInstance().login(sacc, spwd);
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Response<Login> response, Retrofit retrofit) {
                    //保存Token
                    SharedPreferences shared=getSharedPreferences("YXToken",MODE_PRIVATE);
                    SharedPreferences.Editor editor=shared.edit();
                    editor.putString(TOKEN,response.body().getToken());
                    editor.commit();
                    Glide.with(getApplicationContext()).load(response.body().getHeadPortrait()).error(R.mipmap.head).into(ivRight01);
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }
    }

    private void loadData(double mLat, double mLon) {
        if (NetConnectUtil.isNetConnected(getApplicationContext())){
            //检查版本更新
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    checkUpdate();
//                }
//            });
            //获取附近店铺列表（不分页）
//            showToast(mLon+","+mLat+","+backKey);
            Call<ShopList> call= RetrofitService.getInstance().getShopList(mLon+"",mLat+""/*"116.512672","39.92334"*/, backKey);
            call.enqueue(new Callback<ShopList>() {
                @Override
                public void onResponse(Response<ShopList> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        shopList=response.body().getShopList();
                        backX=response.body().getX();
                        backY=response.body().getY();
                        if (isBack.equals("back")){
                            location(Double.parseDouble(backY),Double.parseDouble(backX));
                            isBack="noback";
                        }
                        initOverlay();
                        tvNearShopCount.setText("附近共有"+shopList.size()+"家店铺");
                    }else{
                        showToast(response.body().getMes());
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
            //获取附近店铺列表（分页）
            Call<ShopListPage> pageCall=RetrofitService.getInstance().getShopListPage(mLon+"",mLat+"",pageIndex,pageSize, backKey);
            pageCall.enqueue(new Callback<ShopListPage>() {
                @Override
                public void onResponse(Response<ShopListPage> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        shopListPage=response.body().getShopList();
                        setAdapter();
                    }else{
                        showToast(response.body().getMes());
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }else{
            showToast("请打开网络,并重启APP");
//            setNetworkMethod();
        }
    }

    private void setAdapter() {
        adapter=new ShopListPageAdapter();
        lvShopListPage.setAdapter(adapter);
    }

    class ShopListPageAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return shopListPage.size();
        }

        @Override
        public ShopListPage.ShopListPageShopList getItem(int i) {
            return shopListPage.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ShopListPageHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_main3,null);
                holder=new ShopListPageHolder();
                holder.tvShopName=(TextView)view.findViewById(R.id.tvShopName);
                holder.tvDistance=(TextView)view.findViewById(R.id.tvDistance);
                holder.tvAddress=(TextView)view.findViewById(R.id.tvAddress);
                holder.tvOpenTime=(TextView)view.findViewById(R.id.tvOpenTime);
                holder.ivHead=(ImageView) view.findViewById(R.id.ivHead);
                view.setTag(holder);
            }
            final ShopListPage.ShopListPageShopList shop = getItem(i);
            holder= (ShopListPageHolder) view.getTag();
            holder.tvShopName.setText(shop.getShopName()+"");
            holder.tvDistance.setText(shop.getDistance()+"");
            holder.tvAddress.setText(shop.getAddress()+"");
            holder.tvOpenTime.setText("营业时间:"+shop.getOpenTime());
            Glide.with(getApplicationContext()).load(shop.getShopImg()).into(holder.ivHead);
            if (view!=null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String token = new MyToken(MainActivity.this).getToken();
                        if (token==null || token.equals("")){
                            readyGo(LoginActivity.class);
                        }else{
                            Bundle bundle=new Bundle();
                            bundle.putString("shopId",shop.getShopID()+"");
                            bundle.putDouble("shopX",Double.parseDouble(shop.getLongitude()+""));
                            bundle.putDouble("shopY",Double.parseDouble(shop.getLatitude()+""));
                            bundle.putDouble("mCurrentLat",mCurrentLat);
                            bundle.putDouble("mCurrentLon",mCurrentLon);
                            bundle.putString("mCurrentCity",mCurrentCity);
                            readyGo(ShopDetailsActivity.class, bundle);
                        }
                    }
                });
            }
            return view;
        }

        class ShopListPageHolder{
            TextView tvShopName,tvDistance,tvAddress,tvOpenTime;
            ImageView ivHead;
        }
    }

    /*
     * 更新
     */
    private void checkUpdate() {
        final String versionName= VersionUtil.getVersionName(getApplicationContext());
        // TODO 判断网络VersionName和当前app的VersionName
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("更新").setMessage("是否下载新版本").setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this, UpdateService.class);
                intent.putExtra("Key_App_Name",getResources().getString(R.string.app_name));//app名
                intent.putExtra("Key_Down_Url","下载链接");//网络获取的下载链接
                startService(intent);
                showToast("正在后台下载，不会影响您的正常使用");
                dialogInterface.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setCancelable(false).show();
    }
    /*
     * 打开设置网络界面
     * */
    private void setNetworkMethod(){
        //提示对话框
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if(android.os.Build.VERSION.SDK_INT>10){
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                }else{
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                startActivity(intent);
            }
        }).setNegativeButton("已开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadData(mCurrentLat,mCurrentLon);
                dialog.dismiss();
            }
        }).show();
    }
    /**经纬度地址动画显示在屏幕中间**/
    private void location(double latitude,double longitude){
        LatLng ll = new LatLng(latitude, longitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(msu);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        loadData(mCurrentLat, mCurrentLon);
    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        clearOverlay(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
        // 回收 bitmap 资源
        bdA.recycle();
        bdB.recycle();
        bdC.recycle();
        bdD.recycle();
        bd.recycle();
        bdGround.recycle();
    }

    //--------------使用onKeyDown()干掉他--------------
    //记录用户首次点击返回键的时间
    private long firstTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                firstTime=System.currentTimeMillis();
            }else{
                ActivityManagerUtil.getInstance().finishAllActivity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_main_my;
    }
}
