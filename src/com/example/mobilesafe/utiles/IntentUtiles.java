package com.example.mobilesafe.utiles;

import android.app.Activity;
import android.content.Intent;

/**
 * 用于简单的显示跳转，销毁当前
 * 
 * @author Administrator
 *
 */
public class IntentUtiles {
	/**
	 * 延时跳转
	 * @param activity
	 * @param cls
	 * @param delay 延迟时间
	 */
	public static void startActivityForDelay(final Activity activity, Class<?> cls,
			final long delay) {
		final Intent intent = new Intent(activity, cls);
		new Thread() {
			public void run() {
				try {
					sleep(delay);
					activity.startActivity(intent);
					activity.finish();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			};
		}.start();
	}
	/**
	 * 即时跳转，销毁当前
	 * @param activity
	 * @param cls
	 */
	public static void startActivity(final Activity activity, Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		activity.startActivity(intent);
		activity.finish();
	}
	/**
	 * 即时跳转，不销毁
	 * @param activity
	 * @param cls
	 */
	public static void startActivityNotFinish(final Activity activity, Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		activity.startActivity(intent);
	}
}
