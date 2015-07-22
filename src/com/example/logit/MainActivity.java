package com.example.logit;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	TheatroLogs theatroLogs;

	private static final String LOG_EMAIL_SUBJECT = "log file";

	private static final String LOG_EMAIL_MESSAGE = " Please  find the attached log file with this mail ";

	private static final String LOG_FILE_SEND_EMAILS[] = { "sethu@theatrolabs.com", "abhi@theatrolabs.com" };

	private static final int SEND_EMAIL_REQUEST_CODE = 1222;

	public static final CharSequence[] items2 = { "Crashlytics", "Local", "Remote" };

	Button sendMail, crash, options, exception;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Fabric.with(this, new Crashlytics());
		Fabric.with(this, new Crashlytics());

		// theatroLogs = new TheatroLogs(this);
		//
		// emulateCrash();
		// testLog();

		setContentView(R.layout.activity_main);
		TextView tv = (TextView) findViewById(R.id.textview);
		sendMail = (Button) findViewById(R.id.btnSendMail);
		crash = (Button) findViewById(R.id.btnCrash);
		options = (Button) findViewById(R.id.btnOption);
		exception = (Button) findViewById(R.id.btnException);
		sendMail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendEmail();

			}
		});
		exception.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				testLog();

			}
		});
		crash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				throw new RuntimeException("This s a Crash");

			}
		});
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TheatroLogs.showOption(MainActivity.this);
			}
		});
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// showOption();
				sendEmail();
			}

		});

	}

	private void testLog() {
		try {
			int i = 1 / 0;
		} catch (Exception e) {
			// theatroLogs.startRemoteLogs(true);
			// theatroLogs.startLocalLogs(true);
			TheatroLogs.log(getApplicationContext(), e);
		}
	}

	private void emulateCrash() {
		throw new RuntimeException();
	}

	// public static Map<String, Boolean> readDb(Context context, CharSequence[]
	// ch) {
	// SharedPreferences prefs = context.getSharedPreferences("YourApp",
	// Context.MODE_PRIVATE);
	// ArrayList<String> al = new ArrayList<String>();
	// Map<String, Boolean> m1 = new HashMap<String, Boolean>();
	// for (int i = 0; i < ch.length; i++) {
	// m1.put(ch[i].toString(), prefs.getBoolean(ch[i].toString(), false));
	// }
	// return m1;
	// }

	private void sendEmail() {
		Uri path = WriteLogOnSD.getLogFile();
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, LOG_EMAIL_SUBJECT);
		intent.putExtra(Intent.EXTRA_EMAIL, LOG_FILE_SEND_EMAILS);
		intent.putExtra(Intent.EXTRA_TEXT, LOG_EMAIL_MESSAGE);
		intent.putExtra(Intent.EXTRA_STREAM, path);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivityForResult(Intent.createChooser(intent, "Send mail..."), SEND_EMAIL_REQUEST_CODE);
	}

}
