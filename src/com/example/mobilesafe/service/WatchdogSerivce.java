package com.example.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.activities.WatchdogActivity;
import com.example.mobilesafe.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

public class WatchdogSerivce extends Service {

	private boolean flag = true;
	AppLockDao dao;
	private MyReceiver receiver;
	private List<String> tempPackages;
	List<String> lockApps;
	private ActivityManager am;
	private String packageName;
	Intent intent ;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dao = new AppLockDao(this);
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.XX.letgo");
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(receiver, filter);
		intent = new Intent(WatchdogSerivce.this, WatchdogActivity.class);
		lockApps = dao.getAll();
		tempPackages = new ArrayList<String>();
		getContentResolver().registerContentObserver(Uri.parse("content://com.XX.applock"), true, new ContentObserver(new Handler()) {
			@Override
			public void onChange(boolean selfChange) {
				// TODO Auto-generated method stub
				super.onChange(selfChange);
				lockApps = dao.getAll();
			}
		});
		startDog();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		flag = false;
		dao.close();
		unregisterReceiver(receiver);
	}

	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				tempPackages.clear();;
				stopDog();
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				startDog();
			} else {
				tempPackages.add(intent.getStringExtra("package"));
			}
		}

		

	}

	public void startDog() {
		// TODO Auto-generated method stub
		flag = true;
		new Thread() {

			public void run() {
				while (flag) {
					System.out.println("dog");
					am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					packageName = am.getRunningTasks(1).get(0).topActivity
							.getPackageName();
					if (lockApps.contains(packageName)) {
						if (tempPackages.contains(packageName)) {
							continue;
						} else {
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra("package", packageName);
							startActivity(intent);
						}
					}
					try {
						sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	public void stopDog() {
		// TODO Auto-generated method stub
		flag = false;
	}
	
}
