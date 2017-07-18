package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 获取银行卡列表
 * Created by dell on 2017/7/7.
 */

public class GetBankCardList {
    /** "status": 0,
     "mes": "操作成功",
     "list": []
     */

    private int status;
    private String mes;
    private List<GetBankCardListRes> list;

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

    public List<GetBankCardListRes> getList() {
        return list;
    }

    public void setList(List<GetBankCardListRes> list) {
        this.list = list;
    }

    public static class GetBankCardListRes{
        /**
         * "BankCardID": 2,
         "BankName": "上海浦东发展银行 ",
         "BankLogo": "http://localhost:6114/images/14.png",
         "CardNo": "6225210692223341",
         "OpenBank": "北沙滩支行",
         "Name": "李华",
         "AddDate": "2017-07-06 00:56"
         */
        private int BankCardID;
        private String BankName;
        private String BankLogo;
        private String CardNo;
        private String OpenBank;
        private String Name;
        private String AddDate;

        public int getBankCardID() {
            return BankCardID;
        }

        public void setBankCardID(int bankCardID) {
            BankCardID = bankCardID;
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

        public String getCardNo() {
            return CardNo;
        }

        public void setCardNo(String cardNo) {
            CardNo = cardNo;
        }

        public String getOpenBank() {
            return OpenBank;
        }

        public void setOpenBank(String openBank) {
            OpenBank = openBank;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }
    }
}
