package com.example.mobilesafe.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.example.mobilesafe.bean.Sms;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

public class SmsTools {

	private Context context;
	private int saveCount;
	
	public interface SmsCallBack{
		public void SmsCount(int count);
		public void SmsProgressCount(int count);
	}

	public SmsTools(Context context) {
		this.context = context;
	}
	/**
	 * 
	 * @param filename 存储的文件名
	 * @return
	 */
	public boolean saveSms(String filename, SmsCallBack callBack){
		try {
			File file = new File(Environment.getExternalStorageDirectory(), filename);
			FileOutputStream fos =  new FileOutputStream(file);
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setOutput(fos, "utf-8");
			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "allsms");
			Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/"), new String[]{"address","date","type","body"}, null, null, null);
			serializer.attribute(null, "count", cursor.getCount()+"");
			callBack.SmsCount(cursor.getCount());
			saveCount = 0;
			while(cursor.moveToNext()){
				serializer.startTag(null, "sms");
				
				String address = cursor.getString(0);
				String date = cursor.getString(1);
				String type = cursor.getString(2);
				String body = cursor.getString(3);
				
				serializer.startTag(null, "address");
				try {
					serializer.text(address);
				} catch (Exception e) {
					serializer.text("");
				}
				serializer.endTag(null, "address");
				
				serializer.startTag(null, "date");
				try {
					serializer.text(date);
				} catch (Exception e) {
					serializer.text("");
				}
				serializer.endTag(null, "date");
				
				serializer.startTag(null, "type");
				try {
					serializer.text(type);
				} catch (Exception e) {
					serializer.text("");
				}
				serializer.endTag(null, "type");
				
				serializer.startTag(null, "body");
				try {
					serializer.text(body);
				} catch (Exception e) {
					serializer.text("");
				}
				serializer.endTag(null, "body");
				serializer.endTag(null, "sms");
				
				saveCount++;
				callBack.SmsProgressCount(saveCount);
				
//				Thread.sleep(1000);
			}
			serializer.endTag(null, "allsms");
			serializer.endDocument();
			fos.close();
			cursor.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(saveCount+"+++++++++++++++++++++++++");
			return false;
		}
	}
	
	public boolean loadSms(String filename, SmsCallBack callBack){
		try {
			ContentResolver resolver = context.getContentResolver();
			ContentValues values = new ContentValues();
			
			File file = new File(Environment.getExternalStorageDirectory(), filename);
			FileInputStream fis =  new FileInputStream(file);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(fis, "utf-8");
			int enventtype = parser.getEventType();
			int loadCount = 0;
			Sms sms = new Sms();
			while(enventtype!=XmlPullParser.END_DOCUMENT){
				
				switch (parser.getEventType()) {
				case XmlPullParser.START_TAG:
					if("allsms".equals(parser.getName())){
						int count = Integer.parseInt(parser.getAttributeValue(0));
						callBack.SmsCount(count);
					}else if("sms".equals(parser.getName())){
						//
					}else if("address".equals(parser.getName())){
						sms.address = parser.nextText();
					}else if("date".equals(parser.getName())){
						sms.date = parser.nextText();
					}else if("type".equals(parser.getName())){
						sms.type = parser.nextText();
					}else if("body".equals(parser.getName())){
						sms.body = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					if("sms".equals(parser.getName())){
						//
						values.clear();
						values.put("address", sms.address);
						values.put("date", sms.date);
						values.put("type", sms.type);
						values.put("body", sms.body);
						resolver.insert(Uri.parse("content://sms/"), values);
						loadCount++;
						callBack.SmsProgressCount(loadCount);
					}
					break;
				}
				
				
				enventtype = parser.next();
				
//				Thread.sleep(50);
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	
}
