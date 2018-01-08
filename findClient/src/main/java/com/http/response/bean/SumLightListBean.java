package com.http.response.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengx on 2017/10/19.
 */

public class SumLightListBean  implements Serializable {
    /**
     * build_id : B0003
     * che : 1
     * pag : 1
     * lis : [{"fmn":11,"mac":"600194802163","ftp":32,"lst":3,"err":0,"lran":3,"ert":74668,"lmd":1},{"fmn":12,"mac":"600194802163","ftp":31,"lst":3,"err":0,"lran":3,"ert":74667,"lmd":1},{"fmn":51,"mac":"","ftp":0,"lst":0,"err":0,"lran":2,"ert":103904,"lmd":1},{"fmn":52,"mac":"","ftp":0,"lst":0,"err":0,"lran":2,"ert":103905,"lmd":1},{"fmn":53,"mac":"","ftp":0,"lst":0,"err":0,"lran":2,"ert":103905,"lmd":1},{"fmn":54,"mac":"","ftp":0,"lst":0,"err":0,"lran":2,"ert":103905,"lmd":1},{"fmn":71,"mac":"60019480406a","ftp":34,"lst":3,"err":0,"lran":4,"ert":74670,"lmd":1},{"fmn":72,"mac":"60019480406a","ftp":29,"lst":3,"err":0,"lran":4,"ert":74670,"lmd":1},{"fmn":91,"mac":"600194801a53","ftp":24,"lst":5,"err":0,"lran":5,"ert":74669,"lmd":1},{"fmn":92,"mac":"600194801a53","ftp":24,"lst":5,"err":17,"lran":5,"ert":74669,"lmd":1},{"fmn":111,"mac":"60019480236d","ftp":31,"lst":3,"err":0,"lran":2,"ert":74673,"lmd":1},{"fmn":112,"mac":"60019480236d","ftp":28,"lst":3,"err":0,"lran":2,"ert":74673,"lmd":1},{"fmn":113,"mac":"60019480236d","ftp":29,"lst":3,"err":0,"lran":2,"ert":74673,"lmd":1},{"fmn":114,"mac":"60019480236d","ftp":28,"lst":3,"err":0,"lran":2,"ert":74673,"lmd":1},{"fmn":171,"mac":"600194802a5a","ftp":25,"lst":5,"err":0,"lran":6,"ert":74672,"lmd":1},{"fmn":172,"mac":"600194802a5a","ftp":26,"lst":5,"err":17,"lran":6,"ert":74671,"lmd":1},{"fmn":173,"mac":"600194802a5a","ftp":25,"lst":5,"err":17,"lran":6,"ert":74671,"lmd":1},{"fmn":191,"mac":"600194801bd5","ftp":27,"lst":5,"err":0,"lran":1,"ert":74669,"lmd":1},{"fmn":192,"mac":"600194801bd5","ftp":26,"lst":5,"err":17,"lran":1,"ert":74669,"lmd":1}]
     * result : 1
     */

    private String build_id;
    private String che;
    private String pag;
    private int result;
    private List<LisJavaBean> lis;

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

    @Override
    public String toString() {
        return "SumLightListBean{" +
                "build_id='" + build_id + '\'' +
                ", che='" + che + '\'' +
                ", pag='" + pag + '\'' +
                ", result=" + result +
                ", lis=" + lis +
                '}';
    }

    public static class LisJavaBean {
        /**
         * fmn : 11
         * mac : 600194802163
         * ftp : 32
         * lst : 3
         * err : 0
         * lran : 3
         * ert : 74668
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

        @Override
        public String toString() {
            return "LisJavaBean{" +
                    "fmn=" + fmn +
                    ", mac='" + mac + '\'' +
                    ", ftp=" + ftp +
                    ", lst=" + lst +
                    ", err=" + err +
                    ", lran=" + lran +
                    ", ert=" + ert +
                    ", lmd=" + lmd +
                    '}';
        }
    }
}
