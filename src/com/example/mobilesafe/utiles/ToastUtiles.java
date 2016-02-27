package com.example.mobilesafe.utiles;

import android.app.Activity;
import android.widget.Toast;

/**
 * 用于在各种线程弹toast
 * @author Administrator
 *
 */
public class ToastUtiles {
	public static void makeToast(final Activity context, final String msg){
		if(Thread.currentThread().getName().equalsIgnoreCase("main")){
			Toast.makeText(context, msg, 0).show();
		}else {
			context.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(context, msg, 0).show();
				}
			});
		}
	}
}
