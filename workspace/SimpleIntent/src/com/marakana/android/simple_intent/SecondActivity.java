package com.marakana.android.simple_intent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        
        Intent intent = getIntent();
        String sentMsg = intent.getStringExtra("MESSAGE");
        if (sentMsg != null) {
        	TextView msgView = (TextView) findViewById(R.id.sent_message);
        	msgView.setText(sentMsg);
        }
    }

}
