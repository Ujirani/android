package com.app.nyumbakumi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.app.nyumbakumi.entity.NotiMessage;
import com.app.nyumbakumi.util.CommonUtils;
import com.app.nyumbakumi.utils.DatabaseHelper;

import com.app.nyumbakumi.R;

public class ComposeScreen extends com.app.nyumbakumi.framework.DialogScreen {
	private View root;
	private EditText textMessage;
	private View submit_btn;
	private AsyncTask<Void, Void, Void> mCreateNotification;
	
	public static ComposeScreen newInstance() {
		ComposeScreen f = new ComposeScreen();
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (getDialog() != null) {
			getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		}
		
		root = inflater.inflate(R.layout.compose_layout, container, false);
		
		initUI();

		return root;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		getDialog().getWindow()
		.getAttributes().windowAnimations = R.style.DialogAnimation;

	}
	
	public void initUI() {
		textMessage = (EditText) root.findViewById(R.id.editComposeMessage); 
		submit_btn = root.findViewById(R.id.btn_next);
		
		getParent().setFontRegular((TextView) root.findViewById(R.id.editComposeMessage));		
		getParent().setFontRegular((TextView) root.findViewById(R.id.textCompleteProfile));		
		getParent().setFontRegular((TextView) root.findViewById(R.id.btn_next));		
				
		/**
		 * Onclick Listener
		 */
		OnClickListener notilist = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 * Submit the content to the server, then close this dialog
				 */
				if(textMessage.getText().toString().equalsIgnoreCase("")) {
					textMessage.setError("Message required!");
				}else {
					/**
					 * Message is valid, exit the dialog
					 */
					sendNotification(textMessage.getText().toString());
				}
			}
		};
		
		// TODO A user can also click the 'Enter' Button instead of button.
		((EditText) root.findViewById(R.id.editComposeMessage)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == EditorInfo.IME_ACTION_DONE) {
					
					if(textMessage.getText().toString().equalsIgnoreCase("")) {
						textMessage.setError("Message required!");
					}else {
						/**
						 * Message is valid, exit the dialog
						 */
						sendNotification(textMessage.getText().toString());
					}										
					return true;
				}
				return false;
			}
		});
		
		submit_btn.setOnClickListener(notilist);		
	}

	private void sendNotification(final String message) {
		mCreateNotification = new AsyncTask<Void, Void, Void>() {
			
			private Dialog pDialog;
			private boolean isComplete = false;
			private boolean isConnected;
			private DatabaseHelper db;

			@Override
 			protected void onPreExecute() {
				super.onPreExecute();
				db = new DatabaseHelper(getParent());
				
				// Showing progress dialog
				pDialog = getParent().getProgressDialog();
				//pDialog.setMessage("We are creating a new Group for you...");
				pDialog.setCancelable(true);
				//pDialog.setContentView(R.layout.custom_dialog);
				pDialog.setOnCancelListener(new OnCancelListener() {					

					@Override
					public void onCancel(DialogInterface dialog) {
						/**
						 * Nothing for the time being
						 */
					}
				});
				pDialog.show();
			}
			
			@Override
			protected Void doInBackground(Void... params) {
				try {
					if(CommonUtils.isConnected(getParent())) {
						isConnected = true;
						String group = getParent().getGroupValue();
						String user = getParent().getTemporaryUserID();
						isComplete = com.app.nyumbakumi.util.CommonUtils.createNotification(user, message, group);						
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
				if(isComplete) {
					dismiss();
					getParent().toast("Notification has been send successfully!");
					
					// TODO Add the notification to our db list
					try {
						NotiMessage noti =  new NotiMessage("me", message, "0 mins ago.");
						db.createNotification(noti);
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}else {					
					if(!isConnected) getParent().toast("Network not available..!");
					else getParent().toast("Sorry, could not send notification, please try again...");					
				}
				mCreateNotification = null;
			}
		};
		mCreateNotification.execute(null, null, null);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart() {
		super.onStart();

		// change dialog width
		if (getDialog() != null) {

			int fullWidth = getDialog().getWindow().getAttributes().width;

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
				Display display = getActivity().getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				fullWidth = size.x;
			} else {
				Display display = getActivity().getWindowManager().getDefaultDisplay();
				fullWidth = display.getWidth();
			}

			final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
					.getDisplayMetrics());

			int w = fullWidth - padding;
			int h = getDialog().getWindow().getAttributes().height;

			getDialog().getWindow().setLayout(w, h);
		}
	}
}
