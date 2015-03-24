package com.app.nyumbakumi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.nyumbakumi.framework.Act;
import com.app.nyumbakumi.util.CommonUtils;
import com.app.nyumbakumi.util.MUserService;
import com.app.nyumbakumi.util.MyAppService;

import com.app.nyumbakumi.R;

public class VerificationActivity extends Act {
	
	private AsyncTask<Void, Void, Void> mVerification;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verification_layout);
		
		//setFontRegular((Button) findViewById(R.id.btn_verification_submit));
		setFontRegular((EditText) findViewById(R.id.editVerificationCode));
		setFontRegular((TextView) findViewById(R.id.textView1));
		setFontRegular((TextView) findViewById(R.id.textView2));
		
		Button  submit= (Button) findViewById(R.id.btn_verification_submit);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 * Get the phone Number, make sure its not empty
				 * Check if the phone Number exists in the preferences
				 */
				String code = ((EditText) findViewById(R.id.editVerificationCode)).getText().toString();
				if(code.equals("")) {
					((EditText) findViewById(R.id.editVerificationCode)).setError("Verification code required!");
				}else {					
					verify(code);
				}								
			}
		});	
		
		((EditText) findViewById(R.id.editVerificationCode)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == EditorInfo.IME_ACTION_DONE) {
					
					String code = ((EditText) findViewById(R.id.editVerificationCode)).getText().toString();
					if(code.equals("")) {
						((EditText) findViewById(R.id.editVerificationCode)).setError("Verification code required!");
					}else {					
						verify(code);
					}
					
					return true;
				}
				return false;
			}
		});
	}
	
	/**
	 * Check whether the user is signed up
	 * @param verifycode The user's phone number
	 */
	private void verify(final String verifycode) {
		mVerification = new AsyncTask<Void, Void, Void>() {

			private Dialog pDialog;
			private Bundle bun = null;
			private boolean isConnected = false;
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// Showing progress dialog
				pDialog = getProgressDialog();
				pDialog.setCancelable(true);
				pDialog.setOnCancelListener(new OnCancelListener() {					

					@Override
					public void onCancel(DialogInterface dialog) {						
					}
				});
				pDialog.show();
			}
			
			@Override
			protected Void doInBackground(Void... params) {
				try {
					String userid = getTemporaryUserID();
					
					if(CommonUtils.isConnected(VerificationActivity.this)) {
						isConnected = true;
						bun = com.app.nyumbakumi.util.CommonUtils.verify(verifycode, userid);	
					}else {
						isConnected = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (pDialog.isShowing())
					pDialog.dismiss();
				
				// Check the reponse
				if(bun != null) {
					// Bundle isn't null, check the parameters
					boolean is_verified = bun.getBoolean(com.app.nyumbakumi.util.CommonUtils._USER_VERIFIED, false);
					String status = bun.getString(com.app.nyumbakumi.util.CommonUtils.STATUS, "");
					
					if(is_verified) {
						setIsVerified(is_verified);
						toast(status);
						
						if(isProfileSet()) {
							Intent intent = new Intent(VerificationActivity.this, MUserService.class);
							startService(intent);

							if(!isInGroup()) {
								Intent intents = new Intent(VerificationActivity.this, MyAppService.class);
								startService(intents);

								switchScreen(new CreateGroup());
							}else {
								// Users profile is set, proceed to the timeline
								Intent mainIntent = new Intent(VerificationActivity.this, MainActivity.class);
								VerificationActivity.this.startActivityForResult(mainIntent, 10200);
								VerificationActivity.this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
							}
						}else {
							// Profile Not set, proceed to the complete profile page
							Intent intent = new Intent(VerificationActivity.this, MyAppService.class);
							startService(intent);
							
							//switchScreen(new CompleteProfile());
							Intent mainIntent = new Intent(VerificationActivity.this, ProfilePhoto.class);
							VerificationActivity.this.startActivityForResult(mainIntent, 10200);
							VerificationActivity.this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
						}																
					}else {
						/**
						 * Remain Here...
						 */
						toast(status);
					}
				}else {
					if(!isConnected) toast("Network not available..!");
					else toast("Sorry, could not verify your account, please try again..):-");
				}
				mVerification = null;

			}
		};
		mVerification.execute(null, null, null);

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
	protected void onActivityResult(int requestCode , int result, Intent data) {
		if(requestCode == 10200) {
			finish();
		}
	}
	
}
