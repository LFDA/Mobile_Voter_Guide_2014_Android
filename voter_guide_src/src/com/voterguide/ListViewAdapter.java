package com.voterguide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.voterguide.model.Candidate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Candidate> candidateList = null;
	private ArrayList<Candidate> arraylist;

	public ListViewAdapter(Context context,
			List<Candidate> candidateList) {
		mContext = context;
		this.candidateList = candidateList;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Candidate>();
		this.arraylist.addAll(candidateList);
	}


	@Override
	public int getCount() {
		return candidateList.size();
	}

	@Override
	public Candidate getItem(int position) {
		return candidateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		View convertView = view;
		if (view == null) {
			convertView = inflater.inflate(android.R.layout.simple_list_item_2, null);
			// Locate the TextViews in listview_item.xml
		}
		
		Candidate candidate =  candidateList.get(position);
		TextView title = (TextView) convertView.findViewById(android.R.id.text1);
		TextView subtitle = (TextView) convertView.findViewById(android.R.id.text2);
		
		String rowValue = candidate.getFirstName()+" "+candidate.getLastName()+" ("+candidate.getParty().toUpperCase()+")";
		String subTitleValue = "";
		if(candidate.isIncumbent() && candidate.isFloterial()){
			subTitleValue = "(Incumbent, Floterial)";
		}else if(candidate.isIncumbent()){
			subTitleValue = "(Incumbent)";
		}else if(candidate.isFloterial()){
			subTitleValue = "(Floterial)";
		}
		// Set the results into TextViews
		title.setText(rowValue);
		subtitle.setText(candidate.getOffice());
		// Listen for ListView Item Click
		
		return convertView;
	}

	// Filter Class
	public void filter(String charText) {
		String searchKey = charText.toLowerCase(Locale.getDefault());
		
		candidateList.clear();
		if (charText.length() == 0) {
			candidateList.addAll(arraylist);
		} else {
			for (Candidate ca : arraylist) {
				if (ca.getFirstName().toLowerCase(Locale.getDefault()).contains(searchKey) || (ca.getLastName().toLowerCase(Locale.getDefault()).contains(searchKey))) {
					candidateList.add(ca);
				}
			}
		}
		notifyDataSetChanged();
	}

}
