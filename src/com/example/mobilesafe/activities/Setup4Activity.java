package com.example.mobilesafe.activities;

import com.example.mobilesafe.R;
import com.example.mobilesafe.base.BaseSetupActivity;
import com.example.mobilesafe.receiver.MyDeviceAdminReceiver;
import com.example.mobilesafe.ui.SettingItem;
import com.example.mobilesafe.utiles.IntentUtiles;
import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Setup4Activity extends BaseSetupActivity {
	private SettingItem st_setup4_protect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		st_setup4_protect = (SettingItem) findViewById(R.id.st_setup4_protect);
		st_setup4_protect.setChecked(sp.getBoolean("isProtecting", false));
		st_setup4_protect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(st_setup4_protect.isChecked()){
					st_setup4_protect.setChecked(false);
					sp.edit().putBoolean("isProtecting", false).commit();
				}else{
					st_setup4_protect.setChecked(true);
					sp.edit().putBoolean("isProtecting", true).commit();
				}
			}
		});
	}

	@Override
	public void pre( ) {
		// TODO Auto-generated method stub
		IntentUtiles.startActivity(this, Setup3Activity.class);
	}

	@Override
	public void next( ) {
		// TODO Auto-generated method stub
		sp.edit().putBoolean("initSetup", true).commit();
		IntentUtiles.startActivity(this, SafeActivity.class);
	}
	
	public void activeAdmin(View view){
		ComponentName who = new ComponentName(this, MyDeviceAdminReceiver.class);
		Intent intent = new Intent(
				DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				who);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"说没事没事没事没事没事没事慢慢");
		startActivity(intent);
	}
}
