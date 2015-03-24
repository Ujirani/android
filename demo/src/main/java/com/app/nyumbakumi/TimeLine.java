package com.app.nyumbakumi;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.nyumbakumi.entity.NotiMessage;
import com.app.nyumbakumi.framework.Screen;
import com.app.nyumbakumi.utils.DatabaseHelper;
import com.app.nyumbakumi.utils.Rounder;

import com.app.nyumbakumi.R;

public class TimeLine extends Screen {
	private Timer  t = new Timer();

	/**
	 * @param message
	 * @return TimeLine
	 */
	public static TimeLine getInstance(String message, String sender){
		Bundle bun = new Bundle();
		bun.putString("MESSAGE", message);
		bun.putString("SENDER", sender);
		TimeLine timeline = new TimeLine();
		timeline.setArguments(bun);
		return timeline;
	}
	
	private View v;
	private ListView listv;
	public ArrayList<NotiMessage> notiList = new ArrayList<NotiMessage>();
	private NotiAdapter adapter;
	private DatabaseHelper db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.timeline_layout, container, false);
		db = new DatabaseHelper(getParent());
		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textProfileViews));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textProfileLikes));

		getParent().setFontRegular((TextView) v.findViewById(R.id.textTimelineProfileLocation));	
		getParent().setFontSemiBold((TextView) v.findViewById(R.id.textTimelineProfileName));	
		getParent().setFontRegular((TextView) v.findViewById(R.id.textTimelineProfileProffession));	
		getParent().setFontRegular((TextView) v.findViewById(R.id.textTimelineProfileTitle));

		View navLeft = v.findViewById(R.id.imageLaunchDrawerLeft);
		View navRight = v.findViewById(R.id.imageLaunchDrawerRight);
		View navRightTab = v.findViewById(R.id.profile_footer_likes);

		View navProfileNotification = v.findViewById(R.id.menu_footer_notification);
		View navProfileCompose = v.findViewById(R.id.menu_footer_compose);

		OnClickListener list = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.imageLaunchDrawerLeft:
					getParent().openDrawerLeft();
					break;
					
				case R.id.imageLaunchDrawerRight:
					getParent().openDrawerRight();					
					break;

				case R.id.profile_footer_likes:
					getParent().openDrawerRight();					
					break;

				case R.id.menu_footer_notification:
					/**
					 * Opens Notification - Timeline
					 */					
					break;	
				case R.id.menu_footer_compose:
					/**
					 * Opens the compose Screen
					 */
					ComposeScreen dialog = new ComposeScreen();
					dialog.show(getFragmentManager(), "ComposeScreen");
					break;										
				default:		
					break;		
				}
			}
		};
		
		navLeft.setOnClickListener(list);
		navRight.setOnClickListener(list);
		navRightTab.setOnClickListener(list);
		navProfileCompose.setOnClickListener(list);
		navProfileNotification.setOnClickListener(list);
		
		/*
		 * Set up the notifications list
		 */
		listv = (ListView)v.findViewById(R.id.timeline_notifications);
		adapter = new NotiAdapter();
		listv.setAdapter(adapter);
		
		setUserDetails();
		compineNotifications();
		
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();	
		try{
			t = new Timer();

			t.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					try {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								refreshNotifications();
							}
						});		
					}catch(Exception ex) {ex.printStackTrace();}
				}
			}, 30000, 60000);	
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		t.cancel();
	}

	private void refreshNotifications() {
		// TODO Get from db
		notiList.clear();
		List<NotiMessage> nots = db.getAllNotification();
		notiList.addAll(nots);
		
		adapter.notifyDataSetChanged();	
	}

	private void compineNotifications() {
		try {
			// TODO Get from db
			List<NotiMessage> nots = db.getAllNotification();
			notiList.addAll(nots);

			adapter.notifyDataSetChanged();	
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Set Custom users details
	 */
	private void setUserDetails() {		
		TextView textName = (TextView) v.findViewById(R.id.textTimelineProfileName);
		if(!getParent().getNameValue().equals(""))
			textName.setText(getParent().getNameValue());

		TextView textLocation = (TextView) v.findViewById(R.id.textTimelineProfileLocation);
		if(!getParent().getHseEstateValue().equals(""))
			textLocation.setText(getParent().getHseEstateValue()+" | "+ getParent().getHseNoValue());
		/**
		 * Change the profile Icon
		 */
		ImageView profile = (ImageView)v.findViewById(R.id.imagePhoto);
		if(getParent().getProfilePhoto() != null && !getParent().getProfilePhoto().equals("")) {
			try {
				Bitmap bitmap = Rounder.getRoundedShape(getParent().getProfilePhoto(), 160, 160);
				profile.setImageBitmap(bitmap);
			}catch(Exception ex) {
				ex.printStackTrace();
				Bitmap bitmap = Rounder.getRoundedShape(R.drawable.avatar, getParent(), 160, 160);
				profile.setImageBitmap(bitmap);
			}
		}else {
			Bitmap bitmap = Rounder.getRoundedShape(R.drawable.avatar, getParent(), 160, 160);
			profile.setImageBitmap(bitmap);
		}
	}

	class NotiAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return notiList.size();
		}

		@Override
		public NotiMessage getItem(int position) {
			return notiList .get(position);
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
			v = getParent().getLayoutInflater().inflate(R.layout.timeline_item_headline,  parent, false);				

			// Set the content to be shown, fetched from the company instance acquired above.
			if(getItem(position) != null) {
				TextView textTimeline_hdlineTitle = (TextView) v.findViewById(R.id.textTimeline_hdlineTitle);
				TextView textTimeline_hdline = (TextView) v.findViewById(R.id.textTimeline_hdline);
				TextView textTimeline_hdlineDate = (TextView) v.findViewById(R.id.textTimeline_hdlineDate);

				textTimeline_hdlineTitle.setText(getItem(position).getSender());
				textTimeline_hdline.setText(getItem(position).getMessage());
				textTimeline_hdlineDate.setText(getItem(position).getTimeDuration());

				// The fonts
				getParent().setFontRegular(textTimeline_hdline);
				getParent().setFontRegular(textTimeline_hdlineDate);
				getParent().setFontSemiBold(textTimeline_hdlineTitle);
				return v;
			}else return v;
		}
	}

}
