package com.http.response.bean;

import java.util.List;

/**
 * Created by fengx on 2017/12/1.
 */

public class InPointBean {
    /**
     * result : 1
     * response_inpoint : [{"inpointFloorID":"LXM1","inpointID":"P001","inpointType":1,"inpointName":"楼梯","inpointfloor":"M","coordinates":[33087.500000002445,-19187.499999999767]},{"inpointFloorID":"LXM1","inpointID":"P002","inpointType":3,"inpointName":"电梯","inpointfloor":"M","coordinates":[39537.49999989095,-57274.99999993062]},{"inpointFloorID":"LXM1","inpointID":"P003","inpointType":1,"inpointName":"楼梯","inpointfloor":"M","coordinates":[38487.50398470853,-65374.99999995822]},{"inpointFloorID":"LXM1","inpointID":"P004","inpointType":1,"inpointName":"楼梯","inpointfloor":"M","coordinates":[1972.3174496525826,-76103.89116282713]},{"inpointFloorID":"LXM1","inpointID":"P005","inpointType":1,"inpointName":"楼梯","inpointfloor":"M","coordinates":[2166.779350654455,-19595.22259413387]}]
     * response_carpos : [{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"20","carUID":"LXM1B000220","coordinates":[[23747.3474814485,-30875.000021975],[18247.3474814485,-30875.000021975],[18247.3474814485,-28475.000021975],[23747.3474814485,-28475.000021975],[23747.3474814485,-30875.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"05","carUID":"LXM1B000214","coordinates":[[35224.9999814485,-42075.000021975],[29724.9999814485,-42075.000021975],[29724.9999814485,-39675.000021975],[35224.9999814485,-39675.000021975],[35224.9999814485,-42075.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"22","carUID":"LXM1B000222","coordinates":[[23747.3474814485,-26075.000021975],[18247.3474814485,-26075.000021975],[18247.3474814485,-23675.000021975],[23747.3474814485,-23675.000021975],[23747.3474814485,-26075.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"13","carUID":"LXM1B00028","coordinates":[[23747.4497814485,-50075.000021975],[18247.4497814485,-50075.000021975],[18247.4497814485,-47675.000021975],[23747.4497814485,-47675.000021975],[23747.4497814485,-50075.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"14","carUID":"LXM1B00029","coordinates":[[23747.4119814485,-46875.000021975],[18247.4119814485,-46875.000021975],[18247.4119814485,-44475.000021975],[23747.4119814485,-44475.000021975],[23747.4119814485,-46875.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"04","carUID":"LXM1B000212","coordinates":[[35224.9999814485,-44475.000021975],[29724.9999814485,-44475.000021975],[29724.9999814485,-42075.000021975],[35224.9999814485,-42075.000021975],[35224.9999814485,-44475.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"02","carUID":"LXM1B00027","coordinates":[[35224.9999814485,-52475.000021975],[29724.9999814485,-52475.000021975],[29724.9999814485,-50075.000021975],[35224.9999814485,-50075.000021975],[35224.9999814485,-52475.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"21","carUID":"LXM1B000221","coordinates":[[23747.3474814485,-28475.000021975],[18247.3474814485,-28475.000021975],[18247.3474814485,-26075.000021975],[23747.3474814485,-26075.000021975],[23747.3474814485,-28475.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"18","carUID":"LXM1B000217","coordinates":[[23747.3517814485,-36473.090221975],[18247.3517814485,-36473.090221975],[18247.3517814485,-34073.090221975],[23747.3517814485,-34073.090221975],[23747.3517814485,-36473.090221975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"12","carUID":"LXM1B00026","coordinates":[[23747.4497814485,-52475.000021975],[18247.4497814485,-52475.000021975],[18247.4497814485,-50075.000021975],[23747.4497814485,-50075.000021975],[23747.4497814485,-52475.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"01","carUID":"LXM1B00025","coordinates":[[35224.9999814485,-54875.000021975],[29724.9999814485,-54875.000021975],[29724.9999814485,-52475.000021975],[35224.9999814485,-52475.000021975],[35224.9999814485,-54875.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"23","carUID":"LXM1B000223","coordinates":[[23747.3517814485,-18472.978321975],[18247.3517814485,-18472.978321975],[18247.3517814485,-16072.978321975],[23747.3517814485,-16072.978321975],[23747.3517814485,-18472.978321975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"16","carUID":"LXM1B000213","coordinates":[[23747.4119814485,-42075.000021975],[18247.4119814485,-42075.000021975],[18247.4119814485,-39675.000021975],[23747.4119814485,-39675.000021975],[23747.4119814485,-42075.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"03","carUID":"LXM1B000210","coordinates":[[35224.9999814485,-46875.000021975],[29724.9999814485,-46875.000021975],[29724.9999814485,-44475.000021975],[35224.9999814485,-44475.000021975],[35224.9999814485,-46875.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"15","carUID":"LXM1B000211","coordinates":[[23747.4119814485,-44475.000021975],[18247.4119814485,-44475.000021975],[18247.4119814485,-42075.000021975],[23747.4119814485,-42075.000021975],[23747.4119814485,-44475.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"10","carUID":"LXM1B00023","coordinates":[[23747.4594814485,-58075.000021975],[18247.4594814485,-58075.000021975],[18247.4594814485,-55675.000021975],[23747.4594814485,-55675.000021975],[23747.4594814485,-58075.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"11","carUID":"LXM1B00024","coordinates":[[23747.4497814485,-54875.000021975],[18247.4497814485,-54875.000021975],[18247.4497814485,-52475.000021975],[23747.4497814485,-52475.000021975],[23747.4497814485,-54875.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"17","carUID":"LXM1B000216","coordinates":[[23747.3517814485,-38873.090221975],[18247.3517814485,-38873.090221975],[18247.3517814485,-36473.090221975],[23747.3517814485,-36473.090221975],[23747.3517814485,-38873.090221975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"08","carUID":"LXM1B00021","coordinates":[[23747.4594814485,-62875.000021975],[18247.4594814485,-62875.000021975],[18247.4594814485,-60475.000021975],[23747.4594814485,-60475.000021975],[23747.4594814485,-62875.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"19","carUID":"LXM1B000219","coordinates":[[23747.3517814485,-34073.090221975],[18247.3517814485,-34073.090221975],[18247.3517814485,-31673.090221975],[23747.3517814485,-31673.090221975],[23747.3517814485,-34073.090221975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"07","carUID":"LXM1B000218","coordinates":[[35224.9999814485,-34075.000021975],[29724.9999814485,-34075.000021975],[29724.9999814485,-31675.000021975],[35224.9999814485,-31675.000021975],[35224.9999814485,-34075.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"06","carUID":"LXM1B000215","coordinates":[[35223.3749814485,-38875.000021975],[29723.3749814485,-38875.000021975],[29723.3749814485,-36475.000021975],[35223.3749814485,-36475.000021975],[35223.3749814485,-38875.000021975]]},{"carFloor":"M","carFloorID":"LXM1","carArea":1,"carNum":"09","carUID":"LXM1B00022","coordinates":[[23747.4594814485,-60475.000021975],[18247.4594814485,-60475.000021975],[18247.4594814485,-58075.000021975],[23747.4594814485,-58075.000021975],[23747.4594814485,-60475.000021975]]}]
     */

