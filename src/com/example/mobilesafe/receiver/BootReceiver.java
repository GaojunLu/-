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
		if(sp.getBoolean("isProtecting", false)){//�����˱���
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String realSIM = telephonyManager.getSimSerialNumber();
			String bindSIM = sp.getString("sim", null);
			if(!realSIM.equals(bindSIM)){
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(sp.getString("safenumber", null), null, "SIM change!", null, null);
			}
		}
		//����������
		
		//������ʾ����
	}

}
