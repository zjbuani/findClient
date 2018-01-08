package com.http.response.bean;

import java.io.Serializable;

/**
 * Created by fengx on 2017/10/26.
 */

public class SetModeBean implements Serializable {

    /**
     * ran : 前台
     * success : 1
     * result : 1
     * build_id : B0003
     */

    private String ran;
    private String che;
    private String floor;
    private int success;
    private int result;
    private String build_id;

    public String getChe() {
        return che;
    }

    public String getFloor() {
        return floor;
    }

    public void setChe(String che) {
        this.che = che;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }


    public String getRan() {
        return ran;
    }

    public void setRan(String ran) {
        this.ran = ran;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

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

    @Override
    public String toString() {
        return "SetModeBean{" +
                "ran='" + ran + '\'' +
                ", che='" + che + '\'' +
                ", floor='" + floor + '\'' +
                ", success=" + success +
                ", result=" + result +
                ", build_id='" + build_id + '\'' +
                '}';
    }
}
