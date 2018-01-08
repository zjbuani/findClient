package com.all.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fengxun.base.MyApplication;
import com.fragment.FragmentTag;
import com.google.gson.Gson;
import com.http.response.bean.MapFloorBean;
import com.http.response.bean.MarkerErrListBean;
import com.http.response.bean.MarkerLayoutBean;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewManager;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.TileSet;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.models.Position;
import com.pager.map.pager.MapBasePager;
import com.pager.map.pager.MapPagerAdapter;
import com.pager.map.pager.MapPagerChildOnClickListener;
import com.pager.map.pager.MapViewPagerInfoOne;
import com.pager.map.pager.MapViewPagerInfoTwo;
import com.request.RequstUtils;
import com.sdk.FXMapBoxUtil;
import com.utils.DensityUtil;
import com.utils.ToastShowUtil;
import com.view.NumberPickerView;
import com.zhy.sample.folderlayout.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;
import static com.zhy.sample.folderlayout.R.color;
import static com.zhy.sample.folderlayout.R.drawable;
import static com.zhy.sample.folderlayout.R.id;
import static com.zhy.sample.folderlayout.R.layout;


/**
 * Adding an external Web Map Service layer to the map.
 */
public class AddWMSMapActivity extends MapBaseActivity implements MapboxMap.OnMapClickListener,
        MarkerViewManager.OnMarkerViewAddedListener, OnMapReadyCallback, NumberPickerView.OnScrollListener,
        NumberPickerView.OnValueChangeListener, NumberPickerView.OnValueChangeListenerInScrolling,
        View.OnClickListener, MapboxMap.OnPolygonClickListener, MapboxMap.OnMarkerClickListener, MapboxMap.OnMarkerViewClickListener, MapPagerChildOnClickListener {
    private static final String TAG = "picker";
    public static String loadMapUrl_bbox = "?bbox={bbox-epsg-3857}&format=image/png&service=WMS&version=1.1.1&request=GetMap&"
            + "srs=EPSG:3857&width=232&height=512&layers=";
    private NumberPickerView picker;
    private ImageView mErrdevice02x;
    private ImageView mZoomMax;
    private ImageView mZoomMin;
    private LatLng mCameralatLng;
    private MapboxMap mapboxMap;
    private double locationZoom = 9f;
    private ViewPager mMap_tile_pager;
    private String mBuild_id;
    //    private SignInBean mSignIn;
    private String mCity;
    private Gson json = new Gson();
    private MapFloorBean mapFloorBean;
    private List<MapFloorBean.ResponseJavaBean> projectUrlInfo;  // 地图加载的URL  需要 的数据
    private List<String> layer_id_list;
    private List<String> source_id_list;
    private ImageView err_device_image;
    private TextView err_device_tv;
    private MarkerLayoutBean mMarkerLayoutInfo;
    private String user_netlimit;
    private String build_peanut;
    private MarkerErrListBean mMarkerErrList;
    private ArrayList<Position> positions;
    private ArrayList<Integer> mErrPositionIndexOf;
    private ArrayList<MarkerLayoutBean.DeviceListJavaBean> integers;
    private boolean isClick;

    @Override
    protected void initView() {
        mapView = (MapView) findViewById(id.mapView);
        picker = (NumberPickerView) findViewById(id.picker);
        err_device_image = (ImageView) findViewById(id.err_device_image); // 错误的黄色小图片
        err_device_tv = (TextView) findViewById(id.err_device_tv);        // 错误总数
        mMap_tile_pager = (ViewPager) findViewById(id.map_tile_pager); //首页的 轮播图
        mErrdevice02x = (ImageView) findViewById(id.errdevice02x); // 回到中心点
        mZoomMax = (ImageView) findViewById(id.zoomMax);  // 放大
        mZoomMin = (ImageView) findViewById(id.zoomMin); // 缩小

    }

    @Override
    protected void initListener() {
        mErrdevice02x.setOnClickListener(this);
        mZoomMax.setOnClickListener(this);
        mZoomMin.setOnClickListener(this);
        picker.setOnScrollListener(this);
        picker.setOnValueChangedListener(this);
        picker.setOnValueChangeListenerInScrolling(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        ArrayList<MapBasePager> mPagerList = new ArrayList<>();
        MapViewPagerInfoOne one = new MapViewPagerInfoOne(this);
        MapViewPagerInfoTwo two = new MapViewPagerInfoTwo(this);
        mPagerList.add(one);
        mPagerList.add(two);
        mMap_tile_pager.setVisibility(View.GONE);
        isClick = false;  // 决定 是否是第一次 设置 datper;
        mMap_tile_pager.setAdapter(new MapPagerAdapter(mPagerList));
        mapView.onCreate(savedInstanceState);
        layer_id_list = FXMapBoxUtil.layer_id_list;
        source_id_list = FXMapBoxUtil.source_id_list;
        Intent intent = getIntent();
        //获取到此 开始时选择的 项目 ID
        this.mBuild_id = intent.getStringExtra(FragmentTag.SumListTag);
        mapFloorBean = (MapFloorBean) intent.getSerializableExtra(FragmentTag.MAP_URL);
        projectUrlInfo = mapFloorBean.getResponse();
        Log.e("projectUrlInfo", projectUrlInfo.toString());
        Log.e("projectUrlInfo", mBuild_id);
        RequestFirst(mBuild_id, null);
    }

    public void RequestFirst(String mBuild_id, String device_type) {
        MyApplication application = (MyApplication) getApplication();
        user_netlimit = application.user_netlimit;
        build_peanut = application.build_peanut;
        HashMap<String, String> map = new HashMap<>();
        map.put("dev_type", "2"); // 摄像头为主
        map.put("build_id", mBuild_id);
        map.put("floor_id", projectUrlInfo.get(0).getFloorID());
        if (device_type == null) {
            map.put("md_type", "0");
        } else {
            map.put("md_type", device_type);
        }
        Log.e("projectUrlInfo", map.toString());
        RequstUtils.loadPostMarkerLayoutData(user_netlimit, build_peanut, map, new MapFloorResponseListener(), new MapFloorErrListener());
    }

    private void pickerSetData() {
        int mListSize = mapFloorBean.getResponse().size();
        Log.e("mListSize", mListSize + "");
        if (mListSize % 3 == 0) {
            picker.mShowCount = mListSize;
        } else {
            picker.mShowCount = 3;
        }
        List<MapFloorBean.ResponseJavaBean> pickerDataArray = mapFloorBean.getResponse();
        String[] mPickerData = new String[pickerDataArray.size()];
        for (int i = 0; i < pickerDataArray.size(); i++) {
            mPickerData[i] = pickerDataArray.get(i).getFloorName();
        }
        picker.refreshByNewDisplayedValues(mPickerData);
        if (layer_id_list.size() == 0) {
            mapView.getMapAsync(this);  //添加地图
        } else {  //   如果   layer_id_list  的总数 不等于0 说明已经添加过了  哪现在就直接添加 marker 和符号层
            Log.e("Tag", "如果   layer_id_list  的总数 不等于0 说明已经添加过了 " + layer_id_list.size() + "  " + layer_id_list.toString());
            try {
                addMyMark(mapboxMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        layer_id_list.clear();
        source_id_list.clear();
        return super.onKeyDown(keyCode, event);
    }

    // 界面进入第一次的 数据
    @Override
    protected void geiFirstDate() {
//        mSignIn = (SignInBean) intent.getSerializableExtra(FragmentTag.SignInBean); //登录后携带的
        updataPickNumber();
    }

    // 添加 pickNumber数据
    private void updataPickNumber() {
        pickerSetData();
    }

    @Override
    protected int initLayout() {
        return R.layout.map_layout;
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        mapboxMap.setMinZoomPreference(8.6); // 设置最小的 缩放级别最多为 8.6
        FXMapBoxUtil.inVisibleLogoAndCompass(mapboxMap); //隐藏Logo
        FXMapBoxUtil.removeLayer(mapboxMap); // 删除掉所有的 地图
        mCameralatLng = new LatLng(-0.5493115184203139, 0.05679479101602491);
        this.mapboxMap = mapboxMap;
        mapboxMap.setOnMapClickListener(this);
        mapboxMap.setOnPolygonClickListener(this);
        mapboxMap.setOnMarkerClickListener(this);
//        mapboxMap.setOnInfoWindowLongClickListener(this);
        FXMapBoxUtil.camera(mapboxMap, mCameralatLng); // 定位点
        configMapBox(mapboxMap);
//        layer_id_list
        for (int i = 0; i < layer_id_list.size(); i++) {
            mapboxMap.removeSource(layer_id_list.get(i) + "");
            mapboxMap.removeLayer(layer_id_list.get(i) + "");
        }

        String wms_url = projectUrlInfo.get(0).getFloorOutline() + loadMapUrl_bbox + projectUrlInfo.get(0).getFloorUrl();
        TileSet tileset = new TileSet("tileset", wms_url);
        RasterSource webMapSource = new RasterSource(0 + "", tileset, 100);
        FXMapBoxUtil.addRasterLayer(mapboxMap, webMapSource, 0 + "", 0 + "");
        ArrayList<LatLng> list = new ArrayList<>();   //  Symbol

//        FXMapBoxUtil.addSymbolMarker(this, mapboxMap, list1, errCode);
        try {
            addMyMark(mapboxMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**
         *       此处 设置    是否 是第一次 加载 数据  (符号层 )
         *       如果 已经有符号层  就删除掉 然后再次 添加上   并且添加 addMarker
         *       从原有的数据中比对 获取到正确的坐标然后添加marker
         *       并更新marker 布局
         */
        View view = View.inflate(AddWMSMapActivity.this, R.layout.marker_layout, null);
        Log.e("fromGeometry", "********* " + mErrPositionIndexOf.size());
        for (int i = 0; i < mErrPositionIndexOf.size(); i++) {
            Position position = positions.get(mErrPositionIndexOf.get(i));
            Log.e("Position", position.getLatitude() + "    " + position.getLongitude());
            list.add(new LatLng(position.getLatitude(), position.getLongitude()));
        }
//        mMarkerErrList,mErrPositionIndexOf
        FXMapBoxUtil.addMarker(getBaseContext(), mapboxMap, list, view, integers, mErrPositionIndexOf, mMarkerErrList);
    }

    public void addMyMark(MapboxMap mapboxMap) throws JSONException {
        mErrPositionIndexOf = new ArrayList<>();
        integers = new ArrayList<>();
        List<Feature> markerCoordinates = FXMapBoxUtil.addFeature(mMarkerLayoutInfo, mMarkerErrList, positions, mErrPositionIndexOf, integers);
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(markerCoordinates);
        mapboxMap.removeSource("marker-source");//每次添加之前删除掉 原来的  source
        mapboxMap.removeLayer("marker-layer");// 每次添加之前删除掉之前的 layer
        List<Source> sources = mapboxMap.getSources();
        for (int i = 0; i < sources.size(); i++) {
            if (sources.get(i).getId().contains("marker-source")) {
                mapboxMap.removeSource(sources.get(i).getId());
                continue;
            }
        }
        Source geoJsonSource = new GeoJsonSource("marker-source", featureCollection);
        mapboxMap.addSource(geoJsonSource);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), drawable.errpoint2x);
        mapboxMap.addImage("my-marker", icon);
        SymbolLayer markers = new SymbolLayer("marker-layer", "marker-source")
                .withProperties(PropertyFactory.iconImage("my-marker"));
        PropertyValue<String> symbolPlacement = markers.getSymbolPlacement();
        markers.setProperties(
                textField("{key}"),
                textSize(12f),
                textColor(Color.WHITE)

        );

        mapboxMap.addLayerAbove(markers, "0");
//        mapboxMap.addLayer(markers);


    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        if (isClick) {
            isClick = false;
            startTranslateAnimation();  // 执行 动画  取消viewpager
        }
        final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> mFeatures = mapboxMap.queryRenderedFeatures(pixel, "marker-layer");
        if (mFeatures == null && mFeatures.isEmpty()) {
            return;
        }
        if (mFeatures.size() == 0) {
            return;
        }
        for (Feature feature : mFeatures) {
            Number key = feature.getNumberProperty("key");   // 这样拿到了 点击后 获取到的 显示的 数字
            Log.e("list1", key.toString() + "");
        }

//         onMarkerClick(marker1,view, infoWindow); // 自己调用监听
        //        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show(); // 此处 是 过滤后 获取到 小圆点上的 数据

//        mapboxMap.setAllowConcurrentMultipleOpenInfoWindows();
    }

    /**
     * @param view 自定义接口  用来返回 viewPager 中 点击后返回的view
     */
    @Override
    public void onClickImage(View view) {
        String device_type = "0";
        switch (view.getId()) {
            case R.id.mapdevice03x:
                device_type = "0";
                break;
            case R.id.mapdevice53x:
                device_type = "4";
                break;
            case R.id.mapdevice13x:
                device_type = "1";
                break;
            case R.id.mapdevice23x:
                device_type = "3";
                break;
            case R.id.mapdevice43x:
                device_type = "2";
                break;
            case R.id.mapdevice33x:
                device_type = "5";
                break;
            case R.id.mapdevice63x:
                device_type = "6";
                break;
            default:
                device_type = "0";
                break;
        }
        RequestFirst(mBuild_id, device_type);
        startTranslateAnimation();
        isClick = false;  // 设置为false  说明 viewpager是  隐藏的
    }


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Log.e("onMarkerClick", marker.getTitle());
        return true;
    }

    @Override    //onMarkerClick
    public boolean onMarkerClick(@NonNull Marker marker, @NonNull View view, @NonNull MapboxMap.MarkerViewAdapter adapter) {
        Toast.makeText(view.getContext(), "我要看看" + marker.getTitle(), Toast.LENGTH_SHORT).show();
        return false;
    }


    class MapFloorResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String o) {
            mMarkerLayoutInfo = json.fromJson(o.toString(), MarkerLayoutBean.class);
            if (mMarkerLayoutInfo.getResult() != 1) {
                ToastShowUtil.showToast(AddWMSMapActivity.this, "服务器数据错误");
                AddWMSMapActivity.this.finish();
            } else {
                int mSize = mMarkerLayoutInfo.getDevice_list().size();
                Log.e("getSources", "  " + mSize + "  isFirstRequest  " + isFirstRequest);
                if (!isFirstRequest) {  // if  true  说明已经有 所有的坐标点
                    if (mSize == 0) {
                        err_device_tv.setVisibility(View.GONE);
                        err_device_image.setVisibility(View.GONE);
                    } else {
                        err_device_tv.setVisibility(View.VISIBLE);
                        err_device_image.setVisibility(View.VISIBLE);
                    }
                    String errSum = mSize + "";
                    err_device_tv.setText(errSum);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("floor_id", mMarkerLayoutInfo.getFloor_id());
                    map.put("build_id", mBuild_id);
                    RequstUtils.loadPostMarkerErrList(user_netlimit, build_peanut, map, new MarkerLayoutResponseListener(), new MapFloorErrListener());
                } else {    // 解析  返回的  故障 列表的 json
                    if (mSize == 0) {  //等于 0 说明 返回的数据中时没有 故障
                        err_device_tv.setVisibility(View.GONE);
                        err_device_image.setVisibility(View.GONE);
                        Log.e("getSources", err_device_tv + " " + " " + err_device_image + " " + mSize);
                        FXMapBoxUtil.removeSymbolLayer(mapboxMap);
                    } else {
                        err_device_tv.setVisibility(View.VISIBLE);
                        err_device_image.setVisibility(View.VISIBLE);
                        String errSum = mSize + "";
                        err_device_tv.setText(errSum);
                        geiFirstDate();
                    }
                }
            }
        }
    }

    class MapFloorErrListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }

    class MarkerLayoutResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String o) {
            Log.e("onResponse", o.toString());
            mMarkerErrList = json.fromJson(o.toString(), MarkerErrListBean.class);
            if (mMarkerErrList.getResult() != 1) {
                isFirstRequest = false;     // 如果  失败 可以设置 第二次请求
                AddWMSMapActivity.this.finish();
            } else {
                isFirstRequest = true;   //  设置true 说明 是第一加载 所有的坐标点了
                positions = new ArrayList<>();
                List<MarkerErrListBean.DeviceListJavaBean> device_list = mMarkerErrList.getDevice_list();
                for (int i = 0; i < device_list.size(); i++) {
                    MarkerErrListBean.DeviceListJavaBean mErrDevicePosion = device_list.get(i);
                    FXMapBoxUtil.webMercator2Position(mErrDevicePosion.getDevice_x(), mErrDevicePosion.getDevice_y(), positions);
                }
                for (int i = 0; i < positions.size(); i++) {
                    Position position = positions.get(i);
                    Log.e("数据转换", position.getLatitude() + "  " + position.getLongitude() + " ");
                }
                geiFirstDate();
            }
        }
    }

    @Override
    public void onScrollStateChange(NumberPickerView view, int scrollState) {
        Log.d(TAG, "onScrollStateChange : " + scrollState);
    }

    /**
     * @param picker 滚轮控价
     * @param oldVal 上一次的滚轮记录 索引
     * @param newVal 获取到 滚轮 最新的 索引
     */
    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        String[] content = picker.getDisplayedValues(); // 轮播 数据的源
        if (content != null)
            if (mapboxMap == null) {
                return;
            }
        if (layer_id_list.contains(newVal + "")) {
            source_id_list.remove(newVal + "");
            layer_id_list.remove(newVal + "");
            Log.e("wms_url", "进行删除 **************** " + newVal);
            mapboxMap.removeLayer(newVal + "");
            mapboxMap.removeSource(newVal + "");
        }

        String wms_url = projectUrlInfo.get(newVal).getFloorOutline() + loadMapUrl_bbox + projectUrlInfo.get(newVal).getFloorUrl();

        Log.e("wms_url", wms_url);
        TileSet tileset = new TileSet("tileset", wms_url);
        RasterSource webMapSource = new RasterSource(newVal + "", tileset, 100);
        FXMapBoxUtil.addRasterLayer(mapboxMap, webMapSource, newVal + "", newVal + "");
        Log.e("content", layer_id_list.toString() + " \r\n " + source_id_list.toString() + "\r\n " + newVal);
    }

    @Override
    public void onValueChangeInScrolling(NumberPickerView picker, int oldVal, int newVal) {
        Log.d("wangjj", "onValueChangeInScrolling oldVal : " + oldVal + " newVal : " + newVal);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case id.errdevice02x: // 回到中心点
                if (!isClick) {
                    mMap_tile_pager.setVisibility(View.VISIBLE);
                    int i = DensityUtil.dip2px(this, 220);
                    Log.e("转换px", -i + "");
                    TranslateAnimation animation = new TranslateAnimation(0, 0, -i, 0);
//                    TranslateAnimation animation = new TranslateAnimation(0, 0, -1060, 0);
                    animation.setDuration(400);
//        animation.setRepeatCount(0);//动画的重复次数
                    animation.setFillAfter(true);//设置为true，动画转化结束后被应用
                    mMap_tile_pager.startAnimation(animation);//开始动画
                    isClick = true;
                } else {
                    isClick = false;
                    /*ObjectAnimator animator = ObjectAnimator.ofFloat(mMap_tile_pager, "translationY",   0f,-790f);
                    animator.setDuration(500);
                    animator.start();*/
                    int i = DensityUtil.dip2px(this, 220);
                    TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -i);
                    animation.setDuration(400);
                    animation.setRepeatCount(0);//动画的重复次数
                    animation.setFillAfter(true);//设置为true，动画转化结束后被应用
                    mMap_tile_pager.startAnimation(animation);//开始动画
                    mMap_tile_pager.setVisibility(View.GONE);
                }


