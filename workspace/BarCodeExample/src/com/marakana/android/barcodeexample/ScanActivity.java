package com.marakana.android.barcodeexample;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ScanActivity extends Activity implements OnClickListener {
	private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

	private static final int SCAN_REQUEST = 1;
	
	private TextView formatText;
	private TextView contentText;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button scanButton = (Button) findViewById(R.id.button_scan);
        formatText = (TextView) findViewById(R.id.text_format);
        contentText = (TextView) findViewById(R.id.text_content);

        // Check to see if a scanner activity is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(ACTION_SCAN), 0);
        if (activities.size() != 0) {
            scanButton.setOnClickListener(this);
        } else {
            scanButton.setEnabled(false);
            scanButton.setText(R.string.scanner_not_installed);
        }

    }
    
    public void onClick(View v) {
        Intent intent = new Intent(ACTION_SCAN);

        // Optionally, limit the intent recipient to just the "official" scan app 
        // intent.setPackage("com.google.zxing.client.android");
        
        // Optionally, set this extra to limit the format accepted for scanning
        // intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

        startActivityForResult(intent, SCAN_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	
        if (requestCode == SCAN_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Handle successful scan
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                
                formatText.setText(format);
                contentText.setText(contents);
            } else if (resultCode == RESULT_CANCELED) {
                formatText.setText(R.string.scan_unrecognized);
            }
        }
    }
}