package com.http.response.bean;

import java.util.List;

/**
 * Created by fengx on 2017/12/18.
 */

public class RefreshCarStateBean {

    /**
     * response : [{"carNum":"20","state":"1"},{"carNum":"16","state":"0"},{"carNum":"11","state":"0"},{"carNum":"17","state":"1"},{"carNum":"22","state":"1"},{"carNum":"14","state":"0"},{"carNum":"09","state":"1"},{"carNum":"18","state":"0"},{"carNum":"05","state":"1"},{"carNum":"10","state":"1"},{"carNum":"04","state":"1"},{"carNum":"08","state":"1"},{"carNum":"13","state":"1"},{"carNum":"07","state":"1"},{"carNum":"01","state":"1"},{"carNum":"21","state":"0"},{"carNum":"03","state":"1"},{"carNum":"06","state":"1"},{"carNum":"02","state":"0"},{"carNum":"23","state":"1"},{"carNum":"12","state":"0"},{"carNum":"15","state":"0"},{"carNum":"19","state":"0"}]
     * result : 1
     * tm : 1513595912371
     * floorID : LXM1
     */

    private String result;
    private String tm;
    private String floorID;
    private List<ResponseJavaBean> response;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public String getFloorID() {
        return floorID;
    }

    public void setFloorID(String floorID) {
        this.floorID = floorID;
    }

    public List<ResponseJavaBean> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseJavaBean> response) {
        this.response = response;
    }

    public static class ResponseJavaBean {
        /**
         * carNum : 20
         * state : 1
         */

        private String carNum;
        private String state;

        public String getCarNum() {
            return carNum;
        }

        public void setCarNum(String carNum) {
            this.carNum = carNum;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return "ResponseJavaBean{" +
                    "carNum='" + carNum + '\'' +
                    ", state='" + state + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RefreshCarStateBean{" +
                "result='" + result + '\'' +
                ", tm='" + tm + '\'' +
                ", floorID='" + floorID + '\'' +
                ", response=" + response +
                '}';
    }
}
