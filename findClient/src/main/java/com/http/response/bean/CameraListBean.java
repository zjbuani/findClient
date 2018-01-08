package com.http.response.bean;

import java.util.List;

/**
 * Created by fengx on 2017/12/18.
 */

public class CameraListBean {

    /**
     * build_id : B0002
     * floor : -1
     * pag : 0
     * camera_list : [{"camera_id":11,"mac":"8005dfcf0060","camera_temp":41,"dev_type":"2","rg_color":"ff0000","camera_errcode":0,"camera_area":1,"ert":10,"lmd":1},{"camera_id":51,"mac":"8005dfcf0084","camera_temp":42,"dev_type":"2","rg_color":"ff0000","camera_errcode":0,"camera_area":1,"ert":1,"lmd":1},{"camera_id":52,"mac":"8005dfcf0024","camera_temp":42,"dev_type":"2","rg_color":"ff0000","camera_errcode":0,"camera_area":1,"ert":1,"lmd":1},{"camera_id":111,"mac":"8005dfcf0074","camera_temp":44,"dev_type":"2","rg_color":"ff0000","camera_errcode":0,"camera_area":1,"ert":6,"lmd":1},{"camera_id":112,"mac":"8005dfcf002e","camera_temp":47,"dev_type":"2","rg_color":"ff0000","camera_errcode":0,"camera_area":1,"ert":6,"lmd":1},{"camera_id":171,"mac":"8005dfcf0026","camera_temp":41,"dev_type":"2","rg_color":"ff0000","camera_errcode":0,"camera_area":1,"ert":4,"lmd":1},{"camera_id":172,"mac":"8005dfcf0022","camera_temp":43,"dev_type":"2","rg_color":"ff0000","camera_errcode":0,"camera_area":1,"ert":4,"lmd":1},{"camera_id":231,"mac":"8005dfcf0048","camera_temp":41,"dev_type":"2","rg_color":"ff0000","camera_errcode":0,"camera_area":1,"ert":5,"lmd":1},{"camera_id":271,"mac":"8005dfcf006e","camera_temp":44,"dev_type":"2","rg_color":"ff0000","camera_errcode":0,"camera_area":1,"ert":5,"lmd":1}]
     * result : 1
     */

    private String build_id;
    private String floor;
    private String pag;
    private int result;
    private List<CameraListJavaBean> camera_list;

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getPag() {
        return pag;
    }

    public void setPag(String pag) {
        this.pag = pag;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<CameraListJavaBean> getCamera_list() {
        return camera_list;
    }

    public void setCamera_list(List<CameraListJavaBean> camera_list) {
        this.camera_list = camera_list;
    }

    public static class CameraListJavaBean {
        /**
         * camera_id : 11
         * mac : 8005dfcf0060
         * camera_temp : 41
         * dev_type : 2
         * rg_color : ff0000
         * camera_errcode : 0
         * camera_area : 1
         * ert : 10
         * lmd : 1
         */

        private int camera_id;
        private String mac;
        private int camera_temp;
        private String channel;
        private String dev_type;
        private String rg_color;
        private int camera_errcode;
        private int camera_area;
        private int ert;
        private int lmd;

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public int getCamera_id() {
            return camera_id;
        }

        public void setCamera_id(int camera_id) {
            this.camera_id = camera_id;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public int getCamera_temp() {
            return camera_temp;
        }

        public void setCamera_temp(int camera_temp) {
            this.camera_temp = camera_temp;
        }

        public String getDev_type() {
            return dev_type;
        }

        public void setDev_type(String dev_type) {
            this.dev_type = dev_type;
        }

        public String getRg_color() {
            return rg_color;
        }

        public void setRg_color(String rg_color) {
            this.rg_color = rg_color;
        }

        public int getCamera_errcode() {
            return camera_errcode;
        }

        public void setCamera_errcode(int camera_errcode) {
            this.camera_errcode = camera_errcode;
        }

        public int getCamera_area() {
            return camera_area;
        }

        public void setCamera_area(int camera_area) {
            this.camera_area = camera_area;
        }

        public int getErt() {
            return ert;
        }

        public void setErt(int ert) {
            this.ert = ert;
        }

        public int getLmd() {
            return lmd;
        }

        public void setLmd(int lmd) {
            this.lmd = lmd;
        }

        @Override
        public String toString() {
            return "CameraListJavaBean{" +
                    "camera_id=" + camera_id +
                    ", mac='" + mac + '\'' +
                    ", camera_temp=" + camera_temp +
                    ", channel='" + channel + '\'' +
                    ", dev_type='" + dev_type + '\'' +
                    ", rg_color='" + rg_color + '\'' +
                    ", camera_errcode=" + camera_errcode +
                    ", camera_area=" + camera_area +
                    ", ert=" + ert +
                    ", lmd=" + lmd +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CameraListBean{" +
                "build_id='" + build_id + '\'' +
                ", floor='" + floor + '\'' +
                ", pag='" + pag + '\'' +
                ", result=" + result +
                ", camera_list=" + camera_list +
                '}';
    }
}
