package com.app.nyumbakumi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;

import com.app.nyumbakumi.R;


public class SplashActivity extends ActionBarActivity {

	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		getSupportActionBar().hide();

		new Handler().postDelayed(new Runnable() {
			public void run() {
				i = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(i);
			}
		}, 3000);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
