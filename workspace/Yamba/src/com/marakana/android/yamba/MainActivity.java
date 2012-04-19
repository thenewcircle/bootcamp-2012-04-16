package com.marakana.android.yamba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
	private FragmentManager mFragmentManager;
	private ComposeFragment mComposeFragment;
	private TimelineFragment mTimelineFragment;
	
	private static final String COMPOSE_TAG = "compose_fragment";
	private static final String TIMELINE_TAG = "timeline_fragment";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mFragmentManager = getSupportFragmentManager();
        
        if (savedInstanceState == null) {
        	// Initialize our fragments
        	mComposeFragment = new ComposeFragment();
        	mTimelineFragment = new TimelineFragment();
        	mFragmentManager.beginTransaction()
        		.add(R.id.fragment_container, mComposeFragment, COMPOSE_TAG)
        		.hide(mComposeFragment)
        		.add(R.id.fragment_container, mTimelineFragment, TIMELINE_TAG)
        		.commit();
        }
        else {
        	mComposeFragment = (ComposeFragment) mFragmentManager.findFragmentByTag(COMPOSE_TAG);
        	mTimelineFragment = (TimelineFragment) mFragmentManager.findFragmentByTag(TIMELINE_TAG);
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.options_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		Intent intent;
		switch (id) {
		case R.id.menu_preference:
			// Display the preference activity
			intent = new Intent(this, PrefsActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_refresh:
			// Start the UpdaterService
			intent = new Intent(this, UpdaterService.class);
			startService(intent);
			return true;
		case R.id.menu_timeline:
			// Start the TimelineActivity
			intent = new Intent(this, TimelineActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}