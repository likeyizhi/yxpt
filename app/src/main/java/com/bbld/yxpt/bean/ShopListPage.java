package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 获取附近店铺列表（分页）
 * Created by likey on 2017/7/1.
 */

public class ShopListPage {
    /**"status": 0,
     "mes": "成功",
     "count": 3,
     "x": "125.315115",
     "y": "43.829187",
     "ShopList": []*/
    private int status;
    private String mes;
    private int count;
    private String x;
    private String y;
    private List<ShopListPageShopList> ShopList;

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

    public List<ShopListPageShopList> getShopList() {
        return ShopList;
    }

    public void setShopList(List<ShopListPageShopList> shopList) {
        ShopList = shopList;
    }

    public static class ShopListPageShopList{
        /** "ShopID": "5",
         "ShopName": "红堪KTV(豪华店)",
         "ShopImg": "http://p0.meituan.net/dpdeal/eeb392e76b147a937bf8b463031a8d64203994.jpg%40240w_180h_1e_1c_1l%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20",
         "Contact": "0431-84581111",
         "Address": "自由大路与丰顺街交汇(自由大路1199号招商银行旁)",
         "Latitude": "43.869832",
         "Longitude": "125.330425",
         "OpenTime": "8:00 - 22:00",
         "Distance": "4.61km"*/
        private String ShopID;
        private String ShopName;
        private String ShopImg;
        private String Contact;
        private String Address;
        private String Latitude;
        private String Longitude;
        private String OpenTime;
        private String Distance;

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
