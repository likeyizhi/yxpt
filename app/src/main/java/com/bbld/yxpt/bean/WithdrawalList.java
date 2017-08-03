package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 已提现金额
 * Created by likey on 2017/8/2.
 */

public class WithdrawalList {
    /**"status": 0,
     "mes": "操作成功",
     "total": "0.50",
     "list": []*/
    private int status;
    private String mes;
    private String total;
    private List<WithdrawalListlist> list;

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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<WithdrawalListlist> getList() {
        return list;
    }

    public void setList(List<WithdrawalListlist> list) {
        this.list = list;
    }

    public static class WithdrawalListlist{
        /**"BankName": "中国工商银行",
         "BankLogo": "http://qyyxptapi.bjqydl.com/images/2.png",
         "CardNo": "6222024288869853654",
         "OpenBank": "",
         "Name": "侯卧恰",
         "WithdrawalMoney": "0.50",
         "ServiceMoney": "0.50",
         "IsFinish": "0",
         "Status": "提现处理中",
         "AddDate": "2017-07-31 15:13"*/
        private String BankName;
        private String BankLogo;
        private String CardNo;
        private String OpenBank;
        private String Name;
        private String WithdrawalMoney;
        private String ServiceMoney;
        private String IsFinish;
        private String Status;
        private String AddDate;

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

        public String getWithdrawalMoney() {
            return WithdrawalMoney;
        }

        public void setWithdrawalMoney(String withdrawalMoney) {
            WithdrawalMoney = withdrawalMoney;
        }

        public String getServiceMoney() {
            return ServiceMoney;
        }

        public void setServiceMoney(String serviceMoney) {
            ServiceMoney = serviceMoney;
        }

        public String getIsFinish() {
            return IsFinish;
        }

        public void setIsFinish(String isFinish) {
            IsFinish = isFinish;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }
    }
}
