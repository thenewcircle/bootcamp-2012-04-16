package com.marakana.android.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private static final String TAG = "MainActivity";
	private EditText editMsg;
	private Toast toast;
	
	private Twitter twitter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        editMsg = (EditText) findViewById(R.id.edit_msg);
        Button buttonUpdate = (Button) findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(this);
        
        // Initialize the Twitter object
        twitter = new Twitter("student", "password");
        twitter.setAPIRootUrl("http://yamba.marakana.com/api");
        
        // Initialize Toast object
        toast = Toast.makeText(this, null, Toast.LENGTH_LONG);
    }

	@Override
	public void onClick(View v) {
		Log.v(TAG, "Button clicked");
		int id = v.getId();
		switch (id) {
		case R.id.button_update:
			// Process an Update Status button click
			String msg = editMsg.getText().toString();
			Log.v(TAG, "User entered: " + msg);
			editMsg.setText("");
			
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
				twitter.setStatus(args[0]);
			} catch (TwitterException e) {
				Log.e(TAG, "Failed to post message", e);
				result = R.string.post_status_fail;
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			toast.setText(result);
			toast.show();
		}
		
	}
}