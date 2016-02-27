package com.example.mobilesafe.receiver;

import java.util.List;

import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.widget.Toast;

public class WidgetReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			am.killBackgroundProcesses(runningAppProcessInfo.processName);
		}
		Toast.makeText(context, "清理完成", 0).show();
	}

}
