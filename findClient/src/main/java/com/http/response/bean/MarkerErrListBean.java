package com.http.response.bean;

import java.util.List;

/**
 * Created by fengx on 2017/11/27.
 */

public class MarkerErrListBean {

    /**
     * result : 1
     * floor_id : LS06
     * device_list : [{"fmn":151,"area_id":1,"device_x":4983.49,"device_y":-4449.27,"channel":"19","dev_type":"2"},{"fmn":191,"area_id":1,"device_x":5716.51,"device_y":-4449.27,"channel":"1","dev_type":"1"},{"fmn":192,"area_id":1,"device_x":5340,"device_y":-2650.1,"channel":"1","dev_type":"1"},{"fmn":31,"area_id":2,"device_x":4389.93,"device_y":-8297.53,"channel":"21","dev_type":"2"},{"fmn":32,"area_id":2,"device_x":4380,"device_y":-13162.8,"channel":"21","dev_type":"2"},{"fmn":51,"area_id":2,"device_x":4380,"device_y":-11095.6,"channel":"5","dev_type":"1"},{"fmn":52,"area_id":2,"device_x":6562.8,"device_y":-11540,"channel":"5","dev_type":"1"},{"fmn":53,"area_id":2,"device_x":6187.8,"device_y":-13940.1,"channel":"5","dev_type":"1"},{"fmn":54,"area_id":2,"device_x":4380,"device_y":-13962.8,"channel":"5","dev_type":"1"},{"fmn":111,"area_id":2,"device_x":4380,"device_y":-6527.71,"channel":"3","dev_type":"1"},{"fmn":112,"area_id":2,"device_x":6562.8,"device_y":-6740,"channel":"3","dev_type":"1"},{"fmn":113,"area_id":2,"device_x":6562.8,"device_y":-9140,"channel":"3","dev_type":"1"},{"fmn":114,"area_id":2,"device_x":4380,"device_y":-9178.4,"channel":"3","dev_type":"1"},{"fmn":11,"area_id":3,"device_x":1050,"device_y":-13920,"channel":"7","dev_type":"1"},{"fmn":12,"area_id":3,"device_x":1050,"device_y":-15120.1,"channel":"7","dev_type":"1"},{"fmn":71,"area_id":4,"device_x":1050,"device_y":-11030,"channel":"9","dev_type":"1"},{"fmn":72,"area_id":4,"device_x":1050,"device_y":-12230.1,"channel":"9","dev_type":"1"},{"fmn":51,"area_id":5,"device_x":356.567,"device_y":-8596.72,"channel":"17","dev_type":"2"},{"fmn":91,"area_id":5,"device_x":1050,"device_y":-8640,"channel":"11","dev_type":"1"},{"fmn":92,"area_id":5,"device_x":1050,"device_y":-7440.1,"channel":"11","dev_type":"1"},{"fmn":131,"area_id":6,"device_x":930,"device_y":-5493.1,"channel":"15","dev_type":"2"},{"fmn":152,"area_id":6,"device_x":4380,"device_y":-5761.2,"channel":"19","dev_type":"2"},{"fmn":171,"area_id":6,"device_x":930,"device_y":-4726.59,"channel":"13","dev_type":"1"},{"fmn":172,"area_id":6,"device_x":2587.8,"device_y":-4460.1,"channel":"13","dev_type":"1"},{"fmn":173,"area_id":6,"device_x":2962.8,"device_y":-2060,"channel":"13","dev_type":"1"}]
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

    public static class DeviceListJavaBean {
        /**
         * fmn : 151
         * area_id : 1
         * device_x : 4983.49
         * device_y : -4449.27
         * channel : 19
         * dev_type : 2
         */

        private int fmn;
        private int area_id;
        private double device_x;
        private double device_y;
        private String channel;
        private String dev_type;

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

        public double getDevice_x() {
            return device_x;
        }

        public void setDevice_x(double device_x) {
            this.device_x = device_x;
        }

        public double getDevice_y() {
            return device_y;
        }

        public void setDevice_y(double device_y) {
            this.device_y = device_y;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getDev_type() {
            return dev_type;
        }

        public void setDev_type(String dev_type) {
            this.dev_type = dev_type;
        }

        @Override
        public String toString() {
            return "DeviceListJavaBean{" +
                    "fmn=" + fmn +
                    ", area_id=" + area_id +
                    ", device_x=" + device_x +
                    ", device_y=" + device_y +
                    ", channel='" + channel + '\'' +
                    ", dev_type='" + dev_type + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MarkerErrListBean{" +
                "result=" + result +
                ", floor_id='" + floor_id + '\'' +
                ", device_list=" + device_list +
                '}';
    }
}
