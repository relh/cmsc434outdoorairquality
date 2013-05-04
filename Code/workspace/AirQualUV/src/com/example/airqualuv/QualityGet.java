package com.example.airqualuv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

//Richard Higgins, code snippets cobbled from android forums.
//Feb 12th, 2013

public class QualityGet extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_quality_get);

		// Button Listener
		findViewById(R.id.lookupButton).setOnClickListener(textChangeListener);
		findViewById(R.id.clothingButton).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Clothing.class);
				startActivityForResult(intent, 0);
				
			}
		});
		
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnClickListener textChangeListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {

			new WebTrawl()
			{
			    @Override public void onPostExecute(String result)
			    {
			        TextView tBox = (TextView) findViewById(R.id.results);
			        tBox.setText(result);
			    }
			}.execute("");
		}

	};

	private class WebTrawl extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			EditText tBox = (EditText) findViewById(R.id.results);
			EditText zipT = (EditText) findViewById(R.id.zipText);

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(
					"http://www.airnow.gov/?action=airnow.local_city&zipcode="
							+ zipT.getText().toString().replaceAll("\\s","") + "&submit=Go");

			String result = "Conditions: ";

			try {
				HttpResponse response = client.execute(request);
				InputStream in = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				String line = null;
				boolean firstFlag = true;
				while ((line = reader.readLine()) != null) {
					if (line.matches(".*td height=\"38\" width=\"50\" align=\"center\" valign=\"middle\" .*") && firstFlag) {
						line = reader.readLine();
						result += line.substring(69, 73);
						line = reader.readLine();
						result += " ";
						result += line.substring(45, 47);
						firstFlag = false;
					}
				}
				in.close();
			} catch (IOException E) {
				tBox.setText("FAIL", TextView.BufferType.NORMAL);
			}

			if (result.equals("Conditions: ")) {
				result = "Invalid zip code";
			}
			
			return result;
		}

	}
}