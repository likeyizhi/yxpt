package com.bbld.yxpt.bean;

/**
 * Created by likey on 2017/8/3.
 */

public class MyOrder02 {
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
