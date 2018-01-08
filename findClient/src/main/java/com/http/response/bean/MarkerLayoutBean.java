package com.http.response.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengx on 2017/11/27.
 */

public class MarkerLayoutBean implements Serializable{

    /**
     * result : 1
     * floor_id : LS06
     * device_list : [{"fmn":151,"area_id":1,"errcode":2,"dev_type":"2","err_tm":1374112},{"fmn":31,"area_id":2,"errcode":2,"dev_type":"2","err_tm":427315},{"fmn":32,"area_id":2,"errcode":2,"dev_type":"2","err_tm":594512},{"fmn":51,"area_id":5,"errcode":2,"dev_type":"2","err_tm":350532},{"fmn":131,"area_id":6,"errcode":2,"dev_type":"2","err_tm":533962},{"fmn":152,"area_id":6,"errcode":2,"dev_type":"2","err_tm":1374641}]
     */

    private int result;
    private String floor_id;
    private List<DeviceListJavaBean> device_list;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public List<DeviceListJavaBean> getDevice_list() {
        return device_list;
    }

    public void setDevice_list(List<DeviceListJavaBean> device_list) {
        this.device_list = device_list;
    }

    public static class DeviceListJavaBean  implements  Serializable{
        /**
         * fmn : 151
         * area_id : 1
         * errcode : 2
         * dev_type : 2
         * err_tm : 1374112
         */

        private int fmn;
        private int area_id;
        private int errcode;
        private String dev_type;
        private int err_tm;

        public int getFmn() {
            return fmn;
        }

        public void setFmn(int fmn) {
            this.fmn = fmn;
        }

        public int getArea_id() {
            return area_id;
        }

        public void setArea_id(int area_id) {
            this.area_id = area_id;
        }

        public int getErrcode() {
            return errcode;
        }

        public void setErrcode(int errcode) {
            this.errcode = errcode;
        }

        public String getDev_type() {
            return dev_type;
        }

        public void setDev_type(String dev_type) {
            this.dev_type = dev_type;
        }

        public int getErr_tm() {
            return err_tm;
        }

        public void setErr_tm(int err_tm) {
            this.err_tm = err_tm;
        }

        @Override
        public String toString() {
            return "DeviceListJavaBean{" +
                    "fmn=" + fmn +
                    ", area_id=" + area_id +
                    ", errcode=" + errcode +
                    ", dev_type='" + dev_type + '\'' +
                    ", err_tm=" + err_tm +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MarkerLayoutBean{" +
                "result=" + result +
                ", floor_id='" + floor_id + '\'' +
                ", device_list=" + device_list +
                '}';
    }
}
