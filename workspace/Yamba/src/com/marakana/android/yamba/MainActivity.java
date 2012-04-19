package com.marakana.android.yamba;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
	private FragmentManager mFragmentManager;
	private ComposeFragment mComposeFragment;
	private TimelineFragment mTimelineFragment;
	
	private MenuItem mComposeMenuItem;
	private MenuItem mTimelineMenuItem;
	
	private int mFragmentVisible;
	private static final String FRAGMENT_VISIBLE = "FRAGMENT_VISIBLE";
	private static final String COMPOSE_TAG = "compose_fragment";
	private static final String TIMELINE_TAG = "timeline_fragment";
	
	private static final int COMPOSE_FRAGMENT_VISIBLE = 1;
	private static final int TIMELINE_FRAGMENT_VISIBLE = 2;
	
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
        		.add(R.id.fragment_container, mTimelineFragment, TIMELINE_TAG)
        		.commit();
        	
        	// Default to displaying the TimelineFragment
        	showFragment(TIMELINE_FRAGMENT_VISIBLE);
        }
        else {
        	mComposeFragment = (ComposeFragment) mFragmentManager.findFragmentByTag(COMPOSE_TAG);
        	mTimelineFragment = (TimelineFragment) mFragmentManager.findFragmentByTag(TIMELINE_TAG);
        	
        	showFragment( savedInstanceState.getInt(FRAGMENT_VISIBLE, TIMELINE_FRAGMENT_VISIBLE) );
        }
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(FRAGMENT_VISIBLE, mFragmentVisible);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.options_main, menu);
		
		mComposeMenuItem = menu.findItem(R.id.menu_compose);
		mTimelineMenuItem = menu.findItem(R.id.menu_timeline);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		switch (mFragmentVisible) {
		case COMPOSE_FRAGMENT_VISIBLE:
			mComposeMenuItem.setVisible(false);
			mTimelineMenuItem.setVisible(true);
			break;
		default:
			// Assume TimelineFragment visible
			mTimelineMenuItem.setVisible(false);
			mComposeMenuItem.setVisible(true);
		}
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
		case R.id.menu_compose:
			// Display the ComposeFragment
			showFragment(COMPOSE_FRAGMENT_VISIBLE);
			return true;
		case R.id.menu_timeline:
			// Display the TimelineFragment
			showFragment(TIMELINE_FRAGMENT_VISIBLE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void showFragment(int visibleFragment) {
		mFragmentVisible = visibleFragment;
		switch (visibleFragment) {
		case COMPOSE_FRAGMENT_VISIBLE:
			mFragmentManager.beginTransaction()
				.hide(mTimelineFragment)
				.show(mComposeFragment)
				.commit();
			break;
		default:
			mFragmentManager.beginTransaction()
				.hide(mComposeFragment)
				.show(mTimelineFragment)
				.commit();
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Force refresh of action bar
			invalidateOptionsMenu();
		}
	}

}