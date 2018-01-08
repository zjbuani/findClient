package com.all.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.http.response.bean.InPointBean;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.request.Http_URl;
import com.request.RequstUtils;
import com.sdk.FXMapBoxUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

/**
 * Adding an external Web Map Service layer to the map.
 */
public class AddWMSMapActivity_Test extends WMSBaseActivity {
    public String MCAR_SOURCE1 = "Car-source1";
    public String MCAR_NUMBER_LAYER_ID = "CARNUMBER_LAYER_ID";
    private Gson json;
    private int newVal;


    @Override
    protected String primarydevice() {
        return "2";  // 摄像头为主
    }

    @Override
    protected void addCarNumber(ArrayList<PolygonOptions> mAllCarPosition, InPointBean.ResponseCarposJavaBean mCarposJavaBean) {
        PolygonOptions polygonOptions = new PolygonOptions();
        List<List<Double>> coordinates = mCarposJavaBean.getCoordinates();
        if (coordinates.size() == 5) {  // 说明是 5 个点
            for (int positon = 0; positon < coordinates.size(); positon++) {
                List<Double> doubles = coordinates.get(positon);
                Position position = FXMapBoxUtil.webMercator2PositionSingle(doubles.get(0), doubles.get(1));
                polygonOptions.add(new LatLng(position.getLatitude(), position.getLongitude()));
            }
        }
        mAllCarPosition.add(polygonOptions.fillColor(
                Color.parseColor("#ffffff")).                //设置字体 颜色
                strokeColor(Color.parseColor("#D02090")));   //设置边框 颜色
    }

    @Override
    protected void addCarPositon(List<Feature> markerCoordinates, InPointBean.ResponseCarposJavaBean mCarposJavaBean) {
        String carNum = mCarposJavaBean.getCarNum();
        JsonObject json = new JsonObject();
        json.addProperty("Num", carNum);
        List<Double> carposPoint = mCarposJavaBean.getCarposPoint();
        Position mCarCentral = FXMapBoxUtil.webMercator2PositionSingle(carposPoint.get(0), carposPoint.get(1));
        markerCoordinates.add(Feature.fromGeometry(
                Point.fromCoordinates(Position.fromCoordinates(mCarCentral.getLongitude(), mCarCentral.getLatitude())), json));

    }

    @Override
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

    @Override
    protected void reuseCarPositionAndPolygon(int newVal) {
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

        }
    }

    @Override
    protected void requestCarPositionAndPolygon(Gson json, int newVal) {
        this.json = json;
        this.newVal = newVal;
        HashMap<String, String> map = new HashMap<>();
        map.put("floorID", projectUrlInfo.get(newVal).getFloorID());
        map.put("buildingID", mBuild_id);
        Log.e("onValueChange", map.toString());
        Log.e("onValueChange", Http_URl.MAP_URL + Http_URl.POST_INPOINT);
        RequstUtils.loadPostinpoint(Http_URl.MAP_URL + Http_URl.POST_INPOINT, map, new MapCarPoint());
    }

    @Override
    protected void addInfoImage(int newVal, List<InPointBean.ResponseInpointJavaBean> inpointBean, List<Feature> mInfoPosition) {
        for (int i = 0; i < inpointBean.size(); i++) {
            InPointBean.ResponseInpointJavaBean mResponseInfoBean = inpointBean.get(i);
            List<Double> coordinates = mResponseInfoBean.getCoordinates();
            Log.e("addInfoImageaddInfo", coordinates.get(0).toString() + "   y  " + coordinates.get(1).toString());
            Position mCarCentral = FXMapBoxUtil.webMercator2PositionSingle(coordinates.get(0), coordinates.get(1));
            mInfoPosition.add(Feature.fromGeometry(
                    Point.fromCoordinates(Position.fromCoordinates(mCarCentral.getLongitude(), mCarCentral.getLatitude()))));
        }
    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {

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
                    addInfoImage(newVal, response_inpoint, mInfoPosition);   // 添加兴趣点坐标
                    addCarNumber(polygonOptionses, carposJavaBean);
                    addCarPositon(markerCoordinates, carposJavaBean);
                }
                mapboxMap.addPolygons(polygonOptionses);
                mapboxAddCarNumberLayer(markerCoordinates);
                mapboxAddInfoImage(mInfoPosition);
            } else {
                finish();
            }
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
    public void mapboxAddInfoImage(List<Feature> markerCoordinates) {
        for (int i = 0; i < markerCoordinates.size(); i++) {
            ArrayList<Feature> mInfoSymblo = new ArrayList<>();
            Feature feature = markerCoordinates.get(i);
            mInfoSymblo.add(feature);
            mInfoLayerIDList.add("INFO_LAYER_ID" + i);
            mInfoSourceIDList.add("INFO_SOURCE_ID" + i);
            FeatureCollection mInfoLayerCollection = FeatureCollection.fromFeatures(mInfoSymblo);
       /*     GeoJsonSource mInfoSource = (GeoJsonSource) mapboxMap.getSource("INFO_SOURCE_ID" + i);
            if (mInfoSource == null){
                  mInfoSource = new GeoJsonSource("INFO_SOURCE_ID" + i, mInfoLayerCollection);
                mapboxMap.addSource(mInfoSource);
            }else {
                mInfoSource.setGeoJson(mInfoLayerCollection);
            }
            SymbolLayer symbolLayer = new SymbolLayer("INFO_LAYER_ID" + i, "INFO_SOURCE_ID" + i).
                    withProperties(iconImage(""));
            mapboxMap.addLayer(symbolLayer);*/
        }


    }


}
