package com.app.nyumbakumi;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nyumbakumi.entity.MProfile;
import com.app.nyumbakumi.framework.Screen;
import com.app.nyumbakumi.utils.Rounder;

import com.app.nyumbakumi.R;

public class ProfileScreen extends Screen {
	private View v;
	private static MProfile profile;
	public static int position;

	/**
	 * Get an instance of ProfileScreen
	 * @param profile MProfile
	 * @return ProfileScreen
	 */
	public static ProfileScreen getInstance(MProfile profile, int position) {
		ProfileScreen profScreen = new ProfileScreen();
		Bundle bun = new Bundle();
		bun.putSerializable("profile", profile);
		bun.putInt("position", position);
		profScreen.setArguments(bun);
		return profScreen;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			profile = (MProfile) getArguments().getSerializable("profile");
			position = getArguments().getInt("position", 0);
		}catch(NullPointerException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.profile_layout, container, false);
		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textProfileTitle));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textProfileComments));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textProfileLikes));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textProfileViews));	
		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textProfileName));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textProfileDescription));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textProfileLocation));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textProfileProffession));	
		
		View pleft = v.findViewById(R.id.imageProfileLeft);
		View pright = v.findViewById(R.id.imageProfileRight);
		
		View navLeft = v.findViewById(R.id.imageLaunchDrawerLeft);
		View navRight = v.findViewById(R.id.imageLaunchDrawerRight);
		View navRightTab = v.findViewById(R.id.profile_footer_likes);
		
		View navProfileNotification = v.findViewById(R.id.menu_footer_notification);
		View navProfileCompose = v.findViewById(R.id.menu_footer_compose);
		
		OnClickListener list = new OnClickListener() {
			Intent intent = new Intent(ProfileScreen.this.getParent(), ContactScreen.class);
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.imageProfileLeft:
					position--;
					profile = MembersList.getMProfileAt(position);
					setUserDetails();
					break;
				case R.id.imageProfileRight:
					position++;
					profile = MembersList.getMProfileAt(position);
					setUserDetails();
					break;	
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
					 * Opens Timeline
					 */
					getParent().switchScreen(new TimeLine());
					break;	
				case R.id.menu_footer_compose:
					/**
					 * Open Compose Screen
					 */
					ComposeScreen dialog = new ComposeScreen();
					dialog.show(getFragmentManager(), "ComposeScreen");
					break;										
				default:		
					break;		
				}
			}
		};

		pleft.setOnClickListener(list);
		pright.setOnClickListener(list);

		navLeft.setOnClickListener(list);
		navRight.setOnClickListener(list);
		navRightTab.setOnClickListener(list);

		navProfileCompose.setOnClickListener(list);
		navProfileNotification.setOnClickListener(list);

		setUserDetails();

		return v;
	}

	/**
	 * Set Custom users details
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setUserDetails() {
		TextView textName = (TextView) v.findViewById(R.id.textProfileName);
		TextView textLocation = (TextView) v.findViewById(R.id.textProfileLocation);
		
		if(profile != null) {
			textName.setText(profile.getName());
			textLocation.setText(profile.getHseEstateValue()+" | "+ profile.getHseNoValue());
			textName.setVisibility(View.VISIBLE);		
			textLocation.setVisibility(View.VISIBLE);	
			
			if(getParent().getNameValue() == null || getParent().getNameValue().equals(""))
				textName.setVisibility(View.INVISIBLE);			
			if(getParent().getHseEstateValue() == null || getParent().getHseEstateValue().equals(""))
				textLocation.setVisibility(View.INVISIBLE);	
		}else {
			if(getParent().getNameValue().equals(""))
				textName.setVisibility(View.INVISIBLE);			
			if(getParent().getHseEstateValue().equals(""))
				textLocation.setVisibility(View.INVISIBLE);	
		}
		
		/**
		 * Change the profile Icon
		 */
		ImageView profile = (ImageView)v.findViewById(R.id.imagePhoto);
		if(position == 0 && getParent().getProfilePhoto() != null && !getParent().getProfilePhoto().equals("")) {
			Bitmap bitmap = Rounder.getRoundedShape(getParent().getProfilePhoto(), 160, 160);
			profile.setImageBitmap(bitmap);
		}else {
			Bitmap bitmap = Rounder.getRoundedShape(R.drawable.avatar, getParent(), 160, 160);
			profile.setImageBitmap(bitmap);
			profile.setBackground(null);
		}
	}

}
