package com.marakana.android.yamba;

import java.util.Date;
import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
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
			postNotification();
			notifyNewStatus(count);
		}
	}
	
	private void notifyNewStatus(int count) {
		Intent broadcast = new Intent(YambaApplication.ACTION_NEW_STATUS);
		broadcast.putExtra(YambaApplication.EXTRA_NEW_STATUS_COUNT, count);
		sendBroadcast(broadcast, YambaApplication.PERM_NEW_STATUS);
	}
	
	private void postNotification() {
		int icon = android.R.drawable.stat_notify_chat;
		CharSequence tickerText = getString(R.string.notify_new_status_ticker);
		long when = System.currentTimeMillis();
		
		Notification notification = new Notification(icon, tickerText, when);
		
		CharSequence contentTitle = getString(R.string.notify_new_status_ticker);
		CharSequence contentText = getString(R.string.notify_new_status_content_text);
		
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		notification.setLatestEventInfo(this, contentTitle, contentText, pi);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(YambaApplication.NEW_STATUS_NOTIFICATION, notification);
	}

}
