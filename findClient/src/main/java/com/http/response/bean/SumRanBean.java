package com.http.response.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengx on 2017/10/24.
 */

public class SumRanBean  implements Serializable{

    /**
     * build_id : B0003
     * ran : 办公
     * pag : 1
     * lis : [{"fmn":51,"mac":"600194802a5a","ftp":30,"lst":7,"err":0,"lran":2,"ert":6,"lmd":1},{"fmn":52,"mac":"600194802a5a","ftp":32,"lst":7,"err":0,"lran":2,"ert":6,"lmd":1},{"fmn":53,"mac":"600194802a5a","ftp":33,"lst":7,"err":0,"lran":2,"ert":6,"lmd":1},{"fmn":54,"mac":"600194802a5a","ftp":28,"lst":7,"err":0,"lran":2,"ert":6,"lmd":1},{"fmn":111,"mac":"600194801a53","ftp":39,"lst":7,"err":0,"lran":2,"ert":1,"lmd":1},{"fmn":112,"mac":"600194801a53","ftp":32,"lst":7,"err":0,"lran":2,"ert":1,"lmd":1},{"fmn":113,"mac":"600194801a53","ftp":32,"lst":7,"err":0,"lran":2,"ert":1,"lmd":1},{"fmn":114,"mac":"600194801a53","ftp":33,"lst":7,"err":0,"lran":2,"ert":1,"lmd":1}]
     * result : 1
     */

    private String build_id;
    private String ran;
    private String pag;
    private int result;
    private List<LisJavaBean> lis;

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }

    public String getRan() {
        return ran;
    }

    public void setRan(String ran) {
        this.ran = ran;
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

    public static class LisJavaBean  implements Serializable{
        /**
         * fmn : 51
         * mac : 600194802a5a
         * ftp : 30
         * lst : 7
         * err : 0
         * lran : 2
         * ert : 6
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
