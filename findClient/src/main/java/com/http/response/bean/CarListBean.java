package com.http.response.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengx on 2017/11/2.
 */

public class CarListBean implements Serializable {

    /**
     * result : 1
     * build_id : B0001
     * che : 1
     * pag : 1
     * car_list : [{"carpos":"10","lran":"汽车黄","car_floor":"B2","carnum":"沪A99999","imgUrl":"https://host/xx/xx.jpeg","tm":"14","ful":"0","rg_mode":"普通"}]
     */

    private int result;
    private String build_id;
    private String che;
    private String pag;
    private List<CarListJavaBean> car_list;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }

    public String getChe() {
        return che;
    }

    public void setChe(String che) {
        this.che = che;
    }

    public String getPag() {
        return pag;
    }

    public void setPag(String pag) {
        this.pag = pag;
    }

    public List<CarListJavaBean> getCar_list() {
        return car_list;
    }

    public void setCar_list(List<CarListJavaBean> car_list) {
        this.car_list = car_list;
    }

    @Override
    public String toString() {
        return "CarListBean{" +
                "result=" + result +
                ", build_id='" + build_id + '\'' +
                ", che='" + che + '\'' +
                ", pag='" + pag + '\'' +
                ", car_list=" + car_list +
                '}';
    }

    public static class CarListJavaBean  implements  Serializable{
        /**
         * carpos : 10
         * lran : 汽车黄
         * car_floor : B2
         * carnum : 沪A99999
         * imgUrl : https://host/xx/xx.jpeg
         * tm : 14
         * ful : 0
         * rg_mode : 普通
         */

        private String carpos;
        private String lran;
        private String car_floor;
        private String carnum;
        private String imgUrl;
        private String tm;
        private String ful;
        private String rg_mode;

        public String getCarpos() {
            return carpos;
        }

        public void setCarpos(String carpos) {
            this.carpos = carpos;
        }

        public String getLran() {
            return lran;
        }

        public void setLran(String lran) {
            this.lran = lran;
        }

        public String getCar_floor() {
            return car_floor;
        }

        public void setCar_floor(String car_floor) {
            this.car_floor = car_floor;
        }

        public String getCarnum() {
            return carnum;
        }

        public void setCarnum(String carnum) {
            this.carnum = carnum;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getTm() {
            return tm;
        }

        public void setTm(String tm) {
            this.tm = tm;
        }

        public String getFul() {
            return ful;
        }

        public void setFul(String ful) {
            this.ful = ful;
        }

        public String getRg_mode() {
            return rg_mode;
        }

        public void setRg_mode(String rg_mode) {
            this.rg_mode = rg_mode;
        }

        @Override
        public String toString() {
            return "CarListJavaBean{" +
                    "carpos='" + carpos + '\'' +
                    ", lran='" + lran + '\'' +
                    ", car_floor='" + car_floor + '\'' +
                    ", carnum='" + carnum + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", tm='" + tm + '\'' +
                    ", ful='" + ful + '\'' +
                    ", rg_mode='" + rg_mode + '\'' +
                    '}';
        }
    }

}
