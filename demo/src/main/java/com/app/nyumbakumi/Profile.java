package com.app.nyumbakumi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.app.nyumbakumi.framework.Act;

import com.app.nyumbakumi.R;

public class Profile extends Act {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);

		setFontRegular((TextView) findViewById(R.id.textProfileTitle));		
		setFontRegular((TextView) findViewById(R.id.textProfileComments));		
		setFontRegular((TextView) findViewById(R.id.textProfileLikes));		
		setFontRegular((TextView) findViewById(R.id.textProfileViews));	

		setFontRegular((TextView) findViewById(R.id.textProfileName));		
		setFontRegular((TextView) findViewById(R.id.textProfileDescription));		
		setFontRegular((TextView) findViewById(R.id.textProfileLocation));		
		setFontRegular((TextView) findViewById(R.id.textProfileProffession));	
		
		View pleft = findViewById(R.id.imageProfileLeft);
		View pright = findViewById(R.id.imageProfileRight);
		
		OnClickListener list = new OnClickListener() {
			Intent intent = new Intent(Profile.this, ContactScreen.class);

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.imageProfileLeft:
					startActivity(intent);
					break;
				case R.id.imageProfileRight:
					startActivity(intent);					
					break;					
				default:		
					break;		
				}
			}
		};

		pleft.setOnClickListener(list);
		pright.setOnClickListener(list);
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
