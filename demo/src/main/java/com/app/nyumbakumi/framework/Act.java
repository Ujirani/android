package com.app.nyumbakumi.framework;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nyumbakumi.R;

public abstract class Act extends FragmentActivity{
	public static final String TAG = "NymbaKumiApp";
	
	/**
	 * Source Sans Pro Black Font
	 */
	private String font_Black = "fonts/SOURCESANSPRO-BLACK.OTF";
	
	/**
	 * External Font declaration
	 */
	private Typeface tf_black, tf_bold, tf_extralight, tf_light, tf_regular, tf_semibold;
	
	/**
	 * Source Sans Pro Bold Font
	 */
	private String font_Bold = "fonts/SOURCESANSPRO-BOLD.OTF";
	
	/**
	 * Source Sans Pro Extra light Font
	 */
	private String font_ExtraLight = "fonts/SOURCESANSPRO-EXTRALIGHT.OTF";
	
	/**
	 * Source Sans Pro Light Font
	 */
	private String font_Light = "fonts/SOURCESANSPRO-LIGHT.OTF";
	
	/**
	 * Source Sans Pro Regular Font
	 */
	private String font_Regular = "fonts/SOURCESANSPRO-REGULAR.OTF";

	/**
	 * Source Sans Pro SemiBold Font
	 */
	private String font_SemiBold = "fonts/SOURCESANSPRO-SEMIBOLD.OTF";

	private Screen currentScreen;

	//Preferences
	public static final String PREFS_NAME = "MyChasePrefsFile"; 
	
	/**
	 * Request to be used for notification broadcasts
	 */
	public static final int REQUEST_UPDATE_NOTIFICATION = 400;
	
	// This is the text view to be used for notifications accross the system
	public static TextView notification_number = null;
		
	/**
	 * Inialises the fonts if they have not been set up
	 */
	public void setupFonts() {
		if(tf_black == null) tf_black= Typeface.createFromAsset(getAssets(), font_Black); 
		if(tf_bold == null) tf_bold= Typeface.createFromAsset(getAssets(), font_Bold); 
		if(tf_extralight == null) tf_extralight= Typeface.createFromAsset(getAssets(), font_ExtraLight); 
		if(tf_light == null) tf_light= Typeface.createFromAsset(getAssets(), font_Light); 
		if(tf_regular == null) tf_regular= Typeface.createFromAsset(getAssets(), font_Regular); 
		if(tf_semibold == null) tf_semibold= Typeface.createFromAsset(getAssets(), font_SemiBold); 		
	}

	/**
	 * Applies a source sans pro Black font to the view
	 * @param v Text View
	 */
	public void setFontBlack(TextView v) {
		setupFonts();
		v.setTypeface(tf_black);
	}

	/**
	 * Applies a source sans pro Black font to the view
	 * @param v Button
	 */
	public void setFontBlack(Button v) {
		setupFonts();
		v.setTypeface(tf_black);
	}

	/**
	 * Applies a source sans pro Black font to the view
	 * @param v Edit Text
	 */
	public void setFontBlack(EditText v) {
		setupFonts();
		v.setTypeface(tf_black);
	}

	/**
	 * Applies a soource sans pro Bold Font
	 * @param v Text View
	 */
	public void setFontBold(TextView v) {
		setupFonts();
		v.setTypeface(tf_bold);
	}

	/**
	 * Applies a source sans pro Bold font to the view
	 * @param v Button
	 */
	public void setFontBold(Button v) {
		setupFonts();
		v.setTypeface(tf_bold);
	}

	/**
	 * Applies a source sans pro Bold font to the view
	 * @param v Edit Text
	 */
	public void setFontBold(EditText v) {
		setupFonts();
		v.setTypeface(tf_bold);
	}

	/**
	 * Applies a source sans pro Extra Light font to the view
	 * @param v TextView
	 */
	public void setFontExtraLight(TextView v) {
		setupFonts();
		v.setTypeface(tf_extralight);
	}

	/**
	 * Applies a source sans pro Extra Light font to the view
	 * @param v Button
	 */
	public void setFontExtraLight(Button v) {
		setupFonts();
		v.setTypeface(tf_extralight);
	}

	/**
	 * Applies a source sans pro Extra Light font to the view
	 * @param v Edit Text
	 */
	public void setFontExtraLight(EditText v) {
		setupFonts();
		v.setTypeface(tf_extralight);
	}

	public void setFontLight(TextView v) {
		setupFonts();
		v.setTypeface(tf_light);
	}

	/**
	 * Applies a source sans pro Light font to the view
	 * @param v Button
	 */
	public void setFontLight(Button v) {
		setupFonts();
		v.setTypeface(tf_light);
	}

	/**
	 * Applies a source sans pro Light font to the view
	 * @param v Edit Text
	 */
	public void setFontLight(EditText v) {
		setupFonts();
		v.setTypeface(tf_light);
	}

	/**
	 * Applies a source sans pro Regualar Font
	 * @param v Text View
	 */
	public void setFontRegular(TextView v) {
		setupFonts();
		v.setTypeface(tf_regular);
	}

