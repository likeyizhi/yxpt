package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 我的消息
 * Created by likey on 2017/7/3.
 */

public class MessageList {
    /**"status": 0,
     "mes": "操作成功",
     "count": 0,
     "list": []*/
    private int status;
    private String mes;
    private int count;
    private List<MessageListlist> list;

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

    public List<MessageListlist> getList() {
        return list;
    }

    public void setList(List<MessageListlist> list) {
        this.list = list;
    }

    public static class MessageListlist{
        /**"Title": "支付成功",
         "Content": "您已经成功付款398元，订单号（12398123812），您前面还有201为用户待返还。",
         "Time": "2017-07-03 15:42"*/
        private String Title;
        private String Content;
        private String Time;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String content) {
            Content = content;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String time) {
            Time = time;
        }
    }
}
