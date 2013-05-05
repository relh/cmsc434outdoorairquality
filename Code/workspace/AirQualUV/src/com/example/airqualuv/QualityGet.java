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

			TextView aqBox = (TextView) findViewById(R.id.aq_results);
			TextView agBox = (TextView) findViewById(R.id.ag_results);
			TextView uvBox = (TextView) findViewById(R.id.uv_results);
			aqBox.setText("Loading...");
			agBox.setText("Loading...");
			uvBox.setText("Loading...");
			
			new WebTrawl()
			{
			    @Override public void onPostExecute(String result)
			    {
			       
			        
			    }
			}.execute("");
		}

	};

	private class WebTrawl extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			EditText zipT = (EditText) findViewById(R.id.zipText);
			TextView aqBox = (TextView) findViewById(R.id.aq_results);
			TextView agBox = (TextView) findViewById(R.id.ag_results);
			TextView uvBox = (TextView) findViewById(R.id.uv_results);

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(
					"http://www.airnow.gov/?action=airnow.local_city&zipcode="
							+ zipT.getText().toString().replaceAll("\\s",""));

			String result = pullFromWebsite(client, request, ".*&nbsp;</td>.*");
			aqBox.setText(result);
			
			/*request = new HttpGet(
					"http://oaspub.epa.gov/enviro/uv_search?zipcode="
							+ zipT.getText().toString().replaceAll("\\s",""));

			result = pullFromWebsite(client, request, ".*alt=\"UVI.*", 20, 21);
			uvBox.setText(result);
			*/
			return result;
		}
		
		private String pullFromWebsite(HttpClient client, HttpGet request, String htmlCatch) {
			String result = "";
			
			try {
				HttpResponse response = client.execute(request);
				
				TextView aqBox = (TextView) findViewById(R.id.aq_results);
				aqBox.setText("HIHIHI");
				
				InputStream in = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				String line = null;
				boolean firstFlag = true;
				while ((line = reader.readLine()) != null && firstFlag) {
					
					if (line.matches(htmlCatch)) {
						aqBox.setText(line);
						result += line;
						firstFlag = false;
					}
				}
				in.close();
			} catch (IOException E) {
				result = "Invalid zip code";
			}

			if (result.equals("")) {
				result = "Invalid zip code";
			}
			
			return result;
		}

	}
}