package com.voterguide;

import java.util.ArrayList;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.voterguide.helpers.DataHelper;
import com.voterguide.model.Candidate;
import com.voterguide.model.Position;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class IssuesOnProfileActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_issues_on_profile);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		ListView issueListView = (ListView) findViewById(R.id.issue_listview);

		Candidate candidate = (Candidate) getIntent().getSerializableExtra("candidate");
		String rowValue = candidate.getFirstName() + " "+ candidate.getLastName() + " ("+ candidate.getParty().toUpperCase() + ")";
		((TextView) findViewById(R.id.issues_candidate_name)).setText(rowValue);
		ArrayList<Position> issueList = DataHelper.getPositionListForCandidate(this, candidate);
		IssuesListViewAdapter issueListViewAdapter = new IssuesListViewAdapter(this, issueList);
		issueListView.setAdapter(issueListViewAdapter);
		
		TextView issueTitle =((TextView) findViewById(R.id.issues_title));
		issueTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.issue_title_url)));
				startActivity(browserIntent);
			}
		});
		
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
        t.setScreenName("CandidatePositions");
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.issues_on_profile, menu);
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

}