    private String result;
    private List<ResponseInpointJavaBean> response_inpoint;
    private List<ResponseCarposJavaBean> response_carpos;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<ResponseInpointJavaBean> getResponse_inpoint() {
        return response_inpoint;
    }

    public void setResponse_inpoint(List<ResponseInpointJavaBean> response_inpoint) {
        this.response_inpoint = response_inpoint;
    }

    public List<ResponseCarposJavaBean> getResponse_carpos() {
        return response_carpos;
    }

    public void setResponse_carpos(List<ResponseCarposJavaBean> response_carpos) {
        this.response_carpos = response_carpos;
    }

    public static class ResponseInpointJavaBean {
        /**
         * inpointFloorID : LXM1
         * inpointID : P001
         * inpointType : 1
         * inpointName : 楼梯
         * inpointfloor : M
         * coordinates : [33087.500000002445,-19187.499999999767]
         */

        private String inpointFloorID;
        private String inpointID;
        private int inpointType;
        private String inpointName;
        private String inpointfloor;
        private List<Double> coordinates;

        public String getInpointFloorID() {
            return inpointFloorID;
        }

        public void setInpointFloorID(String inpointFloorID) {
            this.inpointFloorID = inpointFloorID;
        }

        public String getInpointID() {
            return inpointID;
        }

