package com.bbld.yxpt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.bean.CityList;

import java.util.ArrayList;
import java.util.Locale;


public class LoactionCityAdapter extends BaseAdapter implements
        SectionIndexer {
	private Context context;
	private ArrayList<CityList.CityListlist.CityListCityList> brands;
	private LayoutInflater inflater;
	private ViewHolder holder;

	public LoactionCityAdapter(Context context,
							   ArrayList<CityList.CityListlist.CityListCityList> brands) {
		this.context = context;
		this.brands = brands;
		this.inflater = LayoutInflater.from(context);
	}

	public void updateListView(ArrayList<CityList.CityListlist.CityListCityList> brands){
		this.brands = brands;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return brands.size();
	}

	@Override
	public CityList.CityListlist.CityListCityList getItem(int position) {
		return brands.get(position);
	}

	@Override
	public long getItemId(int position) {
		return brands.get(position).getID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_search_location_citylist, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_pinyin = (TextView) convertView
					.findViewById(R.id.tv_pinyin);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		final CityList.CityListlist.CityListCityList brand = getItem(position);
		holder.tv_name.setText(brand.getName2());
		holder.tv_pinyin.setText(brand.getHead());
		if (position == getPositionForSection(getSectionForPosition(position))) {
			holder.tv_pinyin.setVisibility(View.VISIBLE);
		} else {
			holder.tv_pinyin.setVisibility(View.GONE);
		}
		if (brands.get(position) != null) {
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (onItemClickSetChangeCity != null) {
						onItemClickSetChangeCity.OnItemClickSetChangeCity(brand.getID(),brand.getName2(),brand.getX(),brand.getY());
					}
				}
			});
		}
		return convertView;
	}

	@Override
	public int getSectionForPosition(int position) {
		return brands.get(position).getHead().toUpperCase(Locale.CHINA).charAt(0);
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < brands.size(); i++) {
			int ch = getSectionForPosition(i);
			if (ch == section) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	public interface OnItemClickSetChangeCity {
		void OnItemClickSetChangeCity(int id, String name, String x, String y);
	}
	private OnItemClickSetChangeCity onItemClickSetChangeCity;

	public void setOnItemClickSetChangeCity(OnItemClickSetChangeCity onItemClickSetChangeCity) {
		this.onItemClickSetChangeCity = onItemClickSetChangeCity;
	}
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}
	class ViewHolder {
		TextView tv_name;
		TextView tv_pinyin;
	}
}
