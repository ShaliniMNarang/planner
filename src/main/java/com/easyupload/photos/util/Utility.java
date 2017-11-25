package com.easyupload.photos.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

	public static String dateFormatter(Date date) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		return df.format(date);
	}
	
	public static String geyKeyName(String eventId, String fileName) {
		String SUFFIX = "/";
		StringBuffer sb = new StringBuffer();
		sb.append(eventId);
		sb.append(SUFFIX);
		sb.append(fileName);
		return sb.toString();
	}
}
