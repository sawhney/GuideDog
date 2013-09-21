package com.hack.guidedog;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.hack.guidedog.info.LocationWeather;

public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
    
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private LocationClient mLocationClient;
	private	Vibrator vibrator;
	private Speaker speaker;
	private LocationWeather locationWeather;
	
	
	private ImageView central;
	private ImageView call;
	private ImageView msg;
	private ImageView mail;
	private ImageView nfc;
	private ImageView not;
	private ImageView ocr;
	private ImageView alarm;
	private ImageView settings;
	
	
	private static int WIDTH;
	private static int HEIGHT;
	
	private static float X_MAX;
	private static float X_MIN;

	private static float Y_MAX;
	private static float Y_MIN;
	
	private boolean flag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		if(checkPlayServices())
		    mLocationClient = new LocationClient(this, this, this);
		
		speaker = new Speaker(this);
		
		
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		WIDTH = metrics.widthPixels;
		HEIGHT = metrics.heightPixels;
		
//		X_MAX = (float)WIDTH*(float)0.75;
//		X_MIN = (float)WIDTH*(float)0.25;
//		Y_MAX = (float)HEIGHT*(float)0.75;
//		Y_MIN = (float)HEIGHT*(float)0.25;
		
		X_MAX = (float)WIDTH*(float)0.5+100;
		X_MIN = (float)WIDTH*(float)0.5-100;
		Y_MAX = (float)HEIGHT*(float)0.5+100;
		Y_MIN = (float)HEIGHT*(float)0.5-100;
		
		flag = false;
		
		
		central = (ImageView) findViewById(R.id.central);
		call = (ImageView) findViewById(R.id.call);
		msg = (ImageView) findViewById(R.id.msg);
		mail = (ImageView) findViewById(R.id.mail);
		nfc = (ImageView) findViewById(R.id.nfc);
		ocr = (ImageView) findViewById(R.id.ocr);
		alarm = (ImageView) findViewById(R.id.alarm);
		settings = (ImageView) findViewById(R.id.settings);
		not = (ImageView) findViewById(R.id.not);

	}
	
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_UP :
			central.setImageResource(R.drawable.central);
			if(flag) {
				flag = false;
				String result = getResult(event.getX(),event.getY());
				handleResult(result);
			}
			break;
			
		case MotionEvent.ACTION_DOWN:
			System.out.println("x  :"+ event.getX());
			System.out.println("y  :"+ event.getY());
			if (checkCenter(event.getX(),event.getY())) {
				flag = true;
				vibrate();
				central.setImageResource(R.drawable.central2);
			}
			break;
		}
		return true;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		speaker.stop();
		super.onPause();
	}
	
	boolean checkCenter(float x, float y) {
		return (x>X_MIN && x<X_MAX && y>Y_MIN && y<Y_MAX) ;
	}
	
	void handleResult(String result) {
		central.setImageResource(R.drawable.central);
		call.setImageResource(R.drawable.call);
		msg.setImageResource(R.drawable.msg);
		mail.setImageResource(R.drawable.mail);
		nfc.setImageResource(R.drawable.nfc);
		not.setImageResource(R.drawable.not);
		ocr.setImageResource(R.drawable.ocr);
		alarm.setImageResource(R.drawable.alarm);
		settings.setImageResource(R.drawable.settings);


		if(result.equals("CENTRE"))
			centreClicked();
		else if(result.equals("TOP LEFT"))
			settings.setImageResource(R.drawable.settings2);
		else if(result.equals("TOP"))
			call.setImageResource(R.drawable.call2);
		else if(result.equals("TOP RIGHT"))
			msg.setImageResource(R.drawable.msg2);
		else if(result.equals("LEFT"))
			alarm.setImageResource(R.drawable.alarm2);
		else if(result.equals("RIGHT"))		
			mail.setImageResource(R.drawable.mail2);
		else if(result.equals("BOTTOM RIGHT"))
			nfc.setImageResource(R.drawable.nfc2);
		else if(result.equals("BOTTOM"))
			ocr.setImageResource(R.drawable.ocr2);
		else if(result.equals("BOTTOM LEFT"))
			not.setImageResource(R.drawable.not2);
	}
	
	private void centreClicked() {
		central.setImageResource(R.drawable.central2);
		String date = locationWeather.getDate();
		speaker.speak(date);
		String weather = locationWeather.getWeather();
		if(weather!= null && !weather.startsWith("Error"))
			speaker.speak(weather);
		String location = locationWeather.getLocation();
		if(location!= null && !location.startsWith("Error"))
			speaker.speak(location);
	}
	
	String getResult(float x, float y) {
		String result = null;
		if(x<X_MIN&&y<Y_MIN)
			result = "TOP LEFT";
		else if(x>X_MIN && x< X_MAX && y<Y_MIN)
			result = "TOP";
		else if(x>X_MAX && y<Y_MIN)
			result = "TOP RIGHT";
		else if(x<X_MIN && y>Y_MIN && y<Y_MAX)
			result = "LEFT";
		else if(x>X_MIN && x<X_MAX && y>Y_MIN && y<Y_MAX )
			result = "CENTRE";
		else if(x>X_MAX && y>Y_MIN && y<Y_MAX)
			result = "RIGHT";
		else if(x<X_MIN && y>Y_MAX)
			result = "BOTTOM LEFT";
		else if(x>X_MIN && x<X_MAX && y>Y_MAX)
			result = "BOTTOM";
		else if(x>X_MAX && y>Y_MAX)
			result = "BOTTOM RIGHT";
		return result;
	}

	
	
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
////		startService(new Intent(this, MainService.class));
//		
//	}

	
	
	private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("GuideDog", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }	

private void vibrate() {
	vibrator.vibrate(50);
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
		locationWeather = new LocationWeather(this,mLocationClient);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		System.out.println("disconnected");
	}
	
	
}
