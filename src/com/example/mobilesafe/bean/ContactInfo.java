package com.example.mobilesafe.bean;

import android.database.Cursor;

public class ContactInfo {
	private String name, phone;
	/**
	 * 用cursor创建bean，cursor已经移动到相应位置
	 * @param cursor
	 * @return
	 */
	public static ContactInfo createFromCursor(Cursor cursor) {
		ContactInfo contactInfo = new ContactInfo();
		contactInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
		contactInfo.setPhone(cursor.getString(cursor.getColumnIndex("number")));
		return contactInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
