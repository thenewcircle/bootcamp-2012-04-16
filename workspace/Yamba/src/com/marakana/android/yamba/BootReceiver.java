package com.marakana.android.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private static final String TAG = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, "onReceive() invoked");
		// Do a one-shot fetch of data at system boot
		context.startService( new Intent(context, UpdaterService.class) );
	}

}
