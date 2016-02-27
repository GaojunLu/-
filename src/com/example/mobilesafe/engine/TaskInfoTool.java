package com.example.mobilesafe.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.TaskInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class TaskInfoTool {
	/**
	 * 获取当前运行进程数
	 * 
	 * @param context
	 * @return
	 */
	public static int getTaskCount(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getRunningAppProcesses().size();
	}

	/**
	 * 获取可用内存
	 * 
	 * @param context
	 * @return
	 */
	public static long getFreeRam(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		activityManager.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}

	/**
	 * 获取全部内存
	 * 
	 * @param context
	 * @return
	 */
	public static long getTotleRam(Context context) {
		File file = new File("/proc/meminfo");
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String s = bf.readLine();
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < s.length(); i++) {
				if(s.charAt(i)>='0'&&s.charAt(i)<='9'){
					builder.append(s.charAt(i));
				}
			}
			return Long.valueOf(builder.toString())*1024;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static List<TaskInfo> getAllTask(Context context){
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager packageManager = context.getPackageManager();
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			TaskInfo taskInfo = new TaskInfo();
			String packageName = runningAppProcessInfo.processName;
			taskInfo.setPackageName(packageName);
			long memsize = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid})[0].getTotalPrivateDirty()*1024;
			taskInfo.setMemsize(memsize);
			try {
				Drawable icon = packageManager.getApplicationInfo(packageName, 0).loadIcon(packageManager);
				String taskName = (String) packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager);
				taskInfo.setIcon(icon);
				taskInfo.setTaskName(taskName);
				int flags = packageManager.getApplicationInfo(packageName, 0).flags;
				if((flags & ApplicationInfo.FLAG_SYSTEM)!=0){
					taskInfo.setUserTask(false);
				}else{
					taskInfo.setUserTask(true);
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_laun));
				taskInfo.setTaskName(packageName);
				taskInfo.setUserTask(false);
			}
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}
}
