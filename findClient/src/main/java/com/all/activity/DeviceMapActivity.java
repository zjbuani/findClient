package com.all.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.fengxun.base.MyApplication;
import com.fragment.FragmentTag;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.CameraListBean;
import com.http.response.bean.InPointBean;
import com.http.response.bean.MapFloorBean;
import com.http.response.bean.MarkerErrListBean;
import com.http.response.bean.MarkerLayoutBean;
import com.http.response.bean.RefreshCarStateBean;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.TileSet;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.request.Http_URl;
import com.request.RequstUtils;
import com.sdk.FXMapBoxUtil;
import com.utils.PrefUtils;
import com.utils.ToastShowUtil;
import com.view.MarkerInfoWindow;
import com.view.NumberPickerView;
import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

/**
 * Created by fengx on 2017/12/14.
 */

public class DeviceMapActivity extends MapBaseActivity implements MapboxMap.OnMapClickListener, View.OnClickListener, OnMapReadyCallback, NumberPickerView.OnValueChangeListener {
    private NumberPickerView picker;
    private ImageView mErrdevice02x;
    private ImageView mZoomMax;
    private ImageView mZoomMin;
    private ImageView err_device_image;
    private TextView err_device_tv;
    private ViewPager mMap_tile_pager;
    private MarkerInfoWindow mMarkerInfoWindow;
    private String mBuild_id;
    private List<String> layer_id_list;
    private List<String> source_id_list;
    private MapFloorBean mapFloorBean;
    private List<MapFloorBean.ResponseJavaBean> projectUrlInfo;
    private String mPagerFloorID;
    private double locationZoom = 9f;
    private MapboxMap mapboxMap;
    protected ArrayList<String> mInfoImageIDList = new ArrayList<>();
    private String INFO_IMAGE_TYPE_ID;
    private String loadMapUrl_bbox = "?bbox={bbox-epsg-3857}&format=image/png&service=WMS&version=1.1.1&request=GetMap&"
            + "srs=EPSG:3857&width=232&height=512&layers=";
    private String user_netlimit;
    private String build_peanut;

    private Gson json;
    private MarkerErrListBean mMarkerErrList;
    private ArrayList<Position> positions;
    private boolean addCircleLayer;
    private String CLUSTER = "cluster";


    public String MCAR_SOURCE1 = "Car-source1";
    public String MCAR_NUMBER_LAYER_ID = "CARNUMBER_LAYER_ID";
    private InPointBean mInPointBean;
    private String mSaveuser_id;
    private int newVal = 0;
    private String EARTHQUAKES = "earthquakes";
    private String CLUSTER_CAMERA = "cluster";
    private Map<String, String> mClusterLayerIdAndSourceId = new HashMap<>();
    private Map<String, Position> mClusterLayerPositions = new HashMap<>();
    private Map<String, MarkerErrListBean.DeviceListJavaBean> mClusterLayerBean = new HashMap<>();
    private int tm = 0;
    HashMap<String, PolygonOptions> mCarNumber = new HashMap<>();
    protected Map<Integer, ArrayList<Position>> mAllPositon = new HashMap<>();
    private ConcurrentHashMap<Integer, InPointBean> map = new ConcurrentHashMap<>();

