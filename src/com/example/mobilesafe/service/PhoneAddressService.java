package com.example.mobilesafe.service;

import com.example.mobilesafe.R;
import com.example.mobilesafe.dao.PhoneAddressDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 显示归属地 服务
 * 
 * @author Administrator
 *
 */
public class PhoneAddressService extends Service {

	private TelephonyManager telephonyManager;
	private MyListener listener = new MyListener();
	private OutCallReceiver receiver;
	private int[] bgs = { R.drawable.call_locate_blue,
			R.drawable.call_locate_darkgray, R.drawable.call_locate_gray,
			R.drawable.call_locate_green, R.drawable.call_locate_orange,R.drawable.change_color };
	private SharedPreferences sp;
	private WindowManager windowManager;
	private View view;
	private WindowManager.LayoutParams params;
	long[] mHits = new long[2];

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		view = View.inflate(PhoneAddressService.this, R.layout.address_toast, null);
        view.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dx = newX-startX;
					int dy = newY-startY;
					params.x += dx;
					params.y += dy;
					if(params.x<0)params.x = 0;
					if(params.y<0)params.y = 0;
					if(params.x>windowManager.getDefaultDisplay().getWidth()-view.getWidth()){
						params.x=windowManager.getDefaultDisplay().getWidth()-view.getWidth();
					}
					if(params.y>windowManager.getDefaultDisplay().getHeight()-view.getHeight()){
						params.y=windowManager.getDefaultDisplay().getHeight()-view.getHeight();
					}
					windowManager.updateViewLayout(view, params);
					startX = newX;
					startY = newY;
					break;
				case MotionEvent.ACTION_UP:
					System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
		            mHits[mHits.length-1] = SystemClock.uptimeMillis();
		            if (mHits[0] >= (SystemClock.uptimeMillis()-500)) {
		            	params.x=(windowManager.getDefaultDisplay().getWidth()-view.getWidth())/2;
		            	windowManager.updateViewLayout(view, params);
		            }
		            sp.edit().putInt("startX", params.x).putInt("startY", params.y).commit();
					break;
				}
				return true;
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		unregisterReceiver(receiver);
	}

	class MyListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			if (state == TelephonyManager.CALL_STATE_RINGING) {
				String address = PhoneAddressDao.getAddressByPhone(
						PhoneAddressService.this, incomingNumber);
//				Toast.makeText(PhoneAddressService.this, address, 1).show();
				showAddress(address);
			}else if (state == TelephonyManager.CALL_STATE_IDLE) {
				if(windowManager!=null){
					windowManager.removeView(view);
					windowManager=null;
				}
			}
		}
	}

	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String phone = getResultData();
			String address = PhoneAddressDao.getAddressByPhone(
					PhoneAddressService.this, phone);
//			Toast.makeText(PhoneAddressService.this, address, 1).show();
			showAddress(address);
		}

	}

	public void showAddress(String address) {
		// TODO Auto-generated method stub
		windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		params = new WindowManager.LayoutParams();
		params.gravity = Gravity.LEFT+Gravity.TOP;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        params.x = sp.getInt("startX", 0);
        params.y = sp.getInt("startY", 0);
        ((TextView)view.findViewById(R.id.tv_address_toast)).setText(address);
        view.setBackgroundResource(bgs[sp.getInt("addressBg", 0)]);
        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
        if(animationDrawable!=null)animationDrawable.start();
        windowManager.addView(view, params);
	}

}
