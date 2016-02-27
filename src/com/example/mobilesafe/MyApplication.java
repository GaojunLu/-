package com.example.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import android.app.Application;
import android.os.Build;

public class MyApplication extends Application {
	private StringBuilder sb;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Field[] fields = Build.class.getDeclaredFields();
		sb = new StringBuilder();
		for (Field field : fields) {
			try {
				String value = field.get(null).toString();
				String name = field.getName();
				sb.append(name+":"+value+"\n");
			} catch (Exception e) {
				// TODO Auto-generated catch block 
				e.printStackTrace();
			}
		}
		UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				File file = new File(getFilesDir(), "log.txt");
				try {
					StringWriter s = new StringWriter();
					PrintWriter err = new PrintWriter(s);
					ex.printStackTrace(err);
					FileOutputStream fos = new FileOutputStream(file);
					sb.append(s.toString());
					fos.write(sb.toString().getBytes());
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//вти╠╫ЬЁл
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		};
		Thread.currentThread().setUncaughtExceptionHandler(handler);
	}
}
