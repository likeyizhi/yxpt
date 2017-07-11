package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 获取用户订单的返还信息
 * Created by likey on 2017/7/3.
 */

public class OrderReturnInfo {
    private int status;
    private String mes;
    private String QueueInfo;
    private OrderReturnInfoShopInfo ShopInfo;
    private List<OrderReturnInfoReturnList> ReturnList;
    private List<OrderReturnInfoNextList> NextList;
    private List<OrderReturnInfoMyList> MyList;

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

    public String getQueueInfo() {
        return QueueInfo;
    }

    public void setQueueInfo(String queueInfo) {
        QueueInfo = queueInfo;
    }

    public OrderReturnInfoShopInfo getShopInfo() {
        return ShopInfo;
    }

    public void setShopInfo(OrderReturnInfoShopInfo shopInfo) {
        ShopInfo = shopInfo;
    }

    public List<OrderReturnInfoReturnList> getReturnList() {
        return ReturnList;
    }

    public void setReturnList(List<OrderReturnInfoReturnList> returnList) {
        ReturnList = returnList;
    }

    public List<OrderReturnInfoNextList> getNextList() {
        return NextList;
    }

    public void setNextList(List<OrderReturnInfoNextList> nextList) {
        NextList = nextList;
    }

    public List<OrderReturnInfoMyList> getMyList() {
        return MyList;
    }

    public void setMyList(List<OrderReturnInfoMyList> myList) {
        MyList = myList;
    }

    public static class OrderReturnInfoShopInfo{
        /** "ShopName": "企赢动力的小店",
         "ShopImg": "http://img.zcool.cn/community/01bf1655e514b16ac7251df840273f.jpg",
         "LinkName": "呵呵呵",
         "LinkPhone": "jsjsjs",
         "Contact": "u额u额u",
         "Address": "朝阳北路102号",
         "Latitude": "39.92334",
         "Longitude": "116.512672",
         "Describe": "              进店送礼品哦",
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
    public static class OrderReturnInfoReturnList{
        /**"NOID": "10",
         "NickName": " 张立武",
         "HeadPortrait": "http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIkqyiaicz1CIugP37VSJgZythN5wtrrFvR7NDkPicDER9frJAZialdn4NFcFnBVhiaNaop7lgLH5xutyg/0",
         "ActivityTitle": "The ",
         "OrderNo": "00001201706272015555710000100001",
         "ActivityDiscount": "0.80",
         "EnterAmount": "2.00",
         "AddDate": "2017-06-27 20:15:55",
         "IsReturn": 1,
         "ReturnStatus": "已返还"*/
        private String NOID;
        private String NickName;
        private String HeadPortrait;
        private String ActivityTitle;
        private String OrderNo;
        private String ActivityDiscount;
        private String EnterAmount;
        private String AddDate;
        private int IsReturn;
        private String ReturnStatus;

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

        public String getActivityTitle() {
            return ActivityTitle;
        }

        public void setActivityTitle(String activityTitle) {
            ActivityTitle = activityTitle;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String orderNo) {
            OrderNo = orderNo;
        }

        public String getActivityDiscount() {
            return ActivityDiscount;
        }

        public void setActivityDiscount(String activityDiscount) {
            ActivityDiscount = activityDiscount;
        }

        public String getEnterAmount() {
            return EnterAmount;
        }

        public void setEnterAmount(String enterAmount) {
            EnterAmount = enterAmount;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }

        public int getIsReturn() {
            return IsReturn;
        }

        public void setIsReturn(int isReturn) {
            IsReturn = isReturn;
        }

        public String getReturnStatus() {
            return ReturnStatus;
        }

        public void setReturnStatus(String returnStatus) {
            ReturnStatus = returnStatus;
        }
    }
    public static class OrderReturnInfoNextList{
        /**"NOID": "12",
         "NickName": " Toni",
         "HeadPortrait": "http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKI4b5WtMiarqAvedbIFldPCEfcaC4kTR0CV6mRo3wxJVa8CWYd09zFJGyRjyeLTKLr4iap3AKyNKCQ/0",
         "ActivityTitle": "The ",
         "OrderNo": "00001201706272035266660000100003",
         "ActivityDiscount": "0.80",
         "EnterAmount": "1.00",
         "AddDate": "2017-06-27 20:35:26",
         "IsReturn": 0,
         "ReturnStatus": ""*/
        private String NOID;
        private String NickName;
        private String HeadPortrait;
        private String ActivityTitle;
        private String OrderNo;
        private String ActivityDiscount;
        private String EnterAmount;
        private String AddDate;
        private int IsReturn;
        private String ReturnStatus;

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

        public String getActivityTitle() {
            return ActivityTitle;
        }

        public void setActivityTitle(String activityTitle) {
            ActivityTitle = activityTitle;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String orderNo) {
            OrderNo = orderNo;
        }

        public String getActivityDiscount() {
            return ActivityDiscount;
        }

        public void setActivityDiscount(String activityDiscount) {
            ActivityDiscount = activityDiscount;
        }

        public String getEnterAmount() {
            return EnterAmount;
        }

        public void setEnterAmount(String enterAmount) {
            EnterAmount = enterAmount;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }

        public int getIsReturn() {
            return IsReturn;
        }

        public void setIsReturn(int isReturn) {
            IsReturn = isReturn;
        }

        public String getReturnStatus() {
            return ReturnStatus;
        }

        public void setReturnStatus(String returnStatus) {
            ReturnStatus = returnStatus;
        }
    }
    public static class OrderReturnInfoMyList{
        /**"NOID": "10",
         "NickName": " 张立武",
         "HeadPortrait": "http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIkqyiaicz1CIugP37VSJgZythN5wtrrFvR7NDkPicDER9frJAZialdn4NFcFnBVhiaNaop7lgLH5xutyg/0",
         "ActivityTitle": "The ",
         "OrderNo": "00001201706272015555710000100001",
         "ActivityDiscount": "0.80",
         "EnterAmount": "2.00",
         "AddDate": "2017-06-27 20:15:55",
         "IsReturn": 1,
         "ReturnStatus": "已返还"*/
        private String NOID;
        private String NickName;
        private String HeadPortrait;
        private String ActivityTitle;
        private String OrderNo;
        private String ActivityDiscount;
        private String EnterAmount;
        private String AddDate;
        private int IsReturn;
        private String ReturnStatus;

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

        public String getActivityTitle() {
            return ActivityTitle;
        }

        public void setActivityTitle(String activityTitle) {
            ActivityTitle = activityTitle;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String orderNo) {
            OrderNo = orderNo;
        }

        public String getActivityDiscount() {
            return ActivityDiscount;
        }

        public void setActivityDiscount(String activityDiscount) {
            ActivityDiscount = activityDiscount;
        }

        public String getEnterAmount() {
            return EnterAmount;
        }

        public void setEnterAmount(String enterAmount) {
            EnterAmount = enterAmount;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }

        public int getIsReturn() {
            return IsReturn;
        }

        public void setIsReturn(int isReturn) {
            IsReturn = isReturn;
        }

        public String getReturnStatus() {
            return ReturnStatus;
        }

        public void setReturnStatus(String returnStatus) {
            ReturnStatus = returnStatus;
        }
    }
}
