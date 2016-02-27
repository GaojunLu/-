package com.example.mobilesafe.activities;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInfo;
import com.example.mobilesafe.dao.AppLockDao;
import com.example.mobilesafe.engine.AppInfoTool;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AppLockActivity extends Activity {
	private ListView listview_applock_notlock;
	private TextView tv_count_applock;
	private MyAdapter unlockAdapter;
	private MyAdapter lockedAdapter;
	private List<AppInfo> appInfos;
	private List<AppInfo> unlockAppInfos = new ArrayList<AppInfo>();
	private List<AppInfo> lockedAppInfos = new ArrayList<AppInfo>();
	private boolean isUnlockPage = true;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			unlockAdapter = new MyAdapter();
			listview_applock_notlock.setAdapter(unlockAdapter);
			lockedAdapter = new MyAdapter();
			listview_applock_locked.setAdapter(lockedAdapter);
		};
	};
	private AppLockDao dao;
	private TextView bt_locked;
	private TextView bt_unlock;
	private ListView listview_applock_locked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applock);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		listview_applock_notlock = (ListView) findViewById(R.id.listview_applock_notlock);
		listview_applock_locked = (ListView) findViewById(R.id.listview_applock_locked);
		tv_count_applock = (TextView) findViewById(R.id.tv_count_applock);
		bt_locked = (TextView) findViewById(R.id.bt_locked);
		bt_unlock = (TextView) findViewById(R.id.bt_not_lock);
	}

	private void initData() {
		dao = new AppLockDao(this);
		new Thread() {
			public void run() {
				appInfos = AppInfoTool.getAppInfos(AppLockActivity.this);
				for (AppInfo appInfo : appInfos) {
					if (dao.queryApp(appInfo.getPackageName())) {
						lockedAppInfos.add(appInfo);
					} else {
						unlockAppInfos.add(appInfo);
					}
				}

				handler.sendEmptyMessage(0);
			};
		}.start();

	}

	private void initListener() {
		// TODO Auto-generated method stub
		bt_unlock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listview_applock_locked.setVisibility(View.GONE);
				listview_applock_notlock.setVisibility(View.VISIBLE);

				bt_unlock.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.tab_left_pressed));
				bt_locked.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.tab_right_default));

				isUnlockPage = true;
			}
		});
		bt_locked.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listview_applock_locked.setVisibility(View.VISIBLE);
				listview_applock_notlock.setVisibility(View.GONE);

				bt_unlock.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.tab_left_default));
				bt_locked.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.tab_right_pressed));

				isUnlockPage = false;
			}
		});
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (isUnlockPage) {
				tv_count_applock.setText("未加锁的应用：" + unlockAppInfos.size());
				return unlockAppInfos.size();
			} else {
				tv_count_applock.setText("加锁的应用：" + lockedAppInfos.size());
				return lockedAppInfos.size();
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final View view;
			ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(AppLockActivity.this,
						R.layout.item_applock, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.lock = (ImageView) view.findViewById(R.id.iv_lock);
				holder.name = (TextView) view.findViewById(R.id.tv_appname);
				view.setTag(holder);
			}
			final AppInfo appInfo;
			if (isUnlockPage) {
				appInfo = unlockAppInfos.get(position);
				holder.lock.setImageDrawable(getResources().getDrawable(
						R.drawable.list_button_lock_default));
			} else {
				appInfo = lockedAppInfos.get(position);
				holder.lock.setImageDrawable(getResources().getDrawable(
						R.drawable.list_button_unlock_default));
			}

			holder.icon.setImageDrawable(appInfo.getIcon());
			holder.name.setText(appInfo.getName());
			holder.lock.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (isUnlockPage) {
						TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
						ta.setDuration(300);
						view.startAnimation(ta);
						ta.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								// TODO Auto-generated method stub
								unlockAppInfos.remove(appInfo);
								dao.insertApp(appInfo.getPackageName());
								lockedAppInfos.add(appInfo);
								notifyDataSetChanged();
							}
						});
					}else{
						TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
						ta.setDuration(300);
						view.startAnimation(ta);
						ta.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								// TODO Auto-generated method stub
								lockedAppInfos.remove(appInfo);
								dao.deleteApp(appInfo.getPackageName());
								unlockAppInfos.add(appInfo);
								notifyDataSetChanged();
							}
						});
					}
				}
			});
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
			ImageView lock;
			TextView name;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dao.close();
	}

}
