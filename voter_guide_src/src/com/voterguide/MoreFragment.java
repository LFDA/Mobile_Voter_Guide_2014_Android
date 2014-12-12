package com.voterguide;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.voterguide.helpers.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoreFragment extends Fragment implements OnClickListener{
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	Activity activity = null;
	String supportContent = "";
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static MoreFragment newInstance(int sectionNumber) {
		MoreFragment fragment = new MoreFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public MoreFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_more, container,
				false);
		// NH Voter Guide 2014 v1.0 (Android) \n DB MD5: \n
		// 645454fg45g1f5gfg1d4fdfd4fd45fd
		PackageInfo pInfo;
		
		try {
			SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("com.voterguide", Context.MODE_PRIVATE);
			pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
			supportContent = getString(R.string.app_name) + " v"+ pInfo.versionName + " (" + pInfo.versionCode + ")" + " for Android " + " \nDB MD5: \n"+ pref.getString("dataMD5","");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((TextView) rootView.findViewById(R.id.support_content)).setText(supportContent);
		
		View voterResourceView = rootView.findViewById(R.id.voter_resources);
		voterResourceView.setOnClickListener(this);
		
		View votingRightsView = rootView.findViewById(R.id.voting_rights_poster);
		votingRightsView.setOnClickListener(this);
		
		View feedbackView = rootView.findViewById(R.id.feedback_button);
		feedbackView.setOnClickListener(this);

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
        t.setScreenName("More");
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }
	
	public DialogInterface.OnClickListener voterResourceDialogListener (){

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getString(R.string.lfda_voter_resources_url)));
					startActivity(browserIntent);
					break;
				}
			}
		};
		
		return dialogClickListener;
	}
	
	public DialogInterface.OnClickListener votingRightsDialogListener (){

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getString(R.string.sos_voting_rights_urlL)));
					startActivity(browserIntent);
					break;
				}
			}
		};
		
		return dialogClickListener;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;

	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		
		switch (viewId) {
		case R.id.voter_resources:
			Utils.alertMessage(activity, "Do you want to view voter resources in the browser?", voterResourceDialogListener());
			break;
			
		case R.id.voting_rights_poster:
			Utils.alertMessage(activity, "Do you want to view voting rights poster in the browser?", votingRightsDialogListener());
			break;
		
		case R.id.feedback_button:
			openEmailDialog();
			break;

		default:
			break;
		}
		
	}
	
	public void openEmailDialog(){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setData(Uri.parse("mailto:"));
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.feed_back_recipient)});
		intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feed_back_subject));
		String emailBody = String.format( getString(R.string.feed_back_body),supportContent);
		intent.putExtra(Intent.EXTRA_TEXT,emailBody);
		startActivity(Intent.createChooser(intent, getString(R.string.feed_back_title)));
	}
}
