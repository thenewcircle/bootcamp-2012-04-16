package com.marakana.android.yamba;

import java.util.Date;
import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class UpdaterService extends IntentService {
	private static final String TAG = "UpdaterService";

	public UpdaterService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.v(TAG, "onHandleIntent() invoked");
		
		int count = 0;
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
				
				Uri ret = getContentResolver().insert(StatusContract.CONTENT_URI, values);
				if (ret != null) {
					count++;
				}
			}
		} catch (TwitterException e) {
			Log.w(TAG, "Failed to retrieve timeline data");
		}
		if (count > 0) {
			notifyNewStatus(count);
		}
	}
	
	private void notifyNewStatus(int count) {
		Intent broadcast = new Intent(YambaApplication.ACTION_NEW_STATUS);
		broadcast.putExtra(YambaApplication.EXTRA_NEW_STATUS_COUNT, count);
		sendBroadcast(broadcast, YambaApplication.PERM_NEW_STATUS);
	}

}
