package com.app.nyumbakumi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.TextView;

import com.app.nyumbakumi.framework.Act;
import com.app.nyumbakumi.util.MUserService;
import com.app.nyumbakumi.util.MyAppService;

import com.app.nyumbakumi.R;

public class SplashScreen extends Act {
	/** Duration of wait **/
	private final int SPLASH_DISPLAY_LENGTH = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		setFontRegular((TextView) findViewById(R.id.textSplashCopyright));	
		
		((TextView)findViewById(R.id.textSplashCopyright)).setText(Html.fromHtml("&copy;2014 Ujirani"));

		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				isSetup();
			}
		}, SPLASH_DISPLAY_LENGTH);
	}

	/**
	 * Check if this person has set up his account by checking the following
	 * 1. Profile Set
	 * 2. User verified
	 * 3. User is in Group
	 */
	public void isSetup() {
		boolean isProfileSet = isProfileSet();
		boolean isUserVerified = isVerified();
		boolean isInGroup = isInGroup();

		if(isInGroup && isUserVerified && isInGroup) {
			//TODO Open MainActivity
			Intent intent = new Intent(this, MUserService.class);
			startService(intent);
			Intent intents = new Intent(this, MyAppService.class);
			startService(intents);

			Intent mainActivityIntent = new Intent(SplashScreen.this, MainActivity.class);
			SplashScreen.this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			startActivity(mainActivityIntent);
			SplashScreen.this.finish();
		}else {
			//TODO
			Intent mainIntent = new Intent(SplashScreen.this, SignUpActivity.class);
			//Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
			SplashScreen.this.startActivity(mainIntent);
			SplashScreen.this.finish();
			SplashScreen.this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
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
