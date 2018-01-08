package com.http.response.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by  图表数据 和 设备数据 (总数和 故障数)
 */

public class LightSystemLineCharBean implements Serializable {

    /**
     * build_id : B0003
     * che : 1
     * par : {"all":"19","bad":"5","wat":"287","atp":"35","alm":"700","wat_chart":[{"time":"17","value":0},{"time":"16","value":0},{"time":"15","value":0},{"time":"14","value":0},{"time":"13","value":0},{"time":"12","value":0},{"time":"11","value":0},{"time":"10","value":0},{"time":"09","value":0},{"time":"08","value":0},{"time":"07","value":0},{"time":"06","value":0},{"time":"05","value":0},{"time":"04","value":0},{"time":"03","value":0},{"time":"02","value":0},{"time":"01","value":0},{"time":"00","value":0},{"time":"23","value":0},{"time":"22","value":0},{"time":"21","value":0},{"time":"20","value":0},{"time":"19","value":0},{"time":"18","value":0}],"temp_chart":[{"time":"17","value":0},{"time":"16","value":0},{"time":"15","value":0},{"time":"14","value":0},{"time":"13","value":0},{"time":"12","value":0},{"time":"11","value":0},{"time":"10","value":0},{"time":"09","value":0},{"time":"08","value":0},{"time":"07","value":0},{"time":"06","value":0},{"time":"05","value":0},{"time":"04","value":0},{"time":"03","value":0},{"time":"02","value":0},{"time":"01","value":0},{"time":"00","value":0},{"time":"23","value":0},{"time":"22","value":0},{"time":"21","value":0},{"time":"20","value":0},{"time":"19","value":0},{"time":"18","value":0}],"light_chart":[{"time":"17","value":0},{"time":"16","value":0},{"time":"15","value":0},{"time":"14","value":0},{"time":"13","value":0},{"time":"12","value":0},{"time":"11","value":0},{"time":"10","value":0},{"time":"09","value":0},{"time":"08","value":0},{"time":"07","value":0},{"time":"06","value":0},{"time":"05","value":0},{"time":"04","value":0},{"time":"03","value":0},{"time":"02","value":0},{"time":"01","value":0},{"time":"00","value":0},{"time":"23","value":0},{"time":"22","value":0},{"time":"21","value":0},{"time":"20","value":0},{"time":"19","value":0},{"time":"18","value":0}]}
     * result : 1
     */

