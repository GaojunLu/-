package com.example.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import com.example.mobilesafe.R;
import com.example.mobilesafe.engine.TaskInfoTool;
import com.example.mobilesafe.receiver.MyAppWidgetProvider;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

public class WidgetService extends Service {

	private Timer timer;
	private TimerTask task;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("开启了widget服务");
		final AppWidgetManager awm = AppWidgetManager.getInstance(WidgetService.this);
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("计时中");
				ComponentName provider = new ComponentName(WidgetService.this, MyAppWidgetProvider.class);
				RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
				views.setTextViewText(R.id.process_count, "当前进程数:"+TaskInfoTool.getTaskCount(WidgetService.this));
				views.setTextViewText(R.id.process_memory, "可用内存:"+Formatter.formatFileSize(WidgetService.this,TaskInfoTool.getFreeRam(WidgetService.this)));
				Intent intent = new Intent("mobilesafe.kill");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(WidgetService.this, 0, intent , PendingIntent.FLAG_UPDATE_CURRENT);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent );
				awm.updateAppWidget(provider, views);
			}
		};
		timer.schedule(task, 0, 5000);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("关闭了widget服务");
		timer.cancel();
		task.cancel();
		task=null;
		timer=null;
	}

}
