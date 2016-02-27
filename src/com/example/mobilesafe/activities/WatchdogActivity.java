package com.example.mobilesafe.activities;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class WatchdogActivity extends Activity {
	private ImageView iv_icon;
	private EditText et_password;
	private String packageName;
	private Drawable icon;
	private CharSequence name;
	private TextView tv_appname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watchdog);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		et_password = (EditText) findViewById(R.id.et_password);
		tv_appname = (TextView) findViewById(R.id.tv_appname);
		
		Intent intent = getIntent();
		packageName = intent.getStringExtra("package");
		PackageManager pm = getPackageManager();
		try {
			icon = pm.getApplicationInfo(packageName, 0).loadIcon(pm);
			name = pm.getApplicationInfo(packageName, 0).loadLabel(pm);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iv_icon.setBackgroundDrawable(icon);
		tv_appname.setText(name);
	}
	
	public void click(View view){
		String password = et_password.getText().toString().trim();
		if("123".equals(password)){
			finish();
			//通知dog放行
			Intent intent = new Intent("com.XX.letgo");
			intent.putExtra("package", packageName);
			sendBroadcast(intent);
		}else{
			ToastUtiles.makeToast(this, "密码错误");
		}
	}
	
	@Override
	public void onBackPressed() {
//		<action android:name="android.intent.action.MAIN" />
//        <category android:name="android.intent.category.HOME" />
//        <category android:name="android.intent.category.DEFAULT" />
//        <category android:name="android.intent.category.MONKEY"/>
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.MONKEY");
		startActivity(intent);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}
}
