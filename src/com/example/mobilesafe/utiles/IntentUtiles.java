package com.example.mobilesafe.utiles;

import android.app.Activity;
import android.content.Intent;

/**
 * ���ڼ򵥵���ʾ��ת�����ٵ�ǰ
 * 
 * @author Administrator
 *
 */
public class IntentUtiles {
	/**
	 * ��ʱ��ת
	 * @param activity
	 * @param cls
	 * @param delay �ӳ�ʱ��
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
	 * ��ʱ��ת�����ٵ�ǰ
	 * @param activity
	 * @param cls
	 */
	public static void startActivity(final Activity activity, Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		activity.startActivity(intent);
		activity.finish();
	}
	/**
	 * ��ʱ��ת��������
	 * @param activity
	 * @param cls
	 */
	public static void startActivityNotFinish(final Activity activity, Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		activity.startActivity(intent);
	}
}
