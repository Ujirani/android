package com.app.nyumbakumi.util;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.app.nyumbakumi.entity.MProfile;
import com.app.nyumbakumi.framework.Act;

public class MUserService extends IntentService {
	private int result = Activity.RESULT_CANCELED;
	private MProfile profile;

	public static final int RESULT_NETWORK_NOT_AVAILABLE = 122; 

	public static final String RESULT = "result";
	public static final String NOTIFICATION = "com.app.nyumbakumi.userservice.receiver";
	public static final String TAG = "DOWNLOADSERVICE";
	public static final String USER_ID = "user_id";

	public MUserService() {
		super("USERDETAILS_SERVICE");
	}

	// will be called asynchronously by Android
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "User Service started...");	
		try{
			if(isNetworkAvailable()) {
				String userid = getTemporaryUserID();
				Log.i(TAG, userid);
				
				profile = CommonUtils.getUserDetails(userid);	
				Log.i(TAG, userid+" User:"+profile.toString());	

				if(profile != null) {	
					setUserDetails(profile);

					/**
					 * Save the details in the preferences
					 */
					result = Activity.RESULT_OK;
					publishResults(result, profile);
				}			
			}

			/**
			 * You could close yourself
			 */
			stopSelf();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Set the users details in the preferences.
	 * @param profile MProfile
	 */
	public void setUserDetails(MProfile profile) {
		SharedPreferences settings = getSharedPreferences(Act.PREFS_NAME, 0); 
		SharedPreferences.Editor editor = settings.edit(); 
		// User Details
		editor.putString("name", profile.getName()); 
		editor.putString("temp_group", profile.getGroup()); 
		editor.putString("sign_phone", profile.getPhone_number()); 
		editor.commit(); 
	}

	/**
	 * Get temporary userid
	 * @return String user_id
	 */
	public String getTemporaryUserID() {
		SharedPreferences settings = getSharedPreferences(Act.PREFS_NAME, 0);
		return settings.getString("temp_user", "");
	}

	/**
	 * Publish or Broadcast this message to all the entire system
	 * @param result The message to send
	 */
	private void publishResults(int result, MProfile profile) {
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(RESULT, result);
		
		intent.putExtra("NAME", profile.getName());
		intent.putExtra("USER_TYPE", profile.getType());
		intent.putExtra("PHONE", profile.getPhone_number());
		
		intent.putExtra("GROUP", profile.getGroup());
		intent.putExtra("HSE_ESTATE", profile.getHseEstateValue());
		intent.putExtra("HSE_NO", profile.getHseNoValue());
		sendBroadcast(intent);
	}

	/**
	 * Checks if the Network access if available
	 * @return boolean TRUE if available, FALSE otherwise
	 */
	public boolean isNetworkAvailable() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		boolean isConnected = info != null &&
				info.isConnectedOrConnecting();
		return isConnected;
	}

}
