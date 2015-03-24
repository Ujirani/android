package com.app.nyumbakumi.util;
/**
 * @author Gregory Murimi
 */
import java.io.File;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.app.nyumbakumi.entity.AppService;
import com.app.nyumbakumi.entity.MNotification;
import com.app.nyumbakumi.entity.MProfile;
import com.app.nyumbakumi.entity.MyContacts;

public class CommonUtils {
	public static final String SERVER_URL = "http://41.242.2.154:3001/"; 

	private static final String TAG = "CommonUtils";
	public static final String _USER_IS_IN_GROUP = "_USER_IS_IN_GROUP";
	public static final String _USER_PROFILE_SETUP = "_USER_PROFILE_SETUP";
	public static final String _USER_VERIFIED = "_USER_VERIFIED";
	public static final String _USER_ID = "_USER_ID";

	public static final String STATUS = "STATUS";
	public static final String _VERIFICATION_CODE = "_VERIFICATION_CODE";

	public static String message;
	
	/**
	 * Issue a POST request to the server and returns a message from the server
	 *
	 * @param endpoint POST address.
	 * @param //params request parameters.
	 * @return String data from the server
	 *
	 * @throws java.io.IOException propagated from POST.
	 */
	private static String postAndGetData(String endpoint) {
		HttpEntity resEntity;    	
		try {
			HttpClient client = new DefaultHttpClient();

			HttpPost post = new HttpPost(endpoint);

			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();
			final String response_str = EntityUtils.toString(resEntity);

			if (resEntity != null) {
				return response_str;
			}
		} catch (Exception ex) {
			Log.e("Debug", "error: " + ex.getMessage(), ex);
		}
		return "";
	}

