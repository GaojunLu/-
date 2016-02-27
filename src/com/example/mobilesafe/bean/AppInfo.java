package com.example.mobilesafe.bean;

import android.graphics.drawable.Drawable;

public class AppInfo {
	private Drawable icon;
	private String name, packageName;
	private boolean isSystemApp, isInSysStorage;
	private long size;
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public boolean isSystemApp() {
		return isSystemApp;
	}
	public void setSystemApp(boolean isInSystem) {
		this.isSystemApp = isInSystem;
	}
	public boolean isInSysStorage() {
		return isInSysStorage;
	}
	public void setInSysStorage(boolean isInSysStorage) {
		this.isInSysStorage = isInSysStorage;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
}
