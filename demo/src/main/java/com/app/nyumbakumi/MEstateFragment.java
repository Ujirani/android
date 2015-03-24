package com.app.nyumbakumi;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.app.nyumbakumi.framework.Act;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.app.nyumbakumi.R;

public class MEstateFragment extends Act implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener, OnMyLocationButtonClickListener{
	private GoogleMap mMap;

	private LocationClient mLocationClient;

	private LocationManager locationManager;

	private Location lastKnownLocation;
	
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000)         // 5 seconds
			.setFastestInterval(16)    // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	
	// TODO When the google login has worked. 
	public interface OnLocationSelected{
		public void onLocationSelected(String latitude, String longitude);
	}
	
	private OnLocationSelected listener;	
	public MEstateFragment() {
	}
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps_layout);
		
		setUpMapIfNeeded();
		
		mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng latlong) {
				Intent data = new Intent();
				data.putExtra("latitude", latlong.latitude);
				data.putExtra("longitude", latlong.longitude);
				setResult(1230, data);
				finish();
			}
		});
		
		getMyLocation();
		
	}
	
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
				mMap.setOnMyLocationButtonClickListener(this); 
			}
		}
	}
	
	private LatLng getMyLocation() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		lastKnownLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
		
		if(lastKnownLocation != null) {
			double lat_to = (lastKnownLocation.getLatitude());
			double long_to = (lastKnownLocation.getLongitude());
			
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat_to, long_to), 13));   		
			return new LatLng(lat_to, long_to);
		}else {
			return null;
		}
	}
	
	@Override
	public boolean onMyLocationButtonClick() {
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		//setUpLocationClientIfNeeded();
		//mLocationClient.connect();
	}	

	@Override
	public void onPause() {
		super.onPause();
		/*if (mLocationClient != null) {
			mLocationClient.disconnect();
		}*/
	}	
	
	private void setUpLocationClientIfNeeded() {
		/*if (mLocationClient == null) {
			mLocationClient = new LocationClient(
					getApplicationContext(),
					this,  // ConnectionCallbacks
					this); // OnConnectionFailedListener
			
			
		}*/
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
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
			
}
