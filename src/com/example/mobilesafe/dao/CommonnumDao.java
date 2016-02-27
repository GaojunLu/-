package com.example.mobilesafe.dao;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 下标都从0开始
 * @author Administrator
 *
 */
public class CommonnumDao {

	private SQLiteDatabase db;

	public CommonnumDao(Context context) {
		String path = new File(context.getFilesDir(), "commonnum.db").getAbsolutePath();
		db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
	}
	/**
	 * 关闭数据库
	 */
	public void closeDB(){
		db.close();
	}
	/**
	 * 分组数
	 * @return
	 */
	public int getGroupCount(){
		Cursor cursor = db.rawQuery("select count(*) from classlist", null);
		if(cursor.moveToFirst()){
			int i = cursor.getInt(0);
			cursor.close();
			return i;
		}
		return 0;
	}
	/**
	 * 某一分组下的子条目数
	 * @param groupPosotion
	 * @return
	 */
	public int getChildCount(int groupPosition){
		String table = "table"+(groupPosition+1);
		Cursor cursor = db.rawQuery("select count(*) from "+table, null);
		if(cursor.moveToFirst()){
			int i = cursor.getInt(0);
			cursor.close();
			return i;
		}
		return 0;
	}
	/**
	 * 某组名称
	 * @param groupPosotion
	 * @return
	 */
	public String getGroupName(int groupPosition){
		Cursor cursor = db.rawQuery("select name from classlist where idx = "+ (groupPosition+1), null);
		if(cursor.moveToFirst()){
			String s = cursor.getString(0);
			cursor.close();
			return s;
		}
		return null;
	}
	/**
	 * 某组、某子条目的名称[0]和号码[1]
	 * @param groupPosotion
	 * @param childPosotion
	 * @return
	 */
	public String[] getChildNameAndNumber(int groupPosotion,int childPosotion){
		String table = "table"+(groupPosotion+1);
		Cursor cursor = db.rawQuery("select name,number from "+table+" where _id = "+(childPosotion+1), null);
		if(cursor.moveToFirst()){
			String[] s = new String[2];
			s[0] = cursor.getString(0);
			s[1] = cursor.getString(1);
			return s;
		}
		return null;
	}
}
