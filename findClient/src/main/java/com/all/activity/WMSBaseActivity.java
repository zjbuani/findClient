package com.all.activity;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
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
import android.view.MotionEvent;
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
import com.http.response.bean.InPointBean;
import com.http.response.bean.MapFloorBean;
import com.http.response.bean.MarkerErrListBean;
import com.http.response.bean.MarkerLayoutBean;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewManager;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Projection;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.TileSet;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.pager.map.pager.MapBasePager;
import com.pager.map.pager.MapPagerAdapter;
import com.pager.map.pager.MapPagerChildOnClickListener;
import com.pager.map.pager.MapViewPagerInfoOne;
import com.pager.map.pager.MapViewPagerInfoTwo;
import com.pager.map.pager.light.pager.LightMapBasePager;
import com.pager.map.pager.light.pager.LightMapPagerAdapter;
import com.pager.map.pager.light.pager.LightMapViewPagerInfoOne;
import com.pager.map.pager.light.pager.LightMapViewPagerInfoTwo;
import com.request.Http_URl;
import com.request.RequstUtils;
import com.sdk.FXMapBoxUtil;
import com.utils.DensityUtil;
import com.utils.ToastShowUtil;
import com.view.MarkerInfoWindow;
import com.view.NumberPickerView;
import com.zhy.sample.folderlayout.R;


import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.colorToRgbaString;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;
import static com.zhy.sample.folderlayout.R.color;
import static com.zhy.sample.folderlayout.R.drawable;
import static com.zhy.sample.folderlayout.R.id;
import static com.zhy.sample.folderlayout.R.layout;


/**
 * Adding an external Web Map Service layer to the map.
 */
