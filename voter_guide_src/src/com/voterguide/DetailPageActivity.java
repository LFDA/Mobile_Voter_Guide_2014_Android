package com.voterguide;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.voterguide.helpers.DataHelper;
import com.voterguide.helpers.Utils;
import com.voterguide.model.Candidate;
import com.voterguide.model.City;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailPageActivity extends ActionBarActivity implements
		OnClickListener {
	Candidate candidate = null;
	City city = null;
	String candidateName = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_page);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		candidate = (Candidate) getIntent().getSerializableExtra("candidate");
		city = getIntent().getParcelableExtra("city");
		candidate = DataHelper.getCandidateDetail(this, candidate);
		ImageView profileImage = (ImageView) findViewById(R.id.profile_image);
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.nophoto) // resource or drawable
				.showImageForEmptyUri(R.drawable.nophoto) // resource or
				.showImageOnFail(R.drawable.nophoto) // resource or drawable
				.cacheInMemory(true).build();
		imageLoader.displayImage(candidate.getLfdaPhotoUrl(), profileImage,
				options);

		String rowValue = candidate.getFirstName() + " "
				+ candidate.getLastName() + " ("
				+ candidate.getParty().toUpperCase() + ")";
		candidateName = rowValue;
		((TextView) findViewById(R.id.candidate_name)).setText(rowValue);
		((TextView) findViewById(R.id.candidate_office)).setText("Office: "
				+ candidate.getOffice());
		((TextView) findViewById(R.id.candidate_incumbent))
				.setText("Incumbent: "
						+ ((candidate.isIncumbent()) ? "Yes" : "No"));
		String distrinctString = "";

		if (!candidate.getOffice().equals("Governor")
				&& candidate.getDistrict() != null
				&& !candidate.getDistrict().isEmpty()) {
			distrinctString = "Distr: " + candidate.getDistrict();
			if (city != null) {
				distrinctString += ", " + city.getCityName() + " ("
						+ city.getCountyName() + ")";
			} else if (candidate.getCounty() != null
					&& !candidate.getCounty().isEmpty()) {
				distrinctString += ", " + candidate.getCounty();
			}

		}
		((TextView) findViewById(R.id.candidate_distr))
				.setText(distrinctString);

		String experience = (candidate.getExperience() != null && !candidate
				.getExperience().isEmpty()) ? candidate.getExperience() : getString(R.string.no_info_available);
		String education = (candidate.getEducation() != null && !candidate
				.getEducation().isEmpty()) ? candidate.getEducation() : getString(R.string.no_info_available);
		String family = (candidate.getFamily() != null && !candidate
				.getFamily().isEmpty()) ? candidate.getFamily() : getString(R.string.no_info_available);
		String email = (candidate.getEmail() != null && !candidate.getEmail()
				.isEmpty()) ? candidate.getEmail() : getString(R.string.no_info_available);
		String phone = (candidate.getPhone() != null && !candidate.getPhone()
				.isEmpty()) ? candidate.getPhone() : getString(R.string.no_info_available);
		String website = (candidate.getWebsite() != null && !candidate
				.getWebsite().isEmpty()) ? candidate.getWebsite() : getString(R.string.no_info_available);

		((TextView) findViewById(R.id.experience_value)).setText(experience);
		((TextView) findViewById(R.id.education_value)).setText(education);
		((TextView) findViewById(R.id.family_value)).setText(family);
		((TextView) findViewById(R.id.email_value)).setText(email);
		((TextView) findViewById(R.id.phone_value)).setText(phone);
		((TextView) findViewById(R.id.website_value)).setText(website);

		View viewIssuesOnPosition = findViewById(R.id.view_position_view);
		viewIssuesOnPosition.setOnClickListener(this);

		View reportDataError = findViewById(R.id.info_quality_view);
		reportDataError.setOnClickListener(this);

		View viewProfileInBrowser = findViewById(R.id.view_full_profile_view);
		viewProfileInBrowser.setOnClickListener(this);

		ImageView websiteButton = (ImageView) findViewById(R.id.visit_website);
		ImageView copyWebsiteButton = (ImageView) findViewById(R.id.copy_website);
		if (candidate.getWebsite() != null && !candidate.getWebsite().isEmpty()) {
			websiteButton.setOnClickListener(this);
			copyWebsiteButton.setOnClickListener(this);
		} else {
			websiteButton.setColorFilter(Color.GRAY);
			copyWebsiteButton.setColorFilter(Color.GRAY);
		}
		
		
		ImageView emailButton = (ImageView) findViewById(R.id.send_email);
		ImageView copyEmailButton = (ImageView) findViewById(R.id.copy_email);
		if (candidate.getEmail() != null && !candidate.getEmail().isEmpty()) {
			emailButton.setOnClickListener(this);
			copyEmailButton.setOnClickListener(this);
		} else {
			emailButton.setColorFilter(Color.GRAY);
			copyEmailButton.setColorFilter(Color.GRAY);
		}

		ImageView callButton = (ImageView) findViewById(R.id.call_button);
		if (candidate.getPhone() != null && !candidate.getPhone().isEmpty()) {
			View callWrapper = findViewById(R.id.call_button_wrapper);
			callWrapper.setOnClickListener(this);
		} else {
			callButton.setColorFilter(Color.GRAY);
		}

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
        t.setScreenName("CandidateProfile");
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.detail_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();
		switch (viewId) {
		case R.id.view_position_view:
			Intent viewPositionActivity = new Intent(DetailPageActivity.this,
					IssuesOnProfileActivity.class);
			viewPositionActivity.putExtra("candidate", candidate);
			startActivity(viewPositionActivity);
			break;

		case R.id.view_full_profile_view:
			Utils.alertMessage(DetailPageActivity.this,
					getString(R.string.exit_to_profile_prompt),
					viewProfileInBrowserDialogListener());
			break;

		case R.id.visit_website:
			Utils.alertMessage(DetailPageActivity.this,
					getString(R.string.exit_to_website_promote),
					viewWebsiteDialogListener());
			break;

		case R.id.copy_website:
			ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("", candidate.getWebsite());
			clipboard.setPrimaryClip(clip);
			Toast toast = Toast.makeText(this, R.string.copy_website, 500);
			toast.show();
			break;
			
		case R.id.send_email:
			openCandidateEmailDialog();
			break;

		case R.id.copy_email:
			ClipboardManager copyclipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData copyclip = ClipData.newPlainText("", candidate.getEmail());
			copyclipboard.setPrimaryClip(copyclip);
			Toast copytoast = Toast.makeText(this, R.string.copy_email, 500);
			copytoast.show();
			break;

		case R.id.call_button_wrapper:
			
			if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
				String callPhonePrompt = String.format(getString(R.string.call_phone_prompt),	candidate.getPhone());
		    	 Utils.alertMessage(DetailPageActivity.this, callPhonePrompt,clickToCallDialogListener());
		     }else{
		    		Toast callToast = Toast.makeText(this, R.string.call_not_supported_on_device, 2000);
			    	callToast.show();
		     }
			break;

		case R.id.info_quality_view:
			openDataReportEmailDialog();
			break;
		}
	}

	public DialogInterface.OnClickListener viewProfileInBrowserDialogListener() {

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Intent browserIntent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(candidate.getLfdaProfileUrl()));
					startActivity(browserIntent);
					break;
				}
			}
		};

		return dialogClickListener;
	}

	public DialogInterface.OnClickListener clickToCallDialogListener() {

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					 String phoneString = candidate.getPhone();
					 phoneString = phoneString.replaceAll("[^-?0-9]+", ""); 
					 Log.i("call","phone string =="+phoneString);
					callIntent.setData(Uri.parse("tel:"+ phoneString));
					startActivity(callIntent);
					break;
				}
			}
		};

		return dialogClickListener;
	}

	public DialogInterface.OnClickListener viewWebsiteDialogListener() {

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Intent browserIntent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(candidate.getWebsite()));
					startActivity(browserIntent);
					break;
				}
			}
		};

		return dialogClickListener;
	}

	public void openDataReportEmailDialog() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		//intent.setData(Uri.parse("mailto:"));
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { getString(R.string.report_error_recipient) });
		intent.putExtra(Intent.EXTRA_SUBJECT,
				getString(R.string.report_error_subject));
		String emailBody = String.format(getString(R.string.report_error_body),
				candidateName);
		intent.putExtra(Intent.EXTRA_TEXT, emailBody);
		startActivity(Intent.createChooser(intent, "Report Data Error"));
	}
	
	public void openCandidateEmailDialog() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setData(Uri.parse("mailto:"));
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL,	new String[] {candidate.getEmail() });
		intent.putExtra(Intent.EXTRA_SUBJECT,"");
		intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_candidate_body));
		startActivity(Intent.createChooser(intent, "Send mail to candidate"));
	}
}
