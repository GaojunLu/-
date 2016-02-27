package com.example.mobilesafe.activities;

import com.example.mobilesafe.R;
import com.example.mobilesafe.dao.PhoneAddressDao;
import com.example.mobilesafe.engine.SmsTools;
import com.example.mobilesafe.engine.SmsTools.SmsCallBack;
import com.example.mobilesafe.utiles.IntentUtiles;
import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class AToolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}

	public void queryAddress(View view) {
		IntentUtiles.startActivityNotFinish(this, QueryAddress.class);
		//¸ã¸öÒì³£
//		int i = 1/0;
	}

	public void queryCommAddress(View view) {
		IntentUtiles.startActivityNotFinish(this, QueryCommAddress.class);
	}

	public void saveSMS(View view){
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.show();
		final SmsTools smsTools = new SmsTools(this);
		new Thread(){public void run() {
			
			smsTools.saveSms("sms.xml", new SmsCallBack() {
				
				@Override
				public void SmsProgressCount(int count) {
					// TODO Auto-generated method stub
					progressDialog.setProgress(count);
				}
				
				@Override
				public void SmsCount(int count) {
					// TODO Auto-generated method stub
					progressDialog.setMax(count);
				}
			});
			progressDialog.dismiss();
		};}.start();
	}

	public void loadSMS(View view) {
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.show();
		final SmsTools smsTools = new SmsTools(this);
		new Thread(){public void run() {
			
			smsTools.loadSms("sms.xml", new SmsCallBack() {
				
				@Override
				public void SmsProgressCount(int count) {
					// TODO Auto-generated method stub
					progressDialog.setProgress(count);
				}
				
				@Override
				public void SmsCount(int count) {
					// TODO Auto-generated method stub
					progressDialog.setMax(count);
				}
			});
			progressDialog.dismiss();
		};}.start();
	}
	
	public void appLock(View view){
		IntentUtiles.startActivityNotFinish(this, AppLockActivity.class);
	}
}
