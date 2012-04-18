package com.marakana.android.yamba;

import winterwell.jtwitter.TwitterException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeFragment extends Fragment implements OnClickListener {
	private static final String TAG = "ComposeFragment";
	
	private EditText mEditMsg;
	private Toast mToast;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View top = inflater.inflate(R.layout.compose_fragment, container, false);
		
        mEditMsg = (EditText) top.findViewById(R.id.edit_msg);
        Button buttonUpdate = (Button) top.findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(this);
        
        // Initialize Toast object
        mToast = Toast.makeText(getActivity().getApplicationContext(), null, Toast.LENGTH_LONG);
        
		return top;
	}

	@Override
	public void onClick(View v) {
		Log.v(TAG, "Button clicked");
		int id = v.getId();
		switch (id) {
		case R.id.button_update:
			// Process an Update Status button click
			String msg = mEditMsg.getText().toString();
			Log.v(TAG, "User entered: " + msg);
			mEditMsg.setText("");
			
			if (!TextUtils.isEmpty(msg)) {
				// Post the status update
				new PostToTwitter().execute(msg);
			}
			
			break;
		default:
			// We should never get here!
		}
	}
	
	private class PostToTwitter extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... args) {
			int result = R.string.post_status_success;
			
			try {
				YambaApplication.getInstance().getTwitter().setStatus(args[0]);
			} catch (TwitterException e) {
				Log.e(TAG, "Failed to post message", e);
				result = R.string.post_status_fail;
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			mToast.setText(result);
			mToast.show();
		}
		
	}
}
