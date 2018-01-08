package com.http.response.bean;

import java.util.List;

/**
 * Created by fengx on 2017/12/1.
 */

public class RouteLine {

    /**
     * type : MultiLineString
     * coordinates : [[[38912.5000279566,-27275.0000356966],[26987.5000279566,-27275.0000356966]]]
     */

    private String type;
    private List<List<List<Double>>> coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<List<List<Double>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "RouteLine{" +
                "type='" + type + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
