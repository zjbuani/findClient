package com.http.response.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengx on 2017/10/24.
 */

public class SumFloorBean implements Serializable {

    /**
     * build_id : B0003
     * floor : 6
     * pag : 1
     * lis : [{"fmn":11,"mac":"60019480236d","ftp":36,"lst":7,"err":0,"lran":3,"ert":3,"lmd":1},{"fmn":12,"mac":"60019480236d","ftp":36,"lst":7,"err":0,"lran":3,"ert":3,"lmd":1},{"fmn":51,"mac":"600194802a5a","ftp":31,"lst":7,"err":0,"lran":2,"ert":5,"lmd":1},{"fmn":52,"mac":"600194802a5a","ftp":33,"lst":7,"err":0,"lran":2,"ert":5,"lmd":1},{"fmn":53,"mac":"600194802a5a","ftp":34,"lst":7,"err":0,"lran":2,"ert":5,"lmd":1},{"fmn":54,"mac":"600194802a5a","ftp":30,"lst":7,"err":0,"lran":2,"ert":5,"lmd":1},{"fmn":71,"mac":"60019480406a","ftp":37,"lst":7,"err":0,"lran":4,"ert":7,"lmd":1},{"fmn":72,"mac":"60019480406a","ftp":35,"lst":7,"err":0,"lran":4,"ert":7,"lmd":1},{"fmn":91,"mac":"600194803f3b","ftp":41,"lst":7,"err":0,"lran":5,"ert":9,"lmd":1},{"fmn":92,"mac":"600194803f3b","ftp":38,"lst":7,"err":0,"lran":5,"ert":9,"lmd":1},{"fmn":111,"mac":"600194801a53","ftp":40,"lst":7,"err":0,"lran":2,"ert":10,"lmd":1},{"fmn":112,"mac":"600194801a53","ftp":34,"lst":7,"err":0,"lran":2,"ert":9,"lmd":1},{"fmn":113,"mac":"600194801a53","ftp":33,"lst":7,"err":0,"lran":2,"ert":9,"lmd":1},{"fmn":114,"mac":"600194801a53","ftp":35,"lst":7,"err":0,"lran":2,"ert":9,"lmd":1},{"fmn":171,"mac":"600194801bd5","ftp":41,"lst":7,"err":0,"lran":6,"ert":7,"lmd":1},{"fmn":172,"mac":"600194801bd5","ftp":39,"lst":7,"err":0,"lran":6,"ert":7,"lmd":1},{"fmn":173,"mac":"600194801bd5","ftp":40,"lst":7,"err":0,"lran":6,"ert":7,"lmd":1},{"fmn":191,"mac":"600194802163","ftp":39,"lst":7,"err":0,"lran":1,"ert":4,"lmd":1},{"fmn":192,"mac":"600194802163","ftp":37,"lst":7,"err":0,"lran":1,"ert":4,"lmd":1}]
     * result : 1
     */

    private String build_id;
    private String floor;
    private String pag;
    private int result;
    private List<LisJavaBean> lis;

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getPag() {
        return pag;
    }

    public void setPag(String pag) {
        this.pag = pag;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<LisJavaBean> getLis() {
        return lis;
    }

    public void setLis(List<LisJavaBean> lis) {
        this.lis = lis;
    }

    public static class LisJavaBean implements Serializable {
        /**
         * fmn : 11
         * mac : 60019480236d
         * ftp : 36
         * lst : 7
         * err : 0
         * lran : 3
         * ert : 3
         * lmd : 1
         */

        private int fmn;
        private String mac;
        private int ftp;
        private int lst;
        private int err;
        private int lran;
        private int ert;
        private int lmd;

        public int getFmn() {
            return fmn;
        }

        public void setFmn(int fmn) {
            this.fmn = fmn;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public int getFtp() {
            return ftp;
        }

        public void setFtp(int ftp) {
            this.ftp = ftp;
        }

        public int getLst() {
            return lst;
        }

        public void setLst(int lst) {
            this.lst = lst;
        }

        public int getErr() {
            return err;
        }

        public void setErr(int err) {
            this.err = err;
        }

        public int getLran() {
            return lran;
        }

        public void setLran(int lran) {
            this.lran = lran;
        }

        public int getErt() {
            return ert;
        }

        public void setErt(int ert) {
            this.ert = ert;
        }

        public int getLmd() {
            return lmd;
        }

        public void setLmd(int lmd) {
            this.lmd = lmd;
        }
    }
}
