package com.all.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.TileSet;
import com.zhy.sample.folderlayout.R;

import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

/**
 * Created by fengx on 2017/11/16.
 */

public class MapActivity extends MapBaseActivity {

    private FragmentTransaction mFragmentTransaction;
    private MapboxMapOptions options;
    private static final String WMS_URL = "http://139.196.5.153:8181/geoserver/wms?" +
            "bbox={bbox-epsg-3857}&format=image/png&service=WMS&version=1.1.1&request=GetMap&"
            + "srs=EPSG:3857&width=232&height=512&layers=F0028B0004LX02_MAP";

    protected int initLayout() {
        return R.layout.map_activity;
    }

    @Override
    protected void initListener() {

    }

    protected void initView() {
//        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
    }


    protected void initData(Bundle savedInstanceState) {
        // Create fragment
        SupportMapFragment mapFragment;
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            // Build mapboxMap
            options = new MapboxMapOptions();
            options.styleUrl(Style.LIGHT);
            options.camera(new CameraPosition.Builder()
                    .target(new LatLng(0, 0))
                    .zoom(9)
                    .build());
            // Create map fragment
            mapFragment = SupportMapFragment.newInstance(options);
            // Add map fragment to parent container
            transaction.add(R.id.container, mapFragment, "com.mapbox.map");

            transaction.commit();
        } else {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }
        MapView mapView = new MapView(this, options);
        mapView.onCreate(savedInstanceState);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                removeMapBoxMap(mapboxMap);
                inVisibleLogo(mapboxMap);
                CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(latLng.getLatitude(), latLng.getLongitude()))
                        .zoom(10)//放大尺度 从0开始，0即最大比例尺，最大未知，17左右即为街道层级
                        .bearing(180)//地图旋转，但并不是每次点击都旋转180度，而是相对于正方向180度，即如果已经为相对正方向180度了，就不会进行旋转
                        .tilt(30)//地图倾斜角度，同上，相对于初始状态（平面）成30度
                        .build();//创建CameraPosition对象
                LatLng latLn = new LatLng(-0.5987163198054858, 1.4338852517928602E-10);
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLng(latLn));
                TileSet tileset = new TileSet("tileset", WMS_URL);
                RasterSource webMapSource = new RasterSource("web-map-source", tileset, 100);
                mapboxMap.addSource(webMapSource);
                // Add the web map source to the map.
                RasterLayer webMapLayer = new RasterLayer("web-map-layer-map", "web-map-source");

                mapboxMap.addLayer(webMapLayer);

                List<Layer> list = mapboxMap.getLayers();

                Log.e("list.size", list.size() + "");
                MarkerView markerView = mapboxMap.addMarker(new MarkerViewOptions()
                        .position(new LatLng(-0.5987163198054858, 1.4338852517928602E-10)).title("title").snippet("测试"));

            }
        });
    }

    @Override
    protected void geiFirstDate() {

    }

    //去除MapBox 上的 地图 然后用于加载自己的地图
    public void removeMapBoxMap(MapboxMap mapboxMap) {
        List<Layer> layers = mapboxMap.getLayers();
        for (Layer layer : layers) {
            mapboxMap.removeLayer(layer);
        }
    }

    //  隐藏掉mapBox的logo 和点击按钮和 指南针
    public void inVisibleLogo(MapboxMap mapboxMap) {
        UiSettings uiSettings = mapboxMap.getUiSettings();
        uiSettings.setLogoEnabled(false); // 取消掉log
        uiSettings.setCompassEnabled(false);//隐藏指南针
//        uiSettings.setTiltGesturesEnabled(true);//设置是否可以调整地图倾斜角
//        uiSettings.setRotateGesturesEnabled(true);//设置是否可以旋转地图
        uiSettings.setAttributionEnabled(false);//设置是否显示那个提示按钮
    }

    private void initToken() {
        //设置accessToken
        Mapbox.getInstance(this, getString(R.string.access_token));
    }

}
