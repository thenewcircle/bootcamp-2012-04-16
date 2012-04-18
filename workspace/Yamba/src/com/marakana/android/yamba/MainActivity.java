package com.marakana.android.yamba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}