package com.sdk;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.all.activity.AddWMSMapActivity;
import com.google.gson.JsonObject;
import com.http.response.bean.MarkerErrListBean;
import com.http.response.bean.MarkerLayoutBean;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.InfoWindow;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.exceptions.InvalidMarkerPositionException;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.TileSet;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;
import com.vividsolutions.jts.math.Vector2D;
import com.zhy.sample.folderlayout.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;
import static com.mapbox.services.commons.models.Position.fromCoordinates;

/**
 * Created by fengx on 2017/11/16.
 */

public class FXMapBoxUtil {
    public View view;
    public static List<String> layer_id_list = new ArrayList<>(); //默认存储所添加的图层ID
    public static List<String> source_id_list = new ArrayList<>(); //默认存储所添加的图层ID

    /**
     * @param mapboxMap
     * @param latLn     指定地图显示的中心点
     */
    public static void camera(MapboxMap mapboxMap, LatLng latLn) {
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLng(latLn));
    }

    /**
     * @param mapboxMap 指定地图显示的 0 0 点
     */
    public static void cameraDefault(MapboxMap mapboxMap) {
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0, 0)));
    }

    /**
     * @param mapboxMap 指定地图显示的 0 0 点
     */
    public static void addRasterLayer(MapboxMap mapboxMap, RasterSource source, String LayerId, String sourceId) {
        if (LayerId == null) LayerId = "";
        if (sourceId == null) sourceId = "";
        Log.e("sourc", sourceId + " *******  " + mapboxMap.getSources().contains(sourceId));
        mapboxMap.addSource(source);
        FXMapBoxUtil.cameraDefault(mapboxMap);
        layer_id_list.add(LayerId);
        source_id_list.add(sourceId);
        RasterLayer webMapLayer = new RasterLayer(LayerId, sourceId);
        mapboxMap.addLayer(webMapLayer);
    }

    public static void configMapBox(MapboxMap mapboxMap) {
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

    /**
     * @param mapboxMap 隐藏掉mapBox的logo 和点击按钮和 指南针
     */
    public static void inVisibleLogoAndCompass(MapboxMap mapboxMap) {
        UiSettings uiSettings = mapboxMap.getUiSettings();
        uiSettings.setLogoEnabled(false); // 取消掉log
        uiSettings.setCompassEnabled(false);//隐藏指南针
//        uiSettings.setTiltGesturesEnabled(true);//设置是否可以调整地图倾斜角
//        uiSettings.setRotateGesturesEnabled(true);//设置是否可以旋转地图
        uiSettings.setAttributionEnabled(false);//设置是否显示那个提示按钮
    }

    /**
     * @param mapboxMap 删除所有图层
     */
    public static void removeLayer(MapboxMap mapboxMap) {
        List<Layer> layers = mapboxMap.getLayers();
        for (Layer layer : layers) {
//            mapboxMap.removeLayer(layer);
        }
    }

    /**
     * @param mapboxMap 删除所有图层
     */
    public static void removeAllLayer(MapboxMap mapboxMap) {
        List<Layer> layers = mapboxMap.getLayers();
        for (Layer layer : layers) {
            mapboxMap.removeLayer(layer);
        }
    }
    /**
     * @param mapboxMap 删除所有图层
     */
    public static void removeAllSources(MapboxMap mapboxMap) {
        List<Source> sources = mapboxMap.getSources();
        for (Source sources_id : sources) {
            mapboxMap.removeSource(sources_id);
        }
    }

    /**
     * @param mercator 墨卡托转经纬度
     * @return
     */
    public Vector2D Mercator2lonLat(Vector2D mercator) {
        double x = mercator.getX() / 20037508.34 * 180;
        double y = mercator.getY() / 20037508.34 * 180;
        y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);
        Vector2D lonLat = new Vector2D(x, y);
        Log.e("Vector2D", lonLat.getX() + " " + lonLat.getY());
        return lonLat;
    }

    public static void addMarker(Context context, MapboxMap mapboxMap, ArrayList<LatLng> list, View view, ArrayList<MarkerLayoutBean.DeviceListJavaBean> integers, ArrayList<Integer> mErrPositionIndexOf, MarkerErrListBean mMarkerErrList) {
        if (!isEmpty(mapboxMap) && list != null && list.size() != 0 && view != null) {
            mapboxMap.setInfoWindowAdapter(new MapInfoWindow(view, integers, mErrPositionIndexOf, mMarkerErrList));
            for (int i = 0; i < list.size(); i++) {
                mapboxMap.addMarker(new MarkerViewOptions().position(list.get(i))
                        .alpha(0.0f).title("" + i)
                );
            }
        }


    }

    /**
     *
     * @param marker  给一标记层  用来 添加一个可以显示的点
     */
    private void selectMarker(final SymbolLayer marker) {
        ValueAnimator markerAnimator = new ValueAnimator();
        markerAnimator.setObjectValues(1f, 2f);
        markerAnimator.setDuration(300);
        markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                Log.e("marker", marker + "");
                if (marker != null) {
                    marker.setProperties(
                            PropertyFactory.iconSize((float) animator.getAnimatedValue())
                    );
                }
            }
        });
        markerAnimator.start();

    }

    /**
     *
     * @param marker
     */
    private void deselectMarker(final SymbolLayer marker) {
        ValueAnimator markerAnimator = new ValueAnimator();
        markerAnimator.setObjectValues(2f, 0f);
        markerAnimator.setDuration(300);
        markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                marker.setProperties(
                        PropertyFactory.iconSize((float) animator.getAnimatedValue())
                );
            }
        });
        markerAnimator.start();

    }

    /**
     * 此LayerID  对应的 SymbolLayer 执行动画  进行扩 大和 缩放
     * @param point  执行动画的点
     * @param mapboxMap mapbox
     * @param SELECTED_MARKER_LAYER_ID 需要加载的 SymbolLayer 圆形
     * @param SELECTED_SOURCE_ID   需要加载的 SymbolLayer  资源
     */
    public void onMarkerClickHandler(LatLng point, MapboxMap mapboxMap,String SELECTED_MARKER_LAYER_ID,String SELECTED_SOURCE_ID) {
        SymbolLayer marker = (SymbolLayer) mapboxMap.getLayer(SELECTED_MARKER_LAYER_ID);
        Position position = Position.fromCoordinates(point.getLongitude(), point.getLatitude());
        Feature feature = Feature.fromGeometry(Point.fromCoordinates(position));

        FeatureCollection featureCollection = FeatureCollection.fromFeatures(
                new Feature[]{feature});
        GeoJsonSource source = mapboxMap.getSourceAs(SELECTED_SOURCE_ID);
        if (source != null) {
            source.setGeoJson(featureCollection);
        }
        selectMarker(marker);
    }

    public static boolean isEmpty(MapboxMap mapboxMap) {
        return mapboxMap == null;
    }

    public static void addSymbolMarker(MapboxMap mapboxMap, ArrayList<double[]> list1, ArrayList<Integer> errCode) {
        JsonObject objects = null;
        ArrayList<Position> position = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            webMercator2Position(list1.get(i)[0], list1.get(i)[1], position);
        }
        List<Feature> markerCoordinates = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            markerCoordinates.add(Feature.fromGeometry(
                    Point.fromCoordinates(Position.fromCoordinates(position.get(i).getLongitude(), position.get(i).getLatitude())))
            );

        }
        for (int i = 0; i < markerCoordinates.size(); i++) {
            if (objects == null) {
                objects = new JsonObject();
            } else {
                objects.remove("key");
            }
            objects.addProperty("key", errCode.get(i));
            markerCoordinates.get(i).setProperties(objects);
        }
       /* markerCoordinates.add(Feature.fromGeometry(
                Point.fromCoordinates(Position.fromCoordinates(0, 0)))
        );*/
       /* markerCoordinates.add(Feature.fromGeometry(
                Point.fromCoordinates(Position.fromCoordinates(0.2696963730697136, -0.32294277034277086))) // The Paul Revere House
        );
        JsonObject object = new JsonObject();
        object.addProperty("key", 250);
        markerCoordinates.get(0).setProperties(object);
        JsonObject object1 = new JsonObject();
        object1.addProperty("key", 25);
        markerCoordinates.get(1).setProperties(object1);*/
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(markerCoordinates);
        Source geoJsonSource = new GeoJsonSource("marker-source", featureCollection);
        mapboxMap.addSource(geoJsonSource);

        SymbolLayer markers = new SymbolLayer("marker-layer", "marker-source")
                .withProperties(PropertyFactory.iconImage("my-marker"));
        markers.setProperties(
                textField("{key}"),
                textSize(12f),
                textColor(Color.WHITE)
        );
        mapboxMap.addLayerAbove(markers, "0");
