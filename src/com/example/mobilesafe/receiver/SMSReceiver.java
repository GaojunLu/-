package com.example.mobilesafe.receiver;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.LocationService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		ComponentName who = new ComponentName(context, MyDeviceAdminReceiver.class);
		DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		for (Object pdu : pdus) {
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
			String body = smsMessage.getMessageBody();
			if(body.equals("#*location*#")){
				System.out.println("防盗追踪");
				sendLocation(context);
				abortBroadcast();
			}else if(body.equals("#*alarm*#")){
				System.out.println("播放报警音乐");
				MediaPlayer player = MediaPlayer.create(context, R.raw.fox);
				player.setLooping(true);
				player.setVolume(1, 1);
				player.start();
				abortBroadcast();
			}else if(body.equals("#*wipedata*#")){
				if(dpm.isAdminActive(who)){
					System.out.println("远程删除数据");
//					dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
				}
				abortBroadcast();
			}else if(body.equals("#*lockscreen*#")){
				if(dpm.isAdminActive(who)){
					System.out.println("远程锁屏");
					dpm.resetPassword("123", 0);
					dpm.lockNow();
				}
				abortBroadcast();
			}
		}
	}
/**
 * 向安全号码发送短信报告位置
 * @param context
 */
	private void sendLocation(Context context) {
		// TODO Auto-generated method stub
		Intent service = new Intent(context, LocationService.class);
		context.startService(service);
	}

}
