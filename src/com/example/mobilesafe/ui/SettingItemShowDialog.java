package com.example.mobilesafe.ui;

import com.example.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemShowDialog extends RelativeLayout {

	private TextView tv_item_setting_desc;

	public SettingItemShowDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	public SettingItemShowDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		String itemTitle = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "itemTitle");
		((TextView)findViewById(R.id.tv_item_setting)).setText(itemTitle);
	}
	
	private void initView() {
		inflate(getContext(), R.layout.item_setting_showdialog, this);
		tv_item_setting_desc = (TextView) findViewById(R.id.tv_item_setting_desc);
	}	

	public void setText(String text){
		tv_item_setting_desc.setText(text);
	}
}