	/**
	 * Applies a source sans pro Regular font to the view
	 * @param v Button
	 */
	public void setFontRegular(Button v) {
		setupFonts();
		v.setTypeface(tf_regular);
	}

	/**
	 * Applies a source sans pro Regualr Font
	 * @param v Edit Text
	 */
	public void setFontRegular(EditText v) {
		setupFonts();
		v.setTypeface(tf_regular);
	}

	/**
	 * Applies a source sans pro Semi Bold font
	 * @param v Text View 
	 */
	public void setFontSemiBold(TextView v) {
		setupFonts();
		v.setTypeface(tf_semibold);
	}

	/**
	 * Applies a source sans pro Semi Boldfont
	 * @param v Button
	 */
	public void setFontSemiBold(Button v) {
		setupFonts();
		v.setTypeface(tf_semibold);
	}

	/**
	 * Applies a source sans pro Semi Bold font
	 * @param v Edit Text
	 */
	public void setFontSemiBold(EditText v) {
		setupFonts();
		v.setTypeface(tf_semibold);
	}

	/**
	 * Show a toast message to the screen
	 */
	public void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Show toast message using int resource
	 */
	public void toast(int id) {
		toast(getResources().getString(id));
	}
	
	public Dialog getProgressDialog() {
		return new KumiProgressDialog(this, R.drawable.spinner_160);
	}
	
	public static Dialog getProgressDialog(Context context) {
		return new KumiProgressDialog(context, R.drawable.spinner_160);
	}
	
	/**
	 * Check if this is the first login that has been made
	 * @return boolean
	 */
	protected boolean isFirstLogin() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		boolean firstRun = settings.getBoolean("firstLoginSetUp", true); 
		
