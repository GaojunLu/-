package com.example.mobilesafe.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utiles.InputStringUtiles;
import com.example.mobilesafe.utiles.IntentUtiles;
import com.example.mobilesafe.utiles.ToastUtiles;
import com.example.mobilesafe.utiles.VersionUtiles;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources.NotFoundException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;

public class SplashActivity extends Activity {
	protected static final int UPDATE = 0;
	private TextView tv_activity_splash_version;
	private TextView tv_activity_splash_update;
	private RelativeLayout rl_activity_splash;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE:
				showUpdateDialog((UpdateInfo) msg.obj);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_activity_splash_version = (TextView) findViewById(R.id.tv_activity_splash_version);
		tv_activity_splash_update = (TextView) findViewById(R.id.tv_activity_splash_update);
		rl_activity_splash = (RelativeLayout) findViewById(R.id.rl_activity_splash);
		tv_activity_splash_version.setText("版本："
				+ VersionUtiles.getVersionName(this));
		AlphaAnimation animation = new AlphaAnimation(0.2f, 1);
		animation.setDuration(1000);
		rl_activity_splash.startAnimation(animation);
		// 复制数据库到files文件夹
		copyDataBase("address.db");
		copyDataBase("commonnum.db");
		copyDataBase("antivirus.db");

		if (getSharedPreferences("setting", MODE_PRIVATE).getBoolean("update",
				false)) {
			checkUpdate();
		} else {
			IntentUtiles.startActivityForDelay(this, HomeActivity.class, 1000);
		}

		createIcon();
		startNotify();
		//广告
		AdManager.getInstance(this).init("5e4597e736e5f959", "d83b9e3cae676ef5", true);
		SpotManager.getInstance(this).loadSpotAds();
	}
/**
 * 常驻状态栏的通知信息
 */
	private void startNotify() {
		// TODO Auto-generated method stub
		
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher, "tiker手机卫士", System.currentTimeMillis());
		notification.flags = Notification.FLAG_NO_CLEAR;
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, HomeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(this, "标题", "内容", contentIntent);
		nm.notify(0, notification );
		
	}

	/**
	 * 创建桌面图标
	 */
	private void createIcon() {
		// TODO Auto-generated method stub
		if(!getSharedPreferences("setting", MODE_PRIVATE).getBoolean("haveShortcut", false)){
		Intent intent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "快捷方式");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
				BitmapFactory.decodeResource(getResources(), R.drawable.app));
		Intent startIntent = new Intent();
		startIntent.setAction("a.b.c");
		startIntent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, startIntent);
		sendBroadcast(intent);
		System.out.println("执行完毕");
		getSharedPreferences("setting", MODE_PRIVATE).edit().putBoolean("haveShortcut", true).commit();
		}
	}

	/**
	 * 开启线程复制归属地数据库到安装目录
	 */
	private void copyDataBase(final String dbName) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				File file = new File(getFilesDir(), dbName);
				if (file.exists()) {
					System.out.println("数据库" + dbName + "已经存在");
					return;
				}
				AssetManager assetManager = getAssets();
				try {
					InputStream is = assetManager.open(dbName);
					FileOutputStream fos = new FileOutputStream(file);
					int len = 0;
					byte[] b = new byte[1024];
					while ((len = is.read(b)) != -1) {
						fos.write(b, 0, len);
					}
					is.close();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

	protected void showUpdateDialog(final UpdateInfo updateInfo) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("升级提示").setMessage(updateInfo.desc);
		builder.setPositiveButton("马上升级", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				HttpUtils httpUtils = new HttpUtils();
				final File file = new File(Environment
						.getExternalStorageDirectory(), "xx.apk");
				// System.out.println(file.getAbsolutePath());
				httpUtils.download(updateInfo.url, file.getAbsolutePath(),
						false, new RequestCallBack<File>() {

							@Override
							public void onSuccess(ResponseInfo<File> arg0) {
								// TODO Auto-generated method stub
								ToastUtiles.makeToast(SplashActivity.this,
										"下载成功");
								// 安装
								Intent intent = new Intent();
								intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
								intent.addCategory(Intent.CATEGORY_DEFAULT);
								intent.setDataAndType(Uri.fromFile(file),
										"application/vnd.android.package-archive");
								startActivity(intent);
							}

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								// TODO Auto-generated method stub
								ToastUtiles.makeToast(SplashActivity.this,
										"下载成功");
							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								// TODO Auto-generated method stub
								super.onLoading(total, current, isUploading);
								tv_activity_splash_update.setText("下载进度："
										+ current + "/" + total);
							}
						});
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				IntentUtiles.startActivity(SplashActivity.this,
						HomeActivity.class);
			}
		});
		builder.show();
	}

	private void checkUpdate() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				try {
					URL url = new URL(getString(R.string.updatejson));
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(1000);
					conn.setReadTimeout(5000);
					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						InputStream is = conn.getInputStream();
						String s = InputStringUtiles.getStringByInputStream(is);
						JSONObject jsonObject = new JSONObject(s);
						UpdateInfo updateInfo = new UpdateInfo();
						updateInfo.version = jsonObject.getInt("version");
						updateInfo.url = jsonObject.getString("url");
						updateInfo.desc = jsonObject.getString("desc");
						if (VersionUtiles.getVersionCode(SplashActivity.this) < updateInfo.version) {// 如果版本低于服务器版本，就弹出对话框询问更新
							Message msg = handler.obtainMessage();
							msg.what = UPDATE;
							msg.obj = updateInfo;
							// handler.sendMessage(msg);
							handler.sendMessageDelayed(msg, 1000);
						} else {// 版本相同，就跳转到主界面
							IntentUtiles.startActivityForDelay(
									SplashActivity.this, HomeActivity.class,
									2000);
						}
					} else {
						ToastUtiles.makeToast(SplashActivity.this, "服务器问题");
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ToastUtiles.makeToast(SplashActivity.this, "服务器问题");
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SocketTimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ToastUtiles.makeToast(SplashActivity.this, "服务器问题");
					IntentUtiles.startActivity(SplashActivity.this,
							HomeActivity.class);// 跳转
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ToastUtiles.makeToast(SplashActivity.this, "json格式问题");
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

	class UpdateInfo {
		int version;
		String url;
		String desc;

		@Override
		public String toString() {
			return "UpdateInfo [version=" + version + ", url=" + url
					+ ", desc=" + desc + "]";
		}

	}
}
