package com.voterguide;

import java.util.Locale;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.

		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		// setTranslucentStatus(true);
		// }
		final ActionBar actionBar = getSupportActionBar();
		// actionBar.setHomeButtonEnabled(true);
		// actionBar.setDisplayShowTitleEnabled(true);
		// actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		// actionBar.setCustomView(R.layout.abs_layout);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// enable status bar tint
		tintManager.setStatusBarTintEnabled(true);
		// enable navigation bar tint
		tintManager.setNavigationBarTintEnabled(true);

		// tintManager.setTintColor(Color.parseColor("#668CD7"));
		// set a custom navigation bar resource
		tintManager.setNavigationBarTintResource(R.color.tab_unselected);
		// set a custom status bar drawable

		// ColorDrawable cd = new ColorDrawable(R.color.tab_unselected);
		// tintManager.setStatusBarTintDrawable(cd);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		int[] tabIcons = { R.drawable.candidate, R.drawable.search,
				R.drawable.more };
		int[] tabTitles = { R.string.tab1_title, R.string.tab2_title,
				R.string.tab3_title };
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			View view = prepareTabView(this, tabTitles[i], tabIcons[i]);
			actionBar.addTab(actionBar.newTab().setCustomView(view)
					.setTabListener(this));
		}

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        trackView("CityList");

    }

    private void trackView(String screenName) {
        // Get tracker.
        Tracker t = ((MyApplication) getApplication()).getTracker();
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(screenName);
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

	private View prepareTabView(Context context, int textId, int drawable) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tab_bar, null);
		ImageView tabImage = (ImageView) view.findViewById(R.id.tab_icon);
		TextView tabTitle = (TextView) view.findViewById(R.id.tab_title);
		tabImage.setImageResource(drawable);
		tabTitle.setText(textId);
		// setting text and image
		// ...
		// Write your own code here

		return view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		View focus = getCurrentFocus();
		if (focus != null) {
			hiddenKeyboard(focus);
		}
		ImageView tabIcon = (ImageView) tab.getCustomView().findViewById(
				R.id.tab_icon);
		TextView tabTitle = (TextView) tab.getCustomView().findViewById(
				R.id.tab_title);
		tabIcon.setColorFilter(Color.BLACK);
		tabTitle.setTextColor(Color.BLACK);

        // Track a view if SEARCH or MORE
        String screenName="";
        switch (tab.getPosition()) {
            case 0:
                //screenName="CityList";
                break;

            case 1:
                screenName="Search";
                break;

            case 2:
                screenName="More";
                break;
        }
        if (screenName!="") trackView(screenName);

		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		ImageView tabIcon = (ImageView) tab.getCustomView().findViewById(
				R.id.tab_icon);
		TextView tabTitle = (TextView) tab.getCustomView().findViewById(
				R.id.tab_title);

		tabIcon.setColorFilter(Color.WHITE);
		tabTitle.setTextColor(Color.WHITE);

		View focus = getCurrentFocus();
		if (focus != null) {
			hiddenKeyboard(focus);
		}
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		View focus = getCurrentFocus();
		if (focus != null) {
			hiddenKeyboard(focus);
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			// return PlaceholderFragment.newInstance(position + 1);
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = (Fragment) CandidatesFragment.newInstance(position);
				break;

			case 1:
				fragment = (Fragment) SearchFragment.newInstance(position);
				break;

			case 2:
				fragment = (Fragment) MoreFragment.newInstance(position);
				break;
			}

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	private void hiddenKeyboard(View v) {
		InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

}
