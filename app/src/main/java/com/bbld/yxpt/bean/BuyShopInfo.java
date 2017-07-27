package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 获取店铺订单信息
 * Created by likey on 2017/7/26.
 */

public class BuyShopInfo {
    /**"status": 0,
     "mes": "操作成功",
     "ShopID": 1,
     "ShopName": "企赢动力的小店",
     "ShopBigImg": "http://qyyxptapi.bjqydl.com/UpFile/Shop/1-b-20170726093446907.png",
     "count": 5,
     "list": []*/
    private int status;
    private String mes;
    private int ShopID;
    private String ShopName;
    private String ShopBigImg;
    private int count;
    private List<BuyShopInfolist> list;

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

    public int getShopID() {
        return ShopID;
    }

    public void setShopID(int shopID) {
        ShopID = shopID;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getShopBigImg() {
        return ShopBigImg;
    }

    public void setShopBigImg(String shopBigImg) {
        ShopBigImg = shopBigImg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<BuyShopInfolist> getList() {
        return list;
    }

    public void setList(List<BuyShopInfolist> list) {
        this.list = list;
    }

    public static class BuyShopInfolist{
        /**"ShopActivityID": "1",
         "Title": "就是The ",
         "OrderCount": 7*/
        private String ShopActivityID;
        private String Title;
        private int OrderCount;

        public String getShopActivityID() {
            return ShopActivityID;
        }

        public void setShopActivityID(String shopActivityID) {
            ShopActivityID = shopActivityID;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public int getOrderCount() {
            return OrderCount;
        }

        public void setOrderCount(int orderCount) {
            OrderCount = orderCount;
        }
    }
}
