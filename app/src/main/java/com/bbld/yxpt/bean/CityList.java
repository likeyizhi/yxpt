package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 城市列表
 * Created by likey on 2017/7/6.
 */

public class CityList {
    /**"status": 0,
     "mes": "操作成功",
     "list": []*/
    private int status;
    private String mes;
    private List<CityListlist> list;

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

    public List<CityListlist> getList() {
        return list;
    }

    public void setList(List<CityListlist> list) {
        this.list = list;
    }

    public static class CityListlist{
        /**"Key": "A",
         "CityList": []*/
        private String Key;
        private List<CityListCityList> CityList;

        public String getKey() {
            return Key;
        }

        public void setKey(String key) {
            Key = key;
        }

        public List<CityListCityList> getCityList() {
            return CityList;
        }

        public void setCityList(List<CityListCityList> cityList) {
            CityList = cityList;
        }

        public static class CityListCityList{
            /**"ID": 492,
             "Head": "A",
             "Name": "鞍山",
             "Name2": "鞍山市",
             "X": "122.995632",
             "Y": "41.110626"*/
            private int ID;
            private String Head;
            private String Name;
            private String Name2;
            private String X;
            private String Y;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getHead() {
                return Head;
            }

            public void setHead(String head) {
                Head = head;
            }

            public String getName() {
                return Name;
            }

            public void setName(String name) {
                Name = name;
            }

            public String getName2() {
                return Name2;
            }

            public void setName2(String name2) {
                Name2 = name2;
            }

            public String getX() {
                return X;
            }

            public void setX(String x) {
                X = x;
            }

            public String getY() {
                return Y;
            }

            public void setY(String y) {
                Y = y;
            }
        }
    }
}
