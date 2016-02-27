package com.example.mobilesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class LocationService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		String bestProvider = locationManager.getBestProvider(criteria , true);
		System.out.println(bestProvider);
		locationManager.requestLocationUpdates(bestProvider, 0, 0, new LocationListener() {
			

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) { 
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				SharedPreferences sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
				String msg = "Longitude:"+location.getLongitude()+";Latitude"+location.getLatitude();
				SmsManager.getDefault().sendTextMessage(sp.getString("safenumber", null), null, msg, null, null);
				locationManager.removeUpdates(this);
				stopSelf();
			}
		});
		
		return super.onStartCommand(intent, flags, startId);
	}
	
}
