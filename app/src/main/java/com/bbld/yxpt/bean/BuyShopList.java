package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 获取购买过的店铺
 * Created by likey on 2017/7/26.
 */

public class BuyShopList {
    /**"status": 0,
     "mes": "操作成功",
     "count": 2,
     "list": []*/
    private int status;
    private String mes;
    private int count;
    private List<BuyShopListlist> list;

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

    public List<BuyShopListlist> getList() {
        return list;
    }

    public void setList(List<BuyShopListlist> list) {
        this.list = list;
    }

    public static class BuyShopListlist{
        /**"ShopID": "1",
         "ShopName": "企赢动力的小店",
         "ShopImg": "http://qyyxptapi.bjqydl.com/UpFile/Shop/1-45-20170725172645974.png",
         "Address": "朝阳北路102号",
         "ActivityCount": 5,
         "OrderCount": 25*/
        private String ShopID;
        private String ShopName;
        private String ShopImg;
        private String Address;
        private int ActivityCount;
        private int OrderCount;

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

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public int getActivityCount() {
            return ActivityCount;
        }

        public void setActivityCount(int activityCount) {
            ActivityCount = activityCount;
        }

        public int getOrderCount() {
            return OrderCount;
        }

        public void setOrderCount(int orderCount) {
            OrderCount = orderCount;
        }
    }
}
