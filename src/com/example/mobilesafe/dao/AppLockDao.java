package com.example.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AppLockDao {
	private String table = "lock";
	private SQLiteDatabase db;
	private Context context;

	public AppLockDao(Context context) {
		super();
		db = new AppLockDbHelper(context).getWritableDatabase();
		this.context = context;
	}

	public boolean insertApp(String name){
		ContentValues values = new ContentValues();
		values.put("app", name);
		boolean resualt =  db.insert(table , null, values)==-1?false:true;
		context.getContentResolver().notifyChange(Uri.parse("content://com.XX.applock"), null);
		return resualt;
	}
	
	public boolean deleteApp(String name){
		boolean resualt =  db.delete(table, "app = ?", new String[]{name})==0?false:true;
		context.getContentResolver().notifyChange(Uri.parse("content://com.XX.applock"), null);
		return resualt;
	}
	
	public boolean queryApp(String name){
		Cursor cursor = db.query(table, null, "app = ?", new String[]{name}, null, null, null);
		boolean resualt = cursor.moveToFirst();
		cursor.close();
		return resualt;
	}

	public void close() {
		// TODO Auto-generated method stub
		db.close();
	}

	public List<String> getAll() {
		// TODO Auto-generated method stub
		List<String> packageNames = new ArrayList<String>();
		Cursor cursor = db.query(table, new String[]{"app"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			packageNames.add(cursor.getString(0));
		}
		cursor.close();
		return packageNames;
	}
}
