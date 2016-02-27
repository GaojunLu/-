package com.example.mobilesafe.activities;

import java.util.List;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.InterceptService;
import com.example.mobilesafe.service.PhoneAddressService;
import com.example.mobilesafe.service.WatchdogSerivce;
import com.example.mobilesafe.ui.SettingItem;
import com.example.mobilesafe.ui.SettingItemShowDialog;
import com.example.mobilesafe.utiles.ServiceUtiles;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener {

	private boolean update;
	private SettingItem st_setting_autoupdate;
	private Editor editor;
	private SettingItem st_setting_intercept_black;
	private SettingItem st_setting_phoneaddress;
	private SettingItemShowDialog stsd_setting_address_bg;
	private SharedPreferences sp;
	private SettingItem st_setting_watchdog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acticity_setting);
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		editor = sp.edit();
		update = sp.getBoolean("update", false);
		st_setting_autoupdate = (SettingItem) findViewById(R.id.st_setting_autoupdate);
		st_setting_autoupdate.setOnClickListener(this);
		st_setting_autoupdate.setChecked(sp.getBoolean("update", false));
		// 黑名单拦截
		st_setting_intercept_black = (SettingItem) findViewById(R.id.st_setting_intercept_black);
		st_setting_intercept_black.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (st_setting_intercept_black.isChecked()) {
					st_setting_intercept_black.setChecked(false);
					// 关闭服务
					Intent intent = new Intent(SettingActivity.this,
							InterceptService.class);
					stopService(intent);
				} else {
					st_setting_intercept_black.setChecked(true);
					// 开启服务
					Intent intent = new Intent(SettingActivity.this,
							InterceptService.class);
					startService(intent);
				}
			}
		});
		// 归属地显示
		st_setting_phoneaddress = (SettingItem) findViewById(R.id.st_setting_phoneaddress);
		st_setting_phoneaddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(st_setting_phoneaddress.isChecked()){
					st_setting_phoneaddress.setChecked(false);
					Intent intent = new Intent(SettingActivity.this, PhoneAddressService.class);
					stopService(intent);
				}else{
					st_setting_phoneaddress.setChecked(true);
					Intent intent = new Intent(SettingActivity.this, PhoneAddressService.class);
					startService(intent);
				}
			}
		});
		//设置归属地背景
		stsd_setting_address_bg = (SettingItemShowDialog) findViewById(R.id.stsd_setting_address_bg);
		stsd_setting_address_bg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
				builder.setTitle("选择背景色：");
				final String[] items = {"蓝","深灰","灰","绿","橙","循环变色"};
				builder.setSingleChoiceItems(items , 0, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						sp.edit().putInt("addressBg", which).commit();
						stsd_setting_address_bg.setText(items[which]);
						dialog.dismiss();
					}
				});
				builder.show();
			}
		});
		st_setting_watchdog = (SettingItem) findViewById(R.id.st_setting_watchdog);
		st_setting_watchdog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(st_setting_watchdog.isChecked()){
					st_setting_watchdog.setChecked(false);
					Intent intent = new Intent(SettingActivity.this, WatchdogSerivce.class);
					stopService(intent);
				}else{
					st_setting_watchdog.setChecked(true);
					Intent intent = new Intent(SettingActivity.this, WatchdogSerivce.class);
					startService(intent);
				}
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		st_setting_intercept_black.setChecked(ServiceUtiles
				.isServiceRunning(this,
						"com.example.mobilesafe.service.InterceptService"));
		st_setting_phoneaddress.setChecked(ServiceUtiles
				.isServiceRunning(this,
						"com.example.mobilesafe.service.PhoneAddressService"));
		st_setting_watchdog.setChecked(ServiceUtiles
				.isServiceRunning(this,
						"com.example.mobilesafe.service.WatchdogSerivce"));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.st_setting_autoupdate:
			if (st_setting_autoupdate.isChecked()) {
				st_setting_autoupdate.setChecked(false);
				editor.putBoolean("update", false);
				editor.commit();
			} else {
				st_setting_autoupdate.setChecked(true);
				editor.putBoolean("update", true);
				editor.commit();
			}
			break;
		}
	}
}