    private List<MarkerErrListBean.DeviceListJavaBean> mPositions_camera_bean; // 摄像头的javaBean
    private BuildingFloorBean mBuildFloorBean;
    private int floor_num;
    // 一个 摄像头的小圆点layer  对应 一个 摄像头的Layout bean
    public HashMap<String, CameraListBean.CameraListJavaBean> mShowWindowMap = new HashMap<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null && mHandler != null) {
//            mapView.onDestroy();
            mHandler.removeMessages(1);
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.map_layout;
    }

    @Override
    protected void initView() {
        mapView = (MapView) findViewById(R.id.mapView);
        picker = (NumberPickerView) findViewById(R.id.picker);
        err_device_image = (ImageView) findViewById(R.id.err_device_image); // 错误的黄色小图片
        err_device_tv = (TextView) findViewById(R.id.err_device_tv);        // 错误总数
        mMap_tile_pager = (ViewPager) findViewById(R.id.map_tile_pager); //首页的 轮播图
        mErrdevice02x = (ImageView) findViewById(R.id.errdevice02x); // 回到中心点
        mZoomMax = (ImageView) findViewById(R.id.zoomMax);  // 放大
        mZoomMin = (ImageView) findViewById(R.id.zoomMin); // 缩小
        json = new Gson();
        layer_id_list = FXMapBoxUtil.layer_id_list;
        source_id_list = FXMapBoxUtil.source_id_list;
        mSaveuser_id = PrefUtils.getString(this, "user", ""); // 用户账号
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        err_device_image.setVisibility(View.GONE);
        err_device_tv.setVisibility(View.GONE);
        mMap_tile_pager.setVisibility(View.GONE);
        mErrdevice02x.setVisibility(View.GONE);
        mMarkerInfoWindow = new MarkerInfoWindow(this);   // 设置弹出的 dialog 的 布局显示
        Intent intent = getIntent();
        //获取到此 开始时选择的 项目 ID
        mBuild_id = intent.getStringExtra(FragmentTag.SumListTag);
        //右侧菜单列表的 对象
        mBuildFloorBean = (BuildingFloorBean) intent.getSerializableExtra(FragmentTag.RIGHT_REQUEST);
        floor_num = mBuildFloorBean.getFloor_list().get(0).getFloor_num();
        mapFloorBean = (MapFloorBean) intent.getSerializableExtra(FragmentTag.MAP_URL);
        //这里获取到的是 上个界面 返回请求 之后 需要加载底图 的 URL
        projectUrlInfo = mapFloorBean.getResponse();
        loadMap(projectUrlInfo);    //初始化 地图的 url   数据
        mPagerFloorID = projectUrlInfo.get(0).getFloorID();
        MyApplication application = (MyApplication) getApplication();
        user_netlimit = application.user_netlimit;
        build_peanut = application.build_peanut;

    }

    @Override
    protected void initListener() {
        mapView.getMapAsync(this);
        mZoomMax.setOnClickListener(this);
        mZoomMin.setOnClickListener(this);
        picker.setOnValueChangedListener(this);
    }

    private void RequestFirst(String build_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("floor_id", mPagerFloorID);
        map.put("build_id", mBuild_id);
        //请求 数据  设备的 坐标点    就是给  marker上 的 布局数据
        RequstUtils.loadPostMarkerErrList(user_netlimit, build_peanut, map, new MarkerLayoutResponseListener(), null);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setMinZoomPreference(8.6); // 设置最小的 缩放级别最多为 8.6
        FXMapBoxUtil.inVisibleLogoAndCompass(mapboxMap); //隐藏Logo
        FXMapBoxUtil.removeAllLayer(mapboxMap); // 删除掉所有的 地图
        mapboxMap.setOnMapClickListener(this);
        FXMapBoxUtil.camera(mapboxMap, new LatLng(-0.5493115184203139, 0.05679479101602491)); // 定位点
        FXMapBoxUtil.configMapBox(mapboxMap);
        for (int i = 0; i < layer_id_list.size(); i++) {
            mapboxMap.removeSource(layer_id_list.get(i) + "");
            mapboxMap.removeLayer(layer_id_list.get(i) + "");
        }
        String wms_url = projectUrlInfo.get(0).getFloorOutline() + loadMapUrl_bbox + projectUrlInfo.get(0).getFloorUrl();
        TileSet tileset = new TileSet("tileset", wms_url);
        RasterSource webMapSource = new RasterSource(0 + "", tileset, 100);
        FXMapBoxUtil.addRasterLayer(mapboxMap, webMapSource, 0 + "", 0 + "");
        addImage();

        RequestFirst(mBuild_id);
    }

    @Override
    protected void geiFirstDate() {
        updataPickNumber();
    }

    // 添加 pickNumber数据
    private void updataPickNumber() {
        pickerSetData();
    }

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
        Log.e("Tag", "如果   layer_id_list  的总数 不等于0 说明已经添加过了  " + layer_id_list.size() + "  " + layer_id_list.toString());
        addMyMark(mapboxMap);
    }

    public void addMyMark(MapboxMap mapboxMap) {


        requestCarPositionAndPolygon2(0);

    }

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        if (layer_id_list.contains(newVal + "")) {
            source_id_list.remove(newVal + "");
            layer_id_list.remove(newVal + "");
            mapboxMap.removeLayer(newVal + "");
            mapboxMap.removeSource(newVal + "");
        }
        mPagerFloorID = projectUrlInfo.get(newVal).getFloorID();  //作为 每次请求 全楼层 设备坐标的  楼层ID
        List<BuildingFloorBean.FloorListJavaBean> list = mBuildFloorBean.getFloor_list();
        for (int i = 0; i < list.size(); i++) {
            String floor_id = list.get(i).getFloor_id();
            if (mPagerFloorID.equals(floor_id)) {
                floor_num = list.get(i).getFloor_num();
            }
        }
        String wms_url = projectUrlInfo.get(newVal).getFloorOutline() + loadMapUrl_bbox + projectUrlInfo.get(newVal).getFloorUrl();
        TileSet tileset = new TileSet("tileset", wms_url);
        RasterSource webMapSource = new RasterSource(newVal + "", tileset, 100);
        FXMapBoxUtil.addRasterLayer(mapboxMap, webMapSource, newVal + "", newVal + "");
        this.newVal = newVal;
        rollRequestMarkerLayout();
        addInfoMarker(mapboxMap); //    这里是第一次 添加兴趣点 数据


    }

    /**
     * 刷新 车位状态
     */
    @Override
    protected void refreshCarState() {
        Map<String, String> maps = new HashMap<>();
        maps.put("buildingID", mBuild_id);
        maps.put("floorID", mPagerFloorID);
        maps.put("tm", tm + "");
        RequstUtils.loadPostRefreshCarState(maps, new RefrashCarStateResponse());
    }

    //  摄像头的红绿变化
    @Override
    protected void changeTheCamera() {
        ArrayList<String> params = getArrayParams();
        String key = json.toJson(params);
        Map<String, String> maps = new HashMap<>();
        maps.put("build_id", mBuild_id);
        maps.put("user_id", mSaveuser_id);
        maps.put("pag", "0");
        maps.put("floor", floor_num + "");
        maps.put("key", key);

        RequstUtils.loadPostCameraList(user_netlimit, build_peanut, maps, new HandlerRefrashResponse());
    }

    class RefrashCarStateResponse implements Response.Listener<String> {
        @Override
        public void onResponse(String o) {
            RefreshCarStateBean mRefreshCarStateBean = json.fromJson(o, RefreshCarStateBean.class);
            String result = mRefreshCarStateBean.getResult();
            if (result == null) {
                return;
            }
            if (!result.equals("1")) {
                return;
            }
            List<RefreshCarStateBean.ResponseJavaBean> mRefreshBean = mRefreshCarStateBean.getResponse();
            if (mCarNumber.size() == 0) {
                return;
            }
            for (RefreshCarStateBean.ResponseJavaBean bean : mRefreshBean) {
                PolygonOptions polygonOptions = mCarNumber.get(bean.getCarNum());
                if (polygonOptions == null) {
                    return;
                }
                Polygon polygon = polygonOptions.getPolygon();
                if (bean.getState().equals("0")) {
                    polygon.setFillColor(Color.parseColor("#7FB53B"));
                } else if (bean.getState().equals("1")) {
                    polygon.setFillColor(Color.parseColor("#F97d70"));
                }
            }
        }
    }

    class HandlerRefrashResponse implements Response.Listener<String> {
        @Override
        public void onResponse(String o) {
            CameraListBean sumLightListBean = json.fromJson(o, CameraListBean.class);
            if (sumLightListBean.getResult() != 1) {
                return;
            }
            if (sumLightListBean.getCamera_list() != null) {
                // 刷新请求 回来的 动态 摄像头更新
                List<CameraListBean.CameraListJavaBean> beanList = sumLightListBean.getCamera_list();
                for (String layer : mClusterLayerIdAndSourceId.keySet()) {
                    CircleLayer mCircle = (CircleLayer) mapboxMap.getLayer(layer);
                    Position position = mClusterLayerPositions.get(layer); //拿到此layer的 经纬度
                    MarkerErrListBean.DeviceListJavaBean mCameraBean = mClusterLayerBean.get(layer);
                    int fmn = mCameraBean.getFmn();
                    int area_id = mCameraBean.getArea_id();
                    for (int i = 0; i < beanList.size(); i++) {
                        CameraListBean.CameraListJavaBean cameraListJavaBean = beanList.get(i);

                        int camera_id = cameraListJavaBean.getCamera_id();
                        int camera_area = cameraListJavaBean.getCamera_area();
                        if (fmn == camera_id && area_id == camera_area) {
                            mShowWindowMap.put(layer, cameraListJavaBean);
                            mCircle.setProperties(
                                    circleColor("#" + cameraListJavaBean.getRg_color())
                            );
                            break;
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
    }


    private void requestCarPositionAndPolygon2(int newVal) {  //请求 设备的  车位图形的坐标   返回此楼层的所有的 车位  每个车位的坐标点为5个点
        HashMap<String, String> map = new HashMap<>();
        map.put("floorID", projectUrlInfo.get(newVal).getFloorID());
        map.put("buildingID", mBuild_id);
        RequstUtils.loadPostinpoint(Http_URl.MAP_URL + Http_URl.POST_INPOINT, map, new MapCarPoint());
    }

    private void requestCarPositionAndPolygon(int newVal) {
        InPointBean inPointBean = map.get(newVal);
        if (mInPointBean.getResult() != null && !inPointBean.getResult().equals("0")) {
            ArrayList<Feature> markerCoordinates = new ArrayList<>();
            List<InPointBean.ResponseCarposJavaBean> response_carpos = inPointBean.getResponse_carpos();
            ArrayList<PolygonOptions> polygonOptionses = new ArrayList<>();
            for (int i = 0; i < response_carpos.size(); i++) {
                InPointBean.ResponseCarposJavaBean carposJavaBean = response_carpos.get(i);
                addCarNumber(polygonOptionses, carposJavaBean);
                addCarPositon(markerCoordinates, carposJavaBean);
            }
            mapboxMap.addPolygons(polygonOptionses);
            mapboxAddCarNumberLayer(markerCoordinates);
            addCamera(newVal);
        }
    }


    public class MapCarPoint implements Response.Listener<String> {
        @Override
        public void onResponse(String s) {
            mInPointBean = json.fromJson(s.toString(), InPointBean.class);
            map.put(newVal, mInPointBean);
            Log.e("onValueChange", mInPointBean.toString());
            if (mInPointBean.getResult() != null && !mInPointBean.getResult().equals("0")) {
                ArrayList<Feature> markerCoordinates = new ArrayList<>();
                List<Feature> mInfoPosition = new ArrayList<>();
                List<InPointBean.ResponseCarposJavaBean> response_carpos = mInPointBean.getResponse_carpos();
                List<InPointBean.ResponseInpointJavaBean> response_inpoint = mInPointBean.getResponse_inpoint();
                ArrayList<PolygonOptions> polygonOptionses = new ArrayList<>();
                for (int i = 0; i < response_carpos.size(); i++) {
                    InPointBean.ResponseCarposJavaBean carposJavaBean = response_carpos.get(i);
                    addCarNumber(polygonOptionses, carposJavaBean);
                    addCarPositon(markerCoordinates, carposJavaBean);
                }

                mapboxMap.addPolygons(polygonOptionses);
                mapboxAddCarNumberLayer(markerCoordinates);
                addCamera(newVal);
            } else {
                Log.e("onCancel","MapCarPoint");
//                finish();
            }
        }

    }

    public void rollRequestMarkerLayout() {
        HashMap<String, String> map = new HashMap<>();
        map.put("floor_id", mPagerFloorID);
        map.put("build_id", mBuild_id);
        //请求 数据  设备的 坐标点    就是给  marker上 的 布局数据
        RequstUtils.loadPostMarkerErrList(user_netlimit, build_peanut, map, new RollMarkerLayoutResponseListener(), null);

    }

    private void addCarPositon(List<Feature> markerCoordinates, InPointBean.ResponseCarposJavaBean mCarposJavaBean) {
        String carNum = mCarposJavaBean.getCarNum();
        JsonObject json = new JsonObject();
        json.addProperty("Num", carNum);
        List<Double> carposPoint = mCarposJavaBean.getCarposPoint();
        Position mCarCentral = FXMapBoxUtil.webMercator2PositionSingle(carposPoint.get(0), carposPoint.get(1));
        markerCoordinates.add(Feature.fromGeometry(
                Point.fromCoordinates(Position.fromCoordinates(mCarCentral.getLongitude(), mCarCentral.getLatitude())), json));
    }

    private void mapboxAddCarNumberLayer(List<Feature> markerCoordinates) {
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


    /**
     * 添加车位
     */
    public void addPolygons() {
        mCarNumber.clear();
        String result = mInPointBean.getResult();
        if (!"1".equals(result)) { // 这里说明 返回的 车位数据是不对的
            return;
        } else {
            List<InPointBean.ResponseCarposJavaBean> mCarpos = mInPointBean.getResponse_carpos();
            ArrayList<PolygonOptions> mAllCarPosition = new ArrayList<>();
            List<Feature> markerCoordinates = new ArrayList<>();
            for (int i = 0; i < mCarpos.size(); i++) {
                InPointBean.ResponseCarposJavaBean mCarposJavaBean = mCarpos.get(i);
                addCarNumber(mAllCarPosition, mCarposJavaBean);
                addCarPositon(markerCoordinates, mCarposJavaBean);// 绘制车位的 图形
            }
            mapboxMap.addPolygons(mAllCarPosition);
            mapboxAddCarNumberLayer(markerCoordinates);
        }
    }

    //加载 车位号
    private void addCarNumber(ArrayList<PolygonOptions> mAllCarPosition, InPointBean.ResponseCarposJavaBean mCarposJavaBean) {
        PolygonOptions polygonOptions = new PolygonOptions();
        List<List<Double>> coordinates = mCarposJavaBean.getCoordinates();
        if (coordinates.size() == 5) {  // 说明是 5 个点
            for (int positon = 0; positon < coordinates.size(); positon++) {
                List<Double> doubles = coordinates.get(positon);
                Position position = FXMapBoxUtil.webMercator2PositionSingle(doubles.get(0), doubles.get(1));
                polygonOptions.add(new LatLng(position.getLatitude(), position.getLongitude()));
            }
        }
        mCarNumber.put(mCarposJavaBean.getCarNum(), polygonOptions);  // 用于做 车位的动态刷新
        mAllCarPosition.add(polygonOptions.fillColor(
                Color.parseColor("#C0C0C0")).                //设置 车位 颜色
                strokeColor(Color.parseColor("#000000")));   //设置车位 边框 颜色


    }


    @Override
    public void onMapClick(@NonNull LatLng point) {
          PointF pixel = mapboxMap.getProjection().toScreenLocation(point);

//        onMapClick
        for (String clusterId : mClusterLayerIdAndSourceId.keySet()) {
            List<Feature> mFeatures = mapboxMap.queryRenderedFeatures(pixel, clusterId);
            Log.e("onCancel", "onMapClick 进入前  " + mFeatures.size());
            if (mFeatures != null && mFeatures.size() != 0) {
                Log.e("onCancel", "onMapClick 进入前 mMarkerErrList  " + mPositions_camera_bean.toString());
                Log.e("onCancel", "onMapClick 进入前 mClusterLayerBean  " + mClusterLayerBean.get(clusterId).toString());
                for (Feature feature : mFeatures) {
                    if (!addCircleLayer) {
//                        addFindPoint(feature.getGeometry());

                        float x = pixel.x;
                        float y = pixel.y;

                  /*      mMarkerInfoWindow.setX(y);
                        mMarkerInfoWindow.setY(x);*/
                        mMarkerInfoWindow.show();
                        mMarkerInfoWindow.mMarker_tv_err.setText("温度 ");
                        MarkerErrListBean.DeviceListJavaBean deviceListJavaBean = mClusterLayerBean.get(clusterId);
                        CameraListBean.CameraListJavaBean cameraListJavaBean = mShowWindowMap.get(clusterId);
                        cameraListJavaBean.setChannel(deviceListJavaBean.getChannel());
                        mMarkerInfoWindow.showDialog(cameraListJavaBean);
                    }

//            Number key = feature.getNumberProperty("key");   // 这样拿到了 点击后 获取到的 显示的 数字
                }
            } else {
                Layer layer = mapboxMap.getLayer(CLUSTER);
                if (layer != null) {
                    mapboxMap.removeLayer(layer);
                    mMarkerInfoWindow.cancel();
                }
                continue;
            }
        }
/*
        for (String clusterId : mClusterLayerIdAndSourceId.keySet()) {
            List<Feature>   mFeatures = mapboxMap.queryRenderedFeatures(pixel, clusterId);
            Log.e("onCancel", "onMapClick 进入"  + mFeatures.size());
            if (mFeatures == null || mFeatures.isEmpty() || mFeatures.size() == 0) {
                addCircleLayer = false;
                Layer layer = mapboxMap.getLayer(CLUSTER);
                if (layer != null) {
                    mapboxMap.removeLayer(layer);
                    mMarkerInfoWindow.cancel();
                }
                break;
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
        }  */


    }


    /**
     * 返回车位  车位号   兴趣点 坐标 数据
     */
    public class MapCarAndPoint implements Response.Listener<String> {
        @Override
        public void onResponse(String s) {
            mInPointBean = json.fromJson(s.toString(), InPointBean.class);
            map.put(newVal, mInPointBean);   //  用作 滚轮 的 数据源  用来给地图 加载  地图上的 车位  车位号
            Log.e("测试数据", mInPointBean.toString());
            Log.e("测试数据", positions.toString());
            addCamera(newVal);
            addPolygons();
            addInfoMarker(mapboxMap);
        }
    }

    /**
     * 添加 摄像头 数据
     */
    private void addCamera(int newVal) {
     /*   mClusterLayerPositions.clear();
        mClusterLayerIdAndSourceId.clear();
        mClusterLayerBean.clear();*/
//        ArrayList<Position> positions = mAllPositon.get(newVal);
        ArrayList<Position> positions = mAllPositon.get(newVal);
        for (int i = 0; i < positions.size(); i++) {
            List<Feature> mInfoPosition = new ArrayList<>();
            Position mCarCentral = positions.get(i);
            MarkerErrListBean.DeviceListJavaBean mCameraBean = mPositions_camera_bean.get(i);   // 所有类型 为 2 的摄像头 对象
            mInfoPosition.add(Feature.fromGeometry(
                    Point.fromCoordinates(Position.fromCoordinates(mCarCentral.getLongitude(), mCarCentral.getLatitude()))));
            FeatureCollection featureCollection = FeatureCollection.fromFeatures(mInfoPosition);
            GeoJsonSource geoJsonSource;
            if (mapboxMap.getSource(EARTHQUAKES + i) != null) {
                geoJsonSource = (GeoJsonSource) mapboxMap.getSource(EARTHQUAKES + i);
                geoJsonSource.setGeoJson(featureCollection);
            } else {
                geoJsonSource = new GeoJsonSource(EARTHQUAKES + i, featureCollection);
                mapboxMap.addSource(geoJsonSource);
            }
            if (mapboxMap.getLayer(CLUSTER_CAMERA + i) != null) {
                mapboxMap.removeLayer(mapboxMap.getLayer(CLUSTER_CAMERA + i));
            }
            mClusterLayerPositions.put(CLUSTER_CAMERA + i, mCarCentral);
            mClusterLayerIdAndSourceId.put(CLUSTER_CAMERA + i, EARTHQUAKES + i);// 添加摄像头 图标的 layerID,//添加资源的 ID
            mClusterLayerBean.put(CLUSTER_CAMERA + i, mCameraBean);   // 添加摄像头 图标的 layerID 对应的对象的
            CircleLayer circles = new CircleLayer(CLUSTER_CAMERA + i, EARTHQUAKES + i);
            circles.withProperties(
                    circleColor("#C0C0C0"),
                    circleRadius(8f)
            );
            mapboxMap.addLayer(circles);
        }
        mHandler.sendEmptyMessage(new Message().what = 1);

    }

    class MarkerLayoutResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String o) {
            Log.e("mMarkerErrList", "Listener***" + o.toString());    //  这里是 setInfoWindow 的数据 是 marker上的 布局数据 是此楼层的 所有的设备坐标
            mMarkerErrList = json.fromJson(o.toString(), MarkerErrListBean.class);  // 是此楼层的 所有的设备坐标
            if (mMarkerErrList.getResult() != 1) {
                isFirstRequest = false;     // 如果  失败 可以设置 第二次请求
                Log.e("onCancel","MarkerLayoutResponseListener  " );
                DeviceMapActivity.this.finish();
            } else {
                isFirstRequest = true;   //  设置true 说明 是第一加载 所有的坐标点了
                positions = new ArrayList<>();
                // camera
                mPositions_camera_bean = new ArrayList<>();
                List<MarkerErrListBean.DeviceListJavaBean> device_list = mMarkerErrList.getDevice_list();
                for (int i = 0; i < device_list.size(); i++) {
                    MarkerErrListBean.DeviceListJavaBean mErrDevicePosion = device_list.get(i);
                    String dev_type = device_list.get(i).getDev_type();
                    if ("2".equals(dev_type)) {
                        Log.e("MarkerErrListBean", i + " ");
                        mPositions_camera_bean.add(mErrDevicePosion);
                        FXMapBoxUtil.webMercator2Position(mErrDevicePosion.getDevice_x(), mErrDevicePosion.getDevice_y(), positions);
                    } else {
                        continue;
                    }

                }
            }
            mAllPositon.put(newVal, positions);  // 用来存储 每层的 摄像头 位置
            HashMap<String, String> map = new HashMap<>();
            map.put("floorID", mMarkerErrList.getFloor_id());
            map.put("buildingID", mBuild_id);
            RequstUtils.loadPostinpoint(Http_URl.MAP_URL + Http_URl.POST_INPOINT, map, new MapCarAndPoint());
            geiFirstDate();
        }
    }

    class RollMarkerLayoutResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String o) {
            Log.e("mMarkerErrList", "Listener***" + o.toString());    //  这里是 setinfoWiondow 的数据 是 marker上的 布局数据 是此楼层的 所有的设备坐标
            mMarkerErrList = json.fromJson(o.toString(), MarkerErrListBean.class);  // 是此楼层的 所有的设备坐标
            if (mMarkerErrList.getResult() != 1) {
                isFirstRequest = false;     // 如果  失败 可以设置 第二次请求
                Log.e("onCancel","RollMarkerLayoutResponseListener");
                DeviceMapActivity.this.finish();
            } else {
                isFirstRequest = true;   //  设置true 说明 是第一加载 所有的坐标点了
                positions = new ArrayList<>();
                // camera
                mPositions_camera_bean = new ArrayList<>();
                List<MarkerErrListBean.DeviceListJavaBean> device_list = mMarkerErrList.getDevice_list();
                for (int i = 0; i < device_list.size(); i++) {
                    MarkerErrListBean.DeviceListJavaBean mErrDevicePosion = device_list.get(i);
                    String dev_type = device_list.get(i).getDev_type();
                    if ("2".equals(dev_type)) {
                        Log.e("MarkerErrListBean", i + " ");
                        mPositions_camera_bean.add(mErrDevicePosion);
                        FXMapBoxUtil.webMercator2Position(mErrDevicePosion.getDevice_x(), mErrDevicePosion.getDevice_y(), positions);
                    } else {
                        continue;
                    }
                }
            }
            mAllPositon.put(newVal, positions);  // 用来存储 每层的 摄像头 位置
            if (map.get(newVal) != null) {    // 说明有数据 直接加载就可以
                requestCarPositionAndPolygon(newVal);
            } else {
                requestCarPositionAndPolygon2(newVal);
            }

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
                new int[]{20, ContextCompat.getColor(this, R.color.colorGreen)},
                new int[]{0, ContextCompat.getColor(this, R.color.mapbox_blue)}
        };
        CircleLayer circles = new CircleLayer(CLUSTER, "cluster_source");
        circles.setProperties(
                circleColor(layers[1][1]),
                circleRadius(7f)
        );

        mapboxMap.addLayer(circles);
        addCircleLayer = true;
    }


    protected void loadMap(List<MapFloorBean.ResponseJavaBean> projectUrlInfo) {
        if (projectUrlInfo.size() == 0) {
            ToastShowUtil.showToast(this, "无此地图的url");
            Log.e("onCancel","loadMap");
            finish();
        }
    }

    // 设置 地图的 缩放级别
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zoomMax: // 回到放大
                locationZoom = locationZoom + 0.2;
                if (mapboxMap != null) {
                    mapboxMap.setZoom(locationZoom);
                }
//                mapboxMap.setZoom(); locationZoom
                Log.e("最大的缩放级别", mapboxMap.getMaxZoomLevel() + "");
                break;
            case R.id.zoomMin: // 回到缩小
                locationZoom = locationZoom - 0.2;
                Log.e("locationZoom", "locationZoom " + locationZoom);
                if (mapboxMap != null) {
                    List<Polygon> list = mapboxMap.getPolygons();
                    if (list != null) {
                        for (int i = 0; i < 1; i++) {
                            Polygon polygon = list.get(i);
                            List<LatLng> points = polygon.getPoints();
                            for (int h = 0; h < points.size(); h++) {
                                Log.e("PositionCar", points.get(h).toString() + "   size  " + points.size());
                            }
//                            if (latLng.getLongitude() == )
                        }
                    }
                    mapboxMap.setZoom(locationZoom);
                }
                break;

        }
    }


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
                SymbolLayer layer = (SymbolLayer) mapboxMap.getLayer("INFO_LAYER_ID" + i);
                if (layer != null) {
                    mapboxMap.removeLayer(layer);
                }
                SymbolLayer symbolLayer = new SymbolLayer("INFO_LAYER_ID" + i, "INFO_SOURCE_ID" + i).
                        withProperties(iconImage(mImage_id));
                mapboxMap.addLayer(symbolLayer);

            }
        }
    }

    public ArrayList<String> getArrayParams() {
        ArrayList<String> params = new ArrayList<String>(); //下拉列表的请求
        params.add("camera_id");
        params.add("camera_area");
        params.add("dev_type");
        params.add("camera_errcode");
        params.add("camera_temp");
        params.add("rg_color");

        return params;
    }


}
