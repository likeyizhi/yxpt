package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 获取附近店铺列表（不分页）
 * Created by likey on 2017/7/1.
 */

public class ShopList {
    /**"status": 0,
     "mes": "成功",
     "count": 1,
     "x": "125.315115",
     "y": "43.829187",
     "ShopList": []*/
    private int status;
    private String mes;
    private int count;
    private String x;
    private String y;
    private List<ShopListShopList> ShopList;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ShopListShopList> getShopList() {
        return ShopList;
    }

    public void setShopList(List<ShopListShopList> shopList) {
        ShopList = shopList;
    }

    public static class ShopListShopList{
        /**"ShopID": "1",
         "ShopName": "企赢动力的小店",
         "ShopImg": "http://img.zcool.cn/community/01bf1655e514b16ac7251df840273f.jpg",
         "Contact": "400123456789",
         "Address": "朝阳北路102号",
         "Latitude": "39.92334",
         "Longitude": "116.512672",
         "OpenTime": "8:00 - 22:00",
         "Distance": "0m"*/
        private String ShopID;
        private String ShopName;
        private String ShopImg;
        private String Contact;
        private String Address;
        private String Latitude;
        private String Longitude;
        private String OpenTime;
        private String Distance;
        private String ShopTypeIdentity;

        public String getShopTypeIdentity() {
            return ShopTypeIdentity;
        }

        public void setShopTypeIdentity(String shopTypeIdentity) {
            ShopTypeIdentity = shopTypeIdentity;
        }

        public String getShopID() {
            return ShopID;
        }

        public void setShopID(String shopID) {
            ShopID = shopID;
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

        public String getOpenTime() {
            return OpenTime;
        }

        public void setOpenTime(String openTime) {
            OpenTime = openTime;
        }

        public String getDistance() {
            return Distance;
        }

        public void setDistance(String distance) {
            Distance = distance;
        }
    }
}
