package com.marakana.android.yamba;

import java.util.Date;
import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
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
		
		try {
			List<Twitter.Status> timeline = YambaApplication.getInstance().getTwitter().getHomeTimeline();
			for (Twitter.Status status: timeline) {
				String name = status.user.name;
				String msg = status.text;
				long id = status.id;
				Date createdAt = status.createdAt;
				Log.v(TAG, id + ": " + name + " posted at " + createdAt + ": " + msg);
			}
		} catch (TwitterException e) {
			Log.w(TAG, "Failed to retrieve timeline data");
		}
	}

}
