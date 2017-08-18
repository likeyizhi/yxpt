package com.bbld.yxpt.dbSearch;

/**
 * 搜索数据库
 * Created by likey on 2017/7/10.
 */

public class SearchDBBean {
    private String name;
    private String addr;
    private String pointX;
    private String pointY;
    private int type;

    public String getPointX() {
        return pointX;
    }

    public void setPointX(String pointX) {
        this.pointX = pointX;
    }

    public String getPointY() {
        return pointY;
    }

    public void setPointY(String pointY) {
        this.pointY = pointY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
