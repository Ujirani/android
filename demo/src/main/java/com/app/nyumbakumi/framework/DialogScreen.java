package com.app.nyumbakumi.framework;

import android.support.v4.app.DialogFragment;

public class DialogScreen extends DialogFragment {
	
	public static final String TAG = "ScreenFragment";
	
	/**
	 * Returns the Screen Fragment's parent
	 */
	public Act getParent() {
		return (Act)getActivity();
	}

}