        public void setInpointID(String inpointID) {
            this.inpointID = inpointID;
        }

        public int getInpointType() {
            return inpointType;
        }

        public void setInpointType(int inpointType) {
            this.inpointType = inpointType;
        }

        public String getInpointName() {
            return inpointName;
        }

        public void setInpointName(String inpointName) {
            this.inpointName = inpointName;
        }

        public String getInpointfloor() {
            return inpointfloor;
        }

        public void setInpointfloor(String inpointfloor) {
            this.inpointfloor = inpointfloor;
        }

        public List<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
        }

        @Override
        public String toString() {
            return "ResponseInpointJavaBean{" +
                    "inpointFloorID='" + inpointFloorID + '\'' +
                    ", inpointID='" + inpointID + '\'' +
                    ", inpointType=" + inpointType +
                    ", inpointName='" + inpointName + '\'' +
                    ", inpointfloor='" + inpointfloor + '\'' +
                    ", coordinates=" + coordinates +
                    '}';
        }
    }

    public static class ResponseCarposJavaBean {
        /**
         * carFloor : M
         * carFloorID : LXM1
         * carArea : 1
         * carNum : 20
         * carUID : LXM1B000220
         * coordinates : [[23747.3474814485,-30875.000021975],[18247.3474814485,-30875.000021975],[18247.3474814485,-28475.000021975],[23747.3474814485,-28475.000021975],[23747.3474814485,-30875.000021975]]
         */

        private String carFloor;
        private String carFloorID;
        private int carArea;
        private String carNum;
        private String carUID;
        private List<List<Double>> coordinates;
        private List<Double> carposPoint;

        public List<Double> getCarposPoint() {
            return carposPoint;
        }

        public void setCarposPoint(List<Double> carposPoint) {
            this.carposPoint = carposPoint;
        }

        public String getCarFloor() {
            return carFloor;
        }

        public void setCarFloor(String carFloor) {
            this.carFloor = carFloor;
        }

        public String getCarFloorID() {
            return carFloorID;
        }

        public void setCarFloorID(String carFloorID) {
            this.carFloorID = carFloorID;
        }

        public int getCarArea() {
            return carArea;
        }

        public void setCarArea(int carArea) {
            this.carArea = carArea;
        }

        public String getCarNum() {
            return carNum;
        }

        public void setCarNum(String carNum) {
            this.carNum = carNum;
        }

        public String getCarUID() {
            return carUID;
        }

        public void setCarUID(String carUID) {
            this.carUID = carUID;
        }

        public List<List<Double>> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<List<Double>> coordinates) {
            this.coordinates = coordinates;
        }

        @Override
        public String toString() {
            return "ResponseCarposJavaBean{" +
                    "carFloor='" + carFloor + '\'' +
                    ", carFloorID='" + carFloorID + '\'' +
                    ", carArea=" + carArea +
                    ", carNum='" + carNum + '\'' +
                    ", carUID='" + carUID + '\'' +
                    ", coordinates=" + coordinates +
                    ", carposPoint=" + carposPoint +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "InPointBean{" +
                "result='" + result + '\'' +
                ", response_inpoint=" + response_inpoint +
                ", response_carpos=" + response_carpos +
                '}';
    }
}
