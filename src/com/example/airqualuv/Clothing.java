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

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> 2e8b1d4521d29201761f09fb70f8c5b4c78ec620
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
<<<<<<< HEAD
>>>>>>> parent of 63e6234... 'final proto for May 9th'
=======
>>>>>>> 2e8b1d4521d29201761f09fb70f8c5b4c78ec620
	}
	
}