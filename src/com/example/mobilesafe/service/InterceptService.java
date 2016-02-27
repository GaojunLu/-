package com.example.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.example.mobilesafe.dao.PhoneAddressDao;
import com.example.mobilesafe.utiles.ContactDbUtiles;
import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;
/**
 * 拦截电话、短信、显示归属地  服务
 * @author Administrator
 *
 */
public class InterceptService extends Service {

	private SmsReceiver receiver;
	private TelephonyManager telephonyManager;
	private PhoneStateListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//sms
		receiver = new SmsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, filter );
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		listener = new MyPhoneStateListener();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}
	
	class SmsReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for (Object pdu : pdus) {
				SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
				String phone = message.getOriginatingAddress();
				String type = ContactDbUtiles.queryByPhone(context, phone);
				if ("2".equals(type)||"3".equals(type)) {
					abortBroadcast();
					System.out.println("拦截短信"+phone+":"+message.getMessageBody());
				} 
			}
		}
		
	}
	/**
	 * 来电监听
	 * @author Administrator
	 *
	 */
	class MyPhoneStateListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			if(state==TelephonyManager.CALL_STATE_RINGING){
				String type = ContactDbUtiles.queryByPhone(InterceptService.this, incomingNumber);
				if("1".equals(type)||"3".equals(type)){
					System.out.println("挂断电话");
					endCall();
					//清除数据库
					final ContentResolver resolver = getContentResolver();
					final Uri uri = Uri.parse("content://call_log/calls");
					resolver.registerContentObserver(uri, true, new ContentObserver(new Handler()) {

						@Override
						public void onChange(boolean selfChange) {
							// TODO Auto-generated method stub
							super.onChange(selfChange);
							resolver.delete(uri, "number = "+incomingNumber, null);
						}
					});
				}
			}
		}
		
	}

	public void endCall() {
		try {
			Class<?> clazz = getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
			iTelephony.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
