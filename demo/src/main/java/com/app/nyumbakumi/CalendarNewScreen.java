package com.app.nyumbakumi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.nyumbakumi.framework.Screen;

import com.app.nyumbakumi.R;

public class CalendarNewScreen extends Screen {
	private View v;
	private View imageLeft, imageRight;
	private TextView calendarMonthTitle;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.calendar_layout, container, false);
		
		imageLeft = v.findViewById(R.id.imageCalendarLeft);
		imageRight = v.findViewById(R.id.imageCalendarRight);
		calendarMonthTitle = (TextView) v.findViewById(R.id.textCalendarMonth);
		
		setCalendarDaysFont();
		setCalendarIndivDaysFont();
		setCalendarDescFont();
		
		setListener();
		
		View navLeft = v.findViewById(R.id.imageLaunchDrawerLeft);
		View navRight = v.findViewById(R.id.imageLaunchDrawerRight);

		OnClickListener list = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.imageLaunchDrawerLeft:
					getParent().openDrawerLeft();
					break;	
				case R.id.imageLaunchDrawerRight:
					getParent().openDrawerRight();					
					break;					
				case R.id.imageCalendarLeft:
					/**
					 * Navigate to the previous month
					 */
					 
					break;	
				case R.id.imageCalendarRight:
					/**
					 * Navigate to next month
					 */
					
					break;					
				default:		
					break;		
				}
			}
		};


		imageLeft.setOnClickListener(list);
		imageRight.setOnClickListener(list);
		navLeft.setOnClickListener(list);
		navRight.setOnClickListener(list);
		
		return v;
	}

	private void unSelectDates() {
		((TextView) v.findViewById(R.id.textCalendarSun_1)).setBackgroundResource(R.color.app_white);
		((TextView) v.findViewById(R.id.textCalendarSun_2)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarSun_3)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarSun_4)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarSun_5)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarSun_6)).setBackgroundResource(R.color.app_white);	

		((TextView) v.findViewById(R.id.textCalendarMon_1)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarMon_2)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarMon_3)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarMon_4)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarMon_5)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarMon_6)).setBackgroundResource(R.color.app_white);	

		((TextView) v.findViewById(R.id.textCalendarTue_1)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarTue_2)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarTue_3)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarTue_4)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarTue_5)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarTue_6)).setBackgroundResource(R.color.app_white);	


		((TextView) v.findViewById(R.id.textCalendarWed_1)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarWed_2)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarWed_3)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarWed_4)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarWed_5)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarWed_6)).setBackgroundResource(R.color.app_white);	

		((TextView) v.findViewById(R.id.textCalendarThur_1)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarThur_2)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarThur_3)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarThur_4)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarThur_5)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarThur_6)).setBackgroundResource(R.color.app_white);	

		((TextView) v.findViewById(R.id.textCalendarFri_1)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarFri_2)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarFri_3)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarFri_4)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarFri_5)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarFri_6)).setBackgroundResource(R.color.app_white);	


		((TextView) v.findViewById(R.id.textCalendarSat_1)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarSat_2)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarSat_3)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarSat_4)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarSat_5)).setBackgroundResource(R.color.app_white);		
		((TextView) v.findViewById(R.id.textCalendarSat_6)).setBackgroundResource(R.color.app_white);

	}

	private void setListener() {
		OnClickListener list = new OnClickListener() {

			@Override
			public void onClick(View v) {
				unSelectDates();	
				v.setBackgroundResource(R.drawable.calendar_date_selected);
				//((TextView)v).setTextColor(color.white);
			}
		};

		((TextView) v.findViewById(R.id.textCalendarSun_1)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarSun_2)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarSun_3)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarSun_4)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarSun_5)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarSun_6)).setOnClickListener(list);	

		((TextView) v.findViewById(R.id.textCalendarMon_1)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarMon_2)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarMon_3)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarMon_4)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarMon_5)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarMon_6)).setOnClickListener(list);	

		((TextView) v.findViewById(R.id.textCalendarTue_1)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarTue_2)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarTue_3)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarTue_4)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarTue_5)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarTue_6)).setOnClickListener(list);	


		((TextView) v.findViewById(R.id.textCalendarWed_1)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarWed_2)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarWed_3)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarWed_4)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarWed_5)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarWed_6)).setOnClickListener(list);	

		((TextView) v.findViewById(R.id.textCalendarThur_1)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarThur_2)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarThur_3)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarThur_4)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarThur_5)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarThur_6)).setOnClickListener(list);	

		((TextView) v.findViewById(R.id.textCalendarFri_1)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarFri_2)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarFri_3)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarFri_4)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarFri_5)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarFri_6)).setOnClickListener(list);	


		((TextView) v.findViewById(R.id.textCalendarSat_1)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarSat_2)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarSat_3)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarSat_4)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarSat_5)).setOnClickListener(list);		
		((TextView) v.findViewById(R.id.textCalendarSat_6)).setOnClickListener(list);
	}

	private void setCalendarDescFont() {
		getParent().setFontSemiBold((TextView) v.findViewById(R.id.textCalendarTitle));			
		getParent().setFontSemiBold((TextView) v.findViewById(R.id.textCalendarMonth));

		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarTitle_sun));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarTitle_fri));			
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarTitle_mon));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarTitle_sat));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarTitle_thur));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarTitle_tue));	
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarTitle_wed));
	}

	private void setCalendarIndivDaysFont() {
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarSun_1));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarSun_2));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarSun_3));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarSun_4));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarSun_5));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarSun_6));	

		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarMon_1));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarMon_2));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarMon_3));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarMon_4));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarMon_5));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarMon_6));	

		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarTue_1));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarTue_2));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarTue_3));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarTue_4));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarTue_5));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarTue_6));	


		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarWed_1));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarWed_2));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarWed_3));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarWed_4));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarWed_5));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarWed_6));	


		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarThur_1));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarThur_2));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarThur_3));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarThur_4));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarThur_5));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarThur_6));	

		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarFri_1));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarFri_2));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarFri_3));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarFri_4));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarFri_5));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarFri_6));	


		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarSat_1));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarSat_2));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarSat_3));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarSat_4));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarSat_5));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendarSat_6));	


	}

	private void setCalendarDaysFont() {
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendar_time));		
		getParent().setFontSemiBold((TextView) v.findViewById(R.id.textCalendar_title));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendar_place));		
		getParent().setFontRegular((TextView) v.findViewById(R.id.textCalendar_desc));	
	}

}
