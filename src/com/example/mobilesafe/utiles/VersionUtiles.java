package com.example.mobilesafe.utiles;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class VersionUtiles {
	/**
	 * ��ȡ�汾��
	 * @param context
	 * @return û�о���null
	 */
	public static String getVersionName(Context context) {
		// TODO Auto-generated method stub
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * ��ȡ�汾��
	 * @param context
	 * @return û�о���0
	 */
	public static int getVersionCode(Context context) {
		// TODO Auto-generated method stub
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

}
