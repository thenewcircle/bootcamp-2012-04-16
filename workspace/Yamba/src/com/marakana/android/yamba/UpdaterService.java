package com.marakana.android.yamba;

import java.util.Date;
import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.IntentService;
import android.content.ContentValues;
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
		
		try {
			List<Twitter.Status> timeline;
			try {
				timeline = YambaApplication.getInstance().getTwitter().getHomeTimeline();
			} catch (NullPointerException e) {
				// Work around bug in JTwitter library
				return;
			}
			ContentValues values = new ContentValues();
			for (Twitter.Status status: timeline) {
				String name = status.user.name;
				String msg = status.text;
				long id = status.id;
				Date createdAt = status.createdAt;
				Log.v(TAG, id + ": " + name + " posted at " + createdAt + ": " + msg);
				
				values.clear();
				values.put(StatusContract.Columns._ID, id);
				values.put(StatusContract.Columns.USER, name);
				values.put(StatusContract.Columns.MESSAGE, msg);
				values.put(StatusContract.Columns.CREATED_AT, createdAt.getTime());
				
				getContentResolver().insert(StatusContract.CONTENT_URI, values);
			}
		} catch (TwitterException e) {
			Log.w(TAG, "Failed to retrieve timeline data");
		}
	}

}
