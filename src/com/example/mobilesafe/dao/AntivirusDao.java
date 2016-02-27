package com.example.mobilesafe.dao;

import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntivirusDao {
	Context context; 
	private SQLiteDatabase db;

	public AntivirusDao(Context context) {
		super();
		this.context = context;
		db = context.openOrCreateDatabase(context.getFilesDir().getAbsolutePath()+"/antivirus.db", Context.MODE_PRIVATE, null);
	}
	/**
	 * 
	 * @param md5
	 * @return 病毒描述；null就不是病毒
	 */
	public String getVirusDesc(String md5){
		Cursor cursor = db.query("datable", new String[]{"desc"}, "md5 = ?", new String[]{md5}, null, null, null);
		if(cursor.moveToNext()){
			String desc = cursor.getString(0);
			cursor.close();
			return desc;
		}else{
			return null;
		}
	}
	/**
	 * 更新病毒库时插入
	 * @param desc 
	 * @param name 
	 * @param type 
	 * @param md5 
	 * @param newVersion 
	 * @return
	 */
	public boolean insertVirus(String md5, String type, String name, String desc, int newVersion){
		ContentValues values = new ContentValues();
		values.put("md5", md5);
		values.put("type", type);
		values.put("name", name);
		values.put("desc", desc);
		long i = db.insert("datable", null, values);
		values.clear();
		values.put("subcnt", newVersion+"");
		long j = db.update("version", values, null, null);
		return (i+j)==0?false:true;
	}
	/**
	 * 
	 * @return 数据库版本
	 */
	public int getVersion(){
		Cursor cursor = db.query("version", new String[]{"subcnt"}, null, null, null, null, null);
		if(cursor.moveToNext()){
			int version = cursor.getInt(0);
			cursor.close();
			return version;
		}else{
			return 0;
		}
	}
	
	public void closeDB(){
		db.close();
	}
}
