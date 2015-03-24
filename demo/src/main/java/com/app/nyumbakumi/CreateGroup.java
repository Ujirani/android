package com.app.nyumbakumi;

import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.nyumbakumi.MEstateFragment.OnLocationSelected;
import com.app.nyumbakumi.framework.Screen;
import com.app.nyumbakumi.util.CommonUtils;

import com.app.nyumbakumi.R;

public class CreateGroup extends Screen implements OnLocationSelected {
	
	private View v;
	private AsyncTask<Void, Void, Void> mCreateGroup;
	private LocationManager locationManager;
	private Location lastKnownLocation;
	private AsyncTask<Void, Void, Void> mAddress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.group_layout, container, false);
		
		getParent().setFontRegular((Button) v.findViewById(R.id.btn_next));
		getParent().setFontRegular((EditText) v.findViewById(R.id.editGroupDescription));
		getParent().setFontRegular((TextView) v.findViewById(R.id.editGroupLocation));
		getParent().setFontRegular((EditText) v.findViewById(R.id.editGroupName));
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCreateGroup));
		
		getParent().setFontRegular((TextView) v.findViewById(R.id.text_btn_group_info));
		getParent().setFontRegular((TextView) v.findViewById(R.id.text_btn_group_members));
		getParent().setFontRegular((TextView) v.findViewById(R.id.text_btn_group_services));
		
		((TextView) v.findViewById(R.id.text_btn_group_info)).setBackgroundResource(R.drawable.group_title_selected);
		Button btnNext = (Button) v.findViewById(R.id.btn_next);
		btnNext.setText("Next ");
		
		OnClickListener list = new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				switch (view.getId()) {				
				case R.id.btn_next:
					// TODO Get details for the group
					String name = ((EditText) v.findViewById(R.id.editGroupName)).getText().toString();
					String location = ((TextView) v.findViewById(R.id.editGroupLocation)).getText().toString();
					String more_details = ((EditText) v.findViewById(R.id.editGroupDescription)).getText().toString();
					
					//TODO Check the detail to make sure they are entered
					if(name.equals("")) {
						if(name.equals("")) ((EditText) v.findViewById(R.id.editGroupName)).setError("Name Required!");
					}else {
						createGroup(name, location, more_details);						
					}
					break;
				case R.id.editGroupLocation:
					Intent intent = new Intent(getParent(), MEstateFragment.class);
					getParent().startActivityForResult(intent, 1230);
					break;
				default:
					break;
				}
			}
		};
		
		((TextView) v.findViewById(R.id.editGroupLocation)).setOnClickListener(list);
		
		btnNext.setOnClickListener(list);

		locationManager = (LocationManager) getParent().getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		lastKnownLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
		
		if(lastKnownLocation != null){
			double latitude = lastKnownLocation.getLatitude();
			double longitude = lastKnownLocation.getLongitude();

			//Set the location to the location box
			((TextView) v.findViewById(R.id.editGroupLocation)).setText(latitude+", "+longitude);
			getLocationAddress(latitude, longitude);
		}
		return v;
	}

	/**
	 * Get the location address, given the latitude and longitude of the place
	 * 
	 * @param latitude The Latitude
	 * @param longitude The longitude
	 */
	private void getLocationAddress(final double latitude, final double longitude) {
		mAddress = new AsyncTask<Void, Void, Void>(){
			private Geocoder geocoder = null;
			private String addressStr;
			private List<Address> list;

			@Override
			protected Void doInBackground(Void... params) {
				geocoder = new Geocoder(getParent(), Locale.getDefault()); 

				try {
					/**
					 * Get the Address from the given location0
					 */
					list = geocoder.getFromLocation(latitude, longitude, 1);
					if (list != null && list.size() > 0) {
						addressStr = list.get(0).getLocality();
					}
				} catch (Exception e) {
					Log.e(TAG, "Impossible to connect to Geocoder", e);
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if(addressStr != null && !addressStr.equals("")) {
					((TextView) v.findViewById(R.id.editGroupLocation)).setText(addressStr);
				}
				mAddress = null;
				super.onPostExecute(result);
			}

		};

		mAddress.execute();
	}

	/**
	 * Create a new Group, because this user doesn't have a group. 
	 * @param name The Group's name 
	 * @param location The Group's Location
	 * @param more_details More description about the group.
	 */
	private void createGroup(final String name, final String location, final String more_details) {
		mCreateGroup = new AsyncTask<Void, Void, Void>() {

			private Dialog pDialog;
			private String isComplete = null;
			private boolean isConnected;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// Showing progress dialog
				pDialog = getParent().getProgressDialog();
				//pDialog.setMessage("We are creating a new Group for you...");
				pDialog.setCancelable(true);
				//pDialog.setContentView(R.layout.custom_dialog);
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
					String userid = getParent().getTemporaryUserID();

					if(CommonUtils.isConnected(getParent())) {
						isConnected = true;
						isComplete = com.app.nyumbakumi.util.CommonUtils.createGroup(userid, name, location, more_details);
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
				Log.i("CREATE", "GROUP CREATED.");

				// Check the reponse
				if(isComplete != null) {
					getParent().setGroup(isComplete);
					getParent().setUserIsInGroup(true);

					ContactScreen sc = new ContactScreen();
					Bundle bun = new Bundle();
					bun.putString("GROUPID", isComplete);
					sc.setArguments(bun);
					getParent().switchScreen(sc);

				}else {
					if(!isConnected) getParent().toast("Network not available..!");
					else {
						if(CommonUtils.message !=null && !CommonUtils.message.equals("")) {
							//getParent().toast(CommonUtils.message);
							CommonUtils.message = null;
						}
						else getParent().toast("Group could not be created, please try again...");
					}
				}
				mCreateGroup = null;
			}
		};
		mCreateGroup.execute(null, null, null);
	}

	@Override
	public void onLocationSelected(String latitude, String longitude) {
		((TextView) v.findViewById(R.id.editGroupLocation)).setText(latitude+", "+longitude);
	}

}
