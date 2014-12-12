package com.voterguide;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.apache.http.util.ByteArrayBuffer;

import com.voterguide.helpers.ConnectionDetector;
import com.voterguide.helpers.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SplashScreenActivity extends Activity {
	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 1000;
	private ProgressDialog prgDialog;
	// Progress Dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 1;
	
	DownloadDbAsycTask fetchDBTAsycTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes title bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Removes
		this.getWindow().setBackgroundDrawableResource(R.drawable.splash_screen);// notification
		setContentView(R.layout.activity_splash_screen);
		SharedPreferences pref = this.getSharedPreferences("com.voterguide", Context.MODE_PRIVATE);
		if (pref.getLong("com.voterguide.lastupdate", 0) == 0) {
			long last_updatedTimeStamp = Long.parseLong(getString(R.string.last_db_update_timestamp));
			pref.edit().putLong("com.voterguide.lastupdate", last_updatedTimeStamp).apply();
		}

		Long startTimeStamp = pref.getLong("com.voterguide.lastupdate", 0);
		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		Boolean isInternetPresent = cd.isConnectingToInternet();

		if (Utils.isDbNeedToUpdate(startTimeStamp) && isInternetPresent) {
			fetchDBTAsycTask = new DownloadDbAsycTask();
			fetchDBTAsycTask.execute(getString(R.string.remote_db_url));
			
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					  if ( fetchDBTAsycTask.getStatus() == AsyncTask.Status.RUNNING ){
						  Toast.makeText(getApplicationContext(),"Timeout error. Unable to fetch latest data!", Toast.LENGTH_LONG).show();
						  fetchDBTAsycTask.cancel(true);
						  prgDialog.cancel();
					  }						  					
				}
			}, 30000);

		} else {
			pref.edit().putBoolean("IS_DB_UPDATED", false).apply();
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent i = new Intent(SplashScreenActivity.this,MainActivity.class);
					startActivity(i);
					finish();
					handler.removeCallbacks(this);
				}
			}, AUTO_HIDE_DELAY_MILLIS);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			prgDialog = new ProgressDialog(this);
			prgDialog.setMessage("Fetching latest information... \n Press back to cancel.");
			prgDialog.setIndeterminate(false);
			prgDialog.setMax(100);
			prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			prgDialog.setCancelable(true);
			prgDialog.setOnCancelListener(new OnCancelListener() {
		        @Override
		        public void onCancel(DialogInterface dialog) {
		        	Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
					startActivity(i);
					finish();
		        }
		    });
			prgDialog.show();
			return prgDialog;
		default:
			return null;
		}
	}

	class DownloadDbAsycTask extends AsyncTask<String, String, String> {

		// Show Progress bar before downloading Music
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Shows Progress Bar Dialog and then call doInBackground method
			showDialog(progress_bar_type);
		}

		// Download Music File from Internet
		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();
				// Get Music file length
				int lenghtOfFile = conection.getContentLength();
				// input stream to read file - with 8k buffer
				InputStream is = conection.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				/*
				 * Read bytes to the Buffer until there is nothing more to
				 * read(-1).
				 */
				ByteArrayBuffer baf = new ByteArrayBuffer(50);
				int current = 0;
				long total = 0;
				while ((current = bis.read()) != -1) {
					total += current;
					baf.append((byte) current);
				}

				/* Convert the Bytes read to a String. */
				FileOutputStream fos = null;
				// Select storage location
				fos = getApplicationContext().openFileOutput("Data2014.sqlite",	Context.MODE_PRIVATE);
			//	publishProgress("" + (int) ((total * 100) / lenghtOfFile));
				fos.write(baf.toByteArray());
				fos.close();
			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}
			return null;
		}

		// While Downloading Music File
		protected void onProgressUpdate(String... progress) {
			// Set progress percentage
			//prgDialog.setProgress(Integer.parseInt(progress[0]));
		}

		// Once Music File is downloaded
		@Override
		protected void onPostExecute(String file_url) {
			// Dismiss the dialog after the Music file was downloaded
			dismissDialog(progress_bar_type);
			Toast.makeText(getApplicationContext(),"Download complete, saving table", Toast.LENGTH_LONG).show();
			SharedPreferences pref = getApplicationContext().getSharedPreferences("com.voterguide",Context.MODE_PRIVATE);
			pref.edit().putLong("com.voterguide.lastupdate",(new Date().getTime())).apply();
			pref.edit().putBoolean("IS_DB_UPDATED", true).apply();
			Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
			startActivity(i);
			// close this activity
			finish();
			// Play the music
		}
		
	}
	
	@Override
	public void onBackPressed() {
		fetchDBTAsycTask.cancel(true);
		prgDialog.dismiss();
	return;
	}

}
