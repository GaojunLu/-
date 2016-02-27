package com.example.mobilesafe.activities;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utiles.IntentUtiles;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SafeActivity extends Activity{ 
	private TextView tv_activity_safe_safenumber;
	private RelativeLayout rl_activity_safe_reset;
	private ImageView iv_activity_safe_isProtecting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safe);
		SharedPreferences sp = getSharedPreferences("setting", MODE_PRIVATE);
		tv_activity_safe_safenumber = (TextView) findViewById(R.id.tv_activity_safe_safenumber);
		rl_activity_safe_reset = (RelativeLayout) findViewById(R.id.rl_activity_safe_reset);
		iv_activity_safe_isProtecting = (ImageView) findViewById(R.id.iv_activity_safe_isProtecting);
		rl_activity_safe_reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IntentUtiles.startActivity(SafeActivity.this, Setup1Activity.class);
			}
		});
		tv_activity_safe_safenumber.setText(sp.getString("safenumber", null));
		if(sp.getBoolean("isProtecting", false)){
			iv_activity_safe_isProtecting.setImageResource(R.drawable.lock);
		}else{
			iv_activity_safe_isProtecting.setImageResource(R.drawable.unlock);
		}
	}

}
