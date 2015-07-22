package com.example.logit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.crashlytics.android.Crashlytics;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import io.fabric.sdk.android.Fabric;

public class TheatroLogs {
	static Context context;
	private boolean crashlytics, local, remote;

	public final static CharSequence[] items = { "Crashlytics", "Local", "Remote" };
	public static Map<String, Boolean> logMap = null;

	// public TheatroLogs(Context cxt) {
	// this.cxt = cxt;
	//
	//
	// }

	// public void startCrashlytics(boolean start) {
	// crashlytics = start;
	// logMap.put(MainActivity.items2[0].toString(),start);
	// Fabric.with(cxt, new Crashlytics());
	//
	// }

	// public void startLocalLogs(boolean start) {
	//// local = start;
	// logMap.put(MainActivity.items2[1].toString(),start);
	//
	// }
	//
	// public void startRemoteLogs(boolean start) {
	//// remote = start;
	// logMap.put(MainActivity.items2[2].toString(),start);
	// }

	public static void log(Context cxt, Exception error) {
		if (logMap == null) {
			logMap = readDb(cxt, items);
		}
		if (logMap.get(MainActivity.items2[0])) {
//			Crashlytics.logException(error);
			Crashlytics.log(Log.ERROR, "TheatroLogs", "Testing from Sethu"); 
//			Log.e("Log type : crashlytics", error);
		}
		if (logMap.get(MainActivity.items2[1])) {
			WriteLogOnSD.generateLogOnSD(error.toString());
//			Log.e("Log type : Local", error);
		}
		if (logMap.get(MainActivity.items2[2])) {
			try {
				Intent intent = new Intent(cxt, RemoteLogger.class);
				intent.putExtra("className", cxt.getClass().toString());
				Toast.makeText(cxt, cxt.getClass().toString(), 0).show();
				intent.putExtra("error", error);
				cxt.startService(intent);
//				Log.e("Log type : Remote", error);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

	}

	public static Map<String, Boolean> readDb(Context context, CharSequence[] ch) {
		SharedPreferences prefs = context.getSharedPreferences("YourApp", Context.MODE_PRIVATE);
		ArrayList<String> al = new ArrayList<String>();
		Map<String, Boolean> m1 = new HashMap<String, Boolean>();
		for (int i = 0; i < ch.length; i++) {
			m1.put(ch[i].toString(), prefs.getBoolean(ch[i].toString(), false));
		}
		return m1;
	}

	public static void showOption(Context cxt) {

		// create map
		// update map from db
		// set the map to ui
		// edit the map
		// save the new map to db
		context = cxt;
		// MAP CREATED
		final Map<String, Boolean> m2;

		// // UPADTE MAP FROM SHARED PREFERENCE
		m2 = TheatroLogs.readDb(cxt, TheatroLogs.items);

		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
		builder.setTitle("Select The Logging style");

		// SET THE VALUE OF MAP TO UI

		boolean[] checkedValues = new boolean[TheatroLogs.items.length];

		for (int i = 0; i < TheatroLogs.items.length; i++) {

			checkedValues[i] = m2.get(TheatroLogs.items[i]);

		}

		builder.setMultiChoiceItems(TheatroLogs.items, checkedValues, new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
				if (isChecked) {

					m2.put(TheatroLogs.items[indexSelected].toString(), true);

				} else {

					m2.put(TheatroLogs.items[indexSelected].toString(), false);
				}
			}
		})
				// Set the action buttons
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// save map to db
						createDb(context, m2);
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

					}
				});

		dialog = builder.create();// AlertDialog dialog; create like this
									// outside onClick
		dialog.show();

	}

	private static void createDb(Context context, Map<String, Boolean> logtypes) {
		if (context != null) {
			SharedPreferences prefs = context.getSharedPreferences("YourApp", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			for (String s : logtypes.keySet()) {
				editor.putBoolean(s, logtypes.get(s));
			}

			editor.commit();
		} else {
			// null context
			Log.e("TheatroLogs.class::createDb()::", "null context");
		}

	}

}
