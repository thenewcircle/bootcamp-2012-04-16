package com.marakana.android.launchweb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LaunchActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button go = (Button) findViewById(R.id.button_go);
        go.setOnClickListener(this);
    }

	public void onClick(View view) {
		EditText editUrl = (EditText) findViewById(R.id.edit_url);
		
		// Retrieve the string from the EditText and generate a Uri from it
		String uriString = editUrl.getText().toString();
		Uri uri = Uri.parse(uriString);
		
		// Request the system to start an Activity to view the Uri
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
}