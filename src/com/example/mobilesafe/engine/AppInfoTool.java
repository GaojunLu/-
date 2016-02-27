package com.example.mobilesafe.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.mobilesafe.bean.AppInfo;

public class AppInfoTool {
	public static List<AppInfo> getAppInfos(Context context) {
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
		for (PackageInfo packageInfo : packageInfos) {
			AppInfo appInfo = new AppInfo();
			appInfo.setName((String) packageInfo.applicationInfo.loadLabel(pm));
			appInfo.setPackageName(packageInfo.packageName);
			appInfo.setIcon(packageInfo.applicationInfo.loadIcon(pm));
			String path = packageInfo.applicationInfo.sourceDir;
			File file = new File(path);
			appInfo.setSize(file.length());
			if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0){//系统应用
				appInfo.setSystemApp(true);
			}else {//用户应用
				appInfo.setSystemApp(false);
			}
			if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) !=0){//sd存储
				appInfo.setInSysStorage(false);
			}else{//手机存储
				appInfo.setInSysStorage(true);
			}
			appInfos.add(appInfo);
		}
		return appInfos;
	}
}
