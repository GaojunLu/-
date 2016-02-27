package com.example.mobilesafe.activities;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInfo;
import com.example.mobilesafe.engine.AppInfoTool;
import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppManageActivity extends Activity implements OnClickListener {
	private TextView tv_phone_space;
	private TextView tv_sd_space;
	private ListView listview_activity_app;
	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfos = new ArrayList<AppInfo>();
	private List<AppInfo> sysAppInfos = new ArrayList<AppInfo>();
	private AppInfo appInfo;
	PopupWindow popupWindow ;
	MyAdapter adapter;
	MyReceiver receiver;
	
	Handler handler = new Handler(){

		public void handleMessage(android.os.Message msg) {
			adapter = new MyAdapter();
			listview_activity_app.setAdapter(adapter);
		};
	};
	private TextView tv_app_category;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(receiver, filter );
		
		setContentView(R.layout.activity_appmanage);
		tv_phone_space = (TextView) findViewById(R.id.tv_phone_space);
		tv_sd_space = (TextView) findViewById(R.id.tv_sd_space);
		listview_activity_app = (ListView) findViewById(R.id.listview_activity_app);
		tv_app_category = (TextView) findViewById(R.id.tv_app_category);
		long phonesize = Environment.getDataDirectory().getFreeSpace();
		long sdsize = Environment.getExternalStorageDirectory().getFreeSpace();
		tv_phone_space.setText("手机剩余:"
				+ Formatter.formatFileSize(this, phonesize));
		tv_sd_space.setText("SD卡剩余:" + Formatter.formatFileSize(this, sdsize));
		getData();
		listview_activity_app.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				destroyPopupWindow();
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(appInfos!=null){
					if(firstVisibleItem>=0&&firstVisibleItem<=userAppInfos.size()){
						tv_app_category.setText("用户程序("+userAppInfos.size()+")");
					}else if(firstVisibleItem>(userAppInfos.size()+1)){
						tv_app_category.setText("系统程序("+sysAppInfos.size()+")");
					}
				}
			}
		});
		listview_activity_app.setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				destroyPopupWindow();
				int location = 0;
				// 两个信息条目点击无效 
				if(position == 0){ 
					return ;
				}else if(position == (userAppInfos.size()+1)){
					return ;
				}else if (position>0&&position<=userAppInfos.size()) {//用户app
					location = position-1;
					appInfo = userAppInfos.get(location);
//					ToastUtiles.makeToast(AppManageActivity.this, appInfo.getName());
					showPopupWindow(parent, view);
				} else {//系统app
					location = position-2-userAppInfos.size();
					appInfo = sysAppInfos.get(location);
//					ToastUtiles.makeToast(AppManageActivity.this, appInfo.getName());
					showPopupWindow(parent, view);
				}
			}

		});
	}
