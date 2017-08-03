package com.bbld.yxpt.bean;


import java.util.List;

/**
 * 扫码获取店铺信息
 * Created by likey on 2017/7/3.
 */

public class ScanShop {
    /**"status": 0,
     "mes": "成功",
     "ShopInfo": {}*/
    private int status;
    private String mes;
    private ScanShopShopInfo ShopInfo;
    private java.util.List<ScanShopList> List;

    public java.util.List<ScanShopList> getList() {
        return List;
    }

    public void setList(java.util.List<ScanShopList> list) {
        List = list;
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

    public ScanShopShopInfo getShopInfo() {
        return ShopInfo;
    }

    public void setShopInfo(ScanShopShopInfo shopInfo) {
        ShopInfo = shopInfo;
    }

    public static class ScanShopShopInfo{
        /**"ShopName": "企赢动力的小店",
         "ShopImg": "http://img.zcool.cn/community/01bf1655e514b16ac7251df840273f.jpg",
         "LinkName": "何大智",
         "LinkPhone": "18510310218",
         "Contact": "400123456789",
         "Address": "朝阳北路102号",
         "Latitude": "39.92334",
         "Longitude": "116.512672",
         "Describe": "        进店送礼品哦",
         "ActivityTitle": "周一至周四天天欢唱4小时",
         "ActivityDesc": "先到先得",
         "ActivityTotal": "3.00"
         "Tag": "测试专用"*/
        private String ShopName;
        private String ShopImg;
        private String LinkName;
        private String LinkPhone;
        private String Contact;
        private String Address;
        private String Latitude;
        private String Longitude;
        private String Describe;
        private String Tag;
        private String ActivityTitle;
        private String ActivityDesc;
        private String ActivityTotal;

        public String getActivityTitle() {
            return ActivityTitle;
        }

        public void setActivityTitle(String activityTitle) {
            ActivityTitle = activityTitle;
        }

        public String getActivityDesc() {
            return ActivityDesc;
        }

        public void setActivityDesc(String activityDesc) {
            ActivityDesc = activityDesc;
        }

        public String getActivityTotal() {
            return ActivityTotal;
        }

        public void setActivityTotal(String activityTotal) {
            ActivityTotal = activityTotal;
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
    }

    public static class ScanShopList{
        /**"NOID": "96",
         "NickName": "186*****068",
         "HeadPortrait": "http://qyyxptapi.bjqydl.com/UpFile/User/0-8ace-20170731115703889.png",
         "OrderNo": "00001201707311204575260003800001",
         "ReturnPrice": "1.00",
         "ReturnDate": "2017-07-31 12:05:37"*/
        private String NOID;
        private String NickName;
        private String HeadPortrait;
        private String OrderNo;
        private String ReturnPrice;
        private String ReturnDate;

        public String getNOID() {
            return NOID;
        }

        public void setNOID(String NOID) {
            this.NOID = NOID;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public String getHeadPortrait() {
            return HeadPortrait;
        }

        public void setHeadPortrait(String headPortrait) {
            HeadPortrait = headPortrait;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String orderNo) {
            OrderNo = orderNo;
        }

        public String getReturnPrice() {
            return ReturnPrice;
        }

        public void setReturnPrice(String returnPrice) {
            ReturnPrice = returnPrice;
        }

        public String getReturnDate() {
            return ReturnDate;
        }

        public void setReturnDate(String returnDate) {
            ReturnDate = returnDate;
        }
    }
}
