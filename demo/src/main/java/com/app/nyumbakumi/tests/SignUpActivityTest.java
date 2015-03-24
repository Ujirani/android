/**
 * 
 */
package com.app.nyumbakumi.tests;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.app.nyumbakumi.MainActivity;
import com.app.nyumbakumi.SignUpActivity;
import com.robotium.solo.Solo;

import com.app.nyumbakumi.R;

/**
 * @author MATIVO-PC
 *
 */
public class SignUpActivityTest extends ActivityInstrumentationTestCase2<SignUpActivity> {

	public SignUpActivityTest(Class<SignUpActivity> activityClass) {
		super(activityClass);
	}

	private Solo solo;

	public SignUpActivityTest() {
		super(SignUpActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
	
	public void testACompleteProfile() throws Exception {
		// check that we have the right activity
		solo.assertCurrentActivity("wrong activity", SignUpActivity.class);
		
		EditText phoneNum = (EditText) solo.getView(R.id.editPhone);
		//solo.enterText(phoneNum, "0712270408");
		solo.typeText(phoneNum, "0712270415");
		
		// Enter phone number 
		solo.clickOnButton(solo.getString(com.app.nyumbakumi.R.string.submit));
		
		/**
		 * Complete Profile
		 */
		//solo.clickOnButton("Select profile photo");
		//solo.waitForActivity("NONESA", 12000);
		
		solo.typeText((EditText) solo.getView(R.id.editProfileName), "Mimi");
		//solo.typeText((EditText) solo.getView(R.id.editProfileIdNumber), "27652330");
		solo.typeText((EditText) solo.getView(R.id.editProfileHseEstate), "KOMAROCK PHASE 1");
		solo.typeText((EditText) solo.getView(R.id.editProfileHseNo), "KOM PH1 230 A");
		
		solo.clickOnButton("Next");				
		//solo.waitForActivity("ACTIVITIESA", 5000);
		solo.waitForFragmentByTag("CreateGroup", 6000);
		
		/**
		 * Create Group Settings
		 */
		solo.typeText((EditText) solo.getView(R.id.editGroupName), "JKUAT ECE 2015");
		solo.typeText((EditText) solo.getView(R.id.editGroupDescription), "This is the group description.\nNothing Much to write about.");
		solo.clickOnButton("Next ");
		
		assertTrue(solo.waitForLogMessage("GROUP CREATED.", 10000));
		
		//solo.waitForFragmentByTag("ContactScreen", 10000);		
		
	}
	
	public void test1BSignUpWorks() throws Exception {
		// check that we have the right activity
		solo.assertCurrentActivity("wrong activity", SignUpActivity.class);

		EditText phoneNum = (EditText) solo.getView(R.id.editPhone);
		//solo.enterText(phoneNum, "0712270408");
		solo.typeText(phoneNum, "0712270408");

		// Enter phone number 
		solo.clickOnButton(solo.getString(com.app.nyumbakumi.R.string.submit));

		//solo.assertCurrentActivity("wrong activity: Not MainActivity", MainActivity.class);
		//solo.goBack();
		boolean isMain = solo.waitForActivity(MainActivity.class, 12000);
		if(isMain) {
			Log.i("NYUMBAKUMI-TESTING", "Main Activity Opened");
		}

		solo.waitForActivity("ACTIVITIES", 4000);
		//solo.sendKey(Solo.MENU);
		solo.assertCurrentActivity("wrong activity", MainActivity.class);
	     
	    //assertTrue(solo.waitForText("NOTIFICATIONS"));
	    /*ListView listView = (ListView) solo.getView(R.id.left_drawer);
	    
	    for(int i=1; i < listView.getAdapter().getCount(); i++){
	        solo.clickOnView(getViewAtIndex(listView, i, getInstrumentation()));
	    }*/
		testOpenAndCloseNavigationDrawer();
		solo.waitForActivity("ACTIVITIESC", 60000);
	}
	
	
	private boolean testOpenAndCloseNavigationDrawer() {
		solo.assertCurrentActivity("wrong activity", MainActivity.class);
	     
	    //assertTrue(solo.waitForText("NOTIFICATIONS"));
	    ListView listView = (ListView) solo.getView(R.id.left_drawer);
	    /*
	    for(int i=1; i < listView.getAdapter().getCount(); i++){
	        solo.clickOnView(getViewAtIndex(listView, i, getInstrumentation()));
	    }
	    */
	    /*listView.getChildAt(0);
	    solo.clickInList(1);
		solo.typeText((EditText) solo.getView(R.id.editComposeMessage), "This is a Sample notification message");
		solo.clickOnButton("Send");
	    solo.waitForActivity("NONES", 6000);
	    
	    *//**
	     * Second test
	     *//*
	    solo.sendKey(Solo.MENU);
	    listView.getChildAt(1);
	    solo.clickInList(2);
	    solo.waitForActivity("NONEA", 6000);*/
	    
	    /**
	     * Third test
	     */
	    solo.sendKey(Solo.MENU);
	    listView.getChildAt(2);
	    solo.clickInList(3);
	    solo.waitForActivity("NONEB", 6000);
	    
	    solo.clickOnView(solo.getView(R.id.imageMenuRight));	    
	    solo.clickInList(1,1);
	    	   
	    return true;
	    //solo.clickOnView(getViewAtIndex(listView, 0, getInstrumentation()));
	    
	}
	
	public View getViewAtIndex(final ListView listElement, final int indexInList, Instrumentation instrumentation) {
	    ListView parent = listElement;
	    if (parent != null) {
	        if (indexInList <= parent.getAdapter().getCount()) {
	            scrollListTo(parent, indexInList, instrumentation);
	            int indexToUse = indexInList - parent.getFirstVisiblePosition();
	            return parent.getChildAt(indexToUse);
	        }
	    }
	    return null;
	}
	
	public <T extends AbsListView> void scrollListTo(final T listView,
	        final int index, Instrumentation instrumentation) {
	    instrumentation.runOnMainSync(new Runnable() {
	        @Override
	        public void run() {
	            listView.setSelection(index);
	        }
	    });
	    instrumentation.waitForIdleSync();
	}

}
