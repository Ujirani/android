/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.nyumbakumi;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.nyumbakumi.framework.Act;

import com.app.nyumbakumi.R;

public class PanicMainActivity extends Act {
	private DrawerLayout mDrawerLayoutRight;
	private ListView mDrawerListRight;
	private ActionBarDrawerToggle mDrawerToggle;

	public ArrayList<DrawerTitle> drawerTitlesRight = new ArrayList<DrawerTitle>();
	private DrawerAdapterRight adapterRight;
	private View relativeDrawerLayoutRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_panic_main);

		createDrawerArray();		
		mDrawerLayoutRight = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListRight = (ListView) findViewById(R.id.left_drawer);
		adapterRight = new DrawerAdapterRight();
		relativeDrawerLayoutRight = findViewById(R.id.linearDrawer);

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayoutRight.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerListRight.setAdapter(adapterRight);
		mDrawerListRight.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		try{ 
			//getActionBar().setDisplayHomeAsUpEnabled(true);
			//getActionBar().setHomeButtonEnabled(true);
		}catch(Exception ex) {ex.printStackTrace();
		}

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayoutRight,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
				//getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				//getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};

		mDrawerLayoutRight.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
		
		View v = findViewById(R.id.imageDrawerMenu);
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawerLayoutRight.closeDrawer(relativeDrawerLayoutRight);				
			}
		});
	}

	private void createDrawerArray() {
		drawerTitlesRight.clear();
		
		drawerTitlesRight.add(new DrawerTitle("Ambulance", false, R.drawable.icon_drawer_compose));
		drawerTitlesRight.add(new DrawerTitle("Fire Station", false, R.drawable.icon_drawer_notification));
		drawerTitlesRight.add(new DrawerTitle("Police Station", false, R.drawable.icon_drawer_member));
		drawerTitlesRight.add(new DrawerTitle("Kenya Power & Lighting", false, R.drawable.icon_drawer_calendar));
		drawerTitlesRight.add(new DrawerTitle("Help", true, R.drawable.icon_drawer_help));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayoutRight.isDrawerOpen(relativeDrawerLayoutRight);
		if(drawerOpen) mDrawerLayoutRight.closeDrawer(relativeDrawerLayoutRight);
		else mDrawerLayoutRight.openDrawer(relativeDrawerLayoutRight);
		return super.onCreateOptionsMenu(menu);
	}
	
	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayoutRight.isDrawerOpen(relativeDrawerLayoutRight);
		if(drawerOpen) mDrawerLayoutRight.openDrawer(relativeDrawerLayoutRight);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}        
		return super.onOptionsItemSelected(item);
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		switchScreen(new ProfileScreen());
		switch (position) {
		case 0:
			switchScreen(new ContactScreen());
			break;
		case 1:
			switchScreen(new CreateGroup());
			break;
		case 2:
			switchScreen(new MembersList());
			break;

		default:
			switchScreen(new ProfileScreen());
			break;
		}

		// update selected item and title, then close the drawer
		mDrawerListRight.setItemChecked(position, true);
		setTitle(drawerTitlesRight.get(position).getTitle());

		try {
			mDrawerLayoutRight.closeDrawer(relativeDrawerLayoutRight);
		} catch(Exception ex){ex.printStackTrace(); }
	}

	@Override
	public void setTitle(CharSequence title) {
		//mTitle = title;
		//try { getActionBar().setTitle(mTitle); }catch(Exception ex){ex.printStackTrace(); }
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	class DrawerTitle {
		private String title, notification;
		private boolean isNotification;
		private int ImageResource;

		/**
		 * Set the details for the Drawer Title
		 * @param title the title
		 * @param isNotification If this is a notification
		 * @param resource The image resource to be used. 
		 */
		public DrawerTitle(String title, boolean isNotification, int resource) {
			this.setTitle(title);
			this.setNotification(isNotification);
			this.setImageResource(resource);
			this.setNotification("");
		}

		/**
		 * Set the details for the Drawer Title
		 * @param title the title
		 * @param notification the notification
		 * @param isNotification If this is a notification
		 * @param resource The image resource to be used. 
		 */
		public DrawerTitle(String title, String notification, boolean isNotification, int resource) {
			this.setTitle(title);
			this.setNotification(isNotification);
			this.setImageResource(resource);
			this.setNotification(notification);
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getNotification() {
			return notification;
		}

		public void setNotification(String notification) {
			this.notification = notification;
		}

		public boolean isNotification() {
			return isNotification;
		}

		public void setNotification(boolean isNotification) {
			this.isNotification = isNotification;
		}

		public int getImageResource() {
			return ImageResource;
		}

		public void setImageResource(int imageResource) {
			ImageResource = imageResource;
		}
	}
	
	class DrawerAdapterRight extends BaseAdapter {

		@Override
		public int getCount() {
			return drawerTitlesRight .size();
		}

		@Override
		public DrawerTitle getItem(int position) {
			return drawerTitlesRight.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View v, ViewGroup parent) {
			// Get company item from the arraylist at this position
			DrawerTitle drawerItem = getItem(position);

			// Right
			v = getLayoutInflater().inflate(R.layout.drawer_list_item,  parent, false);				

			// Set the content to be shown, fetched from the company instance acquired above.
			if(drawerItem != null) {
				TextView _title = (TextView) v.findViewById(R.id.drawerTitle);
				TextView _notification = (TextView) v.findViewById(R.id.drawerNotification);
				
				ImageView _image = (ImageView) v.findViewById(R.id.drawerImage);
				_image.setImageResource(drawerItem.getImageResource());

				_title.setText(drawerItem.getTitle());
				_notification.setText(drawerItem.getNotification());

				// The fonts
				setFontRegular(_title);
				setFontSemiBold(_notification);
				
				if(!drawerItem.isNotification()) _image.setVisibility(View.GONE);
				else _image.setVisibility(View.VISIBLE);
				
				_notification.setVisibility(View.GONE);			
				return v;
			}else return null;
		}

	}

	@Override
	public void openDrawerLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openDrawerRight() {
		// TODO Auto-generated method stub
		
	}

}