package com.example.mobilesafe.bean;

import android.graphics.drawable.Drawable;

public class TaskInfo {

	private Drawable icon;
	private String taskName, packageName;
	private boolean isUserTask;
	private boolean check = false;
	private long memsize;
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public boolean isUserTask() {
		return isUserTask;
	}
	public void setUserTask(boolean isUserTask) {
		this.isUserTask = isUserTask;
	}
	public long getMemsize() {
		return memsize;
	}
	public void setMemsize(long memsize) {
		this.memsize = memsize;
	}
	public boolean isCheck() {
		return check;
	}
	public void setCheck(boolean check) {
		this.check = check;
	}
	
}