//                FXMapBoxUtil.camera(mapboxMap, mCameralatLng);
                break;
            case id.zoomMax: // 回到放大
                locationZoom = locationZoom + 0.2;
                mapboxMap.setZoom(locationZoom);
//                mapboxMap.setZoom(); locationZoom
                Log.e("最大的缩放级别", mapboxMap.getMaxZoomLevel() + "");
                break;
            case id.zoomMin: // 回到缩小
                locationZoom = locationZoom - 0.2;
                Log.e("locationZoom", "locationZoom " + locationZoom);
                mapboxMap.setZoom(locationZoom);
                break;

        }
    }

    public void startTranslateAnimation() {
        int i = DensityUtil.dip2px(this, 220);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -i);
        animation.setDuration(400);
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
        mMap_tile_pager.startAnimation(animation);//开始动画
        mMap_tile_pager.setVisibility(View.GONE);
    }

    //多边形监听
    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {
        polygon.setFillColor(Color.GREEN);

        ToastShowUtil.showToast(this, polygon.toString());
    }

    public class MapBoxInfoWindow implements MapboxMap.InfoWindowAdapter {
        @Nullable
        @Override
        public View getInfoWindow(@NonNull final Marker marker) {
            View view = View.inflate(AddWMSMapActivity.this, R.layout.marker_layout, null);
            LinearLayout parent = new LinearLayout(AddWMSMapActivity.this);
            parent.setLayoutParams(new LinearLayout.LayoutParams(ZXSystemUtil.dp2px(AddWMSMapActivity.this, 200), ZXSystemUtil.dp2px(AddWMSMapActivity.this, 80)));
            parent.setOrientation(LinearLayout.HORIZONTAL);
            parent.setBackgroundColor(ContextCompat.getColor(AddWMSMapActivity.this, color.green));
            ImageView imageView = new ImageView(AddWMSMapActivity.this);
            TextView textView = new TextView(AddWMSMapActivity.this);
            imageView.setImageDrawable(ContextCompat.getDrawable(AddWMSMapActivity.this, drawable.purple_marker));
            textView.setText(marker.getTitle());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AddWMSMapActivity.this, marker.getTitle() + "textView  点击", Toast.LENGTH_SHORT).show();
                }
            });
            parent.addView(imageView);
            parent.addView(textView);
            return view;

        }

    }

    public void configMapBox(MapboxMap mapboxMap) {
        mapboxMap.setZoom(9);
//                LatLng latLng = new LatLng(  0,0);
        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(latLng.getLatitude(), latLng.getLongitude()))
                .zoom(8)//放大尺度 从0开始，0即最大比例尺，最大未知，17左右即为街道层级
                .bearing(360)//地图旋转，但并不是每次点击都旋转180度，而是相对于正方向180度，即如果已经为相对正方向180度了，就不会进行旋转
                .tilt(30)//地图倾斜角度，同上，相对于初始状态（平面）成30度
                .build();//创建CameraPosition对象
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000);
    }


    @Override
    public void onViewAdded(@NonNull MarkerView markerView) {
        Toast.makeText(this, "是不是点击  " + markerView.getTitle(), Toast.LENGTH_SHORT).show();
    }

}
