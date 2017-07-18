package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 获取银行字典
 * Created by likey on 2017/7/11.
 */

public class BankList {
    /** "status": 0,
     "mes": "操作成功",
     "list": []*/
    private int status;
    private String mes;
    private List<BankListlist> list;

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

    public List<BankListlist> getList() {
        return list;
    }

    public void setList(List<BankListlist> list) {
        this.list = list;
    }

    public static class BankListlist{
        /**"BankID": 1,
         "BankName": "中国农业银行",
         "BankLogo": "http://qyyxptapi.bjqydl.com/images/1.png"*/
        private int BankID;
        private String BankName;
        private String BankLogo;

        public int getBankID() {
            return BankID;
        }

        public void setBankID(int bankID) {
            BankID = bankID;
        }

        public String getBankName() {
            return BankName;
        }

        public void setBankName(String bankName) {
            BankName = bankName;
        }

        public String getBankLogo() {
            return BankLogo;
        }

        public void setBankLogo(String bankLogo) {
            BankLogo = bankLogo;
        }
    }
}
