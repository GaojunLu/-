package com.example.mobilesafe.activities;

import com.example.mobilesafe.R;
import com.example.mobilesafe.dao.PhoneAddressDao;
import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class QueryAddress extends Activity {
	private EditText et_phone;
	private TextView tv_adddress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_address);
		et_phone = (EditText) findViewById(R.id.et_phone);
		tv_adddress = (TextView) findViewById(R.id.tv_adddress);
	}
	
	public void queryAddress(View view){
		String phone = et_phone.getText().toString();
		if(TextUtils.isEmpty(phone)){
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_phone.startAnimation(animation);
			ToastUtiles.makeToast(this, "«Î ‰»Î∫≈¬Î");
			//’∂Ø
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(new long[]{100l,200l,500l,200l,1000l}, -1);
			return;
		}
		String address = PhoneAddressDao.getAddressByPhone(this, phone);
		tv_adddress.setText(address);
	}
}
