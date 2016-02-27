package com.example.mobilesafe.bean;

import android.database.Cursor;

public class ContactInfo {
	private String name, phone;
	/**
	 * ��cursor����bean��cursor�Ѿ��ƶ�����Ӧλ��
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