public abstract class WMSBaseActivity extends MapBaseActivity implements MapboxMap.OnMapClickListener,
        MarkerViewManager.OnMarkerViewAddedListener, OnMapReadyCallback, NumberPickerView.OnScrollListener,
        NumberPickerView.OnValueChangeListener, NumberPickerView.OnValueChangeListenerInScrolling,
        View.OnClickListener, MapboxMap.OnPolygonClickListener, MapboxMap.OnMarkerClickListener, MapboxMap.OnMarkerViewClickListener, MapPagerChildOnClickListener, DialogInterface.OnCancelListener {
    private static final String TAG = "picker";
    // Layers are "visual" representation of things.
    protected static final String SELECTED_MARKER_LAYER_ID = "selected-marker-id";
    // Source ids identify things on the map that you can give to layer to render visually
    protected static final String SELECTED_SOURCE_ID = "selected-marker-source";
    // 给需要执行 动画的 Bitmap的  ID
    protected static final String MY_SELECTED_MARKER_IMAGE = "my-selected-marker-image";
    protected static final String INFO_IMAGE_TYPE_ID = "info_image_type_id";
    public static String loadMapUrl_bbox = "?bbox={bbox-epsg-3857}&format=image/png&service=WMS&version=1.1.1&request=GetMap&"
            + "srs=EPSG:3857&width=232&height=512&layers=";
    private String CLUSTER = "cluster";
    private NumberPickerView picker;
    private ImageView mErrdevice02x;
    private ImageView mZoomMax;
    private ImageView mZoomMin;
    private LatLng mCameralatLng;
    public MapboxMap mapboxMap;
    private double locationZoom = 9f;
    private ViewPager mMap_tile_pager;
    public String mBuild_id;
    private String mCity;
    private Gson json = new Gson();
    private MapFloorBean mapFloorBean;
    public List<MapFloorBean.ResponseJavaBean> projectUrlInfo;  // 地图加载的URL  需要 的数据
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
    public InPointBean mInPointBean;
    protected ArrayList<String> mInfoLayerIDList = new ArrayList<>();
    protected ArrayList<String> mInfoSourceIDList = new ArrayList<>();
    protected ArrayList<String> mInfoImageIDList = new ArrayList<>();

    public Map<Integer, InPointBean> map = new HashMap();  // 记录每次 滚轮请求的 返回的  对象
    public String MCAR_SOURCE1 = "Car-source1";
    public String MCAR_NUMBER_LAYER_ID = "CARNUMBER_LAYER_ID";
    protected boolean markerSelected = false;
    private String mPagerFloorID;
    protected MarkerInfoWindow mMarkerInfoWindow;
    private boolean addCircleLayer;
    private boolean isRequest;
    private View view;


    @Override
    protected void initView() {
        mapView = (MapView) findViewById(id.mapView);
        picker = (NumberPickerView) findViewById(id.picker);
        err_device_image = (ImageView) findViewById(id.err_device_image); // 错误的黄色小图片
        err_device_tv = (TextView) findViewById(id.err_device_tv);        // 错误总数
        mMap_tile_pager = (ViewPager) findViewById(id.map_tile_pager); //首页的 轮播图
        mErrdevice02x = (ImageView) findViewById(R.id.errdevice02x); // 回到中心点
        mZoomMax = (ImageView) findViewById(id.zoomMax);  // 放大
        mZoomMin = (ImageView) findViewById(id.zoomMin); // 缩小
        MyApplication application = (MyApplication) getApplication();
        user_netlimit = application.user_netlimit;
        build_peanut = application.build_peanut;
        view = View.inflate(WMSBaseActivity.this, layout.marker_layout, null);
    }

    @Override
    protected void initListener() {
        mErrdevice02x.setOnClickListener(this);
        mZoomMax.setOnClickListener(this);
        mZoomMin.setOnClickListener(this);
        picker.setOnScrollListener(this);
        picker.setOnValueChangedListener(this);
        picker.setOnValueChangeListenerInScrolling(this);
        mMarkerInfoWindow.setOnCancelListener(this);
        mapView.getMapAsync(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mMarkerInfoWindow = new MarkerInfoWindow(this);
        ArrayList<LightMapBasePager> mPagerList = new ArrayList<>();
        LightMapViewPagerInfoOne one = new LightMapViewPagerInfoOne(this);
        LightMapViewPagerInfoTwo two = new LightMapViewPagerInfoTwo(this);
        mPagerList.add(one);
        mPagerList.add(two);
        mMap_tile_pager.setVisibility(View.GONE);
        isClick = false;  // 决定 是否是第一次 设置 datper;
        mMap_tile_pager.setAdapter(new LightMapPagerAdapter(mPagerList));
        mapView.onCreate(savedInstanceState);
        layer_id_list = FXMapBoxUtil.layer_id_list;
        source_id_list = FXMapBoxUtil.source_id_list;
        Intent intent = getIntent();
        //获取到此 开始时选择的 项目 ID
        this.mBuild_id = intent.getStringExtra(FragmentTag.SumListTag);
        mapFloorBean = (MapFloorBean) intent.getSerializableExtra(FragmentTag.MAP_URL);
        projectUrlInfo = mapFloorBean.getResponse();   //这里获取到的是 上个界面 返回请求 之后 需要加载底图 的 URL
        loadMap(projectUrlInfo);    //初始化 地图的 url   数据
        mPagerFloorID = projectUrlInfo.get(0).getFloorID();
        isRequest = false;
    }

    protected void loadMap(List<MapFloorBean.ResponseJavaBean> projectUrlInfo) {
        if (projectUrlInfo.size() == 0) {
            ToastShowUtil.showToast(this, "无此地图的url");
            WMSBaseActivity.this.finish();
        } else {
            geiFirstDate();
        }
    }

    public void RequestFirst(String mBuild_id, String device_type) {
        HashMap<String, String> map = new HashMap<>();
        map.put("dev_type", (primarydevice() == null ? "0" : primarydevice()));
        map.put("build_id", mBuild_id);
        map.put("floor_id", mPagerFloorID);    //projectUrlInfo 根据  滚轮的返回的索引 进行加载
        if (device_type == null) {
            map.put("md_type", "0");
        } else {
            map.put("md_type", device_type);
        }
        Log.e("RequestFirst", map.toString());
        // 请求 此楼层的 故障 列表
        RequstUtils.loadPostMarkerLayoutData(user_netlimit, build_peanut, map, new MapFloorResponseListener(), null);

    }

    /**
     * 添加请求的 主 设备类型
     */
    protected abstract String primarydevice();

    private void pickerSetData() {
        int mListSize = mapFloorBean.getResponse().size();
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
        //   如果   layer_id_list  的总数 不等于0 说明已经添加过了  哪现在就直接添加 marker 和符号层
    /*    try {
            addMyMark(mapboxMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        layer_id_list.clear();
        source_id_list.clear();
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 界面进入第一次的   数据
     */
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
        return layout.map_layout;
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setMinZoomPreference(8.6); // 设置最小的 缩放级别最多为 8.6
        FXMapBoxUtil.inVisibleLogoAndCompass(mapboxMap); //隐藏Logo
        FXMapBoxUtil.removeAllLayer(mapboxMap); // 删除掉所有的 地图
        mCameralatLng = new LatLng(-0.5493115184203139, 0.05679479101602491);

        mapboxMap.setOnMapClickListener(this);
//            mapboxMap.setOnPolygonClickListener(this);
        mapboxMap.setOnMarkerClickListener(this);
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
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable.errpo2x);
        mapboxMap.addImage(MY_SELECTED_MARKER_IMAGE, bitmap);  // 添加故障点的 图标
        addImage();  //用来添加 兴趣点的 image
        RequestFirst(mBuild_id, null);


    }

    /**
     * 添加 故障点 和 故障点上 的 布局 和 布局内容
     */
    public void addMalfunctionAndLayout() {
        try {
            addMyMark(mapboxMap);
            ArrayList<LatLng> list = new ArrayList<>();   //  Symbol
            for (int i = 0; i < mErrPositionIndexOf.size(); i++) {
                Position position = positions.get(mErrPositionIndexOf.get(i));
                list.add(new LatLng(position.getLatitude(), position.getLongitude()));
            }


            FXMapBoxUtil.addMarker(getBaseContext(), mapboxMap, list, view, integers, mErrPositionIndexOf, mMarkerErrList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param picker 滚轮控件
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
            mapboxMap.removeLayer(newVal + "");
            mapboxMap.removeSource(newVal + "");
        }
        List<Layer> layers = mapboxMap.getLayers();
        for (int i = 0; i < layers.size(); i++) {
            String id = layers.get(i).getId();
            if (id.contains("com.mapbox.annotations") || id.contains("INFO_LAYER_ID")) {  // 每次都删除掉 车位 和 兴趣点的 图标
                mapboxMap.removeLayer(id);
            }
        }
        mPagerFloorID = projectUrlInfo.get(newVal).getFloorID();
        String wms_url = projectUrlInfo.get(newVal).getFloorOutline() + loadMapUrl_bbox + projectUrlInfo.get(newVal).getFloorUrl();
        TileSet tileset = new TileSet("tileset", wms_url);
        RasterSource webMapSource = new RasterSource(newVal + "", tileset, 100);
        FXMapBoxUtil.addRasterLayer(mapboxMap, webMapSource, newVal + "", newVal + "");
        if (map.get(newVal) != null) {    // 说明有数据 直接加载就可以
            reuseCarPositionAndPolygon(newVal);
        } else {
            requestCarPositionAndPolygon(json, newVal);
        }
        addErrLayer(newVal);

    }


    /**
     * @param mAllCarPosition 绘制 车位
     */
    protected abstract void addCarNumber(ArrayList<PolygonOptions> mAllCarPosition, InPointBean.ResponseCarposJavaBean mCarposJavaBean);

    /**
     * @param markerCoordinates 绘制车位号
     */
    protected abstract void addCarPositon(List<Feature> markerCoordinates, InPointBean.ResponseCarposJavaBean mCarposJavaBean);

    public void addPolygons() {
        String result = mInPointBean.getResult();
        if (!"1".equals(result)) { // 这里说明 返回的 车位数据是不对的
            return;
        } else {

            ArrayList<Position> mCarPosition = new ArrayList<>();
            List<InPointBean.ResponseCarposJavaBean> mCarpos = mInPointBean.getResponse_carpos();
            ArrayList<PolygonOptions> mAllCarPosition = new ArrayList<>();
            List<Feature> markerCoordinates = new ArrayList<>();
            List<InPointBean.ResponseInpointJavaBean> mInpoint = mInPointBean.getResponse_inpoint();
            for (int i = 0; i < mCarpos.size(); i++) {
                InPointBean.ResponseCarposJavaBean mCarposJavaBean = mCarpos.get(i);
                addCarNumber(mAllCarPosition, mCarposJavaBean);  // 绘制车位的 图形
                addCarPositon(markerCoordinates, mCarposJavaBean);
            }
            mapboxMap.addPolygons(mAllCarPosition);
            mapboxAddCarNumberLayer(markerCoordinates);
            Log.e("SymbolLayer", mapboxMap.getLayers().size() + "");
        }
    }

    public void mapboxAddCarNumberLayer(List<Feature> markerCoordinates) {
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(markerCoordinates);
        GeoJsonSource geoJsonSource;
        if (mapboxMap.getSource(MCAR_SOURCE1) != null) {
            geoJsonSource = (GeoJsonSource) mapboxMap.getSource(MCAR_SOURCE1);
            geoJsonSource.setGeoJson(featureCollection);
        } else {
            geoJsonSource = new GeoJsonSource(MCAR_SOURCE1, featureCollection);
            mapboxMap.addSource(geoJsonSource);
        }
        if (mapboxMap.getLayer(MCAR_NUMBER_LAYER_ID) != null) {
            mapboxMap.removeLayer(mapboxMap.getLayer(MCAR_NUMBER_LAYER_ID));
        }
        SymbolLayer layer = new SymbolLayer(MCAR_NUMBER_LAYER_ID, MCAR_SOURCE1);
        layer.setProperties(
                textField("{Num}"),
                textSize(12f),
                textColor(Color.BLACK)
        );
        mapboxMap.addLayer(layer);

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.e("onCancel", "onCancel 进入");
        addCircleLayer = false;
        Layer layer = mapboxMap.getLayer(CLUSTER);
        if (layer != null) {
            Log.e("onCancel", "删除掉图片");
            mapboxMap.removeLayer(layer);
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        if (isClick) {
            isClick = false;
            startTranslateAnimation();  // 执行 动画  取消viewpager
        }
        Log.e("onCancel", "onMapClick 进入");
        final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> mFeatures = mapboxMap.queryRenderedFeatures(pixel, "marker-layer");
        if (mFeatures == null || mFeatures.isEmpty() || mFeatures.size() == 0) {
            addCircleLayer = false;
            Layer layer = mapboxMap.getLayer(CLUSTER);
            if (layer != null) {
                mapboxMap.removeLayer(layer);
                mMarkerInfoWindow.cancel();
            }
            return;
        }
        Log.e("onCancel", "onMapClick 进入");
        for (Feature feature : mFeatures) {
            if (!addCircleLayer) {
                addFindPoint(feature.getGeometry());
                mMarkerInfoWindow.show();
                mMarkerInfoWindow.showDialog(integers, mErrPositionIndexOf, mMarkerErrList);
            }
//            Number key = feature.getNumberProperty("key");   // 这样拿到了 点击后 获取到的 显示的 数字

        }


    }

    protected void addFindPoint(Geometry geometry) {
        ArrayList<Feature> list = new ArrayList<>();
        list.add(Feature.fromGeometry(geometry));
        FeatureCollection mInfoLayerCollection = FeatureCollection.fromFeatures(list);
        GeoJsonSource source = (GeoJsonSource) mapboxMap.getSource("cluster_source");
        if (source == null) {
            source = new GeoJsonSource("cluster_source", mInfoLayerCollection);
            mapboxMap.addSource(source);
        } else {
            source.setGeoJson(mInfoLayerCollection);
        }
        int[][] layers = new int[][]{
                new int[]{20, ContextCompat.getColor(this, color.colorGreen)},
                new int[]{0, ContextCompat.getColor(this, R.color.mapbox_blue)}
        };
        CircleLayer circles = new CircleLayer(CLUSTER, "cluster_source");
        circles.setProperties(
                circleColor(layers[0][1]),
                circleRadius(13f)
        );

        mapboxMap.addLayer(circles);
        addCircleLayer = true;
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
            case id.mapdevice13x:
                device_type = "1";
                break;
            case id.mapdevice23x:
                device_type = "3";
                break;
            case id.mapdevice43x:
                device_type = "2";
                break;
            case id.mapdevice33x:
                device_type = "5";
                break;
            case id.mapdevice63x:
                device_type = "6";
                break;
            default:
                device_type = "0";
                break;
        }
        isRequest = true;   // 是true 说明  不是 第一次加载 所以不用给 滚轮 设置 数据
        isClick = false;  // 设置为false  说明 viewpager是  隐藏的
        RequestFirst(mBuild_id, device_type);

        startTranslateAnimation();

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

    public void addMyMark(MapboxMap mapboxMap) throws JSONException {
        mErrPositionIndexOf = new ArrayList<>();
        integers = new ArrayList<>();
        List<Feature> markerCoordinates = FXMapBoxUtil.addFeature(mMarkerLayoutInfo, mMarkerErrList, positions, mErrPositionIndexOf, integers);
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(markerCoordinates);
        Layer layer = mapboxMap.getLayer("marker-layer");
        if (layer != null) {
            mapboxMap.removeLayer("marker-layer");  // 每次添加之前删除掉之前的 layer
        }
        GeoJsonSource source = (GeoJsonSource) mapboxMap.getSource("marker-source");
        if (source == null) {
            Source geoJsonSource = new GeoJsonSource("marker-source", featureCollection);
            mapboxMap.addSource(geoJsonSource);
        } else {
            source.setGeoJson(FeatureCollection.fromFeatures(markerCoordinates));
        }

        Bitmap icon = BitmapFactory.decodeResource(getResources(), drawable.errpoint2x);
        mapboxMap.addImage("my-marker", icon);
        SymbolLayer markers = new SymbolLayer("marker-layer", "marker-source")
                .withProperties(PropertyFactory.iconImage("my-marker"));
        markers.setProperties(
                textField("{key}"),
                textSize(12f),
                textColor(Color.WHITE)
        );
        mapboxMap.addLayer(markers);


    }

    class MapFloorResponseErrListener implements Response.Listener<String> {
        @Override
        public void onResponse(String o) {
            mMarkerLayoutInfo = json.fromJson(o.toString(), MarkerLayoutBean.class);
            Log.e("getSources", "onResponse****" + mMarkerLayoutInfo.toString());
            if (mMarkerLayoutInfo.getResult() != 1) {
                ToastShowUtil.showToast(WMSBaseActivity.this, "服务器数据错误");
                WMSBaseActivity.this.finish();
            } else {
                int mSize = mMarkerLayoutInfo.getDevice_list().size();
                Log.e("getSources", "  " + mSize + "  isFirstRequest  " + isFirstRequest);
                // 解析  返回的  故障 列表的 json
                if (mSize == 0) {  //等于 0 说明 返回的数据中时没有 故障
                    err_device_tv.setVisibility(View.GONE);
                    err_device_image.setVisibility(View.GONE);
                    Log.e("getSources", " " + mSize);
                    FXMapBoxUtil.removeSymbolLayer(mapboxMap);
                } else {
                    err_device_tv.setVisibility(View.VISIBLE);
                    err_device_image.setVisibility(View.VISIBLE);
                    String errSum = mSize + "";
                    err_device_tv.setText(errSum);
                }
                String errSum = mSize + "";
                err_device_tv.setText(errSum);
                HashMap<String, String> map = new HashMap<>();
                map.put("floor_id", mMarkerLayoutInfo.getFloor_id());
                map.put("build_id", mBuild_id);
                //请求 数据  设备的 坐标点    就是给  marker上 的 布局数据
                RequstUtils.loadPostMarkerErrList(user_netlimit, build_peanut, map, new MarkerLayoutErrResponseListener(), null);

            }
        }
    }

    class MapFloorResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String o) {
            mMarkerLayoutInfo = json.fromJson(o.toString(), MarkerLayoutBean.class);
            Log.e("getSources", "onResponse****" + mMarkerLayoutInfo.toString());
            if (mMarkerLayoutInfo.getResult() != 1) {
                ToastShowUtil.showToast(WMSBaseActivity.this, "服务器数据错误");
                WMSBaseActivity.this.finish();
            } else {
                int mSize = mMarkerLayoutInfo.getDevice_list().size();
                if (mSize == 0) {
                    err_device_tv.setVisibility(View.GONE);
                    err_device_image.setVisibility(View.GONE);
                    FXMapBoxUtil.removeSymbolLayer(mapboxMap);
                } else {
                    err_device_tv.setVisibility(View.VISIBLE);
                    err_device_image.setVisibility(View.VISIBLE);
                }
                String errSum = mSize + "";
                err_device_tv.setText(errSum);
                HashMap<String, String> map = new HashMap<>();
                map.put("floor_id", mMarkerLayoutInfo.getFloor_id());
                map.put("build_id", mBuild_id);
                //请求 数据  设备的 坐标点    就是给  marker上 的 布局数据
                RequstUtils.loadPostMarkerErrList(user_netlimit, build_peanut, map, new MarkerLayoutResponseListener(), null);


            }
        }
    }


    class MarkerLayoutErrResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String o) {
            Log.e("mMarkerErrList", o.toString());    //  这里是 setinfoWiondow 的数据 是 marker上的 布局数据
            mMarkerErrList = json.fromJson(o.toString(), MarkerErrListBean.class);
            if (mMarkerErrList.getResult() != 1) {
                isFirstRequest = false;     // 如果  失败 可以设置 第二次请求
                WMSBaseActivity.this.finish();
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
                try {
                    addMyMark(mapboxMap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addInfoMarker(mapboxMap); //    这里是第一次 添加兴趣点 数据
    /*
                HashMap<String, String> map = new HashMap<>();
                map.put("floorID", mMarkerErrList.getFloor_id());
                map.put("buildingID", mBuild_id);
                RequstUtils.loadPostinpoint(Http_URl.MAP_URL + Http_URl.POST_INPOINT, map, new MapCarAndPoint());*/
            }
        }
    }


    class MarkerLayoutResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String o) {
            Log.e("mMarkerErrList", o.toString());    //  这里是 setinfoWiondow 的数据 是 marker上的 布局数据
            mMarkerErrList = json.fromJson(o.toString(), MarkerErrListBean.class);
            if (mMarkerErrList.getResult() != 1) {
                isFirstRequest = false;     // 如果  失败 可以设置 第二次请求
                WMSBaseActivity.this.finish();
            } else {
                isFirstRequest = true;   //  设置true 说明 是第一加载 所有的坐标点了
                positions = new ArrayList<>();
                List<MarkerErrListBean.DeviceListJavaBean> device_list = mMarkerErrList.getDevice_list();
                for (int i = 0; i < device_list.size(); i++) {
                    MarkerErrListBean.DeviceListJavaBean mErrDevicePosion = device_list.get(i);
                    FXMapBoxUtil.webMercator2Position(mErrDevicePosion.getDevice_x(), mErrDevicePosion.getDevice_y(), positions);
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("floorID", mMarkerErrList.getFloor_id());
                map.put("buildingID", mBuild_id);

                RequstUtils.loadPostinpoint(Http_URl.MAP_URL + Http_URl.POST_INPOINT, map, new MapCarAndPoint());
            }
        }
    }


    public class MapCarAndPoint implements Response.Listener<String> {
        @Override
        public void onResponse(String s) {
            mInPointBean = json.fromJson(s.toString(), InPointBean.class);
            map.put(0, mInPointBean);   //  用作 滚轮 的 数据源  用来给地图 加载  地图上的 车位  车位号
            Log.e("测试数据", mInPointBean.toString());
     /*       if (mInPointBean.getResult() != null &&  !mInPointBean.getResult().equals("0")) {
                geiFirstDate();
            } else {  // 说明 是空的  就只 加载 绘制 故障点
                ToastShowUtil.showToast(WMSBaseActivity.this, "兴趣点数据错误");
                finish();
            }
*/
            addInfoMarker(mapboxMap);
            addMalfunctionAndLayout();
            addPolygons();

        }
    }

    @Override
    public boolean isInMultiWindowMode() {
        return super.isInMultiWindowMode();
    }

    @Override
    public void onScrollStateChange(NumberPickerView view, int scrollState) {
        Log.d(TAG, "onScrollStateChange : " + scrollState);
    }


    /**
     * 如果 滚轮 之后  容器中 请求 是有数据的 就直接接 加载  绘制 车位和  车位号
     */
    protected abstract void reuseCarPositionAndPolygon(int newVal);

    /**
     * @param json   滚轮 每次请求之后 返回的  当前的滚轮 索引 和  需要请求 解析 gson 的 引用
     * @param newVal
     */
    protected abstract void requestCarPositionAndPolygon(Gson json, int newVal);

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

    //viewpager 执行的动画
    public void startTranslateAnimation() {
        int i = DensityUtil.dip2px(this, 220);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -i);
        animation.setDuration(400);
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
        mMap_tile_pager.startAnimation(animation);//开始动画
        mMap_tile_pager.setVisibility(View.GONE);
    }

    //多边形监听
  /*  @Override
    public void onPolygonClick(@NonNull Polygon polygon) {
        polygon.setFillColor(Color.GREEN);

    }*/

    public class MapBoxInfoWindow implements MapboxMap.InfoWindowAdapter {
        @Nullable
        @Override
        public View getInfoWindow(@NonNull final Marker marker) {
            View view = View.inflate(WMSBaseActivity.this, layout.marker_layout, null);
            LinearLayout parent = new LinearLayout(WMSBaseActivity.this);
            parent.setLayoutParams(new LinearLayout.LayoutParams(ZXSystemUtil.dp2px(WMSBaseActivity.this, 200), ZXSystemUtil.dp2px(WMSBaseActivity.this, 80)));
            parent.setOrientation(LinearLayout.HORIZONTAL);
            parent.setBackgroundColor(ContextCompat.getColor(WMSBaseActivity.this, color.green));
            ImageView imageView = new ImageView(WMSBaseActivity.this);
            TextView textView = new TextView(WMSBaseActivity.this);
            imageView.setImageDrawable(ContextCompat.getDrawable(WMSBaseActivity.this, drawable.purple_marker));
            textView.setText(marker.getTitle());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(WMSBaseActivity.this, marker.getTitle() + "textView  点击", Toast.LENGTH_SHORT).show();
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

    /**
     * 添加兴趣点  标签 和 图标
     *
     * @param mapboxMap
     */
    public void addInfoMarker(MapboxMap mapboxMap) {
//            这里是第一次 添加兴趣点 数据
        if (mInPointBean.getResponse_inpoint() != null) { // 添加兴趣点
            List<InPointBean.ResponseInpointJavaBean> response_inpoint = mInPointBean.getResponse_inpoint();
            for (int i = 0; i < response_inpoint.size(); i++) {
                InPointBean.ResponseInpointJavaBean mInfobean = response_inpoint.get(i);
                List<Feature> mInfoPosition = new ArrayList<>();
                int mInpointType = mInfobean.getInpointType();
                List<Double> coordinates = mInfobean.getCoordinates();
                Position mCarCentral = FXMapBoxUtil.webMercator2PositionSingle(coordinates.get(0), coordinates.get(1));
                mInfoPosition.add(Feature.fromGeometry(
                        Point.fromCoordinates(Position.fromCoordinates(mCarCentral.getLongitude(), mCarCentral.getLatitude()))));
                FeatureCollection mInfoLayerCollection = FeatureCollection.fromFeatures(mInfoPosition);
                GeoJsonSource source = (GeoJsonSource) mapboxMap.getSource("INFO_SOURCE_ID" + i);
                if (source == null) {
                    source = new GeoJsonSource("INFO_SOURCE_ID" + i, mInfoLayerCollection);
                    mapboxMap.addSource(source);
                } else {
                    source.setGeoJson(mInfoLayerCollection);
                }
                String mImage_id = mInfoImageIDList.get(mInpointType - 1);
                SymbolLayer symbolLayer = new SymbolLayer("INFO_LAYER_ID" + i, "INFO_SOURCE_ID" + i).
                        withProperties(iconImage(mImage_id));
                mapboxMap.addLayer(symbolLayer);

            }
        }
    }

    /**
     * @param newVal 添加新区点 图标
     */
    protected abstract void addInfoImage(int newVal, List<InPointBean.ResponseInpointJavaBean> inpointBean, List<Feature> mInfoPosition);

    /**
     * 设置 兴趣点 需要添加的图标
     */
    protected void addImage() {
        Bitmap mChargingPoint = BitmapFactory.decodeResource(getResources(), R.drawable.chargingpoint3x);
        Bitmap mCollect = BitmapFactory.decodeResource(getResources(), R.drawable.collect3x);
        Bitmap mDistribution = BitmapFactory.decodeResource(getResources(), R.drawable.distribution3x);
        Bitmap mDown = BitmapFactory.decodeResource(getResources(), R.drawable.down3x);
        Bitmap mDraught = BitmapFactory.decodeResource(getResources(), R.drawable.draught3x);
        Bitmap mElevator = BitmapFactory.decodeResource(getResources(), R.drawable.elevator3x);
        Bitmap mEscalator = BitmapFactory.decodeResource(getResources(), R.drawable.escalator3x);
        Bitmap mExit3x = BitmapFactory.decodeResource(getResources(), R.drawable.exit3x);
        Bitmap mStairs = BitmapFactory.decodeResource(getResources(), R.drawable.stairs3x);
        Bitmap mUpstream = BitmapFactory.decodeResource(getResources(), R.drawable.upstream3x);
        Bitmap mWc3x = BitmapFactory.decodeResource(getResources(), R.drawable.wc3x);
        for (int i = 1; i < 13; i++) {
            mInfoImageIDList.add(INFO_IMAGE_TYPE_ID + "" + i);
        }
        mapboxMap.addImage(INFO_IMAGE_TYPE_ID + "1", mStairs);
        mapboxMap.addImage(INFO_IMAGE_TYPE_ID + "2", mEscalator);
        mapboxMap.addImage(INFO_IMAGE_TYPE_ID + "3", mElevator);
        mapboxMap.addImage(INFO_IMAGE_TYPE_ID + "4", mWc3x);
        mapboxMap.addImage(INFO_IMAGE_TYPE_ID + "5", mCollect);
        mapboxMap.addImage(INFO_IMAGE_TYPE_ID + "7", mChargingPoint);
        mapboxMap.addImage(INFO_IMAGE_TYPE_ID + "8", mExit3x);
        mapboxMap.addImage(INFO_IMAGE_TYPE_ID + "9", mUpstream);
        mapboxMap.addImage(INFO_IMAGE_TYPE_ID + "10", mDown);
        mapboxMap.addImage(INFO_IMAGE_TYPE_ID + "11", mDistribution);
        mapboxMap.addImage(INFO_IMAGE_TYPE_ID + "12", mDraught);
    }


    protected void addErrLayer(int newVal) {
        MyApplication application = (MyApplication) getApplication();
        user_netlimit = application.user_netlimit;
        build_peanut = application.build_peanut;
        HashMap<String, String> map = new HashMap<>();
        map.put("dev_type", "1"); // 表示 以 灯 为主
        map.put("build_id", mBuild_id);
        map.put("floor_id", projectUrlInfo.get(newVal).getFloorID());    //projectUrlInfo 根据  滚轮的返回的索引 进行加载
        map.put("md_type", "0");
        Log.e("projectUrlInfo", map.toString());
        // 请求 此楼层的 故障 列表
        RequstUtils.loadPostMarkerLayoutData(user_netlimit, build_peanut, map, new MapFloorResponseErrListener(), null);

    }


}
