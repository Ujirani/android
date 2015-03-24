package com.app.nyumbakumi.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.app.nyumbakumi.SignUpActivity;

public class SignUpTest extends ActivityInstrumentationTestCase2<SignUpActivity> {
	
	private SignUpActivity signUpActivity;
	public SignUpTest() {
		super(SignUpActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		signUpActivity = getActivity();
	}

}
