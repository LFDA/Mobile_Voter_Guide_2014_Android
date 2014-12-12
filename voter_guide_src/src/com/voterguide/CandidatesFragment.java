package com.voterguide;

import java.util.List;

import com.voterguide.helpers.DataHelper;
import com.voterguide.model.City;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link CandidatesFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link CandidatesFragment#newInstance} factory method to create an instance
 * of this fragment.
 *
 */
public class CandidatesFragment extends Fragment{

	private static final String ARG_SECTION_NUMBER = "section_number";
	View rootView = null;
	IndexableListView listView = null;
	Activity activity = null;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 *
	 * 
	 * @return A new instance of fragment CandidatesFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static CandidatesFragment newInstance(int sectionNumber) {
		CandidatesFragment fragment = new CandidatesFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public CandidatesFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("sections", "onCreate called");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		
		//Tab bar color : #668CD7
		//Actionbar color : #668CD7
		Log.e("sections", "oncreateView called");
		
		
		rootView = inflater.inflate(R.layout.fragment_candidates, container,
				false);
		listView = (IndexableListView) rootView.findViewById(R.id.fast_listview);
		List<City> cityList = DataHelper.getCityList(activity);


		
		 ContentAdapter adapter = new ContentAdapter(activity,
	                android.R.layout.simple_list_item_1, cityList);
		listView.setAdapter(adapter);
		listView.setFastScrollEnabled(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View row, int index,
					long arg3) {
				Log.i("candidate", "onItemclicked ==>");
				City city = (City) row.getTag();
				if(city!=null){
					Intent demoActivity = new Intent(activity.getApplicationContext(),CandidatesListActivity.class);
					demoActivity.putExtra("city", city);
					activity.startActivity(demoActivity);
				}
				
				
			}
		});
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
		Log.e("sections", "onAttach called");
		
	}

	

}
