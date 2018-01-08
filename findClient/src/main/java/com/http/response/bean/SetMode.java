package com.http.response.bean;

import java.io.Serializable;

/**
 * Created by fengx on 2017/10/26.
 */

public class SetMode implements Serializable {
    public String lmd;
    public String lva;
    public String eng;
/*    ntm	否	设置灯的节能时段 开始 时 分；结束 时 分
    mnl	否	设置车道照度1的档位 （0-7档）
    mxl	否	设置车位照度1的档位 （0-7档）
    mnl2	否	设置车道照度2的档位 （0-7档）
    mxl2	否	设置车位照度2的档位 （0-7档）
    */
    public String ntm;
    public String mnl;
    public String mxl;
    public String mnl2;
    public String mxl2;



    public void setEng(String eng) {
        this.eng = eng;
    }
    public void setLmd(String lmd) {
        this.lmd = lmd;
    }
    public void setLva(String lva) {
        this.lva = lva;
    }
    public void setNtm(String ntm) {
        this.ntm = ntm;
    }
    public void setMnl(String mnl) {
        this.mnl = mnl;
    }
    public void setMxl(String mxl) {
        this.mxl = mxl;
    }
    public void setMnl2(String mnl2) {
        this.mnl2 = mnl2;
    }
    public void setMxl2(String mxl2) {
        this.mxl2 = mxl2;
    }
    public String getEng() {
        return eng;
    }
    public String getLmd() {
        return lmd;
    }
    public String getLva() {
        return lva;
    }
    public String getNtm() {
        return ntm;
    }
    public String getMnl() {
        return mnl;
    }
    public String getMxl() {
        return mxl;
    }
    public String getMnl2() {
        return mnl2;
    }
    public String getMxl2() {
        return mxl2;
    }

    @Override
    public String toString() {
        return "SetMode{" +
                "lmd='" + lmd + '\'' +
                ", lva='" + lva + '\'' +
                ", eng='" + eng + '\'' +
                ", ntm='" + ntm + '\'' +
                ", mnl='" + mnl + '\'' +
                ", mxl='" + mxl + '\'' +
                ", mnl2='" + mnl2 + '\'' +
                ", mxl2='" + mxl2 + '\'' +
                '}';
    }
}
