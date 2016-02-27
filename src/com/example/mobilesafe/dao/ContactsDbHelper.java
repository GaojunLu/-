package com.example.mobilesafe.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 黑名单数据库创建
 * @author Administrator
 *
 */
public class ContactsDbHelper extends SQLiteOpenHelper {

	public ContactsDbHelper(Context context) {
		super(context, "contacts.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = 
				"create table black("
				+ "_id integer primary key autoincrement,"
				+ "phone varchar(20),"
				+ "type varchar(20))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
