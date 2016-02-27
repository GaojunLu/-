package com.example.mobilesafe.activities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.Activity;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ClearCacheActivity extends Activity {
	private List<CacheInfo> cacheInfos = new ArrayList<CacheInfo>();
	private FrameLayout fl_pb;
	private ProgressBar pb;
	private TextView tv_pb;
	private ListView listView;
	final static int SCANNING = 0;
	protected static final int SCAN_FINISH = 1;
	protected static final int REFRESH = 2;
	private int progress;
	PackageManager pm;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANNING:
				tv_pb.setText((String) msg.obj);
				progress++;
				pb.setProgress(progress);
				break;
			case SCAN_FINISH:
				fl_pb.setVisibility(View.INVISIBLE);
				listView.setVisibility(View.VISIBLE);
				listView.setAdapter(new MyAdapter());
				if(cacheInfos.size()==0){
					ToastUtiles.makeToast(ClearCacheActivity.this, "没有缓存文件");
				}
				break;
			case REFRESH:
				fl_pb.setVisibility(View.VISIBLE);
				listView.setVisibility(View.INVISIBLE);
				getCacheInfos();
				break;
			}
		};
	};
	private Button bt_clearall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clearcache);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		fl_pb = (FrameLayout) findViewById(R.id.fl_pb);
		pb = (ProgressBar) findViewById(R.id.pb);
		tv_pb = (TextView) findViewById(R.id.tv_pb);
		listView = (ListView) findViewById(R.id.listview);
		bt_clearall = (Button) findViewById(R.id.bt_clearall);
	}

	private void initData() {
		// TODO Auto-generated method stub
		pm = getPackageManager();
		getCacheInfos();
	}

	private void getCacheInfos() {
		cacheInfos.clear();
		fl_pb.setVisibility(View.VISIBLE);
		listView.setVisibility(View.INVISIBLE);
		new Thread() {
			public void run() {
				try {
					Method method = PackageManager.class.getDeclaredMethod(
							"getPackageSizeInfo", String.class,
							IPackageStatsObserver.class);
					List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
					pb.setMax(packageInfos.size());
					progress = 0;
					for (PackageInfo packageInfo : packageInfos) {
						final CacheInfo cacheInfo = new CacheInfo();
						final String packageName = packageInfo.packageName;
						method.invoke(pm, packageName,
								new IPackageStatsObserver.Stub() {

									@Override
									public void onGetStatsCompleted(
											PackageStats pStats,
											boolean succeeded)
											throws RemoteException {
										// TODO Auto-generated method stub
										cacheInfo.cacheSize = pStats.cacheSize;
										cacheInfo.packageName = packageName;
										try {
											cacheInfo.appName = (String) pm
													.getApplicationInfo(
															packageName, 0)
													.loadLabel(pm);
											cacheInfo.icon = pm
													.getApplicationInfo(
															packageName, 0)
													.loadIcon(pm);
											Message message = handler
													.obtainMessage(SCANNING);
											message.obj = cacheInfo.appName;
											handler.sendMessage(message);
											if (cacheInfo.cacheSize > 0) {
												cacheInfos.add(cacheInfo);
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});
						Thread.sleep(5);
					}
					handler.sendEmptyMessage(SCAN_FINISH);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		bt_clearall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearAllCache();
			}
		});
	}

	protected void clearAllCache() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				try {
					Method method = PackageManager.class.getDeclaredMethod("freeStorageAndNotify", long.class,
							IPackageDataObserver.class); 
					method.invoke(pm, Integer.MAX_VALUE, new IPackageDataObserver.Stub(){

						@Override
						public void onRemoveCompleted(String packageName,
								boolean succeeded) throws RemoteException {
							// TODO Auto-generated method stub
							handler.sendEmptyMessage(REFRESH);
						}
						
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	class CacheInfo {
		Drawable icon;
		String packageName, appName;
		long cacheSize;
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cacheInfos.size();
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(ClearCacheActivity.this,
						R.layout.item_cache, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.clear = (ImageView) view.findViewById(R.id.iv_clear);
				holder.appname = (TextView) view.findViewById(R.id.tv_appname);
				holder.cachesize = (TextView) view
						.findViewById(R.id.tv_cachesize);
				view.setTag(holder);
			}
			holder.icon.setBackgroundDrawable(cacheInfos.get(position).icon);
			holder.clear
					.setBackgroundResource(R.drawable.list_button_clean_default);
			holder.appname.setText(cacheInfos.get(position).appName);
			holder.cachesize
					.setText(Formatter.formatFileSize(ClearCacheActivity.this,
							cacheInfos.get(position).cacheSize));
			return view;
		}

		class ViewHolder {
			ImageView icon;
			TextView appname;
			TextView cachesize;
			ImageView clear;
		}

	}
}
