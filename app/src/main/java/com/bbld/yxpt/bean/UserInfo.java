package com.bbld.yxpt.bean;

/**
 * 个人中心
 * Created by likey on 2017/7/1.
 */

public class UserInfo {
    /**"status": 0,
     "mes": "成功",
     "UserInfo": {}*/
    private int status;
    private String mes;
    private UserInfoUserInfo UserInfo;

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

    public UserInfoUserInfo getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(UserInfoUserInfo userInfo) {
        UserInfo = userInfo;
    }

    public static class UserInfoUserInfo{
        /**"NickName": "返客1001707840",
         "Mobile": "18346665781",
         "HeadPortrait": "",
         "Remark": "",
         "Sex": "男",
         "RegisterTime": "2017-06-30 23:32",
         "TotialSale": "0.00",
         "ReturnTotialSale": "0.00",
         "RewardOrderCount": 0,
         "RewardTotial": "0.00",
         "PlatformUserCount": 92312*/
        private String NickName;
        private String Mobile;
        private String HeadPortrait;
        private String Remark;
        private String Sex;
        private String RegisterTime;
        private String TotialSale;
        private String ReturnTotialSale;
        private int RewardOrderCount;
        private String RewardTotial;
        private int PlatformUserCount;

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public String getHeadPortrait() {
            return HeadPortrait;
        }

        public void setHeadPortrait(String headPortrait) {
            HeadPortrait = headPortrait;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public String getSex() {
            return Sex;
        }

        public void setSex(String sex) {
            Sex = sex;
        }

        public String getRegisterTime() {
            return RegisterTime;
        }

        public void setRegisterTime(String registerTime) {
            RegisterTime = registerTime;
        }

        public String getTotialSale() {
            return TotialSale;
        }

        public void setTotialSale(String totialSale) {
            TotialSale = totialSale;
        }

        public String getReturnTotialSale() {
            return ReturnTotialSale;
        }

        public void setReturnTotialSale(String returnTotialSale) {
            ReturnTotialSale = returnTotialSale;
        }

        public int getRewardOrderCount() {
            return RewardOrderCount;
        }

        public void setRewardOrderCount(int rewardOrderCount) {
            RewardOrderCount = rewardOrderCount;
        }

        public String getRewardTotial() {
            return RewardTotial;
        }

        public void setRewardTotial(String rewardTotial) {
            RewardTotial = rewardTotial;
        }

        public int getPlatformUserCount() {
            return PlatformUserCount;
        }

        public void setPlatformUserCount(int platformUserCount) {
            PlatformUserCount = platformUserCount;
        }
    }
}
