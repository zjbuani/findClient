package com.http.response.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengx on 2017/9/20.
 */

public class SignInBean implements Serializable{
    /**
     * result : 1
     * city_list : [{"city_id":"F0021","city_name":"上海市","build_sum":"3"},{"city_id":"F0028","city_name":"成都市","build_sum":"1"}]
     * build_list : [{"user_netlimit":"2","city_id":"F0021","city_name":"上海市","user_manager":"15","build_id":"B0001","build_type":"15","build_peanut":"http://174411gw95.51mypc.cn:36834","build_name":"中山公园龙之梦","system_verson":"1.0"},{"user_netlimit":"3","city_id":"F0021","city_name":"上海市","user_manager":"15","build_id":"B0002","build_type":"15","build_peanut":"http://1n7o321771.51mypc.cn:12804","build_name":"上海九院门诊","system_verson":"2.0"},{"user_netlimit":"1","city_id":"F0021","city_name":"上海市","user_manager":"15","build_id":"B0003","build_type":"15","build_name":"蜂寻研发中心","system_verson":"2.0"},{"user_netlimit":"3","city_id":"F0028","city_name":"成都市","user_manager":"15","build_id":"B0004","build_type":"15","build_peanut":"http://18437x97f3.iok.la:25965","build_name":"成都仁和春天","system_verson":"2.0"}]
     */
    private String result;
    private List<CityListJavaBean> city_list;
    private List<BuildListJavaBean> build_list;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<CityListJavaBean> getCity_list() {
        return city_list;
    }

    public void setCity_list(List<CityListJavaBean> city_list) {
        this.city_list = city_list;
    }

    public List<BuildListJavaBean> getBuild_list() {
        return build_list;
    }

    public void setBuild_list(List<BuildListJavaBean> build_list) {
        this.build_list = build_list;
    }

    @Override
    public String toString() {
        return "SignInBean{" +
                "result='" + result + '\'' +
                ", city_list=" + city_list +
                ", build_list=" + build_list +
                '}';
    }

    public static class CityListJavaBean implements Serializable{
        /**
         * city_id : F0021
         * city_name : 上海市
         * build_sum : 3
         */

        private String city_id;
        private String city_name;
        private String build_sum;

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getBuild_sum() {
            return build_sum;
        }

        public void setBuild_sum(String build_sum) {
            this.build_sum = build_sum;
        }

        @Override
        public String toString() {
            return "CityListJavaBean{" +
                    "city_id='" + city_id + '\'' +
                    ", city_name='" + city_name + '\'' +
                    ", build_sum='" + build_sum + '\'' +
                    '}';
        }
    }

    public static class BuildListJavaBean  implements  Serializable{
        /**
         * user_netlimit : 2
         * city_id : F0021
         * city_name : 上海市
         * user_manager : 15
         * build_id : B0001
         * build_type : 15
         * build_peanut : http://174411gw95.51mypc.cn:36834
         * build_name : 中山公园龙之梦
         * system_verson : 1.0
         */

        private String user_netlimit;
        private String city_id;
        private String city_name;
        private String user_manager;
        private String build_id;
        private String build_type;
        private String build_peanut;
        private String build_name;
        private String system_verson;

        public String getUser_netlimit() {
            return user_netlimit;
        }

        public void setUser_netlimit(String user_netlimit) {
            this.user_netlimit = user_netlimit;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getUser_manager() {
            return user_manager;
        }

        public void setUser_manager(String user_manager) {
            this.user_manager = user_manager;
        }

        public String getBuild_id() {
            return build_id;
        }

        public void setBuild_id(String build_id) {
            this.build_id = build_id;
        }

        public String getBuild_type() {
            return build_type;
        }

        public void setBuild_type(String build_type) {
            this.build_type = build_type;
        }

        public String getBuild_peanut() {
            return build_peanut;
        }

        public void setBuild_peanut(String build_peanut) {
            this.build_peanut = build_peanut;
        }

        public String getBuild_name() {
            return build_name;
        }

        public void setBuild_name(String build_name) {
            this.build_name = build_name;
        }

        public String getSystem_verson() {
            return system_verson;
        }

        public void setSystem_verson(String system_verson) {
            this.system_verson = system_verson;
        }

        @Override
        public String toString() {
            return "BuildListJavaBean{" +
                    "user_netlimit='" + user_netlimit + '\'' +
                    ", city_id='" + city_id + '\'' +
                    ", city_name='" + city_name + '\'' +
                    ", user_manager='" + user_manager + '\'' +
                    ", build_id='" + build_id + '\'' +
                    ", build_type='" + build_type + '\'' +
                    ", build_peanut='" + build_peanut + '\'' +
                    ", build_name='" + build_name + '\'' +
                    ", system_verson='" + system_verson + '\'' +
                    '}';
        }
    }
}
