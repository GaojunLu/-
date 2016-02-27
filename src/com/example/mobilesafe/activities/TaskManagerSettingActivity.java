package com.example.mobilesafe.activities;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.KillService;
import com.example.mobilesafe.ui.SettingItem;
import com.example.mobilesafe.utiles.ServiceUtiles;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TaskManagerSettingActivity extends Activity {

	private SettingItem st_show_systask;
	private SettingItem st_auto_kill;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acticity_task_setting);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		st_show_systask = (SettingItem) findViewById(R.id.st_show_systask);
		st_auto_kill = (SettingItem) findViewById(R.id.st_auto_kill);
	}

	private void initData() {
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		st_show_systask.setChecked(sp.getBoolean("showSysTask", false));
	}

	private void initListener() {
		// TODO Auto-generated method stub
		st_show_systask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(st_show_systask.isChecked()){
					st_show_systask.setChecked(false);
					sp.edit().putBoolean("showSysTask", false).commit();
				}else{
					st_show_systask.setChecked(true);
					sp.edit().putBoolean("showSysTask", true).commit();
				}
			}
		});
		st_auto_kill.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(st_auto_kill.isChecked()){
					st_auto_kill.setChecked(false);
					//停止服务
					Intent service = new Intent(TaskManagerSettingActivity.this, KillService.class);
					stopService(service);
				}else{
					st_auto_kill.setChecked(true);
					//开启服务
					Intent service = new Intent(TaskManagerSettingActivity.this, KillService.class);
					startService(service);
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(ServiceUtiles.isServiceRunning(this, "com.example.mobilesafe.service.KillService")){
			st_auto_kill.setChecked(true);
		}else{
			st_auto_kill.setChecked(false);
		}
	}
}
