package com.smsmissedcall.methods;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

	// Augmenter une date jour par jour
	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}

	// Augmenter une date jour par jour
	public static Date addMinutes(Date date, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minute); // minus number would decrement the minute
		return cal.getTime();
	}

	public Date addMonth(Date date, int minute) {
		Date now = date;
		Calendar myCal = Calendar.getInstance();
		myCal.setTime(now);
		myCal.add(Calendar.MONTH, +minute);
		now = myCal.getTime();
		System.out.println("===========> " + now);
		return now;
	}

	public static String dateNow() {

		String dateNow = null;

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		dateNow = sdf.format(date);

		return dateNow;
	}

	public static Date stringToDate(String str) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		if (str != null) {
			try {
				date = sdf.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}

	// Cette fonction permet de calculer la difference entre deux date
	public static void CrunchifyTimeDiff(String[] args) {
		try {
			String date1 = "07/15/2016";
			String time1 = "11:00 AM";
			String date2 = "07/17/2016";
			String time2 = "12:15 AM";

			String format = "MM/dd/yyyy hh:mm a";

			SimpleDateFormat sdf = new SimpleDateFormat(format);

			Date dateObj1 = sdf.parse(date1 + " " + time1);
			Date dateObj2 = sdf.parse(date2 + " " + time2);
			System.out.println(dateObj1);
			System.out.println(dateObj2 + "\n");

			DecimalFormat crunchifyFormatter = new DecimalFormat("###,###");

			// getTime() returns the number of milliseconds since January 1, 1970, 00:00:00
			// GMT represented by this Date object
			long diff = dateObj2.getTime() - dateObj1.getTime();

			int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
			System.out.println("difference between days: " + diffDays);

			int diffhours = (int) (diff / (60 * 60 * 1000));
			System.out.println("difference between hours: " + crunchifyFormatter.format(diffhours));

			int diffmin = (int) (diff / (60 * 1000));
			System.out.println("difference between minutues: " + crunchifyFormatter.format(diffmin));

			int diffsec = (int) (diff / (1000));
			System.out.println("difference between seconds: " + crunchifyFormatter.format(diffsec));

			System.out.println("difference between milliseconds: " + crunchifyFormatter.format(diff));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
