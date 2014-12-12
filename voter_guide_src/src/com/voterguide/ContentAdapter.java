package com.voterguide;

import java.util.List;

import com.voterguide.model.City;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ContentAdapter extends ArrayAdapter<City> implements
		SectionIndexer {

	private String mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static LayoutInflater inflater = null;
	private Activity activity;
	private List<City> cityList = null;
	private int listItemResId;

	public ContentAdapter(Activity activity, int listItemResId,
			List<City> cityList) {
		super(activity, listItemResId, cityList);
		this.listItemResId = listItemResId;
		this.cityList = cityList;
		this.activity = activity;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		// If there is no item for current section, previous section will be
		// selected
		for (int i=0; i < this.getCount(); i++) {
            String item = this.getItem(i).getCityName().toUpperCase();
            if (item.charAt(0) == mSections.charAt(sectionIndex))
                return i;
        }
        return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}

	@Override
	public View getView(int index, View container, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vi = container;
		if (container == null)
			vi = inflater.inflate(listItemResId, null);

		TextView cityName = (TextView) vi.findViewById(android.R.id.text1);
		City city = cityList.get(index);
		cityName.setText(city.getCityName());
		vi.setTag(city);
		return vi;
	}

}
