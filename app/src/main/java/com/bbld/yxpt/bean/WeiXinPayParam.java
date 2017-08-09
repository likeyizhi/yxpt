package com.bbld.yxpt.bean;

/**
 * 微信支付
 * Created by likey on 2017/8/9.
 */

public class WeiXinPayParam {
    /**"status": 0,
     "mes": "成功",
     "orderString": {}*/
    private int status;
    private String mes;
    private WeiXinPayParamOrderString orderString;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public WeiXinPayParamOrderString getOrderString() {
        return orderString;
    }

    public void setOrderString(WeiXinPayParamOrderString orderString) {
        this.orderString = orderString;
    }

    public static class WeiXinPayParamOrderString{
        /**"appid": "wxb32b83a9fe661456",
         "partnerid": "1485166862",
         "prepayid": "wx20170809133408a75bd7cd5a0459897678",
         "timestamp": "1502256848",
         "noncestr": "A8E864D04C95572D1AECE099AF852D0A",
         "Package": "Sign=WXPay",
         "signType": "MD5",
         "sign": "6A4C41AD9B2DA72795B13503B4D6D792"*/
        private String appid;
        private String partnerid;
        private String prepayid;
        private String timestamp;
        private String noncestr;
        private String Package;
        private String signType;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackage() {
            return Package;
        }

        public void setPackage(String aPackage) {
            Package = aPackage;
        }

        public String getSignType() {
            return signType;
        }

        public void setSignType(String signType) {
            this.signType = signType;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
