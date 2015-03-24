package com.app.nyumbakumi;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nyumbakumi.MEstateFragment.OnLocationSelected;
import com.app.nyumbakumi.framework.Act;
import com.app.nyumbakumi.utils.Rounder;

import com.app.nyumbakumi.R;

public class ProfilePhoto extends Act {
	final static int REQUEST_CODE = 0;
	// keep track of camera capture intent
	final int CAMERA_CAPTURE = 3;

	private String selectedImagePath;
	private ImageView img;
	private Button btn;
	private Uri selectedImageUri;
	private OnLocationSelected listener;

	// keep track of cropping intent
	final int PIC_CROP = 2;
	private AsyncTask<Void, Void, Void> mCompleteProfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complete_profile_layout);
		img = (ImageView) findViewById(R.id.imagePick);
		btn = (Button) findViewById(R.id.btnImagePick);

		init();
	}

	private void init() {
		setFontRegular((Button) findViewById(R.id.btn_next));
		setFontRegular((Button) findViewById(R.id.btnImagePick));
		// Set Font Type to be used
		setFontRegular((EditText) findViewById(R.id.editProfileHseEstate));
		setFontRegular((EditText) findViewById(R.id.editProfileHseNo));
		setFontRegular((TextView) findViewById(R.id.editProfileName));
		setFontRegular((TextView)findViewById(R.id.textCompleteProfile));
		setFontRegular((EditText)findViewById(R.id.editProfileID));

		Button btnNext = (Button) findViewById(R.id.btn_next);

		OnClickListener list = new OnClickListener() {

			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.btn_next:
					String name = ((EditText) findViewById(R.id.editProfileName)).getText().toString();
					String hse_estate = ((EditText) findViewById(R.id.editProfileHseEstate)).getText().toString();
					String hse_no = ((EditText) findViewById(R.id.editProfileHseNo)).getText().toString();
					String idNumber = ((EditText)findViewById(R.id.editProfileID)).getText().toString();

					/**
					 * Check the detail to make sure they are entered
					 */
					if(selectedImagePath !=null && !selectedImagePath.equals("")) {
						if(name.equals("")) {
							if(name.equals("")) ((EditText) findViewById(R.id.editProfileName)).setError("Name Required!");
							if(hse_estate.equals("")) ((EditText) findViewById(R.id.editProfileHseEstate)).setError("House Estate Required!");
							if(hse_no.equals("")) ((EditText) findViewById(R.id.editProfileHseNo)).setError("House Number Required!");
						}else {
							completeMyProfile(name, hse_estate, hse_no, idNumber);						
						}
					}else {
						toast("Please select your profile photo..:)-");
					}
					break;
				default:
					break;
				}
			}
		};

		btnNext.setOnClickListener(list);
	}


	/**
	 * Save the users profile
	 * @param name The User's name 
	 * @param idNumber The Id Number
	 * @param hse_estate The House Estate Name
	 * @param hse_no The House Number
	 */
	private void completeMyProfile(final String name, final String hse_estate, 
			final String hse_no, final String idNumber) {
		mCompleteProfile = new AsyncTask<Void, Void, Void>() {

			private Dialog pDialog;
			private boolean isComplete = false;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// Showing progress dialog
				pDialog = getProgressDialog();
				//pDialog.setMessage("We are saving your Profile...");
				pDialog.setCancelable(true);
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
					// Obtain the phone number and user_id from the preferences
					String phone_number = getPhoneValue();					
					String userid = getTemporaryUserID();
					isComplete = com.app.nyumbakumi.util.CommonUtils.updateProfile(userid, name, phone_number, 
							selectedImagePath, hse_estate, hse_no, idNumber);					

				} catch (Exception e) {
					e.printStackTrace();
				}				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				try{
					if (pDialog.isShowing())
						pDialog.dismiss();
				}catch(Exception ex){ex.printStackTrace();}

				// Check the reponse
				if(isComplete) {					
					setIsProfileSet(true);

					/**
					 * Set profilephoto Path in the preferences
					 */
					if(selectedImagePath !=null && !selectedImagePath.equals("")) 
						setProfilePhoto(selectedImagePath);

					toast("Your Profile has been saved..");
					setUserDetails(name, hse_estate, hse_no);

					/**
					 * If is_in_group is true, load notification, else load create_group
					 */
					if(isInGroup()) {
						Intent mainIntent = new Intent(ProfilePhoto.this, MainActivity.class);
						startActivity(mainIntent);
						overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
					}else {
						CreateGroup createGr = new CreateGroup();
						listener = (OnLocationSelected)createGr;
						switchScreen(createGr);
					}
				}else {
					toast("Your Profile could not be set, please try again..");
				}
				mCompleteProfile = null;

			}
		};
		mCompleteProfile.execute(null, null, null);

	}

	public void btn_SelectPhoto(View view) {
		// Intent chon hinh tu gallery.
		Intent imageGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
		imageGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
		imageGalleryIntent.setType("image/*");
		// Intent chup hinh tu camera
		final Intent captureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
		Intent chooserIntent = Intent.createChooser(imageGalleryIntent, "Image Chooser");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[] { captureIntent });
		startActivityForResult(chooserIntent, REQUEST_CODE);
	}

	public String getRealPath(Uri uri) {
		String[] projection = { Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		int column_index = cursor
				.getColumnIndexOrThrow(Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {
			switch (requestCode) {
			case REQUEST_CODE:
				if (android.os.Build.VERSION.SDK_INT < 13) {
					selectedImageUri = data.getData();
					// case: android 2.3 va chup hinh tu camera
					if (selectedImageUri == null) {
						final ContentResolver cr = getContentResolver();
						final String[] p1 = new String[] {
								Images.ImageColumns._ID, Images.ImageColumns.DATE_TAKEN };
						Cursor c1 = cr.query(
								Images.Media.EXTERNAL_CONTENT_URI, p1, null, null, p1[1] + " DESC");
						if (c1.moveToFirst()) {
							String uristringpic = "content://media/external/images/media/" + c1.getInt(0);
							Uri newuri = Uri.parse(uristringpic);
							selectedImageUri = newuri;
							Log.i("TAG", "newuri   " + newuri);
							img.setImageURI(newuri);
							performCrop();
							return;
						}
					}
				}

				if (resultCode == Activity.RESULT_OK) {

					// user is returning from capturing an image using the camera					
					if (requestCode == CAMERA_CAPTURE) {
						// get the Uri for the captured image
						selectedImageUri = data.getData();
						// carry out the crop operation
						performCrop();
					}

					// user is returning from cropping the image
					else if (requestCode == PIC_CROP) {
						// get the returned data
						Bundle extras = data.getExtras();
						// get the cropped bitmap
						Bitmap thePic = extras.getParcelable("data");
						// display the returned cropped image
						img.setImageBitmap(thePic);
						return;
					}

					// data gives you the image uri. Try to convert that to bitmap
					selectedImageUri = data.getData();
					selectedImagePath = getRealPath(selectedImageUri);
					img.setImageURI(selectedImageUri);
					performCrop();
					break;
				} else if (resultCode == Activity.RESULT_CANCELED) {
				}
				break;

			case PIC_CROP:
				if (resultCode == Activity.RESULT_OK) {
					// get the returned data
					Bundle extras = data.getExtras();
					// get the cropped bitmap
					Bitmap thePic = extras.getParcelable("data");

					// display the returned cropped image
					Uri cropImage = getImageUri(getBaseContext(), thePic);
					selectedImagePath = getRealPath(cropImage);
					Bitmap bitmap = Rounder.getRoundedShape(selectedImagePath, 256, 256);
					//img.setImageBitmap(thePic);
					img.setImageBitmap(bitmap);

					//btn.setVisibility(View.GONE);
					img.setVisibility(View.VISIBLE);
					btn.setText("Change photo");
					img.setBackgroundResource(R.drawable.image_shape_small);
					//btn.setGravity(Gravity.BOTTOM|Gravity.RIGHT);

				} else if (resultCode == Activity.RESULT_CANCELED) {
					// Log.e(TAG, "Selecting picture cancelled");
				}
				break;
			case 1230:
				//TODO Forward the location to createGroup
				if(listener != null) {
					double latitude = data.getDoubleExtra("latitude", 0.0);
					double longitude = data.getDoubleExtra("longitude", 0.0);
					Log.i(TAG, "Location: "+latitude+", "+longitude);
					listener.onLocationSelected(String.valueOf(latitude), String.valueOf(longitude));
				}
				break;

			}
		} catch (Exception e) {
			Log.e("error 1", "Exception in onActivityResult : " + e.getMessage());
		}
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	private void performCrop() {
		try {
			// call the standard crop action intent (the user device may not support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(selectedImageUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);
		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
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
