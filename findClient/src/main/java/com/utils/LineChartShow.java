package com.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.Log;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ObjectPool;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.http.response.bean.LightSystemLineCharBean;
import com.view.MyMarkerView;
import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by fengx on 2017/9/29.
 */

public class LineChartShow {


    public static void showChart(Context context, LineChart mChart, LightSystemLineCharBean mLightSystemLineCharBean, int radio_child_id) {


        mChart = mChart;
        mChart.setDrawGridBackground(true);
//        mChart.setDrawBorders(true); //是否给添加边框
        // no description text
        mChart.getDescription().setEnabled(false);
        // enable touch gestures
        mChart.setTouchEnabled(false);
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);  //设置 x y 不可缩放
        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);
        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
//        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(15f);
        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
       /* leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);*/
//      /*  */
        leftAxis.setAxisMaximum(23);
//        leftAxis.setYOffset(50f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        LightSystemLineCharBean.ParJavaBean par = mLightSystemLineCharBean.getPar();
        // add data
        setData(mChart, par, radio_child_id);

     /*   mChart.setVisibleXRange(20,20);
        mChart.setVisibleYRange(20f, 50,YAxis.AxisDependency.LEFT);
        mChart.centerViewTo(20, 50, YAxis.AxisDependency.LEFT);*/

        mChart.animateX(2500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
//        l.setForm(LegendForm.LINE);
        l.setForm(Legend.LegendForm.CIRCLE);

    }

    private static void setData(LineChart mChart, LightSystemLineCharBean.ParJavaBean par, int radio_child) {

        ArrayList<Entry> values = new ArrayList<Entry>();
        if (par.getLight_chart() == null || par.getLight_chart().size() == 0 ||par.getTemp_chart()==null ||par.getTemp_chart().size() == 0 ||par.getWat_chart()==null ||par.getWat_chart().size()==0) {
            if (radio_child == 0) {
                for (int i = 0; i < 10 ;i ++) {
                    Entry entry = new Entry(i, 0);
                    values.add(entry);
                }
            }
        }else {
//        responseSort(par, radio_child, values);  //对返回的javaBean的 X 轴(time) 进行排序
            responseSortTest(par, radio_child, values);  //对返回的javaBean的 X 轴(time) 进行排序
        }
        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            set1.setValues(value);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
//                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }
    }


    private static void responseSortTest(LightSystemLineCharBean.ParJavaBean par, final int radio_child, ArrayList<Entry> values) {

        ArrayList<Object> sortList = new ArrayList<>();
        if (radio_child == 0) {
            if (par.getWat_chart() == null || par.getWat_chart().size() == 0) {
                return;
            }
            List<LightSystemLineCharBean.ParJavaBean.WatChartJavaBean> wat_chart = par.getWat_chart();
            sortList.addAll(wat_chart);
        } else if (radio_child == 1) {
            if (par.getLight_chart() == null || par.getLight_chart().size() == 0) {
                return;
            }
            List<LightSystemLineCharBean.ParJavaBean.LightChartJavaBean> light_chart = par.getLight_chart();
            sortList.addAll(light_chart);
        } else if (radio_child == 2) {
            if (par.getTemp_chart() == null || par.getTemp_chart().size() == 0) {
                return;
            }
            List<LightSystemLineCharBean.ParJavaBean.TempChartJavaBean> temp_chart = par.getTemp_chart();
            sortList.addAll(temp_chart);
        }
      /*  Collections.sort(sortList, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                int x1 = 0;
                int x2 = 0;
                if (radio_child == 0) {
                    LightSystemLineCharBean.ParJavaBean.WatChartJavaBean mWcjb1 = (LightSystemLineCharBean.ParJavaBean.WatChartJavaBean) o1;
                    LightSystemLineCharBean.ParJavaBean.WatChartJavaBean mWcjb2 = (LightSystemLineCharBean.ParJavaBean.WatChartJavaBean) o2;
                    x1 = Integer.parseInt(mWcjb1.getTime());
                    x2 = Integer.parseInt(mWcjb2.getTime());
                } else if (radio_child == 1) {
                    LightSystemLineCharBean.ParJavaBean.LightChartJavaBean mjb1 = (LightSystemLineCharBean.ParJavaBean.LightChartJavaBean) o1;
                    LightSystemLineCharBean.ParJavaBean.LightChartJavaBean mjb2 = (LightSystemLineCharBean.ParJavaBean.LightChartJavaBean) o2;
                    x1 = Integer.parseInt(mjb1.getTime());
                    x2 = Integer.parseInt(mjb2.getTime());
                } else if (radio_child == 2) {
                    LightSystemLineCharBean.ParJavaBean.TempChartJavaBean mTcjb1 = (LightSystemLineCharBean.ParJavaBean.TempChartJavaBean) o1;
                    LightSystemLineCharBean.ParJavaBean.TempChartJavaBean mTcjb2 = (LightSystemLineCharBean.ParJavaBean.TempChartJavaBean) o1;
                    x1 = Integer.parseInt(mTcjb1.getTime());
                    x2 = Integer.parseInt(mTcjb2.getTime());
                }
                return x1 - x2;

            }
        });*/
        for (int i = 0; i < sortList.size(); i++) {
            Object o1 = sortList.get(i);
            if (radio_child == 0) {
                LightSystemLineCharBean.ParJavaBean.WatChartJavaBean mjb1 = (LightSystemLineCharBean.ParJavaBean.WatChartJavaBean) o1;
                int time = Integer.parseInt(mjb1.getTime());
                int value = mjb1.getValue();
                Log.e("wat_chart_Entry", "x: " + time + "  y : " + value);
//                values.add(new Entry(time, value));
                values.add(new Entry(i, time));
            } else if (radio_child == 1) {
                LightSystemLineCharBean.ParJavaBean.LightChartJavaBean mjb1 = (LightSystemLineCharBean.ParJavaBean.LightChartJavaBean) o1;
                int time = Integer.parseInt(mjb1.getTime());
                int value = mjb1.getValue();
                Log.e("wat_chart_Entry", "x: " + time + "  y : " + value);
//                values.add(new Entry(time, value));
                values.add(new Entry(i, time));
            } else if (radio_child == 2) {
                LightSystemLineCharBean.ParJavaBean.TempChartJavaBean mTcjb1 = (LightSystemLineCharBean.ParJavaBean.TempChartJavaBean) o1;
                int time = Integer.parseInt(mTcjb1.getTime());
                int value = mTcjb1.getValue();
                Log.e("wat_chart_Entry", "x: " + time + "  y : " + value);
//                values.add(new Entry(time, value));
                values.add(new Entry(i, time));
            }
        }
    }


}
