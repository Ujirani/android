package com.app.nyumbakumi;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.app.nyumbakumi.entity.AppService;
import com.app.nyumbakumi.framework.Screen;
import com.app.nyumbakumi.util.CommonUtils;
import com.app.nyumbakumi.util.MyAppService;

import com.app.nyumbakumi.R;

public class CompleteGroup extends Screen {
	
	private View v;
	private ListView listsc1, listsc2, listsc3, listsc4;
	private AsyncTask<Void, Void, Void> mAddServices;
	private String groupId;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.group_services_layout, container, false);
		
		// Group ID 
		groupId = getArguments().getString("GROUPID");
		
		getParent().setFontRegular((Button) v.findViewById(R.id.btn_next));
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCreateGroup));
		
		getParent().setFontRegular((TextView) v.findViewById(R.id.text_btn_group_info));
		getParent().setFontRegular((TextView) v.findViewById(R.id.text_btn_group_members));
		getParent().setFontRegular((TextView) v.findViewById(R.id.text_btn_group_services));
		
		((TextView) v.findViewById(R.id.text_btn_group_info)).setBackgroundResource(R.drawable.group_title_selected);
		v.findViewById(R.id.linear_layout_group_title).setBackgroundResource(R.drawable.group_title_selected);
		v.findViewById(R.id.linear_layout_group_outer).setBackgroundResource(R.drawable.group_title_selected);					

		Button btnNext = (Button) v.findViewById(R.id.btn_next);

		OnClickListener list = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_next:
					SparseBooleanArray secArr = listsc1.getCheckedItemPositions();
					SparseBooleanArray healArr = listsc2.getCheckedItemPositions();
					SparseBooleanArray fireArr = listsc3.getCheckedItemPositions();
					
					AppService sec = new AppService();
					AppService health = new AppService();
					AppService fire = new AppService();
					
					try{
						if(MyAppService.securityList != null && secArr != null && secArr.size() > 0 && secArr.valueAt(0)) {
							int position = secArr.keyAt(0);
							sec = MyAppService.securityList.get(position);
							Log.i(TAG, sec.getName());
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
					try {
						if(MyAppService.healthList != null && healArr != null && healArr.size() > 0 && healArr.valueAt(0)) {
							int position = healArr.keyAt(0);
							health = MyAppService.healthList.get(position);
							Log.i(TAG, health.getName());
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
					try{
						if(MyAppService.fireList != null && fireArr != null && fireArr.size() > 0 && fireArr.valueAt(0)) {
							int position = fireArr.keyAt(0);
							fire = MyAppService.fireList.get(position);
							Log.i(TAG, fire.getName());
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
					ArrayList<AppService> services = new ArrayList<AppService>();
					services.add(sec);
					services.add(fire);
					services.add(health);
					
					addServiceToGroup(services);
					
					break;
				default:
					break;
				}
			}
		};
		
		btnNext.setOnClickListener(list);
		
		listsc1 = (ListView) v.findViewById(R.id.group_list_sec1);
		listsc2 = (ListView) v.findViewById(R.id.group_list_sec2);
		listsc3 = (ListView) v.findViewById(R.id.group_list_sec3);

		initList(1);
		initList(2);
		initList(3);		
		return v;
	}

	private void addServiceToGroup(final ArrayList<AppService> services) {
		mAddServices = new AsyncTask<Void, Void, Void>() {

			private Dialog pDialog;
			private boolean isComplete = false;
			private boolean isConnected;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
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
					String group_id = getParent().getGroupValue();
					if(CommonUtils.isConnected(getParent()) && group_id != null && !group_id.equals("")) {
						isConnected = true;
						isComplete = com.app.nyumbakumi.util.CommonUtils.AddGroupServices(group_id, services);						
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
					/**
					 * Save the temporary value in the preferences
					*/
					String phone = getParent().getTemporaryValue();
					if(!phone.equals("")) {
						// Save the value
						getParent().setRegistered(phone);
					}
										
					Intent mainIntent = new Intent(getParent(), MainActivity.class);
					getParent().startActivityForResult(mainIntent, 10200);
					getParent().overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
					getParent().finish();
					
				}else {					
					if(!isConnected) getParent().toast("Network not available..!");
					else getParent().toast("Sorry, could not add contacts to group, please try again...");					
				}
				mAddServices = null;
			}
		};
		mAddServices.execute(null, null, null);
	}
	
	/**
	 * Initialise Lists for the accordion.
	 */
	private void initList(int num) {
		/**
		 * Set up the adapters
		 */
		switch (num) {
		case 1:
			if(MyAppService.securityList != null && MyAppService.securityList.size() > 0) {
				ArrayList<String> secList = getList(MyAppService.securityList);
				listsc1.setAdapter(new ArrayAdapter<String>(getParent(), R.layout.accordion_item_layout, secList));
			}else 
				listsc1.setAdapter(new ArrayAdapter<String>(getParent(), R.layout.accordion_item_layout, 
						getResources().getStringArray(R.array.accordion_string_1)));
			break;
		case 2:
			if(MyAppService.healthList != null && MyAppService.healthList.size() > 0) {
				ArrayList<String> secList = getList(MyAppService.healthList);
				listsc2.setAdapter(new ArrayAdapter<String>(getParent(), R.layout.accordion_item_layout, secList));
			}else 
				listsc2.setAdapter(new ArrayAdapter<String>(getParent(), R.layout.accordion_item_layout, 
						getResources().getStringArray(R.array.accordion_string_2)));
			break;
		case 3:
			if(MyAppService.fireList != null && MyAppService.fireList.size() > 0) {
				ArrayList<String> secList = getList(MyAppService.fireList);
				listsc3.setAdapter(new ArrayAdapter<String>(getParent(), R.layout.accordion_item_layout, secList));
			}else 
				listsc3.setAdapter(new ArrayAdapter<String>(getParent(), R.layout.accordion_item_layout, 
						getResources().getStringArray(R.array.accordion_string_3)));
			break;			
		default:
			break;
		}	

	}
	
	/**
	 * 
	 * @param securityList List of Services
	 * @return ArrayList<String>
	 */
	private ArrayList<String> getList(ArrayList<AppService> securityList) {
		ArrayList<String> list = new ArrayList<String>();
		for (AppService svs: securityList) {
			list.add(svs.getName());
		}
		return list;
	}
	
	class AccordionAdapter extends BaseAdapter {
		private View btn;
		private String[] section;

		public AccordionAdapter(int num_section){
			switch (num_section) {
			case 1:
				section = getResources().getStringArray(R.array.accordion_string_1);
				break;
			case 2:
				section = getResources().getStringArray(R.array.accordion_string_2);

				break;
			case 3:
				section = getResources().getStringArray(R.array.accordion_string_3);

				break;
			case 4:
				section = getResources().getStringArray(R.array.accordion_string_4);

				break;				
			default:
				break;
			}
		}

		@Override
		public int getCount() {
			return section.length;
		}

		@Override
		public String getItem(int position) {
			return section[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View v, ViewGroup parent) {
			// Right
			v = getParent().getLayoutInflater().inflate(R.layout.contacts_list_holder,  parent, false);				

			// Set the content to be shown, fetched from the company instance acquired above.
			if(section[position] != null) {
				TextView name = (TextView) v.findViewById(R.id.contactName);

				return v;
			}else return null;
		}

	}

}
