package com.marakana.android.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TimelineFragment extends ListFragment implements ViewBinder, 
		LoaderManager.LoaderCallbacks<Cursor> {
	private SimpleCursorAdapter mAdapter;
	private TimelineReceiver mReceiver;
	private IntentFilter mFilter;
	
	private static final String[] FROM = {
		StatusContract.Columns.USER,
		StatusContract.Columns.MESSAGE,
		StatusContract.Columns.CREATED_AT
	};
	private static final int[] TO = {
		R.id.status_data_user,
		R.id.status_data_msg,
		R.id.status_data_date
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		
		mAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
							R.layout.timeline_row, null, FROM, TO, 0);
		mAdapter.setViewBinder(this);
		setListAdapter(mAdapter);
		
		mReceiver = new TimelineReceiver();
		mFilter = new IntentFilter(YambaApplication.ACTION_NEW_STATUS);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(mReceiver, mFilter, YambaApplication.PERM_NEW_STATUS, null);
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(mReceiver);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.options_timeline_fragment, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.menu_refresh:
			// Start the UpdaterService to refresh the timeline
			Intent intent = new Intent(getActivity(), UpdaterService.class);
			getActivity().startService(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean setViewValue(View v, Cursor cursor, int columnIndex) {
		int id = v.getId();
		switch (id) {
		case R.id.status_data_date:
			// Customize the appearance of the date
			long timestamp = cursor.getLong(columnIndex);
			CharSequence relTime = DateUtils.getRelativeTimeSpanString(timestamp);
			TextView tv = (TextView) v;
			tv.setText(relTime);
			return true;
		default:
			// Let the SimpleCursorAdapter perform the default binding of data
			return false;
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity().getApplicationContext(),
						StatusContract.CONTENT_URI, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}
	
	private class TimelineReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			getLoaderManager().restartLoader(0, null, TimelineFragment.this);
		}
		
	}

}
