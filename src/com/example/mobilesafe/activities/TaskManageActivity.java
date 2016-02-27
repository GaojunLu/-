package com.example.mobilesafe.activities;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.TaskInfo;
import com.example.mobilesafe.engine.TaskInfoTool;
import com.example.mobilesafe.utiles.IntentUtiles;
import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskManageActivity extends Activity {
	private TextView tv_task_count;
	private TextView tv_ram_space;
	private TextView tv_task_category;
	private ListView listview_activity_task;
	private LinearLayout ll_pb;
	private List<TaskInfo> taskInfos;
	private List<TaskInfo> userTaskInfos = new ArrayList<TaskInfo>();
	private List<TaskInfo> sysTaskInfos = new ArrayList<TaskInfo>();
	private MyAdapter adapter;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ll_pb.setVisibility(View.INVISIBLE);
			adapter = new MyAdapter();
			listview_activity_task.setAdapter(adapter);
		};
	};
	private int taskCount;
	private long totleRam;
	private long freeRam;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskmanage);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		tv_task_count = (TextView) findViewById(R.id.tv_task_count);
		tv_ram_space = (TextView) findViewById(R.id.tv_ram_space);
		tv_task_category = (TextView) findViewById(R.id.tv_task_category);
		listview_activity_task = (ListView) findViewById(R.id.listview_activity_task);
		ll_pb = (LinearLayout) findViewById(R.id.ll_pb);
	}

	private void initData() {
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		taskCount = TaskInfoTool.getTaskCount(this);
		totleRam = TaskInfoTool.getTotleRam(this);
		freeRam = TaskInfoTool.getFreeRam(this);
		tv_task_count.setText("当前进程:" + taskCount);
		tv_ram_space.setText("可用内存:" + Formatter.formatFileSize(this, freeRam)
				+ "/" + Formatter.formatFileSize(this, totleRam));
		getData();
	}

	/**
	 * 开起线程，获取进程信息
	 */
	private void getData() {
		// TODO Auto-generated method stub
		new Thread() {

			public void run() {
				taskInfos = TaskInfoTool.getAllTask(TaskManageActivity.this);
				for (TaskInfo taskInfo : taskInfos) {
					if (taskInfo.isUserTask()) {
						userTaskInfos.add(taskInfo);
					} else {
						sysTaskInfos.add(taskInfo);
					}
				}
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		listview_activity_task.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (userTaskInfos.size() > 0 && sysTaskInfos.size() > 0) {
					if (firstVisibleItem <= userTaskInfos.size()) {
						tv_task_category.setText("用户进程：" + userTaskInfos.size());
					} else {
						tv_task_category.setText("系统进程：" + sysTaskInfos.size());
					}
				}
			}
		});
		listview_activity_task
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						int location;
						TaskInfo taskInfo;
						if (position == 0
								|| position == userTaskInfos.size() + 1) {
							return;
						} else if (position < userTaskInfos.size() + 1) {
							location = position - 1;
							taskInfo = userTaskInfos.get(location);
						} else {
							location = position - 2 - userTaskInfos.size();
							taskInfo = sysTaskInfos.get(location);
							// 过滤自己
							if (taskInfo.getPackageName().equals(
									getPackageName())) {
								return;
							}
						}
						if (taskInfo.isCheck()) {
							taskInfo.setCheck(false);
						} else {
							taskInfo.setCheck(true);
						}
						adapter.notifyDataSetChanged();
					}
				});
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (sp.getBoolean("showSysTask", false)) {
				return userTaskInfos.size() + sysTaskInfos.size() + 2;
			} else {
				return userTaskInfos.size() + 1;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			/**
			 * 用来填充的bean
			 */
			TaskInfo taskInfo;
			if (position == 0) {
				TextView tv = new TextView(TaskManageActivity.this);
				tv.setText("用户进程：" + userTaskInfos.size());
				tv.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
				return tv;
			} else if (position == userTaskInfos.size() + 1) {
				TextView tv = new TextView(TaskManageActivity.this);
				tv.setText("系统进程：" + sysTaskInfos.size());
				tv.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
				return tv;
			} else if (position < userTaskInfos.size() + 1) {
				int location = position - 1;
				taskInfo = userTaskInfos.get(location);
			} else {
				int location = position - 2 - userTaskInfos.size();
				taskInfo = sysTaskInfos.get(location);
			}
			View view;
			ViewHolder holder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(TaskManageActivity.this,
						R.layout.item_task, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) view.findViewById(R.id.iv_item_task);
				holder.taskName = (TextView) view
						.findViewById(R.id.tv_taskname_item_task);
				holder.taskSize = (TextView) view
						.findViewById(R.id.tv_tasksize_item_task);
				holder.cb = (CheckBox) view.findViewById(R.id.cb_item_task);
				view.setTag(holder);
			}
			holder.icon.setImageDrawable(taskInfo.getIcon());
			holder.taskName.setText(taskInfo.getTaskName());
			holder.taskSize.setText(Formatter.formatFileSize(
					TaskManageActivity.this, taskInfo.getMemsize()));
			if (taskInfo.getPackageName().equals(getPackageName())) {// 过滤自己
				holder.cb.setVisibility(View.INVISIBLE);
			} else {
				holder.cb.setVisibility(View.VISIBLE);
			}
			if (taskInfo.isCheck()) {
				holder.cb.setChecked(true);
			} else {
				holder.cb.setChecked(false);
			}
			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		class ViewHolder {
			ImageView icon;
			TextView taskName;
			TextView taskSize;
			CheckBox cb;
		}
	}

	/**
	 * 全选按钮
	 * 
	 * @param view
	 */
	public void selectAll(View view) {
		for (TaskInfo taskInfo : userTaskInfos) {
			// 过滤自己
			if (taskInfo.getPackageName().equals(getPackageName())) {
				continue;
			}
			taskInfo.setCheck(true);
		}
		for (TaskInfo taskInfo : sysTaskInfos) {
			taskInfo.setCheck(true);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 反选按钮
	 * 
	 * @param view
	 */
	public void selectInvert(View view) {
		for (TaskInfo taskInfo : userTaskInfos) {
			// 过滤自己
			if (taskInfo.getPackageName().equals(getPackageName())) {
				continue;
			}
			taskInfo.setCheck(!taskInfo.isCheck());
		}
		for (TaskInfo taskInfo : sysTaskInfos) {
			taskInfo.setCheck(!taskInfo.isCheck());
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 清理按钮
	 * 
	 * @param view
	 */
	public void kill(View view) {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<TaskInfo> remove = new ArrayList<TaskInfo>();
		long totle = 0;
		int kill = 0;
		for (TaskInfo taskInfo : userTaskInfos) {
			if (taskInfo.isCheck()) {
				am.killBackgroundProcesses(taskInfo.getPackageName());
				remove.add(taskInfo);
				totle += taskInfo.getMemsize();
				kill++;
			}
		}
		for (TaskInfo taskInfo : sysTaskInfos) {
			if (taskInfo.isCheck()) {
				am.killBackgroundProcesses(taskInfo.getPackageName());
				remove.add(taskInfo);
				totle += taskInfo.getMemsize();
				kill++;
			}
		}
		for (TaskInfo taskInfo : remove) {
			if (taskInfo.isUserTask()) {
				userTaskInfos.remove(taskInfo);
			} else {
				sysTaskInfos.remove(taskInfo);
			}
		}
		adapter.notifyDataSetChanged();
		ToastUtiles.makeToast(this,
				"一共释放了" + Formatter.formatFileSize(this, totle) + "内存");
		tv_task_count.setText("当前进程:" + (taskCount - kill));
		tv_ram_space.setText("可用内存:"
				+ Formatter.formatFileSize(this, freeRam + totle) + "/"
				+ Formatter.formatFileSize(this, totleRam));
	}

	public void setting(View view){
		IntentUtiles.startActivityNotFinish(this, TaskManagerSettingActivity.class);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}
}
