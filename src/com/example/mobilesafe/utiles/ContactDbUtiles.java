package com.example.mobilesafe.utiles;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mobilesafe.bean.BlackInfo;
import com.example.mobilesafe.dao.ContactsDbHelper;

/**
 * 操作黑名单数据库，type：1电话；2短信；3都
 * @author Administrator
 *
 */
public class ContactDbUtiles {
	public static boolean insert(Context context, String phone, String type) {
		ContactsDbHelper dbHelper = new ContactsDbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("type", type);
		long l = db.insert("black", null, values );
		db.close();
		if(l>0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean deleteByPhone(Context context, String phone){
		ContactsDbHelper dbHelper = new ContactsDbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int i = db.delete("black", "phone = "+phone, null);
		db.close();
		if(i>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 根据电话改类型
	 * @param context
	 * @param phone
	 * @return
	 */
	public static boolean updateTypeByPhone(Context context, String phone, String type){
		ContactsDbHelper dbHelper = new ContactsDbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("type", type);
		int i = db.update("black", values, "phone = "+phone, null);
		db.close();
		if(i>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 查号码的拦截类型
	 * @param context
	 * @param phone
	 * @return
	 */
	public static String queryByPhone(Context context, String phone){
		String type = null;
		ContactsDbHelper dbHelper = new ContactsDbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("black", new String[]{"type"}, "phone = "+phone, null, null, null, null);
		while(cursor.moveToNext()){
			type = cursor.getString(0);
		}
		db.close();
		cursor.close();
		return type;
	}
	
	public static List<BlackInfo> getAllBlackInfo(Context context) {
		List<BlackInfo> blackInfos = new ArrayList<BlackInfo>();
		ContactsDbHelper dbHelper = new ContactsDbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("black", null, null, null, null, null, "_id desc");
		while(cursor.moveToNext()){
			BlackInfo blackInfo = BlackInfo.getInstanceByCursor(cursor);
			blackInfos.add(blackInfo);
			//延时测试
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		db.close();
		cursor.close();
		return blackInfos;
	}
	/**
	 * 分页查询
	 * @param context
	 * @param limit	查询数量
	 * @param offset 开始位置
	 * @return
	 */
	public static List<BlackInfo> getLimitBlackInfo(Context context, int limit, int offset) {
		List<BlackInfo> blackInfos = new ArrayList<BlackInfo>();
		ContactsDbHelper dbHelper = new ContactsDbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select phone,type from black order by _id desc limit ? offset ?", new String[]{limit+"", offset+""});
//		db.query("black", null, null, null, null, null, "_id desc");
		while(cursor.moveToNext()){
			BlackInfo blackInfo = BlackInfo.getInstanceByCursor(cursor);
			blackInfos.add(blackInfo);
			//延时测试
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		db.close();
		cursor.close();
		return blackInfos;
	}

	public static int getBlackInfoCount(Context context) {
		// TODO Auto-generated method stub
		ContactsDbHelper dbHelper = new ContactsDbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("black", new String[]{"count(*)"}, null, null, null, null, "_id desc");
		while(cursor.moveToNext()){
			return cursor.getInt(0);
		}
		db.close();
		cursor.close();
		return 0;
	}
}
