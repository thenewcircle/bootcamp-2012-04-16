package com.marakana.android.yamba;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class UpdaterService extends IntentService {
	private static final String TAG = "UpdaterService";

	public UpdaterService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.v(TAG, "onHandleIntent() invoked");
	}

}
