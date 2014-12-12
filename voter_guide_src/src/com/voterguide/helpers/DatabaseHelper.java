package com.voterguide.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1;
	private static final String DATABASE_NAME = "Data2014.sqlite";
	private static File DATABASE_FILE;

	// This is an indicator if we need to copy the
	// database file.
	private boolean mInvalidDatabaseFile = false;
	private boolean mIsUpgraded = false;
	private Context mContext;

	/**
	 * number of users of the database connection.
	 * */
	private int mOpenConnections = 0;

	private static DatabaseHelper mInstance;

	synchronized static public DatabaseHelper getInstance(Context context) {
		SharedPreferences pref = context.getSharedPreferences("com.voterguide",
				Context.MODE_PRIVATE);
		boolean isRemoteDbUpdated = pref.getBoolean("IS_DB_UPDATED", false);
		if (mInstance == null || isRemoteDbUpdated) {
			mInstance = new DatabaseHelper(context.getApplicationContext(),	isRemoteDbUpdated);
		}
		return mInstance;
	}

	private DatabaseHelper(Context context, boolean isRemoteDbUpdated) {
		super(context, DATABASE_NAME, null, VERSION);
		this.mContext = context;

		SQLiteDatabase db = null;
		try {

			db = getReadableDatabase();
			if (db != null) {
				db.close();
			}

			DATABASE_FILE = context.getDatabasePath(DATABASE_NAME);

			if (isRemoteDbUpdated) {
				copyDatabase(isRemoteDbUpdated);
				SharedPreferences pref = context.getSharedPreferences("com.voterguide", Context.MODE_PRIVATE);
				pref.edit().putBoolean("IS_DB_UPDATED", false).apply();
			} else if (mInvalidDatabaseFile) {
				copyDatabase(isRemoteDbUpdated);
			}
			if (mIsUpgraded) {
				doUpgrade();
			}
		} catch (SQLiteException e) {
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		mInvalidDatabaseFile = true;
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int old_version,
			int new_version) {
		mInvalidDatabaseFile = true;
		mIsUpgraded = true;
	}

	/**
	 * called if a database upgrade is needed
	 */
	private void doUpgrade() {
		// implement the database upgrade here.
	}

	@Override
	public synchronized void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		// increment the number of users of the database connection.
		mOpenConnections++;
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	/**
	 * implementation to avoid closing the database connection while it is in
	 * use by others.
	 */
	@Override
	public synchronized void close() {
		mOpenConnections--;
		if (mOpenConnections == 0) {
			super.close();
		}
	}

	private void copyDatabase(boolean isRemoteDbUpdated) {
		AssetManager assetManager = mContext.getResources().getAssets();
		InputStream in = null;
		OutputStream out = null;
		try {

			if (isRemoteDbUpdated) {
				in = mContext.openFileInput(DATABASE_NAME);
				//Log.i("getMD5CheckSum",getMD5Checksum(mContext.openFileInput(DATABASE_NAME)));
				SharedPreferences pref = mContext.getSharedPreferences("com.voterguide", Context.MODE_PRIVATE);
				pref.edit().putString("dataMD5",getMD5Checksum(mContext.openFileInput(DATABASE_NAME))).apply();
			} else {
				in = assetManager.open(DATABASE_NAME);
			//	Log.i("getMD5CheckSum",getMD5Checksum(assetManager.open(DATABASE_NAME)));
				SharedPreferences pref = mContext.getSharedPreferences("com.voterguide", Context.MODE_PRIVATE);
				pref.edit().putString("dataMD5",getMD5Checksum(assetManager.open(DATABASE_NAME))).apply();
			}
			
			
			out = new FileOutputStream(DATABASE_FILE);
			byte[] buffer = new byte[1024];
			int read = 0;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		setDatabaseVersion();
		mInvalidDatabaseFile = false;
	}

	private void setDatabaseVersion() {
		SQLiteDatabase db = null;
		try {
			db = SQLiteDatabase.openDatabase(DATABASE_FILE.getAbsolutePath(),
					null, SQLiteDatabase.OPEN_READWRITE);
			db.execSQL("PRAGMA user_version = " + VERSION);
		} catch (SQLiteException e) {
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}


	
	 public static byte[] createChecksum( InputStream fis) throws Exception {
	      // InputStream fis =  new FileInputStream(DATABASE_FILE.getAbsolutePath());

	       byte[] buffer = new byte[1024];
	       MessageDigest complete = MessageDigest.getInstance("MD5");
	       int numRead;

	       do {
	           numRead = fis.read(buffer);
	           if (numRead > 0) {
	               complete.update(buffer, 0, numRead);
	           }
	       } while (numRead != -1);

	       fis.close();
	       return complete.digest();
	   }

	   // see this How-to for a faster way to convert
	   // a byte array to a HEX string
	   public static String getMD5Checksum( InputStream fis) throws Exception {
	       byte[] b = createChecksum(fis);
	       String result = "";

	       for (int i=0; i < b.length; i++) {
	           result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	       }
	       return result;
	   }

	

}