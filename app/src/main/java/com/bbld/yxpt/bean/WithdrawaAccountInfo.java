package com.bbld.yxpt.bean;

/**
 * 默认提现银行卡
 * Created by likey on 2017/7/11.
 */

public class WithdrawaAccountInfo {
    /**"status": 0,
     "mes": "操作成功",
     "accountMoney": "84.80",
     "cardinfo": {}*/
    private int status;
    private String mes;
    private String accountMoney;
    private WithdrawaAccountInfocardinfo cardinfo;

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

    public String getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(String accountMoney) {
        this.accountMoney = accountMoney;
    }

    public WithdrawaAccountInfocardinfo getCardinfo() {
        return cardinfo;
    }

    public void setCardinfo(WithdrawaAccountInfocardinfo cardinfo) {
        this.cardinfo = cardinfo;
    }

    public static class WithdrawaAccountInfocardinfo{
        /** "HasCard": 1,
         "BankCardID": 10,
         "BankName": "中国建设银行",
         "BankLogo": "http://qyyxptapi.bjqydl.com/images/4.png",
         "CardNo": "6227001021820604468",
         "OpenBank": "中国建设银行",
         "Name": "聂贺"*/
        private int HasCard;
        private int BankCardID;
        private String BankName;
        private String BankLogo;
        private String CardNo;
        private String OpenBank;
        private String Name;

        public int getHasCard() {
            return HasCard;
        }

        public void setHasCard(int hasCard) {
            HasCard = hasCard;
        }

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
    }
}
