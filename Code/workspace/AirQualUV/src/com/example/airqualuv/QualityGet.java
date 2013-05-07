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
import android.app.Notification;
import android.app.NotificationManager;
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
				intent.putExtra("aq",((EditText)findViewById(R.id.aq_results)).getText().toString());
				intent.putExtra("ag",((EditText)findViewById(R.id.ag_results)).getText().toString());
				intent.putExtra("uv",((EditText)findViewById(R.id.uv_results)).getText().toString());
				startActivityForResult(intent, 0);
			}
		});
		
	}
	
	  public void createNotification(View view, String type) {
		    // Prepare intent which is triggered if the
		    // notification is selected
//		    Intent intent = new Intent(this, NotificationReceiver.class);

		    // Build notification
		    // Actions are just fake
		    Notification noti = new Notification.Builder(this)
		        .setContentTitle(type +  " Warning")
		        .setContentText("Dangerous levels of " + type).setSmallIcon(type.equals("AG") ? R.drawable.exp_ag_hev : type.equals("AQ") ? R.drawable.exp_aq_hev : R.drawable.exp_uv_hev).build();
		        
		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    // Hide the notification after its selected
		    noti.flags |= Notification.FLAG_AUTO_CANCEL;

		    notificationManager.notify(0, noti);

		  }

	View.OnClickListener clickedListener = new View.OnClickListener() {
		@Override
		public void onClick(final View view) {

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

					aqBox.setText(tokens[0]);
					agBox.setText(tokens[1]);
					uvBox.setText(tokens[2]);      
					
					//choosing low, moderate, and high for each feature
					//aq choosing
					ImageView aqView = (ImageView)findViewById(R.id.ImageView1);
					if (tokens[0].contains("invalid")) {
						aqView.setImageResource(R.drawable.invalid);
					} else {
					int aqVal = Integer.parseInt(tokens[0]);
					if (aqVal < 50) {
				        aqView.setImageResource(R.drawable.aq_low);
					} else if (aqVal < 100) {
				        aqView.setImageResource(R.drawable.aq_mod);
					} else {
				        aqView.setImageResource(R.drawable.aq_high);
						createNotification(view, "AQ");
					} }
					
					//ag choosing
					ImageView agView = (ImageView)findViewById(R.id.ImageView2);
					if (tokens[1].contains("invalid")) {
						agView.setImageResource(R.drawable.invalid);
					} else {
					float agVal = Float.parseFloat(tokens[1]);
					if (agVal < 5) {
				        agView.setImageResource(R.drawable.ag_low);
					} else if (agVal < 8) {
				        agView.setImageResource(R.drawable.ag_mod);
					} else {
				        agView.setImageResource(R.drawable.ag_high);
				        createNotification(view, "AG");
					} }
					
					//uv choosing
					ImageView uvView = (ImageView)findViewById(R.id.ImageView3);
					if (tokens[2].contains("invalid")) {
						uvView.setImageResource(R.drawable.invalid);
					} else {
					int uvVal = Integer.parseInt(tokens[2]);
					if (uvVal < 3) {
				        uvView.setImageResource(R.drawable.uv_low);
					} else if (uvVal < 6) {
				        uvView.setImageResource(R.drawable.uv_mod);
					} else {
				        uvView.setImageResource(R.drawable.uv_high);
				        createNotification(view, "UV");
					} }					
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
				result = "invalid";
			}

			if (result.equals("")) {
				result = "invalid";
			}
			
			return result;
		}

	}
}