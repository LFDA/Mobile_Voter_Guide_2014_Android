package com.voterguide;

import java.util.ArrayList;


import java.util.Locale;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.voterguide.helpers.DataHelper;
import com.voterguide.model.Candidate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link SearchFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link SearchFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class SearchFragment extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 *
	 * 
	 * @return A new instance of fragment CandidatesFragment.
	 */
	private ListView lv;

	// Listview Adapter
	ListViewAdapter adapter;
	Activity activity = null;

	// Search EditText
	EditText inputSearch;
	
	ArrayList<Candidate> candidateList = new ArrayList<Candidate>();

	// ArrayList for Listview

	// TODO: Rename and change types and number of parameters
	public static SearchFragment newInstance(int sectionNumber) {
		SearchFragment fragment = new SearchFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public SearchFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_search, container,
				false);

		candidateList = DataHelper.getAllCandidateList(activity);

		lv = (ListView) rootView.findViewById(R.id.list_view);
		inputSearch = (EditText) rootView.findViewById(R.id.inputSearch);

		// Pass results to ListViewAdapter Class
				adapter = new ListViewAdapter(activity, candidateList);
		lv.setAdapter(adapter);

		/**
		 * Enabling Search Filter
		 * */
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
				adapter.filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				// TODO Auto-generated method stub
				//Log.d("candidateList","candidate name ==> "+candidateList.get(index).getFirstName());
				Candidate candidate = candidateList.get(index);
				Intent detailPageActivity = new Intent(activity,DetailPageActivity.class);
				detailPageActivity.putExtra("candidate", candidate);  
				startActivity(detailPageActivity);
			}
		});

		return rootView;
	}

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first


    }

    private void trackView() {
        // Get tracker.
        Tracker t = ((MyApplication) getActivity().getApplication()).getTracker();
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("Search");
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
		Log.e("sections", "onAttach called");
		
	}

}
