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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;

//Richard Higgins
//May 6th, 2013

public class QualityGet extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_quality_get);

		//button listeners
		findViewById(R.id.lookupButton).setOnClickListener(clickedListener);
		findViewById(R.id.clothingButton).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Clothing.class);
				startActivityForResult(intent, 0);
			}
		});
		findViewById(R.id.exposureButton).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), ExposurePlot.class);
				startActivityForResult(intent, 0);
			}
		});
		
	}

	View.OnClickListener clickedListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {

			final TextView aqBox = (TextView) findViewById(R.id.aq_results);
			final TextView agBox = (TextView) findViewById(R.id.ag_results);
			final TextView uvBox = (TextView) findViewById(R.id.uv_results);
			aqBox.setText("Loading...");
			agBox.setText("Loading...");
			uvBox.setText("Loading...");
			
			new WebTrawl()
			{
				@Override public void onPostExecute(String result)
			    {
			       
					String [] tokens = result.split(" ");
					//choosing low, moderate, and high for each feature
					//aq choosing
					int aqVal = Integer.parseInt(tokens[0]);
					ImageView aqView = (ImageView)findViewById(R.id.ImageView1);
					if (aqVal < 50) {
				        aqView.setImageResource(R.drawable.aq_low);
					} else if (aqVal < 100) {
				        aqView.setImageResource(R.drawable.aq_mod);
					} else {
				        aqView.setImageResource(R.drawable.aq_high);
					}
					
					//ag choosing
					float agVal = Float.parseFloat(tokens[1]);
					ImageView agView = (ImageView)findViewById(R.id.ImageView2);
					if (agVal < 5) {
				        agView.setImageResource(R.drawable.ag_low);
					} else if (agVal < 8) {
				        agView.setImageResource(R.drawable.ag_mod);
					} else {
				        agView.setImageResource(R.drawable.ag_high);
					}
					
					//uv choosing
					int uvVal = Integer.parseInt(tokens[2]);
					ImageView uvView = (ImageView)findViewById(R.id.ImageView3);
					if (uvVal < 3) {
				        uvView.setImageResource(R.drawable.uv_low);
					} else if (uvVal < 6) {
				        uvView.setImageResource(R.drawable.uv_mod);
					} else {
				        uvView.setImageResource(R.drawable.uv_high);
					}

					aqBox.setText(tokens[0]);
					agBox.setText(tokens[1]);
					uvBox.setText(tokens[2]);
			        
					
			    }
				
			}.execute("");
		}

	};

	private class WebTrawl extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			//set-up
			EditText zipT = (EditText) findViewById(R.id.zipText);
			HttpClient client = new DefaultHttpClient();
			
			//sequences delimited in result by a space
			//air quality get
			HttpGet request = new HttpGet(
					"http://www.airnow.gov/?action=airnow.local_city&zipcode="
							+ zipT.getText().toString().replaceAll("\\s",""));
			String result = pullFromWebsite(client, request, ".*&nbsp;</td>.*", 45, 47);

			//allergy get
			request = new HttpGet(
					"http://www.wunderground.com/DisplayPollen.asp?Zipcode="
							+ zipT.getText().toString().replaceAll("\\s",""));
			result += " " + pullFromWebsite(client, request, ".*<div class=\"ce.*", 50, 54);

			//uv get
			request = new HttpGet(
					"http://oaspub.epa.gov/enviro/uv_search?zipcode="
							+ zipT.getText().toString().replaceAll("\\s",""));
			result += " " + pullFromWebsite(client, request, ".*alt=\"UVI.*", 72, 74);
			
			result = result.replace("\"", "");
			
			return result;
		}
		
		//gets the data from website source code
		private String pullFromWebsite(HttpClient client, HttpGet request, String htmlCatch, int b, int e) {
			String result = "";
			
			try {
				HttpResponse response = client.execute(request);
				InputStream in = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				String line = null;
				boolean firstFlag = true; //flag to show first line is the correct one
				while ((line = reader.readLine()) != null && firstFlag) {
					if (line.matches(htmlCatch)) {
						result += line.substring(b, e);
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