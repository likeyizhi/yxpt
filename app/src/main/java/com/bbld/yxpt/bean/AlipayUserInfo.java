package com.bbld.yxpt.bean;

/**
 * 支付宝登录返回信息
 * Created by likey on 2017/8/17.
 */

public class AlipayUserInfo {
    private int status;
    private String mes;
    private AlipayUserInfoUserInfo userInfo;

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

    public AlipayUserInfoUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(AlipayUserInfoUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static class AlipayUserInfoUserInfo{
        /**"code": "10000",
         "msg": "Success",
         "avatar": "https://tfs.alipayobjects.com/images/partner/T1ZLJoXb0aXXXXXXXX",
         "city": "鹤岗市",
         "gender": "m",
         "is_certified": "T",
         "is_student_certified": "F",
         "nick_name": "郭子岩",
         "province": "黑龙江省",
         "user_id": "2088902951729976",
         "user_status": "T",
         "user_type": "2"*/
        private String code;
        private String msg;
        private String avatar;
        private String city;
        private String gender;
        private String is_certified;
        private String is_student_certified;
        private String nick_name;
        private String province;
        private String user_id;
        private String user_status;
        private String user_type;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getIs_certified() {
            return is_certified;
        }

        public void setIs_certified(String is_certified) {
            this.is_certified = is_certified;
        }

        public String getIs_student_certified() {
            return is_student_certified;
        }

        public void setIs_student_certified(String is_student_certified) {
            this.is_student_certified = is_student_certified;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }
    }
}
