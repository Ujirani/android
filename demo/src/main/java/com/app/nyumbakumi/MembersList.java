package com.app.nyumbakumi;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.nyumbakumi.entity.MProfile;
import com.app.nyumbakumi.framework.Act;
import com.app.nyumbakumi.framework.AppController;
import com.app.nyumbakumi.framework.Screen;
import com.app.nyumbakumi.util.CommonUtils;
import com.app.nyumbakumi.utils.Rounder;

import com.app.nyumbakumi.R;

public class MembersList extends Screen {	

	private ListView list;				
	private static ArrayList<MProfile> contactsList = new ArrayList<MProfile>();
	private contactsAdapter adapter;	
	private View v;
	private View imageLeft;
	private View imageRight;
	private Dialog pDialog;
	private View imageAdd;
	
	/**
	 * Returns The MProfile at the indicated Position
	 * @param position Position of the desired MProfile
	 * @return {@link MProfile}
	 */
	public static MProfile getMProfileAt(int position) {
		// TODO Adjust the position such that it kind of goes round
		if(position < 0) position = contactsList.size() - 1;
		if(position > contactsList.size() - 1) position = 0;
		ProfileScreen.position = position;
		
		if(contactsList == null || contactsList.size() <= 0) return null;		
		else return contactsList.get(position);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.members_layout, container, false);
		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textContactsTitle));	
		list = (ListView) v.findViewById(R.id.listContacts);					
		
		adapter = new contactsAdapter();
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				int posit=position;
				int i=0;
				while(i<list.getChildCount()){
					View row=list.getChildAt(i);
					TextView txview = (TextView) row.findViewById(R.id.contactName);
					if(i==posit){
						row.setBackgroundColor(Color.parseColor("#012842"));
						if(txview != null) txview.setTextColor(Color.parseColor("#ffffff")); 
					}else{
						row.setBackgroundColor(Color.parseColor("#ffffff"));
						if(txview != null) txview.setTextColor(Color.parseColor("#012842"));
					}
					i++;
				}
				
				MProfile profile = adapter.getItem(position);
				ProfileScreen profileScreen = ProfileScreen.getInstance(profile, position);
				getParent().switchScreen(MembersList.this, profileScreen);
			}
		});
		
		imageLeft = v.findViewById(R.id.imageMenuLeft);
		imageRight = v.findViewById(R.id.imageMenuRight);
		imageAdd = v.findViewById(R.id.imageViewAdd);
		
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.imageMenuLeft:
					/**
					 * On the left: move to previous
					 */
					getParent().openDrawerLeft();
					break;

				case R.id.imageMenuRight:
					/**
					 * Add members button, move to services
					 */
					getParent().openDrawerRight();		
					break;
				case R.id.imageViewAdd:
					//TODO Open 
					getParent().switchScreen(MembersList.this, ContactScreen.getInstance("MembersList"));
					break;
				default:
					break;
				}
			}
		};
		
		imageLeft.setOnClickListener(listener);
		imageRight.setOnClickListener(listener);
		imageAdd.setOnClickListener(listener);
		
		// TODO Check the user type of the current user
		if(!getParent().getUserRole().equals("Admin")) {
			imageAdd.setVisibility(View.GONE);			
		}else {			
			imageAdd.setVisibility(View.VISIBLE);	
		}
		
		return v;
	}

	/**
	 * Add the list of members onto the contactsList
	 */
	protected void addMembers(ArrayList<MProfile> memberList) {
		contactsList.clear();
		if(memberList.size() > 0) {
			for(MProfile profile: memberList) {
				if(profile != null && profile.getName() != null && !profile.getName().equals("") && 
						profile.getPhone_number() != null && !profile.getPhone_number().equals("")) {
					contactsList.add(profile);
				}
			}
			
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//TODO Check if the member has been added
		boolean isAdded = Act.getIsMemberAdded(getActivity());
		if(isAdded) getMembersList(true); 
		else getMembersList(false);
	}

	@Override
	public void onPause() {
		super.onPause();		
		
	}
	
	class contactsAdapter extends BaseAdapter {
		private View btn;

		@Override
		public int getCount() {
			return contactsList.size();
		}

		@Override
		public MProfile getItem(int position) {
			return contactsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		@Override
		public View getView(final int position, View v, ViewGroup parent) {
			// Get company item from the arraylist at this position
			MProfile profile = getItem(position);
			
			// Right
			v = getParent().getLayoutInflater().inflate(R.layout.contacts_list_holder,  parent, false);				
			
			// Set the content to be shown, fetched from the company instance acquired above.
			if(profile != null && profile.getName() != null && !profile.getName().equals("") && 
					profile.getPhone_number() != null && !profile.getPhone_number().equals("")) {
				TextView name = (TextView) v.findViewById(R.id.contactName);
				TextView location = (TextView) v.findViewById(R.id.contactLocation);
				ImageView profileIcon = (ImageView)v.findViewById(R.id.icon);
				
				if(profile.getItemImage() != null && !profile.getItemImage().equals("")) {
					try {
						//Load the users photo.
						Bitmap bitmap = Rounder.getRoundedShape(profile.getItemImage(), 48, 48);
						profileIcon.setImageBitmap(bitmap);
						profileIcon.setBackground(null);
					}catch(Exception ex) {
						ex.printStackTrace();
						// Back to the Default
						Bitmap bitmap = Rounder.getRoundedShape(R.drawable.avatar, getParent(), 48, 48);
						profileIcon.setImageBitmap(bitmap);
						profileIcon.setBackground(null);
					}
				}else {
					//Load the avatar as the default icon for all users whose icons are missing.
					Bitmap bitmap = Rounder.getRoundedShape(R.drawable.avatar, getParent(), 48, 48);
					profileIcon.setImageBitmap(bitmap);
					profileIcon.setBackground(null);
				}
				
				if(profile.getType().equals("Admin")) {
					v.findViewById(R.id.contactAdmin).setVisibility(View.VISIBLE);
				}
				
				name.setText(profile.getName());
				location.setText(profile.getPhone_number());
				
				// The fonts
				getParent().setFontRegular(name);
				getParent().setFontSemiBold(location);
				//btn = v.findViewById(R.id.imageContactsAdd);
				//btn.setVisibility(View.GONE);
				
				return v;
			}else return v;
		}

	}

	/**
	 * Get the members List from the server.
	 * @param reload boolean Force it to refresh the list of members
	 */
	private void getMembersList(final boolean reload) {
		String group_id = getParent().getGroupValue();
		
		if(group_id != null && !group_id.equals("")) {
			String  serverUrl = CommonUtils.SERVER_URL + "groups/"+group_id+"/members";
			
			Cache cache = AppController.getInstance().getRequestQueue().getCache();
			Entry entry = cache.get(serverUrl);
			if(entry != null && !reload){
				try {
					String data = new String(entry.data, "UTF-8");
					Log.i(TAG, "getMembersList-Cache: "+ data);
					
					JSONArray response = new JSONArray(data);
					ArrayList<MProfile> membersList = CommonUtils.getMembers(response);
					addMembers(membersList);
					
				} catch (UnsupportedEncodingException e) {      
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				// Tag used to cancel the request
				String tag_json_obj = "json_obj_req";
				if(!reload){
					pDialog = Act.getProgressDialog(getParent());
					pDialog.setCancelable(true);					
					pDialog.show();
				}
				
				JsonArrayRequest jsonObjReq = new JsonArrayRequest(serverUrl,
						new Response.Listener<JSONArray>() {
					
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());				
						if(!reload) pDialog.hide();
						ArrayList<MProfile> membersList = CommonUtils.getMembers(response);
						addMembers(membersList);
					}
				}, 
				new Response.ErrorListener() {
					
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						// hide the progress dialog
						if(!reload) pDialog.hide();
					}
				});
				
				// Adding request to request queue
				AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
			}
		}

	}
}
