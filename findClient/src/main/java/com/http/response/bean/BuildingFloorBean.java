package com.http.response.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by buildingFloor javaBean
 */

public class BuildingFloorBean implements Serializable{
    /**
     * result : 1
     * floor_list : [{"floor_num":6,"floor_name":"F6","floor_id":"LS06"}]
     * area_list : [{"area_name":"前台","area_num":1},{"area_name":"办公","area_num":2},{"area_name":"办公室1","area_num":3},{"area_name":"办公室2","area_num":4},{"area_name":"会议室","area_num":5},{"area_name":"实验室","area_num":6}]
     */
    private int result;
    private List<FloorListJavaBean> floor_list;
    private List<AreaListJavaBean> area_list;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<FloorListJavaBean> getFloor_list() {
        return floor_list;
    }

    public void setFloor_list(List<FloorListJavaBean> floor_list) {
        this.floor_list = floor_list;
    }

    public List<AreaListJavaBean> getArea_list() {
        return area_list;
    }

    public void setArea_list(List<AreaListJavaBean> area_list) {
        this.area_list = area_list;
    }

    @Override
    public String toString() {
        return "BuildingFloorBean{" +
                "result=" + result +
                ", floor_list=" + floor_list +
                ", area_list=" + area_list +
                '}';
    }

    public static class FloorListJavaBean implements Serializable{
        /**
         * floor_num : 6
         * floor_name : F6
         * floor_id : LS06
         */

        private int floor_num;
        private String floor_name;
        private String floor_id;

        public int getFloor_num() {
            return floor_num;
        }

        public void setFloor_num(int floor_num) {
            this.floor_num = floor_num;
        }

        public String getFloor_name() {
            return floor_name;
        }

        public void setFloor_name(String floor_name) {
            this.floor_name = floor_name;
        }

        public String getFloor_id() {
            return floor_id;
        }

        public void setFloor_id(String floor_id) {
            this.floor_id = floor_id;
        }

        @Override
        public String toString() {
            return "FloorListJavaBean{" +
                    "floor_num=" + floor_num +
                    ", floor_name='" + floor_name + '\'' +
                    ", floor_id='" + floor_id + '\'' +
                    '}';
        }
    }

    public static class AreaListJavaBean implements Serializable{
        /**
         * area_name : 前台
         * area_num : 1
         */

        private String area_name;
        private int area_num;

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public int getArea_num() {
            return area_num;
        }

        public void setArea_num(int area_num) {
            this.area_num = area_num;
        }

        @Override
        public String toString() {
            return "AreaListJavaBean{" +
                    "area_name='" + area_name + '\'' +
                    ", area_num=" + area_num +
                    '}';
        }
    }
}