	/**
	 * Send a signup request for the user with the given phone number
	 * 
	 * @param phone_number The user's phone number
	 * @param device 
	 * @return Bundle {is_in_group, is_profile_set, _is_verified, user_id}
	 */
	public static Bundle signup(String phone_number, String device) {
		HttpEntity resEntity;    
		String  serverUrl = SERVER_URL + "sign_up";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(serverUrl);

			MultipartEntity entity = new MultipartEntity();

			entity.addPart("sign_up", new StringBody("sign_up")); 
			entity.addPart("number", new StringBody(phone_number));
			entity.addPart("registration_id", new StringBody(device)); 

			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();

			final String response_str = EntityUtils.toString(resEntity).trim();
			message = response_str;
			Log.i(TAG, "LOGIN RESPONSE: "+ response_str);
			
			/**
			 * Parse the response here, json format
			 */
			JSONObject verifyJson = new JSONObject(response_str);	
			String user_id = verifyJson.getString("user_id");
			boolean is_in_a_group = verifyJson.getBoolean("is_in_a_group");
			boolean profile_setup = verifyJson.getBoolean("profile_setup");
			boolean verified = verifyJson.getBoolean("verified");
			String verification_code = verifyJson.getString("verification_code");
			
			Bundle bun = new Bundle();
			bun.putBoolean(_USER_IS_IN_GROUP, is_in_a_group);
			bun.putBoolean(_USER_PROFILE_SETUP, profile_setup);
			bun.putBoolean(_USER_VERIFIED, verified);
			bun.putString(_USER_ID, user_id);
			bun.putString(_VERIFICATION_CODE, verification_code);
			
			return bun;
		} catch (Exception ex) {
			Log.e("Debug@sign_up", "error@sign_up: " + ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * Verify User
	 * @param code
	 * @return Bundle
	 */
	public static Bundle verify(String code, String user) {
		HttpEntity resEntity;    
		String  serverUrl = SERVER_URL + "verify";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(serverUrl);

			MultipartEntity entity = new MultipartEntity();

			entity.addPart("verification_code", new StringBody(code)); 
			entity.addPart("id", new StringBody(user)); 

			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();

			final String response_str = EntityUtils.toString(resEntity).trim();
			message = response_str;
			Log.i(TAG, "verify: "+ response_str);

			/**
			 * Parse the response here, json format
			 */
			JSONObject verifyJson = new JSONObject(response_str);	
			String user_id = verifyJson.getString("id");
			String status = verifyJson.getString("status");
			boolean verified = verifyJson.getBoolean("verified");

			Bundle bun = new Bundle();
			bun.putBoolean(_USER_VERIFIED, verified);
			bun.putString(STATUS, status);
			return bun;
		} catch (Exception ex) {
			Log.e(TAG, "error@verify: " + ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * Update the user's profile
	 * 
	 * @param user_id The User ID as received in the sign up stage
	 * @param name The name of the user
	 * @param phone_number The user's phone number
	 * @param //id_number The user's ID number
	 * @param selectedImagePath The Profile Photo Path
	 * @param idNumber 
	 * @return boolean true | false
	 */
	public static boolean updateProfile(String user_id, String name, String phone_number, String selectedImagePath, String hse_estate, String hse_no, String idNumber) {
		HttpEntity resEntity;    
		String  serverUrl = SERVER_URL + "users/"+user_id+".json";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPut post = new HttpPut(serverUrl);
			
			MultipartEntity entity = new MultipartEntity();
			
			entity.addPart("user[name]", new StringBody(name)); 
			entity.addPart("user[id_number]", new StringBody(idNumber)); 
			entity.addPart("user[house_name]", new StringBody(hse_estate)); 
			entity.addPart("user[house_number]", new StringBody(hse_no)); 
			File photoFile = new File(selectedImagePath);
			entity.addPart("photo", new FileBody(photoFile));
			
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();
			
			final String response_str = EntityUtils.toString(resEntity).trim();
			message = response_str;
			Log.i(TAG, "UPDATE PROFILE RESPONSE: "+ response_str);
			
			/**
			 * Parse the response here, json format
			 */
			JSONObject verifyJson = new JSONObject(response_str);	
			String id = verifyJson.getString("id");
			if(id.equals(user_id)) {
				return true;
			}else return false;
		} catch (Exception ex) {
			Log.e(TAG, "error@update Profile: " + ex.getMessage(), ex);
			return false;
		}
	}

	/**
	 * Create a new Group. Sent its parameters to the server
	 * @param userid The user's id
	 * @param group_name The Group's Name
	 * @param group_location The Group's location
	 * @param more_details More details about the group
	 * @return TRUE | FALSE
	 */
	public static String createGroup(String userid, String group_name, String group_location, String more_details) {
		HttpEntity resEntity;    
		String  serverUrl = SERVER_URL + "groups.json";
		JSONObject data = getGroupDetails(group_name, group_location, more_details, userid);

		try {
			Log.i(TAG, data.toString(4));
			String json = data.toString();

			StringEntity se = new StringEntity(json); 

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(serverUrl);
			post.setEntity(se);

			// Some headers for the json format
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");

			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();

			final String response_str = EntityUtils.toString(resEntity).trim();
			Log.i(TAG, "UPDATE GROUP RESPONSE: "+ response_str);

			/**
			 * Parse the response here, json format
			 */
			JSONObject verifyJson = new JSONObject(response_str);	
			String id = verifyJson.getString("id");
			String status = verifyJson.getString("status");
			if(status.equals("success")) {
				//if(id.equals(user_id)) {
				return id;
			}else {
				message = response_str;
				return null;
			}
		} catch (Exception ex) {
			Log.e("Debug@createGroup", "error@update Group: " + ex.getMessage(), ex);
			return null;
		}
	}
	
	/**
	 * Fetch A list of services and companies
	 * @return Object array
	 */
	public static Object[] getServicesNCompanies() {
		HttpEntity resEntity;    
		String  serverUrl = SERVER_URL + "services_and_companies";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet post = new HttpGet(serverUrl);
			
			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();
			
			final String response_str = EntityUtils.toString(resEntity).trim();
			Log.i(TAG, "SERVICES AND COMPANIES: "+ response_str);
			
			JSONObject obj = new JSONObject(response_str);
			
			JSONArray security = obj.getJSONArray("Security");
			JSONArray health = obj.getJSONArray("Ambulance");
			JSONArray fire = obj.getJSONArray("Fire Station");

			ArrayList<AppService> securityList = new ArrayList<AppService>();
			ArrayList<AppService> healthList = new ArrayList<AppService>();
			ArrayList<AppService> fireList = new ArrayList<AppService>();
			
			for (int i = 0; i < security.length(); i++) {
				JSONObject c = security.getJSONObject(i);

				String id = c.getString("id"),
						name = c.getString("name"),
						service_id = c.getString("service_id"),
						location = c.getString("location"),
						phone_number = c.getString("phone_number");

				securityList.add(new AppService(id, name, service_id, location, phone_number));
			}

			for (int i = 0; i < health.length(); i++) {
				JSONObject c = health.getJSONObject(i);

				String id = c.getString("id"),
						name = c.getString("name"),
						service_id = c.getString("service_id"),
						location = c.getString("location"),
						phone_number = c.getString("phone_number");

				healthList.add(new AppService(id, name, service_id, location, phone_number));
			}

			for (int i = 0; i < fire.length(); i++) {
				JSONObject c = fire.getJSONObject(i);

				String id = c.getString("id"),
						name = c.getString("name"),
						service_id = c.getString("service_id"),
						location = c.getString("location"),
						phone_number = c.getString("phone_number");

				fireList.add(new AppService(id, name, service_id, location, phone_number));
			}
			Object[] data = {true, securityList, healthList, fireList};
			return data;
		} catch (Exception ex) {
			Log.e(TAG, "error: " + ex.getMessage(), ex);
			Object[] data = {false};
			return data;
		}
	}	

	/**
	 * Add members to a certain group
	 * @param group_id Group ID as from the group creation stage
	 * @param contacts List of selected contacts to be added to this group
	 * @return boolean TRUE | FALSE
	 */
	public static boolean AddGroupMembers(String group_id, ArrayList<MyContacts> contacts) {
		HttpEntity resEntity;    
		String  serverUrl = SERVER_URL + "add_members.json";
		JSONObject data = getJsonMembersData(group_id, contacts);		

		try {
			Log.i(TAG, data.toString(4));
			String json = data.toString();

			StringEntity se = new StringEntity(json); 

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(serverUrl);
			post.setEntity(se);

			// Some headers for the json format
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");

			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();

			final String response_str = EntityUtils.toString(resEntity).trim();
			Log.i(TAG, "ADD MEMBERS: "+ response_str);

			return true;
		} catch (Exception ex) {
			Log.e(TAG, "error@add members: " + ex.getMessage(), ex);
			return false;
		}
	}

	/**
	 * Create the json formatted data for upload
	 * @param group_id The group ID 
	 * @param contacts The List of contacts to include in te json string
	 * @return JSON string
	 */
	private static JSONObject getJsonMembersData(String group_id, ArrayList<MyContacts> contacts) {
		try {
			JSONObject group = new JSONObject();
			group.put("group", group_id);

			/**
			 * Add all users to the json array
			 */
			JSONArray users = new JSONArray();
			for (MyContacts contact: contacts) {
				if(contact.getName() != null && !contact.getName().equals("") || contact.getLocation() != null && !contact.getLocation().equals("")) {
					JSONObject user = new JSONObject();
					user.put("name", contact.getName());
					user.put("phone_number", contact.getLocation());
					users.put(user);
				}
			}

			// Add json array to the root
			group.put("users", users);
			return group;

		}catch(JSONException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private static JSONObject getGroupDetails(String group_name, String location, String details, String user_id) {
		try {
			JSONObject group = new JSONObject();
			group.put("group_name", group_name);
			group.put("location", location);
			group.put("details", details);
			group.put("user_id", user_id);
			
			return group;		
		}catch(JSONException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Add service to a group. 
	 * @param group_id The group where services are to be added
	 * @param services The list of services to be uploaded
	 * @return boolean TRUE | FALSE
	 */
	public static boolean AddGroupServices(String group_id, ArrayList<AppService> services) {
		HttpEntity resEntity;    
		String  serverUrl = SERVER_URL + "add_services.json";
		JSONObject data = getJsonServiceData(group_id, services);
		
		try {
			Log.i(TAG, data.toString(4));
			String json = data.toString();

			StringEntity se = new StringEntity(json); 

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(serverUrl);
			post.setEntity(se);

			// Some headers for the json format
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");

			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();

			final String response_str = EntityUtils.toString(resEntity).trim();
			Log.i(TAG, "ADD SERVICES: "+ response_str);

			return true;
		} catch (Exception ex) {
			Log.e(TAG, "error@sendPanic: " + ex.getMessage(), ex);
			return false;
		}
	}

	/**
	 * Send a request when the panic menu is selected
	 * @param user_id The User ID 
	 * @param category The category of panic menu selected
	 * @param latitude The Latitude for the location
	 * @param longitude The Longitude for the location
	 */
	public static boolean sendPanic(String user_id, String category, double latitude, double longitude) {
		HttpEntity panEntity;
		String serverUrl = SERVER_URL + "panic_menu_actions";
		
		try {				
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(serverUrl);
				
			MultipartEntity entity = new MultipartEntity();
			
			entity.addPart("user_id", new StringBody(user_id)); 
			entity.addPart("service", new StringBody(category)); 
			entity.addPart("location", new StringBody(latitude+", "+longitude)); 
			
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			panEntity = response.getEntity();
			
			final String response_str = EntityUtils.toString(panEntity).trim();
			Log.i(TAG, "sendPanic: "+ response_str);
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Create a json out of the panic information to be submitted
	 * @param user_id The User ID 
	 * @param category The category of panic menu selected
	 * @param latitude The Latitude for the location
	 * @param longitude The Longitude for the location
	 * @return JSONObject
	 */
	private static JSONObject getPanicData(String user_id, String category, double latitude, double longitude) {
		try{
			JSONObject rootJson = new JSONObject();
			rootJson.put("user_id", user_id);
			rootJson.put("service", category);
			rootJson.put("location", latitude+", "+longitude);
			
			return rootJson;			
		}catch(JSONException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Create Json formatted string for the services to be uploaded
	 * @param group_id The group id
	 * @param services The list of services to be uploaded
	 * @return JSON string
	 */
	private static JSONObject getJsonServiceData(String group_id, ArrayList<AppService> services) {
		try {
			JSONObject rootJson = new JSONObject();
			rootJson.put("group_id", group_id);
			
			/**
			 * Add all users to the json array
			 */
			JSONArray users = new JSONArray();
			for (AppService svs: services) {
				if(svs.getName() != null && !svs.getName().equals("") || svs.getService_id() != null && !svs.getService_id().equals("")) {
					JSONObject svzJson = new JSONObject();
					svzJson.put("service_id", svs.getService_id());
					svzJson.put("company_id", svs.getId());
					users.put(svzJson);
				}
			}

			// Add json array to the root
			rootJson.put("services", users);
			return rootJson;

		}catch(JSONException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Check for network connectivity
	 * @param context Context
	 * @return TRUE | FALSE
	 */
	public static boolean isConnected(Context context){
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) 
			return true;
		else
			return false;    
	}

	/**
	 * Get the Group Services
	 * @param group_id The Group ID 
	 */
	public void getGroupServices(String group_id) {
		HttpEntity resEntity;    
		String  serverUrl = SERVER_URL + "group_services.json";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(serverUrl);

			MultipartEntity entity = new MultipartEntity();
			entity.addPart("group_id", new StringBody(group_id));

			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();

			final String response_str = EntityUtils.toString(resEntity).trim();
			Log.i(TAG, "getGroupServices: "+ response_str);

			JSONObject obj = new JSONObject(response_str);

		} catch (Exception ex) {
			Log.e("Debug", "error: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Get the new notifications for the group
	 * @param group_id Group ID as in the previous steps
	 * @return 
	 */
	public ArrayList<MNotification> getGroupNotifications(String group_id) {
		HttpEntity resEntity;    
		String  serverUrl = SERVER_URL + "group_notifications.json?group_id="+group_id;
		ArrayList<MNotification> noti = new ArrayList<MNotification>();

		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet post = new HttpGet(serverUrl);

			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();

			final String response_str = EntityUtils.toString(resEntity).trim();
			Log.i(TAG, "getGroupnotifications: "+ response_str);

			JSONArray jarr = new JSONArray(response_str);
			for (int i = 0; i < jarr.length(); i++) {
				JSONObject obj = jarr.getJSONObject(i);
				String user_id = obj.getString("user_id");
				String group = obj.getString("group");
				String message = obj.getString("message");
				String time = obj.getString("time");

				noti.add(new MNotification(user_id, group, message, time));
			}

			return noti;			
		} catch (Exception ex) {
			Log.e("Debug", "error: " + ex.getMessage(), ex);
			return noti;
		}
	}

	/**
	 * Create New Notification
	 * @param userid User ID
	 * @param message Message to send
	 * @param group_id Group To send To
	 * @return boolean
	 */
	public static boolean createNotification(String userid, String message, String group_id) {
		HttpEntity resEntity;    
		String  serverUrl = SERVER_URL + "notifications.json";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(serverUrl);

			JSONObject obj = new JSONObject();
			obj.put("user_id", userid);
			obj.put("group_id", group_id);
			obj.put("message", message);

			String json = obj.toString();			 
			StringEntity se = new StringEntity(json); 

			post.setEntity(se);

			// Some headers for the json format
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");

			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();

			final String response_str = EntityUtils.toString(resEntity).trim();
			Log.i(TAG, "Create New Notitication: "+ response_str);

			/**
			 * Parse the response here, json format
			 */
			JSONObject verifyJson = new JSONObject(response_str);
			if(verifyJson.has("id")) {
				String id = verifyJson.getString("id");
				String status = verifyJson.getString("status");
				if(status.equals("success")) {
					return true;
				}else return false;
			}else return false;

		} catch (Exception ex) {
			Log.e("Debug@createNotification", "error@createNotification: " + ex.getMessage(), ex);
			return false;
		}
	}

	/**
	 * Get the user's details
	 * @param userid The user id
	 * @return MProfile
	 */
	public static MProfile getUserDetails(String userid) {
		HttpEntity resEntity;    
		String  serverUrl = SERVER_URL + "users/"+userid+".json";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet post = new HttpGet(serverUrl);
			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();

			final String response_str = EntityUtils.toString(resEntity).trim();
			Log.i(TAG, "Fetch User's Details: "+ response_str);

			JSONObject verifyJson = new JSONObject(response_str);
			String id = verifyJson.getString("id");
			String name = verifyJson.getString("name");
			String phone_number = verifyJson.getString("phone_number");
			String group_id = verifyJson.getString("group_id");
			String user_type = verifyJson.getString("user_type");

			MProfile profile = new MProfile(id, name, phone_number, group_id, user_type);
			return profile;
		} catch (Exception ex) {
			Log.e(TAG, "error@Fetch User's Details: " + ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * Get the user's details
	 * @param group_id The Group id
	 * @return ArrayList<MProfile>
	 */
	public static ArrayList<MProfile> getMembersList(String group_id) {
		HttpEntity resEntity;    
		String  serverUrl = SERVER_URL + "groups/"+group_id+"/members";
		ArrayList<MProfile> memberList = new ArrayList<MProfile>();
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet post = new HttpGet(serverUrl);
			HttpResponse response = client.execute(post);
			resEntity = response.getEntity();

			final String response_str = EntityUtils.toString(resEntity).trim();
			Log.i(TAG, "getMembersList: "+ response_str);

			JSONArray jarr = new JSONArray(response_str);
			for (int i = 0; i < jarr.length(); i++) {
				JSONObject c = jarr.getJSONObject(i);

				String id = c.getString("id");
				String name = c.getString("name");
				String phone_number = c.getString("phone_number");
				String idNumber = c.getString("id_number");
				String user_type = c.getString("user_type");

				if(name != null && !name.equals("") && phone_number != null && !phone_number.equals("")) {
					MProfile profile = new MProfile(id, name, phone_number, group_id, user_type);
					profile.SetIDNumber(idNumber);					
					if(name.equals("null")) Log.i("CO: MEMBERS LIST:", profile.toString());
					else memberList.add(profile);
				}
			}

			return memberList;
		} catch (Exception ex) {
			Log.e(TAG, "error@Fetch User's Details: " + ex.getMessage(), ex);
			return null;
		}
	}
	
	/**
	 * Parse the response and return the List of Group Members Objects
	 * @param responseStr The String response from the server
	 * @return ArrayList<MProfile> List of members
	 */
	public static ArrayList<MProfile> getMembers(JSONArray jarr) {
		ArrayList<MProfile> memberList = new ArrayList<MProfile>();
		try {
			for (int i = 0; i < jarr.length(); i++) {
				JSONObject c = jarr.getJSONObject(i);

				String id = c.getString("id");
				String name = c.getString("name");
				String phone_number = c.getString("phone_number");
				//String idNumber = c.getString("id_number");
				String user_type = c.getString("user_type");
				
				if(name != null && !name.equals("") && phone_number != null && !phone_number.equals("")) {
					MProfile profile = new MProfile(id, name, phone_number, "", user_type);
					//profile.SetIDNumber(idNumber);					
					if(name.equals("null")) Log.i("CO: MEMBERS LIST:", profile.toString());
					else memberList.add(profile);
				}
			}

			return memberList;
		} catch (Exception ex) {
			Log.e(TAG, "error@Fetch User's Details: " + ex.getMessage(), ex);
			return null;
		}
	}

}
