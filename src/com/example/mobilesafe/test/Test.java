package com.example.mobilesafe.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

import com.example.mobilesafe.bean.AppInfo;
import com.example.mobilesafe.dao.AppLockDao;
import com.example.mobilesafe.dao.CommonnumDao;
import com.example.mobilesafe.dao.ContactsDbHelper;
import com.example.mobilesafe.dao.PhoneAddressDao;
import com.example.mobilesafe.engine.AppInfoTool;
import com.example.mobilesafe.engine.SmsTools;
import com.example.mobilesafe.engine.TaskInfoTool;
import com.example.mobilesafe.utiles.ContactDbUtiles;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

public class Test extends AndroidTestCase {

	public void createdB(){
		getContext().getContentResolver();
		ContactsDbHelper dbHelper = new ContactsDbHelper(getContext());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
	}
	
	public void testInsert(){
		Context context = getContext();
		long num = 13555550000l;
		Random random = new Random();
		for(int i = 0; i < 200; i++){
			ContactDbUtiles.insert(context, String.valueOf(num+i), String.valueOf(random.nextInt(3)+1));
		}
	}
	
	public void testgetBlackInfoCount(){
		System.out.println(ContactDbUtiles.getBlackInfoCount(getContext()));
	}
	
	public void testAppInfo(){
		List<AppInfo> appInfos = AppInfoTool.getAppInfos(getContext());
		System.out.println("sss");
	}
	
	public void testguishudi(){
		String add = PhoneAddressDao.getAddressByPhone(getContext(), "18584913189");
		System.out.println(add);
	}
	public void testCommonnumDao(){
		CommonnumDao dao = new CommonnumDao(getContext());
		System.out.println(dao.getGroupCount());
//		System.out.println(dao.getChildCount(3));
//		System.out.println(dao.getGroupName(3));
//		System.out.println(dao.getChildNameAndNumber(3, 3)[0]);
		
	}
	
	public void testsmstool() throws Exception{
		SmsTools smsTools = new SmsTools(getContext());
		smsTools.saveSms("haha.xml", null);
	}
	
	public void test() {
		ContentValues values = new ContentValues();
		values.put("address", "123");
		values.put("date", "1111111111");
		values.put("type", "1");
		values.put("body", "kkk");
		getContext().getContentResolver().insert(Uri.parse("content://sms/"), values);
	}
	
	public void testTaskInfo() throws Exception{
		TaskInfoTool.getAllTask(getContext());
	}
	
	public void testAppLock(){
		AppLockDao dao = new AppLockDao(getContext());
		System.out.println(dao.queryApp("b"));
	}
	
}
