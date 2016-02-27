package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		SharedPreferences sp = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
		if(sp.getBoolean("isProtecting", false)){//开启了保护
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String realSIM = telephonyManager.getSimSerialNumber();
			String bindSIM = sp.getString("sim", null);
			if(!realSIM.equals(bindSIM)){
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(sp.getString("safenumber", null), null, "SIM change!", null, null);
			}
		}
		//黑名单服务
		
		//来电显示服务
	}

}
