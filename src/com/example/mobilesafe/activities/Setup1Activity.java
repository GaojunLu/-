package com.example.mobilesafe.activities;

import com.example.mobilesafe.R;
import com.example.mobilesafe.base.BaseSetupActivity;
import com.example.mobilesafe.utiles.IntentUtiles;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Setup1Activity extends BaseSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void pre() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		IntentUtiles.startActivity(this, Setup2Activity.class);
	}
}
