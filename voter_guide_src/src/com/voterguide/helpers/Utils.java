package com.voterguide.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class Utils {

	public static void alertMessage(Context context, String Message,
			OnClickListener dialogClickListener) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(Message)
				.setPositiveButton("Ok", dialogClickListener)
				.setNegativeButton("Cancel", dialogClickListener).show();
	}

	public static boolean isDbNeedToUpdate(long lastUpdateDate) {

		// milliseconds
		long different = System.currentTimeMillis() - lastUpdateDate;
		System.out.println("current : " +  System.currentTimeMillis());
		System.out.println("lastUpdateDate : " +  lastUpdateDate);
		
		System.out.println("different : " + different);

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;

		long elapsedDays = different / daysInMilli;
		
		System.out.println("elapsedDays : " + elapsedDays);

		return (elapsedDays >= 1);
	}
	

}
