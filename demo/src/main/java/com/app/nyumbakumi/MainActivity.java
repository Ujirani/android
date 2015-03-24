package com.app.nyumbakumi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;
import com.app.nyumbakumi.R;

public class MainActivity extends NavigationLiveo implements NavigationLiveoListener {
    private static String TAG = "BestLocationProvider";
    public List<String> mListNameItem;
    private BestLocationProvider mBestLocationProvider;
    private BestLocationListener mBestLocationListener;
    @Override
    public void onUserInformation() {
        //User information here
        this.mUserName.setText("Philomena Juma");
        this.mUserEmail.setText("philomena@gmail.com");
        this.mUserPhoto.setImageResource(R.drawable.profile_dummy);
        this.mUserBackground.setImageResource(R.drawable.ic_user_background);
    }

    @Override
    public void onInt(Bundle savedInstanceState) {
        //Creation of the list items is here

        // set listener {required}
        this.setNavigationListener(this);

        //First item of the position selected from the list
        this.setDefaultStartPositionNavigation(1);

        // name of the list items
        mListNameItem = new ArrayList<>();
        mListNameItem.add(0, "Profile");
        mListNameItem.add(1, "Settings");
        mListNameItem.add(2, "Invite Members");
        mListNameItem.add(3, "Others"); //This item will be a subHeader
        mListNameItem.add(4, "Help");
        mListNameItem.add(5, "FAQ");
        mListNameItem.add(6, "About");

        List<Integer> mListIconItem = new ArrayList<>();
        mListIconItem.add(0, R.drawable.ic_inbox_black_24dp);
        mListIconItem.add(1, R.drawable.ic_settings_black_24dp); //Item no icon set 0
        mListIconItem.add(2, R.drawable.ic_send_black_24dp); //Item no icon set 0
        mListIconItem.add(3, 0); //When the item is a subHeader the value of the icon 0
        mListIconItem.add(4, R.drawable.ic_delete_black_24dp);
        mListIconItem.add(5, R.drawable.ic_report_black_24dp);
        mListIconItem.add(6, R.drawable.ic_report_black_24dp);


        //{optional} - Among the names there is some subheader, you must indicate it here
        List<Integer> mListHeaderItem = new ArrayList<>();
        mListHeaderItem.add(3);

        //{optional} - Among the names there is any item counter, you must indicate it (position) and the value here
        SparseIntArray mSparseCounterItem = new SparseIntArray(); //indicate all items that have a counter
        mSparseCounterItem.put(0, 7);
        mSparseCounterItem.put(1, 123);
        mSparseCounterItem.put(6, 250);

        //If not please use the FooterDrawer use the setFooterVisible(boolean visible) method with value false
        this.setFooterInformationDrawer("Panic Button", R.drawable.ic_settings_black_24dp);


        this.setNavigationAdapter(mListNameItem, mListIconItem, mListHeaderItem, mSparseCounterItem);
    }

    @Override
    public void onItemClickNavigation(int position, int layoutContainerId) {

        FragmentManager mFragmentManager = getSupportFragmentManager();

        Fragment mFragment = new FragmentMain().newInstance(mListNameItem.get(position));

        if (mFragment != null){
            mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment).commit();
        }
    }

    @Override
    public void onPrepareOptionsMenuNavigation(Menu menu, int position, boolean visible) {

        //hide the menu when the navigation is opens
        switch (position) {
            case 0:
                menu.findItem(R.id.menu_add).setVisible(!visible);
                menu.findItem(R.id.menu_search).setVisible(!visible);
                break;

            case 1:
                menu.findItem(R.id.menu_add).setVisible(!visible);
                menu.findItem(R.id.menu_search).setVisible(!visible);
                break;
        }
    }

    @Override
    public void onClickUserPhotoNavigation(View v) {
        //user photo onClick
        Toast.makeText(this, R.string.open_user_profile, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickFooterItemNavigation(View v) {
        //footer onClick
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    protected void onResume() {
        initLocation();
        mBestLocationProvider.startLocationUpdatesWithListener(mBestLocationListener);

        super.onResume();
    }

    @Override
    protected void onPause() {
        initLocation();
        mBestLocationProvider.stopLocationUpdates();

        super.onPause();
    }

    private void initLocation(){
        if(mBestLocationListener == null){
            mBestLocationListener = new BestLocationListener() {

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.i(TAG, "onStatusChanged PROVIDER:" + provider + " STATUS:" + String.valueOf(status));
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.i(TAG, "onProviderEnabled PROVIDER:" + provider);
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.i(TAG, "onProviderDisabled PROVIDER:" + provider);
                }

                @Override
                public void onLocationUpdateTimeoutExceeded(BestLocationProvider.LocationType type) {
                    Log.w(TAG, "onLocationUpdateTimeoutExceeded PROVIDER:" + type);
                }

                @Override
                public void onLocationUpdate(Location location, BestLocationProvider.LocationType type,
                                             boolean isFresh) {
                    Log.i(TAG, "onLocationUpdate TYPE:" + type + " Location:" + mBestLocationProvider.locationToString(location));
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("LocationUpdate",location.getLatitude() +","+ location.getLongitude());
                    editor.apply();
                }
            };

            if(mBestLocationProvider == null){
                mBestLocationProvider = new BestLocationProvider(this, true, true, 10000, 1000, 2, 0);
            }
        }
    }
}
