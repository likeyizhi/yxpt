package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 卡号识别银行
 * Created by likey on 2017/7/11.
 */

public class BankCardRecognition {
    /**"status": 0,
     "mes": "操作成功",
     "list": []*/
    private int status;
    private String mes;
    private List<BankCardRecognitionlist> list;

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

    public List<BankCardRecognitionlist> getList() {
        return list;
    }

    public void setList(List<BankCardRecognitionlist> list) {
        this.list = list;
    }

    public static class BankCardRecognitionlist{
        /** "BankID": 14,
         "BankName": "上海浦东发展银行 ",
         "BankLogo": "http://qyyxptapi.bjqydl.com/images/14.png",
         "CardType": "借记卡",
         "CardName": "东方卡(银联卡)",
         "CardLength": "16",
         "CardNo": "622521xxxxxxxxxx",
         "HeadNo": "622521",
         "HeadLength": "6"*/
        private int BankID;
        private String BankName;
        private String BankLogo;
        private String CardType;
        private String CardName;
        private String CardLength;
        private String CardNo;
        private String HeadNo;
        private String HeadLength;

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

        public String getCardType() {
            return CardType;
        }

        public void setCardType(String cardType) {
            CardType = cardType;
        }

        public String getCardName() {
            return CardName;
        }

        public void setCardName(String cardName) {
            CardName = cardName;
        }

        public String getCardLength() {
            return CardLength;
        }

        public void setCardLength(String cardLength) {
            CardLength = cardLength;
        }

        public String getCardNo() {
            return CardNo;
        }

        public void setCardNo(String cardNo) {
            CardNo = cardNo;
        }

        public String getHeadNo() {
            return HeadNo;
        }

        public void setHeadNo(String headNo) {
            HeadNo = headNo;
        }

        public String getHeadLength() {
            return HeadLength;
        }

        public void setHeadLength(String headLength) {
            HeadLength = headLength;
        }
    }
}
