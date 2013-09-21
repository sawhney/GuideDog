package com.hack.guidedog;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
	
	LocationClient mLocationClient;
	Button btn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startService(new Intent(this, MainService.class));
		mLocationClient = new LocationClient(this, this, this);
		btn = (Button) findViewById(R.id.btn);
		btn.setEnabled(false);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Location mCurrentLocation = mLocationClient.getLastLocation();
				double lat = mCurrentLocation.getLatitude();
				double lng = mCurrentLocation.getLongitude();
//				Toast.makeText(getApplicationContext(),lat+" ,  "+lng , Toast.LENGTH_SHORT).show();
				Geocoder gcd = new Geocoder(getApplicationContext(), Locale.US);
				List<Address> addresses = null;
				try {
					addresses = gcd.getFromLocation(lat, lng, 1);
					System.out.println("done");
					if (addresses.size() > 0) 
					    System.out.println("locality: "+addresses.get(0).getLocality());
						System.out.println("Admin area: "+addresses.get(0).getAdminArea());
						System.out.println("postal code: "+addresses.get(0).getPostalCode());
						System.out.println("sub locality: "+addresses.get(0).getSubLocality());
						System.out.println("premises: "+addresses.get(0).getPremises());
						System.out.println("thoroughfare: "+addresses.get(0).getThoroughfare());
						System.out.println("SubAdminArea: "+addresses.get(0).getSubAdminArea());
						System.out.println("SubThroroughfare: "+addresses.get(0).getSubThoroughfare());
						System.out.println("FeatureName: "+addresses.get(0).getFeatureName());
						System.out.println("toString: "+ addresses.get(0).toString());
						System.out.println("\n\n\n");
						Calendar c = Calendar.getInstance(); 
						String date = c.get(Calendar.DAY_OF_MONTH)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.YEAR);
						String time = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
						System.out.println(date+"      "+ time+"\n\n\n");
						try {
							URL url = new URL("http://api.worldweatheronline.com/free/v1/weather.ashx?key=nu5h5purxrw9g7qyjjc87ys8&q="+lat+","+lng+"&format=xml");
							HttpURLConnection con = (HttpURLConnection) url
									.openConnection();

							con.setRequestMethod("GET");
					 					 
							int responseCode = con.getResponseCode();
//							System.out.println("\nSending 'GET' request to URL : " + url);
//							System.out.println("Response Code : " + responseCode);
							String s = readStream(con.getInputStream());
							System.out.println(s);
						} catch (Exception e) {
							e.printStackTrace();
						}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		});
	}
	
	public String readStream(InputStream is) {
		BufferedReader in = null;
		String temp = null;
		String description = null;
		try {
			in = new BufferedReader(
			        new InputStreamReader(is));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			String line = response.substring(response.indexOf("<current_condition>"), response.indexOf("</current_condition>"));
		    temp = line.substring(line.indexOf("<temp_C>"), line.indexOf("</temp_C>"));
		    description = line.substring(line.indexOf("<weatherDesc><![CDATA["), line.indexOf("]]></weatherDesc>"));
		} catch (IOException e) {
		    e.printStackTrace();
		  } finally {
		    if (in != null) {
		      try {
		        in.close();
		      } catch (IOException e) {
		        e.printStackTrace();
		        }
		    }
		  }
		
		return "The temperature is " + temp +"degrees Celcius and it is " + description;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mLocationClient.connect();
	}

	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mLocationClient.disconnect();
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		System.out.println("connection failed");
		Toast.makeText(this, "connection failed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		System.out.println("connected");
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		btn.setEnabled(true);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		System.out.println("disconnected");
		Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();

	}
 
}
