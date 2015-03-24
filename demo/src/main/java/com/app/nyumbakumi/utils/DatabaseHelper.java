package com.app.nyumbakumi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.app.nyumbakumi.entity.NotiMessage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	// Logcat tag
	private static final String LOG = DatabaseHelper.class.getName();
	private Context context;
	
	// Database Version
	private static final int DATABASE_VERSION = 1;
	
	// Database Name
	private static final String DATABASE_NAME = "ujirani_DB";
	
	// Table Names
	private static final String TABLE_NOTIFICATION = "notifications";
	
	// Common column names
	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_AT = "created_at";
	private static final String KEY_SENDER = "sender";
	private static final String KEY_MESSAGE = "message";
	

	// Leads table create statement
	private static final String CREATE_TABLE_LEADS = "CREATE TABLE "
			+ TABLE_NOTIFICATION + "(" + 
			KEY_ID + " integer primary key," + 
			KEY_SENDER + " TEXT," + 
			KEY_MESSAGE + " TEXT," + 
			KEY_CREATED_AT + " DATETIME" + ")";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_LEADS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);

		// create new tables
		onCreate(db);
	}

	/**
	 * Save a new notification. 
	 * @param noti Notification Message instance
	 * @return long
	 */
	public long createNotification(NotiMessage noti) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SENDER, noti.getSender());
		values.put(KEY_MESSAGE, noti.getMessage());
		values.put(KEY_CREATED_AT, getDateTime());
		long id = db.insert(TABLE_NOTIFICATION, null, values);
		
		//close db connection
		db.close();
		return id;
	}
		
	/**
	 * getting all Sales
	 * @return List<Lead>
	 */
	public List<NotiMessage> getAllNotification() {
		List<NotiMessage> all_leads = new ArrayList<NotiMessage>();
		String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION +" ORDER BY "+ KEY_ID + " DESC ";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cusorsales = db.rawQuery(selectQuery, null);
		
		// looping through all rows and adding to list
		if (cusorsales.moveToFirst()) {
			do {
				NotiMessage t = new NotiMessage();
				t.setSender(cusorsales.getString(cusorsales.getColumnIndex(KEY_SENDER)));
				t.setMessage(cusorsales.getString(cusorsales.getColumnIndex(KEY_MESSAGE)));
				t.setTimeDuration(getDateDifference(getDateFromString(cusorsales.getString(cusorsales.getColumnIndex(KEY_CREATED_AT)))));
				
				// adding to tags list
				all_leads.add(t);
			} while (cusorsales.moveToNext());
		}

		cusorsales.close();
		db.close();
		return all_leads;
	}
	
	/**
	 * get datetime
	 * */
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * Converts a string to date
	 * @param dateString String date
	 * @return Date
	 */
	private Date getDateFromString(String dateString) {
		try {
			Date dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateString);
			return dateFormat;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get the difference in hours between the two dates
	 * @param from Date in database
	 * @return int Number of hours between the two dates
	 */
	private String getDateDifference(Date from) {
		long secs = ((new Date().getTime()) - (from.getTime())) / 1000;
		int hours = (int) (secs / 3600);
		int days = (int) (secs / 3600) / 24;
		int mins = (int) secs / 60;
		if(days > 0) return days + " days ago";
		if(mins > 60) return hours + " hours ago";
		else if(mins < 60) return mins+" mins ago";
		return "";
	}
}
