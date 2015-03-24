package com.app.nyumbakumi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.nyumbakumi.framework.Act;

import com.app.nyumbakumi.R;

public class Login extends Act {
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		setFontRegular((TextView) findViewById(R.id.textLogin));
		setFontRegular((EditText) findViewById(R.id.editLoginPassword));
		setFontRegular((EditText) findViewById(R.id.editLoginUsername));	
		
		Button  submit= (Button) findViewById(R.id.btnLogin);
		setFontRegular(submit);
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Open a new activity
				startActivity(new Intent(Login.this, SignUpActivity.class));
                Login.this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			}
		});
		
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
