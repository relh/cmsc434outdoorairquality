package com.example.airqualuv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


//Richard Higgins, code snippets cobbled from android forums.
//Feb 12th, 2013

public class Clothing extends Activity {
	public int clothing = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_clothing);

		findViewById(R.id.clothing1).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				clothing += 1;
			}
		});
		findViewById(R.id.clothing2).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				clothing += 1;
			}
		});
		findViewById(R.id.clothing3).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				clothing += 1;
			}
		});
	
	}
	
	public void onBackPressed() {
		Intent resultIntent = new Intent();
		resultIntent.putExtra("clothing", clothing);
		setResult(Activity.RESULT_OK, resultIntent);
	    super.onBackPressed();
	}
	
}