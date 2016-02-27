package com.example.mobilesafe.dao;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PhoneAddressDao {

	public static String getAddressByPhone(Context context, String phone) {
		// TODO Auto-generated method stub
		String path = new File(context.getFilesDir(), "address.db").getAbsolutePath();
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		String sql;
		if(phone.matches("^1[3578][0-9]{9}$")){//手机号
			sql = "select location from data2 where id =(select outkey from data1 where id = ?)";
			Cursor cursor = db.rawQuery(sql, new String[]{phone.substring(0, 7)});
			if(cursor.moveToFirst()){
				String add = cursor.getString(0);
				cursor.close();
				db.close();
				return add;
			}
		}
		if (phone.matches("^0\\d{2}\\d{7,8}$")) {//三位区号电话号
			sql = "select location from data2 where area = ?";
			Cursor cursor = db.rawQuery(sql, new String[]{phone.substring(1, 3)});
			if(cursor.moveToFirst()){
				String add = cursor.getString(0);
				cursor.close();
				db.close();
				return add.substring(0, add.length()-2);
			}
		}
		if (phone.matches("^0\\d{3}\\d{7,8}$")) {//四位区号电话号
			sql = "select location from data2 where area = ?";
			Cursor cursor = db.rawQuery(sql, new String[]{phone.substring(1, 4)});
			if(cursor.moveToFirst()){
				String add = cursor.getString(0);
				cursor.close();
				db.close();
				return add.substring(0, add.length()-2);
			}
		}
		if (phone.matches("\\d{3}")) {//三位电话号
			if("110".equals(phone)){
				return "匪警";
			}else if("120".equals(phone)){
				return "急救";
			}else if("119".equals(phone)){
				return "火警";
			}
		}else if (phone.matches("\\d{4}")) {//四位电话号
			return "模拟器";
		}else if (phone.matches("\\d{5}")) {//五位电话号
			return "客服电话";
		}else if (phone.matches("\\d{7,8}")) {//五位电话号
			return "本地号码";
		}
		
		
		return phone+"未知";
	}

}