		if (firstRun) {	        
			return true;
		} else {
			return false;
		}
	}

	/**
	 * First login been set up, set first setup preference to true
	 */
	public void setFirstLogin() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		SharedPreferences.Editor editor = settings.edit(); 
		editor.putBoolean("firstLoginSetUp", false);
		editor.commit(); 
	}
	
	/**
	 * Set the Users Details 
	 * 
	 * @param user_id The User's ID
	 * @param user_role The User's Role
	 */
	public void setUser(String user_id, String user_role) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		SharedPreferences.Editor editor = settings.edit(); 
		editor.putString("USER_ID", user_id);
		editor.putString("USER_ROLE", user_role);
		editor.commit(); 
	}
	
	/**
	 * Get Logged in user's id
	 * @return String
	 */
	public String getUserId() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		return settings.getString("USER_ID", "");
	}
	
	/**
	 * Get Logged in user's Role
	 * @return String
	 */
	public String getUserRole() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		return settings.getString("USER_ROLE", "");
	}
	
	/**
	 * Check if this is the first login that has been made
	 * @return boolean TRUE if its the first time we are running the app.
	 */
	protected boolean isFirstRun() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		boolean firstRun = settings.getBoolean("firstRunSetUp", true); 		
		if (firstRun) {	        
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * First login been set up, set first setup preference to true
	 */
	protected void setFirstRun() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		SharedPreferences.Editor editor = settings.edit(); 
		editor.putBoolean("firstRunSetUp", false); 
		editor.commit(); 
	}
		
	/**
	 * Checks if the device is online
	 * @return boolean
	 */
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
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
	
	/**
	 * Update the number of notifications if any
	 */
	public void updateNotification(String num) {
		if(num != null && !num.equals("")) {
			int nums = Integer.parseInt(num);
			if(nums > 0) {
				
			}else {
				// Set to gone
				if (notification_number.isShown()) notification_number.setVisibility(View.GONE);
			}
		}else {
			// Set to gone
			if (notification_number.isShown()) notification_number.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Switch screens
	 * 
	 * @param current Current Fragment
	 * @param newScreen Fragment To launch
	 * @param addToStack whether to add the current fragment to the stack
	 */
	public void switchScreen(Screen current, Screen newScreen, boolean addToStack) {
		try {
			FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
			if (addToStack) trans.addToBackStack(null);
			newScreen.setHasOptionsMenu(true);
			trans.replace(R.id.content_frame, newScreen);
			currentScreen = current;
			trans.commit();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Switch screens from a Fragment Activity
	 * 
	 * @param newScreen Fragment To launch
	 */
	public void switchScreen(Screen newScreen) {
		try {
			FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
			newScreen.setHasOptionsMenu(true);
			trans.replace(R.id.content_frame, newScreen);
			trans.commit();
		}catch(Exception ex) {ex.printStackTrace(); }
	}

	/**
	 * Get the current Screen
	 */
	public Screen getCurrentScreen() {
		return currentScreen;
	}

	/**
	 * Get the current screen name
	 */
	public String getCurrentScreenName() {
		return currentScreen.getClass().getSimpleName();
	}

	/**
	 * Switch screens (put the current screen on the stack by default)
	 * 
	 * @param current The current fragment
	 * @param newScreen The Fragment to launch
	 */

	public void switchScreen(Screen current, Screen newScreen) {
		switchScreen(current, newScreen, true);
	}
	
	/**
	 * Opens Drawer on the left
	 */
	public abstract void openDrawerLeft();
	
	/**
	 * Opens the drawer on the right
	 */
	public abstract void openDrawerRight();
	
	/**
	 * Check if this users phone number is registered
	 * @return boolean
	 */
	protected boolean isRegistered(String phone) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		String myphone = settings.getString("sign_phone", ""); 
		
		if (myphone.equals(phone)) {	        
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Set that the member is signed up
	 */
	public void setRegistered(String phone) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		SharedPreferences.Editor editor = settings.edit(); 
		editor.putString("sign_phone", phone); 
		editor.commit(); 
	}
	
	/**
	 * Set that the member is signed up
	 */
	public void setTemporary(String phone) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		SharedPreferences.Editor editor = settings.edit(); 
		editor.putString("temp_phone", phone); 
		editor.commit(); 
	}
	
	public String getTemporaryValue() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return settings.getString("temp_phone", "");
	}
	
	/**
	 * Set temproray value of user id
	 * @param user_id The user id, obtained during signup
	 */
	public void setTemporaryUserID(String user_id) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("temp_user", user_id);
		editor.commit();
	}
	
	/**
	 * Get temporary userid
	 * @return String user_id
	 */
	public String getTemporaryUserID() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return settings.getString("temp_user", "");
	}
	
	/**
	 * Set if the users profile has been set
	 * @param is_profile_set boolean
	 */
	public void setIsProfileSet(boolean is_profile_set) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("temp_is_profile_set", is_profile_set);
		editor.commit();
	}
	
	/**
	 * Check if user profile has been set
	 * @return boolean
	 */
	public boolean isProfileSet() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return settings.getBoolean("temp_is_profile_set", false);
	}
	
	/**
	 * Set if this user is in a group
	 * @param is_in_group true if user is in group
	 */
	public void setUserIsInGroup(boolean is_in_group) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("temp_is_in_group", is_in_group);
		editor.commit();
	}
	
	/**
	 * Check if the user is in a group
	 * @return boolean true if user is in a group
	 */
	public boolean isInGroup() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return settings.getBoolean("temp_is_in_group", false);
	}
	
	/**
	 * Set if this user is verified
	 * @param is_in_group true if user is verified
	 */
	public void setIsVerified(boolean is_verified) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("temp_is_verified", is_verified);
		editor.commit();
	}
	
	/**
	 * Check if the user is verified
	 * @return boolean true if user is verified
	 */
	public boolean isVerified() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return settings.getBoolean("temp_is_verified", false);
	}
	
	/**
	 * Save users details in the preferences
	 * @param name Name of the user
	 * @param hse_estate The House Estate
	 * @param hse_no The House Number
	 */
	public void setUserDetails(String name, String hse_estate, String hse_no) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		SharedPreferences.Editor editor = settings.edit(); 
		// User Details
		editor.putString("name", name); 
		editor.putString("hse_estate", hse_estate); 
		editor.putString("hse_no", hse_no); 
		editor.commit(); 
	}
	
	/**
	 * Set the Profile image Path
	 * @param selectedImagePath
	 */
	public void setProfilePhoto(String selectedImagePath) {
		/**
		 * Clear the previous
		 */
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		SharedPreferences.Editor editor = settings.edit(); 
		// User Details
		editor.putString("profilePhoto", selectedImagePath); 
		editor.commit(); 
	}
	
	/**
	 * Return the profile Photo Image Path
	 * @return String Image Path
	 */
	public String getProfilePhoto() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return settings.getString("profilePhoto", "");
	}
	
	/**
	 * Get the phone number of the current user
	 * @return String Phone Number
	 */
	public String getPhoneValue() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return settings.getString("sign_phone", "");
	}
	
	/**
	 * Get the name of the current user
	 * @return String Name
	 */
	public String getNameValue() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return settings.getString("name", "");
	}
	
	/**
	 * Get the Hse Estate of the current user
	 * @return String House Estate
	 */
	public String getHseEstateValue() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return settings.getString("hse_estate", "");
	}
	
	/**
	 * Get the House Number of the Current user
	 * @return String House Number
	 */
	public String getHseNoValue() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return settings.getString("hse_no", "");
	}

	/**
	 * Create a temporary Group 
	 * @param group Group ID
	 */
	public void setGroup(String group) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		SharedPreferences.Editor editor = settings.edit(); 
		editor.putString("temp_group", group); 
		editor.commit(); 
	}
	
	/**
	 * Get the temporary Group Value
	 * @return string Group ID 
	 */
	public String getGroupValue() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return settings.getString("temp_group", "");
	}
	
	public static void setIsMemberAdded(boolean isAdded, Context context) {
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0); 
		SharedPreferences.Editor editor = settings.edit(); 
		editor.putBoolean("temp_member_added", isAdded); 
		editor.commit(); 
	}
	
	/**
	 * Get the temporary Group Value
	 * @return string Group ID 
	 */
	public static boolean getIsMemberAdded(Context context) {
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		return settings.getBoolean("temp_member_added", false);
	}
}
