package com.marakana.android.startactivityforresult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class First extends Activity implements OnClickListener {
	// Define a symbolic constant to identify which Activity responds
	private static final int GET_NAME_ACTIVITY = 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        
        Button chooseName = (Button) findViewById(R.id.button_choose_name);
        chooseName.setOnClickListener(this);
    }

	public void onClick(View v) {
		startActivityForResult( new Intent(this, Second.class), GET_NAME_ACTIVITY);
	}
	
	// Handle the result from the Activity started
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		TextView displayName = (TextView) findViewById(R.id.text_display_name);
		String name;
		
		// Identify which Activity responded with a result
		switch (requestCode) {
		case GET_NAME_ACTIVITY:
			if (resultCode == RESULT_OK) {
				name = data.getStringExtra("NAME");
			}
			else {
				name = "No name provided";
			}
			displayName.setText(name);
			break;
		}
	}
    
}