    private String build_id;
    private int che;
    private ParJavaBean par;
    private int result;

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }

    public int getChe() {
        return che;
    }

    public void setChe(int che) {
        this.che = che;
    }

    public ParJavaBean getPar() {
        return par;
    }

    public void setPar(ParJavaBean par) {
        this.par = par;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "LightSystemLineCharBean{" +
                "build_id='" + build_id + '\'' +
                ", che=" + che +
                ", par=" + par +
                ", result=" + result +
                '}';
    }

    public static class ParJavaBean implements  Serializable {
        /**
         * all : 19
         * bad : 5
         * wat : 287
         * atp : 35
         * alm : 700
         * wat_chart : [{"time":"17","value":0},{"time":"16","value":0},{"time":"15","value":0},{"time":"14","value":0},{"time":"13","value":0},{"time":"12","value":0},{"time":"11","value":0},{"time":"10","value":0},{"time":"09","value":0},{"time":"08","value":0},{"time":"07","value":0},{"time":"06","value":0},{"time":"05","value":0},{"time":"04","value":0},{"time":"03","value":0},{"time":"02","value":0},{"time":"01","value":0},{"time":"00","value":0},{"time":"23","value":0},{"time":"22","value":0},{"time":"21","value":0},{"time":"20","value":0},{"time":"19","value":0},{"time":"18","value":0}]
         * temp_chart : [{"time":"17","value":0},{"time":"16","value":0},{"time":"15","value":0},{"time":"14","value":0},{"time":"13","value":0},{"time":"12","value":0},{"time":"11","value":0},{"time":"10","value":0},{"time":"09","value":0},{"time":"08","value":0},{"time":"07","value":0},{"time":"06","value":0},{"time":"05","value":0},{"time":"04","value":0},{"time":"03","value":0},{"time":"02","value":0},{"time":"01","value":0},{"time":"00","value":0},{"time":"23","value":0},{"time":"22","value":0},{"time":"21","value":0},{"time":"20","value":0},{"time":"19","value":0},{"time":"18","value":0}]
         * light_chart : [{"time":"17","value":0},{"time":"16","value":0},{"time":"15","value":0},{"time":"14","value":0},{"time":"13","value":0},{"time":"12","value":0},{"time":"11","value":0},{"time":"10","value":0},{"time":"09","value":0},{"time":"08","value":0},{"time":"07","value":0},{"time":"06","value":0},{"time":"05","value":0},{"time":"04","value":0},{"time":"03","value":0},{"time":"02","value":0},{"time":"01","value":0},{"time":"00","value":0},{"time":"23","value":0},{"time":"22","value":0},{"time":"21","value":0},{"time":"20","value":0},{"time":"19","value":0},{"time":"18","value":0}]
         */

        private String all;
        private String bad;
        private String wat;
        private String atp;
        private String alm;

        private String ntm;
        private String lmd;
        private String lva;
        private String eng;
        private String mnl;
        private String mxl;
        private String mnl2;
        private String mxl2;


        private List<WatChartJavaBean> wat_chart;
        private List<TempChartJavaBean> temp_chart;
        private List<LightChartJavaBean> light_chart;

        public String getAll() {
            return all;
        }

        public void setAll(String all) {
            this.all = all;
        }

        public String getBad() {
            return bad;
        }

        public void setBad(String bad) {
            this.bad = bad;
        }

        public String getWat() {
            return wat;
        }

        public void setWat(String wat) {
            this.wat = wat;
        }

        public String getAtp() {
            return atp;
        }

        public void setAtp(String atp) {
            this.atp = atp;
        }

        public String getAlm() {
            return alm;
        }

        public void setAlm(String alm) {
            this.alm = alm;
        }

        public List<WatChartJavaBean> getWat_chart() {
            return wat_chart;
        }

        public void setWat_chart(List<WatChartJavaBean> wat_chart) {
            this.wat_chart = wat_chart;
        }

        public List<TempChartJavaBean> getTemp_chart() {
            return temp_chart;
        }

        public void setTemp_chart(List<TempChartJavaBean> temp_chart) {
            this.temp_chart = temp_chart;
        }

        public List<LightChartJavaBean> getLight_chart() {
            return light_chart;
        }

        public void setLight_chart(List<LightChartJavaBean> light_chart) {
            this.light_chart = light_chart;
        }

        public void setNtm(String ntm) {
            this.ntm = ntm;
        }

        public void setLmd(String lmd) {
            this.lmd = lmd;
        }

        public void setLva(String lva) {
            this.lva = lva;
        }

        public void setEng(String eng) {
            this.eng = eng;
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

        public String getNtm() {
            return ntm;
        }

        public String getLmd() {
            return lmd;
        }

        public String getLva() {
            return lva;
        }

        public String getEng() {
            return eng;
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
            return "ParJavaBean{" +
                    "all='" + all + '\'' +
                    ", bad='" + bad + '\'' +
                    ", wat='" + wat + '\'' +
                    ", atp='" + atp + '\'' +
                    ", alm='" + alm + '\'' +
                    ", ntm='" + ntm + '\'' +
                    ", lmd='" + lmd + '\'' +
                    ", lva='" + lva + '\'' +
                    ", eng='" + eng + '\'' +
                    ", mnl='" + mnl + '\'' +
                    ", mxl='" + mxl + '\'' +
                    ", mnl2='" + mnl2 + '\'' +
                    ", mxl2='" + mxl2 + '\'' +
                    ", wat_chart=" + wat_chart +
                    ", temp_chart=" + temp_chart +
                    ", light_chart=" + light_chart +
                    '}';
        }

        public static class WatChartJavaBean implements  Serializable {
            /**
             * time : 17
             * value : 0
             */

            private String time;
            private int value;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return "WatChartJavaBean{" +
                        "time='" + time + '\'' +
                        ", value=" + value +
                        '}';
            }
        }

        public static class TempChartJavaBean implements Serializable{
            /**
             * time : 17
             * value : 0
             */

            private String time;
            private int value;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return "TempChartJavaBean{" +
                        "time='" + time + '\'' +
                        ", value=" + value +
                        '}';
            }
        }

        public static class LightChartJavaBean implements  Serializable{
            /**
             * time : 17
             * value : 0
             */

            private String time;
            private int value;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return "LightChartJavaBean{" +
                        "time='" + time + '\'' +
                        ", value=" + value +
                        '}';
            }
        }
    }
}
