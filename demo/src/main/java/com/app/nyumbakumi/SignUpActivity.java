package com.app.nyumbakumi;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.nyumbakumi.MEstateFragment.OnLocationSelected;
import com.app.nyumbakumi.framework.Act;
import com.app.nyumbakumi.util.CommonUtils;
import com.app.nyumbakumi.util.MUserService;
import com.app.nyumbakumi.util.MyAppService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.app.nyumbakumi.R;

public class SignUpActivity extends Act {
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	private String SENDER_ID = "947565597088";
	private String api_key = "AIzaSyDuJo6xtLyOZDkPb8YVclGBw7RMCIsGKLE";
	static final String TAG = "GCM NYUMBAKUMI";
	private GoogleCloudMessaging gcm;
	private AtomicInteger msgId = new AtomicInteger();

	private String regid;
	private AsyncTask<Void, Void, Void> mSignUp;
	private Context context;
	private OnLocationSelected listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_layout);
		
		context = getApplicationContext();

		// Check device for Play Services APK. If check succeeds, proceed with GCM registration.
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);

			if (regid.isEmpty()) {
				registerInBackground();
			}
		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
		}

		setFontRegular((Button) findViewById(R.id.btn_submit));
		setFontRegular((EditText) findViewById(R.id.editPhone));
		setFontRegular((TextView) findViewById(R.id.textView1));

		Button  submit= (Button) findViewById(R.id.btn_submit);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				signIn();								
			}
		});
		
		((EditText) findViewById(R.id.editPhone)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == EditorInfo.IME_ACTION_DONE) {
					
					signIn();					
					return true;
				}
				return false;
			}
		});
	}
	
	/**
	 * Get the phone Number, make sure its not empty
	 * Check if the phone Number exists in the preferences
	 */
	private void signIn() {
		String phone = ((EditText) findViewById(R.id.editPhone)).getText().toString();
		if(phone.equals("")) {
			((EditText) findViewById(R.id.editPhone)).setError("Phone Number required!");
		}else {
			/**
			 * Shd start with 07
			 */
			if(!isPhoneNumberValid(phone)) 
				((EditText) findViewById(R.id.editPhone)).setError("Phone Number invalid!");
			else {
				// Open the asych task to check if the user is registered
				authenticate(phone);
			}
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.
					sendRegistrationIdToBackend();

					// For this demo: we don't need to send it because the device will send
					// upstream messages to a server that echo back the message using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Log.i(TAG, "onPostExecute:"+msg);
			}
		}.execute(null, null, null);
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Gets the current registration ID for application on GCM service, if there is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(SignUpActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
	 * messages to your app. Not needed for this demo since the device sends upstream messages
	 * to a server that echoes back the message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
		// Your implementation here.
	}

	private boolean isPhoneNumberValid(String phoneNumber) {
		if(!phoneNumber.equals("")) {
			if(phoneNumber.startsWith("07") && phoneNumber.length()==10) {
				phoneNumber = "254"+phoneNumber.substring(1);
				((EditText) findViewById(R.id.editPhone)).setText(phoneNumber);
				return true;
			}
			else if(phoneNumber.startsWith("2547") && phoneNumber.length()==12) {
				return true;
			}
			else return false;
		}else return false;
	}

	/**
	 * Check whether the user is signed up
	 * @param phone_number The user's phone number
	 */
	private void authenticate(final String phone_number) {
		mSignUp = new AsyncTask<Void, Void, Void>() {

			private Dialog pDialog;
			private Bundle bun = null;
			private boolean isConnected = false;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// Showing progress dialog
				pDialog = getProgressDialog();
				pDialog.setCancelable(true);
				pDialog.setOnCancelListener(new OnCancelListener() {					

					@Override
					public void onCancel(DialogInterface dialog) {
						/**
						 * Nothing for the time being
						 */
					}
				});
				pDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					if(CommonUtils.isConnected(SignUpActivity.this)) {
						isConnected = true;
						String device = getDeviceImei();
						bun = com.app.nyumbakumi.util.CommonUtils.signup(phone_number, getRegistrationId(context));	
						//CommonUtils.TestNetwork();
					}else {
						isConnected = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (pDialog.isShowing())
					pDialog.dismiss();

				// Check the reponse
				if(bun != null) {
					// Bundle isn't null, check the parameters
					boolean is_profile_set = bun.getBoolean(com.app.nyumbakumi.util.CommonUtils._USER_PROFILE_SETUP, false);
					boolean is_verified = bun.getBoolean(com.app.nyumbakumi.util.CommonUtils._USER_VERIFIED, false);

					setTemporary(phone_number);	
					setTemporaryUserID(bun.getString(CommonUtils._USER_ID, ""));
					setIsProfileSet(bun.getBoolean(CommonUtils._USER_PROFILE_SETUP, false));
					setUserIsInGroup(bun.getBoolean(CommonUtils._USER_IS_IN_GROUP, false));
					setIsVerified(bun.getBoolean(CommonUtils._USER_VERIFIED, false));

					if(is_verified) {
						if(is_profile_set) {
							// TODO Download users information
							Intent intent = new Intent(SignUpActivity.this, MUserService.class);
							startService(intent);

							if(!isInGroup()) {
								// TODO Start service to download services list
								Intent intents = new Intent(SignUpActivity.this, MyAppService.class);
								startService(intents);
								
								CreateGroup createGr = new CreateGroup();
								listener = (OnLocationSelected)createGr;
								switchScreen(createGr);
							}else {
								// TODO Users profile is set, proceed to the timeline
								Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
								SignUpActivity.this.startActivity(mainIntent);
								SignUpActivity.this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
								SignUpActivity.this.finish();
							}
						}else {
							// TODO Start service to download services list
							Intent intent = new Intent(SignUpActivity.this, MyAppService.class);
							startService(intent);

							//switchScreen(new CompleteProfile());
							Intent mainIntent = new Intent(SignUpActivity.this, ProfilePhoto.class);
							SignUpActivity.this.startActivity(mainIntent);
							SignUpActivity.this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
							SignUpActivity.this.finish();
						}
					}else {
						Intent verifyIntent = new Intent(SignUpActivity.this, VerificationActivity.class);
						SignUpActivity.this.startActivityForResult(verifyIntent, 10200);
						SignUpActivity.this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
						//SignUpActivity.this.finish();
					}
				}else {
					if(!isConnected) toast("Network not available..!");
					else toast("Sorry, could not login, please try again..):-");
				}
				mSignUp = null;
			}
		};
		mSignUp.execute(null, null, null);
	}

	private String getDeviceImei() {
		TelephonyManager  tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String IMEINumber = tm.getDeviceId();
		return IMEINumber;
	}

	@Override
	public void openDrawerLeft() {
		// TODO Auto-generated method stub

	}

	@Override
	public void openDrawerRight() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode , int result, Intent data) {
		if(requestCode == 10200) {
			//finish();
		}else if (requestCode == 1230) {
			if(listener != null) {
				double latitude = data.getDoubleExtra("latitude", 0.0);
				double longitude = data.getDoubleExtra("longitude", 0.0);
				Log.i(TAG, "Location: "+latitude+", "+longitude);
				listener.onLocationSelected(String.valueOf(latitude), String.valueOf(longitude));
			}
		}
	}


}
