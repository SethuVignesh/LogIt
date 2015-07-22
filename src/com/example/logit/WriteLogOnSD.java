/**************************************************************************
 * 
 *  @author Nagendra K Srivastava
 *
 * Copyright (c) Theatro Labs, 2015
 *
 * This unpublished material is proprietary to Theatro.
 * All rights reserved.
 * The methods and techniques described herein are considered trade
 * secrets and/or confidential. Reproduction or distribution, in whole or
 * in part, is forbidden except by express written permission of Theatro.
 * 
 **************************************************************************/

package com.example.logit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.R.bool;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;

public class WriteLogOnSD {

	/**
	 *  Below variable is the name of the log file while will be stored in a folder named theatro on external
	 *  memory
	 */
	
	private static final String LOG_FILE_NAME = "log.txt";
	
	/**
	 *  below variable is the directory name in which log file will be stored 
	 */
	private static final String DIRECTORY_NAME = "theatro";
	
	
	/**
	 *  This method is responsible for generating log file on mobile phone, this method checks for availability of 
	 *  external storage on the phone 
	 * @param sBody
	 */
	public static void generateLogOnSD(String sBody) {
		try {
			if (isExternalStoragePresent()) {
				File file = new File(Environment.getExternalStorageDirectory(),
						DIRECTORY_NAME);
				if (!file.exists()) {
					file.mkdirs();
				}
				File gpxfile = new File(file, LOG_FILE_NAME);
				FileWriter writer = new FileWriter(gpxfile, true);// appending
																	// data to
																	// file
				writer.write(getCurrentDateAndTime() + ":" + sBody +"\n");
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  This method return true if external storage is present either it returns to false
	 * @return
	 */
	private static boolean isExternalStoragePresent() {

		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		if (!((mExternalStorageAvailable) && (mExternalStorageWriteable))) {
		}
		return (mExternalStorageAvailable) && (mExternalStorageWriteable);
	}
	
	
	public static Uri getLogFile(){
		Uri path = null;
		if(isExternalStoragePresent()){
			File file = new File(Environment.getExternalStorageDirectory(), DIRECTORY_NAME);
			if (file.exists()) {
				File eventDataFile = new File(file, LOG_FILE_NAME);
				if (eventDataFile.exists()) {
					path = Uri.fromFile(eventDataFile);
				}	
			}
		}
		return path;
	}
	
	
	public static boolean deleteFile(){
		boolean deleteFile = false;
		if(isExternalStoragePresent()){
			File file = new File(Environment.getExternalStorageDirectory(), DIRECTORY_NAME);
			if(file.exists()){
				File eventDataFile = new File(file, LOG_FILE_NAME);
				deleteFile = eventDataFile.delete();
			}
		}
		return deleteFile;
	}
	
	private static String getCurrentDateAndTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss",Locale.ENGLISH);
		String dateAndTime = sdf.format(new Date());
		return dateAndTime;
	}
}


