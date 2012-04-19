package com.marakana.android.yamba;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

public class TimelineFragment extends ListFragment implements ViewBinder {
	private Cursor mCursor;
	private SimpleCursorAdapter mAdapter;
	
	private static final String[] FROM = {
		TimelineHelper.KEY_USER,
		TimelineHelper.KEY_MESSAGE,
		TimelineHelper.KEY_CREATED_AT
	};
	private static final int[] TO = {
		R.id.status_data_user,
		R.id.status_data_msg,
		R.id.status_data_date
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCursor = YambaApplication.getInstance().getDb()
					.query(TimelineHelper.T_TIMELINE,
							null, null, null, null, null,
							TimelineHelper.KEY_CREATED_AT + " desc");
		mAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
							R.layout.timeline_row, mCursor, FROM, TO, 0);
		mAdapter.setViewBinder(this);
		setListAdapter(mAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		mCursor.requery();
	}

	@Override
	public void onPause() {
		super.onPause();
		mCursor.deactivate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mCursor.close();
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

}
