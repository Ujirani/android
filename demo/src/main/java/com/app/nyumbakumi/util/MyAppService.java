package com.app.nyumbakumi.util;

import java.util.ArrayList;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.app.nyumbakumi.entity.AppService;

public class MyAppService extends IntentService {
	public static Object[] allServicesList = null;
	public static ArrayList<AppService> securityList = new ArrayList<AppService>();
	public static ArrayList<AppService> healthList = new ArrayList<AppService>();	
	public static ArrayList<AppService> fireList = new ArrayList<AppService>();

	private int result = Activity.RESULT_CANCELED;

	public static final int RESULT_NETWORK_NOT_AVAILABLE = 122;   	// That the Leads have completed processing and are now ready	

	public static final String RESULT = "result";
	public static final String NOTIFICATION = "com.app.nyumbakumi.service.receiver";
	public static final String TAG = "DOWNLOADSERVICE";
	public static final String USER_ID = "user_id";

	public MyAppService() {
		super("DownloadService");
	}

	// will be called asynchronously by Android
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "Service started...");	

		if(isNetworkAvailable()) {

			/**
			 * Load companies
			 */
			allServicesList = CommonUtils.getServicesNCompanies();	

			if(allServicesList != null && allServicesList.length > 0 && ((Boolean)allServicesList[0])) {	
				securityList = (ArrayList<AppService>) allServicesList[1];
				healthList = (ArrayList<AppService>) allServicesList[2];
				fireList = (ArrayList<AppService>) allServicesList[3];
				
				/**
				 * Finished saving and updating the database, we can now send a broadcast
				 */
				result = Activity.RESULT_OK;
				publishResults(result);
			}			
		}

		/**
		 * You could close yourself
		 */
		stopSelf();
	}

	/**
	 * Publish or Broadcast this message to all the entire system
	 * @param result The message to send
	 */
	private void publishResults(int result) {
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(RESULT, result);
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
