package com.app.nyumbakumi.framework;

import android.support.v4.app.Fragment;

public class Screen extends Fragment {
	
	public static final String TAG = "ScreenFragment";
    
	/**
	 * Returns the Screen Fragment's parent
	 */
	public Act getParent() {
		return (Act)getActivity();
	}

}
