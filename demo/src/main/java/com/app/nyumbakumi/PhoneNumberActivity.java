package com.app.nyumbakumi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.app.nyumbakumi.R;

public class PhoneNumberActivity extends ActionBarActivity {

	Intent i;
	Button bEstate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.phone_number);
		getSupportActionBar().hide();

		setUp();
	}

	private void setUp() {
		// TODO Auto-generated method stub
		bEstate = (Button) findViewById(R.id.bSubmit);
		bEstate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				i = new Intent(getApplicationContext(), EstateActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
