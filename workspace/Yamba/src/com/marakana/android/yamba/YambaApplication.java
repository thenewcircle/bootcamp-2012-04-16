package com.marakana.android.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

public class YambaApplication extends Application implements OnSharedPreferenceChangeListener {
	
	private static YambaApplication instance;
	
	private SharedPreferences prefs;
	private String prefUserKey;
	private String prefPasswordKey;
	private String prefSiteUrlKey;
	
	private Twitter twitter;
	
	private TimelineHelper timelineHelper;

	public synchronized Twitter getTwitter() {
		if (twitter == null) {
			// Create a Twitter object based on current preference values
			String user = prefs.getString(prefUserKey, null);
			String password = prefs.getString(prefPasswordKey, null);
			String url = prefs.getString(prefSiteUrlKey, null);
			twitter = new Twitter(user, password);
			twitter.setAPIRootUrl(url);
		}
		return twitter;
	}
	
	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		twitter = null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		prefUserKey = getString(R.string.pref_user_key);
		prefPasswordKey = getString(R.string.pref_password_key);
		prefSiteUrlKey = getString(R.string.pref_site_url_key);
		
		timelineHelper = new TimelineHelper(this);
	}
	
	public SQLiteDatabase getDb() {
		return timelineHelper.getWritableDatabase();
	}

	public static YambaApplication getInstance() {
		return instance;
	}

}
