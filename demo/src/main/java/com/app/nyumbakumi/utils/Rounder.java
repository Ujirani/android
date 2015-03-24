package com.app.nyumbakumi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;

public class Rounder {
	/**
	 * Create a rounded Bitmap Image from the External Directory Pth
	 * @param photoPath External Director Path
	 * @param targetWidth Required width of the image
	 * @param targetHeight Required Height of the image
	 * @return Bitmap
	 */
	public static Bitmap getRoundedShape(String photoPath, int targetWidth,  int targetHeight) {		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
		
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
				targetHeight,Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), 
						((float) targetHeight)) / 2),
						Path.Direction.CCW);
		
		canvas.clipPath(path);
		Bitmap sourceBitmap = bitmap;
		canvas.drawBitmap(sourceBitmap, 
				new Rect(0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight()), 
						new Rect(0, 0, targetWidth,
								targetHeight), null);
		return targetBitmap;
	}
	
	/**
	 * 
	 * @param imageId
	 * @param context
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 */
	public static Bitmap getRoundedShape(int imageId, Context context, int targetWidth,  int targetHeight) {		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageId); 
		
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
				targetHeight,Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), 
						((float) targetHeight)) / 2),
						Path.Direction.CCW);
		
		canvas.clipPath(path);
		Bitmap sourceBitmap = bitmap;
		canvas.drawBitmap(sourceBitmap, 
				new Rect(0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight()), 
						new Rect(0, 0, targetWidth,
								targetHeight), null);
		return targetBitmap;
	}
}
