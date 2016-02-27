package com.example.mobilesafe.ui;

import com.example.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItem extends RelativeLayout {

	private CheckBox cb;

	public SettingItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
		String itemTitle = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "itemTitle");
		((TextView)findViewById(R.id.tv_item_setting)).setText(itemTitle);
		
	}

	public SettingItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	void initView(Context context){
		this.addView(View.inflate(context, R.layout.item_setting, null));
		cb = (CheckBox) findViewById(R.id.cb_item_setting);
	}
	
	public boolean isChecked() {
		return cb.isChecked();
	}
	
	public void setChecked(boolean checked) {
		cb.setChecked(checked);
	}

}
