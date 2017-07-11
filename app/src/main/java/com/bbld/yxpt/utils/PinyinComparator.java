package com.bbld.yxpt.utils;


import com.bbld.yxpt.bean.CityList;

import java.util.Comparator;

public class PinyinComparator implements Comparator<CityList.CityListlist.CityListCityList> {

	public int compare(CityList.CityListlist.CityListCityList o1, CityList.CityListlist.CityListCityList o2) {
		if (o1.getHead().equals("@")
				|| o2.getHead().equals("#")) {
			return -1;
		} else if (o1.getHead().equals("#")
				|| o2.getHead().equals("@")) {
			return 1;
		} else {
			return o1.getHead().compareTo(o2.getHead());
		}
	}

}
