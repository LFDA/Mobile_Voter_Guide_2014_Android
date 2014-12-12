package com.voterguide;

import java.util.List;

import com.voterguide.model.Position;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IssuesListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Position> issueList = null;

	public IssuesListViewAdapter(Context context, List<Position> issueList) {
		mContext = context;
		this.issueList = issueList;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return issueList.size();
	}

	@Override
	public Position getItem(int index) {
		return issueList.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	public View getView(final int index, View view, ViewGroup parent) {
		View convertView = view;
		if (view == null) {
			convertView = inflater.inflate(R.layout.issues_list_item, null);
		}
		Position position = issueList.get(index);
		Log.i("Issue", " position " + position);
			TextView title = (TextView) convertView.findViewById(R.id.issue_title_string);
			TextView subtitle = (TextView) convertView.findViewById(R.id.issue_sub_title_string);
			ImageView issueImage = (ImageView) convertView.findViewById(R.id.issue_status);
			// Set the results into TextViews
			title.setText(position.getIssue());
			String positionStatus = (position.getPosition() == null) ? "" : position.getPosition();
			subtitle.setText("("+positionStatus+")");
			Log.i("position"," status ==> "+positionStatus);
			int positionImage = R.drawable.issue_noresponse;
			
			if (positionStatus.equalsIgnoreCase("NO RESPONSE")) {
				positionImage = R.drawable.issue_noresponse;
			} else if (positionStatus.equalsIgnoreCase("AGAINST")) {
				positionImage = R.drawable.issue_thumbsdown;
			} else if (positionStatus.equalsIgnoreCase("FOR")) {
				positionImage = R.drawable.issue_thumbup;
			} else if (positionStatus.equalsIgnoreCase("UNDECIDED")) {
				positionImage = R.drawable.issue_undecided;
			}
			
			issueImage.setImageResource(positionImage);
		// issueImage.
		// Listen for ListView Item Click

		return convertView;
	}

}
