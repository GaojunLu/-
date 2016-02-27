package com.example.mobilesafe.activities;

import com.example.mobilesafe.R;
import com.example.mobilesafe.base.BaseSetupActivity;
import com.example.mobilesafe.bean.ContactInfo;
import com.example.mobilesafe.utiles.CursorUtils;
import com.example.mobilesafe.utiles.IntentUtiles;
import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Setup3Activity extends BaseSetupActivity {
	private EditText et_setup3_safephone;
	private Button bt_setup3_selectcontact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		et_setup3_safephone = (EditText) findViewById(R.id.et_setup3_safephone);
		bt_setup3_selectcontact = (Button) findViewById(R.id.bt_setup3_selectcontact);
		bt_setup3_selectcontact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 打开系统选择联系人界面
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("vnd.android.cursor.dir/phone");
				startActivityForResult(intent, 0);
			}
		});
		et_setup3_safephone.setText(sp.getString("safenumber", null));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(RESULT_OK==resultCode){
			Uri uri = data.getData();
			Cursor cursor = getContentResolver().query(uri, null, null, null, null);
			cursor.moveToFirst();
			ContactInfo contactInfo = ContactInfo.createFromCursor(cursor);
			String number = contactInfo.getPhone().replace("-", "").replace(" ", "").trim();
			et_setup3_safephone.setText(number);
		}
	}

	@Override
	public void pre() {
		// TODO Auto-generated method stub
		IntentUtiles.startActivity(this, Setup2Activity.class);
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		String safenumber = et_setup3_safephone.getText().toString();
		if(!TextUtils.isEmpty(safenumber)){
			sp.edit().putString("safenumber", safenumber).commit();
			IntentUtiles.startActivity(this, Setup4Activity.class);
		}else{
			ToastUtiles.makeToast(this, "请先输入安全号码");
		}
	}
}
