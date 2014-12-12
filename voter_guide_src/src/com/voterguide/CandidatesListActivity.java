package com.voterguide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.voterguide.helpers.DataHelper;
import com.voterguide.model.Candidate;
import com.voterguide.model.City;
import com.voterguide.model.Office;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.HitBuilders;

public class CandidatesListActivity extends Activity {

	City city = null;
	HashMap<String, ArrayList<Candidate>> cityCandidateListFilteredForOffice = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_candidates_list);

		HeaderListView list = (HeaderListView) findViewById(R.id.header_listview);

		if (city == null) {
			city = this.getIntent().getParcelableExtra("city");
			cityCandidateListFilteredForOffice = DataHelper
					.loadCityCandidatesTableViewForCity(this, city);
		}
		
		String cityTitle = city.getCityName() + " ("+city.getCountyName()+")";		
		((TextView) findViewById(R.id.city_title)).setText(cityTitle);

		ArrayList<Office> OfficeList = DataHelper.getOfficeList(this);
		List<String> sectionsList = new ArrayList<String>();
		for(Office office: OfficeList){
			sectionsList.add(office.getOfficeName());
		}

		// for (String key: cityCandidateListFilteredForOffice.keySet()) {
		// sectionsList.add(key);
		// }
		final List<String> sections = sectionsList;

		list.setAdapter(new SectionAdapter() {

			@Override
			public int numberOfSections() {
				return sections.size();
			}

			@Override
			public int numberOfRows(int section) {
				if (section < 0)
					return 0;
				return cityCandidateListFilteredForOffice.get(
						sections.get(section)).size();
			}

			@Override
			public Object getRowItem(int section, int row) {
				return null;
			}

			@Override
			public boolean hasSectionHeaderView(int section) {
				return true;
			}

			@Override
			public View getRowView(int section, int row, View convertView,
					ViewGroup parent) {
				if (convertView == null) {
					convertView = getLayoutInflater().inflate(getResources().getLayout(android.R.layout.simple_list_item_2), null);
				}
				Candidate candidate = cityCandidateListFilteredForOffice.get(sections.get(section)).get(row);
				String rowValue = candidate.getFirstName();
				if (candidate.getLastName() != null) {
					rowValue += " " + candidate.getLastName();
				}
				if (candidate.getParty() != null)
					rowValue += " (" + candidate.getParty().toUpperCase() + ")";

				String subTitleValue = "";
				if (candidate.isIncumbent() && candidate.isFloterial()) {
					subTitleValue = "(Incumbent, Floterial)";
				} else if (candidate.isIncumbent()) {
					subTitleValue = "(Incumbent)";
				} else if (candidate.isFloterial()) {
					subTitleValue = "(Floterial)";
				}
				TextView rowTitle = (TextView) convertView
						.findViewById(android.R.id.text1);
				TextView subTitle = (TextView) convertView
						.findViewById(android.R.id.text2);
				if (candidate.getLastName() == null
						&& candidate.getParty() == null) {
					rowTitle.setTextColor(Color.GRAY);
					rowTitle.setGravity(Gravity.CENTER);
					
				} else {
					rowTitle.setTextColor(Color.BLACK);
					rowTitle.setGravity(Gravity.LEFT);
				}

				rowTitle.setText(rowValue);
				subTitle.setText(subTitleValue);
				return convertView;
			}

			@Override
			public int getSectionHeaderViewTypeCount() {
				return 2;
			}

			@Override
			public int getSectionHeaderItemViewType(int section) {
				return section % 2;
			}

			@Override
			public View getSectionHeaderView(int section, View convertView,	ViewGroup parent) {

				if (convertView == null) {
					convertView =  getLayoutInflater().inflate(getResources().getLayout(R.layout.list_section_header), null);
				}
				TextView sectionTitle = (TextView) convertView.findViewById(R.id.section_title);
				sectionTitle.setText(sections.get(section));
				convertView.setBackgroundColor(getResources().getColor(R.color.tab_unselected));

				return convertView;
			}

			@Override
			public void onRowItemClick(AdapterView<?> parent, View view,
					int section, int row, long id) {
				super.onRowItemClick(parent, view, section, row, id);

				// Log.d("candidateList","candidate name ==> "+cityCandidateListFilteredForOffice.get(sections.get(section)).get(row).getFirstName());
				Candidate candidate = cityCandidateListFilteredForOffice.get(
						sections.get(section)).get(row);
				if (candidate.getFirstName() != null
						&& candidate.getParty() != null) {
					Intent detailPageActivity = new Intent(
							CandidatesListActivity.this,
							DetailPageActivity.class);
					detailPageActivity.putExtra("candidate", candidate);
					detailPageActivity.putExtra("city", city);
					startActivity(detailPageActivity);
				}

			}
		});
		//setContentView(list);

	}

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        trackView();

    }

    private void trackView() {
        // Get tracker.
        Tracker t = ((MyApplication) getApplication()).getTracker();
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("CandidatesForCityList");
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
