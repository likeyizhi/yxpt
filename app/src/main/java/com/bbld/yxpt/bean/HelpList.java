package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 使用指南
 * Created by likey on 2017/7/3.
 */

public class HelpList {
    /**"status": 0,
     "mes": "操作成功",
     "count": 4,
     "list": []*/
    private int status;
    private String mes;
    private int count;
    private List<HelpListlist> list;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<HelpListlist> getList() {
        return list;
    }

    public void setList(List<HelpListlist> list) {
        this.list = list;
    }

    public static class HelpListlist{
        /** "ID": 1,
         "Title": "成为企业用户需要哪些条件？（如何成为企业用户？）"*/
        private int ID;
        private String Title;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }
    }
}
