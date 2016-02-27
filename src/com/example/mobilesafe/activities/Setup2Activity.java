package com.example.mobilesafe.activities;

import com.example.mobilesafe.R;
import com.example.mobilesafe.base.BaseSetupActivity;
import com.example.mobilesafe.ui.SettingItem;
import com.example.mobilesafe.utiles.IntentUtiles;
import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;

public class Setup2Activity extends BaseSetupActivity {
	private SettingItem st_setup2_bindsim;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		st_setup2_bindsim = (SettingItem) findViewById(R.id.st_setup2_bindsim);
		st_setup2_bindsim.setChecked(sp.getBoolean("isBindSim", false));
		st_setup2_bindsim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(st_setup2_bindsim.isChecked()){
					st_setup2_bindsim.setChecked(false);
					sp.edit().putBoolean("isBindSim", false).commit();
				}else{
					st_setup2_bindsim.setChecked(true);
					sp.edit().putBoolean("isBindSim", true).commit();
				}
			}
		});
	}

	@Override
	public void pre( ) {
		// TODO Auto-generated method stub
		IntentUtiles.startActivity(this, Setup1Activity.class);
	}

	@Override
	public void next( ) {
		// TODO Auto-generated method stub
		if(st_setup2_bindsim.isChecked()){
			bindSim();
			IntentUtiles.startActivity(this, Setup3Activity.class);
		}else {
			ToastUtiles.makeToast(this, "请先绑定SIM卡");
		}
	}
/**
 * 绑定sim卡：获取SIM卡号，存到setting中的sim属性中
 */
	private void bindSim() {
		// TODO Auto-generated method stub
		TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String simSerialNumber = manager.getSimSerialNumber();
		Editor editor = sp.edit();
		editor.putString("sim", simSerialNumber);
		editor.commit();
	}
}
