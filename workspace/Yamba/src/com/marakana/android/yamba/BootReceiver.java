package com.marakana.android.yamba;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private static final String TAG = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, "onReceive() invoked");
		
		Intent startUpdaterService = new Intent(context, UpdaterService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 1, startUpdaterService,
											PendingIntent.FLAG_UPDATE_CURRENT);
		
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 10, 15000, pendingIntent);
	}

}
