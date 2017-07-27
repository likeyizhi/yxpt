package com.bbld.yxpt.bean;

/**
 * 获取店铺详情
 * Created by likey on 2017/7/1.
 */

public class ShopInfo {
    /**"status": 0,
     "mes": "成功",
     "ShopInfo": {}*/
    private int status;
    private String mes;
    private ShopInfoShopInfo ShopInfo;

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

    public ShopInfoShopInfo getShopInfo() {
        return ShopInfo;
    }

    public void setShopInfo(ShopInfoShopInfo shopInfo) {
        ShopInfo = shopInfo;
    }

    public static class ShopInfoShopInfo{
        /**"ShopName": "企赢动力的小店",
         "ShopImg": "http://img.zcool.cn/community/01bf1655e514b16ac7251df840273f.jpg",
         "LinkName": "H",
         "LinkPhone": "18510310218",
         "Contact": "400123456789",
         "Address": "朝阳北路102号",
         "Latitude": "39.92334",
         "Longitude": "116.512672",
         "Describe": "何大智弱智",
         "Tag": "测试专用",
         "Distance": "0m"*/
        private String ShopName;
        private String ShopImg;
        private String ShopBigImg;
        private String LinkName;
        private String LinkPhone;
        private String Contact;
        private String Address;
        private String Latitude;
        private String Longitude;
        private String Describe;
        private String Tag;
        private String Distance;

        public String getShopBigImg() {
            return ShopBigImg;
        }

        public void setShopBigImg(String shopBigImg) {
            ShopBigImg = shopBigImg;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String shopName) {
            ShopName = shopName;
        }

        public String getShopImg() {
            return ShopImg;
        }

        public void setShopImg(String shopImg) {
            ShopImg = shopImg;
        }

        public String getLinkName() {
            return LinkName;
        }

        public void setLinkName(String linkName) {
            LinkName = linkName;
        }

        public String getLinkPhone() {
            return LinkPhone;
        }

        public void setLinkPhone(String linkPhone) {
            LinkPhone = linkPhone;
        }

        public String getContact() {
            return Contact;
        }

        public void setContact(String contact) {
            Contact = contact;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String latitude) {
            Latitude = latitude;
        }

        public String getLongitude() {
            return Longitude;
        }

        public void setLongitude(String longitude) {
            Longitude = longitude;
        }

        public String getDescribe() {
            return Describe;
        }

        public void setDescribe(String describe) {
            Describe = describe;
        }

        public String getTag() {
            return Tag;
        }

        public void setTag(String tag) {
            Tag = tag;
        }

        public String getDistance() {
            return Distance;
        }

        public void setDistance(String distance) {
            Distance = distance;
        }
    }
}
