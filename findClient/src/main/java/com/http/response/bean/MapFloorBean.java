package com.http.response.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengx on 2017/11/24.
 */

public class MapFloorBean  implements  Serializable{

    /**
     * response : [{"floorName":"F6","floorID":"LS06","floorUrl":"F0021B0003LS06_MAP","floorOutline":"http://139.196.5.153:8181/geoserver/F0021B0003LS06/wms"}]
     * result : 1
     */

    private String result;
    private List<ResponseJavaBean> response;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<ResponseJavaBean> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseJavaBean> response) {
        this.response = response;
    }

    public static class ResponseJavaBean implements Serializable{
        /**
         * floorName : F6
         * floorID : LS06
         * floorUrl : F0021B0003LS06_MAP
         * floorOutline : http://139.196.5.153:8181/geoserver/F0021B0003LS06/wms
         */

        private String floorName;
        private String floorID;
        private String floorUrl;
        private String floorOutline;

        public String getFloorName() {
            return floorName;
        }

        public void setFloorName(String floorName) {
            this.floorName = floorName;
        }

        public String getFloorID() {
            return floorID;
        }

        public void setFloorID(String floorID) {
            this.floorID = floorID;
        }

        public String getFloorUrl() {
            return floorUrl;
        }

        public void setFloorUrl(String floorUrl) {
            this.floorUrl = floorUrl;
        }

        public String getFloorOutline() {
            return floorOutline;
        }

        public void setFloorOutline(String floorOutline) {
            this.floorOutline = floorOutline;
        }

        @Override
        public String toString() {
            return "ResponseJavaBean{" +
                    "floorName='" + floorName + '\'' +
                    ", floorID='" + floorID + '\'' +
                    ", floorUrl='" + floorUrl + '\'' +
                    ", floorOutline='" + floorOutline + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MapFloorBean{" +
                "result='" + result + '\'' +
                ", response=" + response +
                '}';
    }
}