//        mapboxMap.addLayer(markers);


    }

    /**
     * 墨卡托投影转经纬度坐标
     *
     * @return
     */
    public static Position webMercator2Position(double longitude, double latitude, ArrayList<Position> list) {
        Position latLng = fromCoordinates(longitude, latitude);
        double x = latLng.getLongitude() / 20037508.34 * 180.0;
        double y = latLng.getLatitude() / 20037508.34 * 180.0;
        y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);
        Log.d("TAG", "webMercator2Position: x " + x + " , " + " y " + y);
        Position position = fromCoordinates(x, y);
        list.add(position);
        return position;
    }

    public static Position webMercator2PositionSingle(double longitude, double latitude) {
        Position latLng = fromCoordinates(longitude, latitude);
        double x = latLng.getLongitude() / 20037508.34 * 180.0;
        double y = latLng.getLatitude() / 20037508.34 * 180.0;
        y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);
        Log.d("TAG", "webMercator2Position: x " + x + " , " + " y " + y);
        Position position = fromCoordinates(x, y);

        return position;
    }

    // 添加多边形  直接传入  x  y  集合 自动转换成 经纬度 添加到mapbox 上
    public static void addPolygon(MapboxMap mapboxMap, ArrayList<double[]> mLatLon) {
        List<LatLng> polygon = new ArrayList<>();
        ArrayList<Position> positions = new ArrayList<>();
        for (int i = 0; i < mLatLon.size(); i++) {
            double[] doubles = mLatLon.get(i);
            webMercator2Position(doubles[0], doubles[1], positions);
        }
        for (int i = 0; i < positions.size(); i++) {
            Position position = positions.get(i);
            polygon.add(new LatLng(position.getLatitude(), position.getLongitude()));
        }
 /*       polygon.add(new LatLng(-0.32294277034277086, 0.2696963730697136));
        polygon.add(new LatLng(-0.32294277034277086, 0.22478060885748524));
        polygon.add(new LatLng(-0.30138352363113347, 0.22478060885748524));
        polygon.add(new LatLng(-0.30138352363113347, 0.2696963730697136));
        polygon.add(new LatLng(-0.32294277034277086, 0.2696963730697136));*/
        mapboxMap.addPolygon(new PolygonOptions()
                .addAll(polygon)
                .fillColor(Color.parseColor("#ffffff")));
    }

    //  添加地图上的 错误的  点
    public static List<Feature> addFeature(MarkerLayoutBean mMarkerLayoutInfo, MarkerErrListBean mMarkerErrList, ArrayList<Position> positions, ArrayList<Integer> mErrPositionList, ArrayList<MarkerLayoutBean.DeviceListJavaBean> integers) {
        List<Feature> markerCoordinates = new ArrayList<>();
        for (int i = 0; i < mMarkerLayoutInfo.getDevice_list().size(); i++) {
            MarkerLayoutBean.DeviceListJavaBean deviceListJavaBean = mMarkerLayoutInfo.getDevice_list().get(i);
            for (int j = 0; j < mMarkerErrList.getDevice_list().size(); j++) {
                MarkerErrListBean.DeviceListJavaBean errXY = mMarkerErrList.getDevice_list().get(j);
                if (deviceListJavaBean.getFmn() == errXY.getFmn() && deviceListJavaBean.getArea_id() == errXY.getArea_id()) {
                    Position position = positions.get(j);
                    Log.e("fromGeometry", " x  " + position.getLatitude() + " y " + position.getLongitude());
                    markerCoordinates.add(Feature.fromGeometry(
                            Point.fromCoordinates(Position.fromCoordinates(position.getLongitude(), position.getLatitude())
                            )));

                    integers.add(deviceListJavaBean);
                    mErrPositionList.add(j);
                } else {
                    continue;
                }
            }
        }
        for (int i = 0; i < markerCoordinates.size(); i++) {
            JsonObject object = new JsonObject();
            int errcode = integers.get(i).getErrcode();
            if (errcode < 10) {
                object.addProperty("key", errcode);
                markerCoordinates.get(i).setProperties(object);
            }
        }
        return markerCoordinates;
    }

    public static void removeSymbolLayer(MapboxMap mapboxMap) {
        mapboxMap.removeLayer("marker-layer");
        mapboxMap.removeSource("marker-source");
    }

    private static class MapInfoWindow implements MapboxMap.InfoWindowAdapter {
        private View view;
        private ArrayList<MarkerLayoutBean.DeviceListJavaBean> integers;
        private TextView mMarker_ch_err;
        private TextView mArea_fmn;
        private TextView mMarker_err;
        private ArrayList<Integer> mErrPositionIndexOf;
        private List<MarkerErrListBean.DeviceListJavaBean> mDevice_list;

        public MapInfoWindow(View view, ArrayList<MarkerLayoutBean.DeviceListJavaBean> integers, ArrayList<Integer> mErrPositionIndexOf, MarkerErrListBean mMarkerErrList) {
            this.view = view;
            this.integers = integers;
            this.mErrPositionIndexOf = mErrPositionIndexOf;
            this.mDevice_list = mMarkerErrList.getDevice_list();
            this.mArea_fmn = (TextView) view.findViewById(R.id.area_fmn);
            this.mMarker_ch_err = (TextView) view.findViewById(R.id.marker_ch_err);
            this.mMarker_err = (TextView) view.findViewById(R.id.marker_err);
            Log.e("fromGeometry", "***********MapInfoWindow********");
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {
            for (int i = 0; i < integers.size(); i++) {
                String title = marker.getTitle();
                MarkerLayoutBean.DeviceListJavaBean mErrbean = integers.get(i);
                String i1 = mErrbean.getArea_id() + "--" + mErrbean.getFmn();
                String mErrCode = mErrbean.getErrcode() + "";
                String mChannel = mDevice_list.get(mErrPositionIndexOf.get(i)).getChannel();
                if (title.equals("" + i)) {
                    mArea_fmn.setText(i1);
                    mMarker_err.setText(mErrCode);
                    mMarker_ch_err.setText(mChannel);
                    Log.e("fromGeometry", "******相等******" + marker.getTitle() + "     " + i1);
                    break;
                } else if (title.equals("1")) {
                    mArea_fmn.setText(i1);
                    mMarker_ch_err.setText(mChannel);
                    mMarker_err.setText(mChannel);
                }

            }
            return this.view;
        }
    }
}
