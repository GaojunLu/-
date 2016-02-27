package com.example.mobilesafe.utiles;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtiles {
	public static boolean isServiceRunning(Context context, String setviceClassName) {
		// TODO Auto-generated method stub
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String name = runningServiceInfo.service.getClassName();
			if(name.equals(setviceClassName)){
				return true;
			}
		}
		return false;
	}
}
