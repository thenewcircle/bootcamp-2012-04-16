package com.marakana.android.simple_intent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FirstActivity extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        
        Button sendButton = (Button) findViewById(R.id.send);
        
        sendButton.setOnClickListener(this);
    }
    
	public void onClick(View v) {
        EditText msg = (EditText) findViewById(R.id.msg);
		Intent intent = new Intent(this, SecondActivity.class);
		intent.putExtra("MESSAGE", msg.getText().toString());
		startActivity(intent);
	}
}