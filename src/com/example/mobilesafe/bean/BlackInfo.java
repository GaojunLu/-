package com.example.mobilesafe.bean;

import android.database.Cursor;

public class BlackInfo {
	private String phone, type;
	
	public static BlackInfo getInstanceByCursor(Cursor cursor) {
		BlackInfo blackInfo = new BlackInfo();
		blackInfo.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
		blackInfo.setType(cursor.getString(cursor.getColumnIndex("type")));
		return blackInfo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