/**
 * 气泡消失
 */

	protected void destroyPopupWindow() {
		// TODO Auto-generated method stub
		if(popupWindow!=null&&popupWindow.isShowing()){
			popupWindow.dismiss();
			popupWindow = null;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		destroyPopupWindow();
		unregisterReceiver(receiver);
		super.onDestroy();
	}


	/**
	 * 开启子线程，获取软件信息
	 */
	private void getData() {
		new Thread() {
			public void run() {
				appInfos = AppInfoTool.getAppInfos(AppManageActivity.this);
				for (AppInfo appInfo : appInfos) {
					if(appInfo.isSystemApp()){
						sysAppInfos.add(appInfo);
					}else{
						userAppInfos.add(appInfo);
					}
				}
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	private void showPopupWindow(AdapterView<?> parent, View view) {
		int[] viewLocation = new int[2];
		view.getLocationOnScreen(viewLocation);
		LinearLayout contentView = (LinearLayout) View.inflate(AppManageActivity.this, R.layout.popup_app_item, null);
		popupWindow = new PopupWindow(contentView, -2, -2);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.showAtLocation(parent, Gravity.TOP+Gravity.LEFT, 50, viewLocation[1]);
		//设置动画
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1);
		alphaAnimation.setDuration(300);
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(300);
		AnimationSet set = new AnimationSet(false);
		set.addAnimation(scaleAnimation);
		set.addAnimation(alphaAnimation);

		contentView.startAnimation(set);
		
		LinearLayout ll_uninstall = (LinearLayout) contentView.findViewById(R.id.ll_uninstall);
		LinearLayout ll_start = (LinearLayout) contentView.findViewById(R.id.ll_start);
		LinearLayout ll_share = (LinearLayout) contentView.findViewById(R.id.ll_share);
		LinearLayout ll_info = (LinearLayout) contentView.findViewById(R.id.ll_info);
		ll_uninstall.setOnClickListener(this);
		ll_info.setOnClickListener(this);
		ll_share.setOnClickListener(this);
		ll_start.setOnClickListener(this);
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return appInfos.size()+2;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(position == 0){
				TextView textView = new TextView(AppManageActivity.this);
				textView.setText("用户程序("+userAppInfos.size()+")");
				textView.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
				return textView;
			}else if(position == (userAppInfos.size()+1)){
				TextView textView = new TextView(AppManageActivity.this);
				textView.setText("系统程序("+sysAppInfos.size()+")");
				textView.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
				return textView;
			}
			View view;
			ViewHolder holder;
			if(convertView!=null && convertView instanceof RelativeLayout){
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}else{
				view = View.inflate(AppManageActivity.this, R.layout.item_app, null);
				holder = new ViewHolder();
				holder.iv_item_app = (ImageView) view.findViewById(R.id.iv_item_app);
				holder.tv_appname_item_app = (TextView) view.findViewById(R.id.tv_appname_item_app);
				holder.tv_applocation_item_app = (TextView) view.findViewById(R.id.tv_applocation_item_app);
				holder.tv_appsize_item_app = (TextView) view.findViewById(R.id.tv_appsize_item_app);
				view.setTag(holder);
			}
			int location = 0;
			if (position>0&&position<=userAppInfos.size()) {
				location = position-1;
				holder.iv_item_app.setImageDrawable(userAppInfos.get(location).getIcon());
				holder.tv_appname_item_app.setText(userAppInfos.get(location).getName());
				holder.tv_appsize_item_app.setText(Formatter.formatFileSize(AppManageActivity.this, userAppInfos.get(location).getSize()));
				if(userAppInfos.get(location).isInSysStorage()){
					holder.tv_applocation_item_app.setText("手机存储");
				}else{
					holder.tv_applocation_item_app.setText("SD卡存储");
				}
			} else {
				location = position-2-userAppInfos.size();
				holder.iv_item_app.setImageDrawable(sysAppInfos.get(location).getIcon());
				holder.tv_appname_item_app.setText(sysAppInfos.get(location).getName());
				holder.tv_appsize_item_app.setText(Formatter.formatFileSize(AppManageActivity.this, sysAppInfos.get(location).getSize()));
				if(sysAppInfos.get(location).isInSysStorage()){
					holder.tv_applocation_item_app.setText("手机存储");
				}else{
					holder.tv_applocation_item_app.setText("SD卡存储");
				}
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

		class ViewHolder{
			ImageView iv_item_app;
			TextView tv_appname_item_app;
			TextView tv_applocation_item_app;
			TextView tv_appsize_item_app;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_info:
			appDetails();
			break;
		case R.id.ll_share:
			shareApp();
			break;
		case R.id.ll_start:
			startApp();
			break;
		case R.id.ll_uninstall:
			uninstallApp();
			break;
		}
		destroyPopupWindow();
	}
	private void appDetails() {
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:"+appInfo.getPackageName()));
		startActivity(intent);
	}
	private void uninstallApp() {
		Intent intent3 = new Intent();
		intent3.setAction("android.intent.action.UNINSTALL_PACKAGE");
		intent3.addCategory("android.intent.category.DEFAULT");
		intent3.setData(Uri.parse("package:"+appInfo.getPackageName()));
		startActivity(intent3);
	}
	private void startApp() {
		PackageManager pm = getPackageManager();
		Intent intent2 = pm.getLaunchIntentForPackage(appInfo.getPackageName());
		if(intent2!=null){
			startActivity(intent2);
		}else{
			ToastUtiles.makeToast(this, "该程序不可启动");
		}
	}
	private void shareApp() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "分享软件："+appInfo.getName());
		startActivity(intent);
	}
	/**
	 * 接收卸载广播
	 * @author Administrator
	 *
	 */
	class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(appInfo.isSystemApp()){
				sysAppInfos.remove(appInfo);
			}else{
				userAppInfos.remove(appInfo);
			}
			adapter.notifyDataSetChanged();
			System.out.println("卸载"+appInfo.getName());
		}
		
	}
}
