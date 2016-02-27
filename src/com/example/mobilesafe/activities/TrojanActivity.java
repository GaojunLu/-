package com.example.mobilesafe.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.List;

import org.json.JSONObject;

import com.example.mobilesafe.R;
import com.example.mobilesafe.dao.AntivirusDao;
import com.example.mobilesafe.utiles.InputStringUtiles;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TrojanActivity extends Activity {
	private ImageView iv_scanning;
	PackageManager pm;
	AntivirusDao dao;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				TextView tv = new TextView(TrojanActivity.this);
				tv.setText((CharSequence) msg.obj);
				if (msg.arg1 == 1) {
					tv.setTextColor(Color.RED);
				} else {
					tv.setTextColor(Color.GREEN);
				}
				ll_result.addView(tv, 0);
			} else {
				tv_scan.setText("扫描结束");
				iv_scanning.clearAnimation();
			}
		};
	};
	private LinearLayout ll_result;
	private ProgressBar pb;
	private TextView tv_scan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trojan);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		iv_scanning = (ImageView) findViewById(R.id.iv_scanning);
		ll_result = (LinearLayout) findViewById(R.id.ll_result);
		pb = (ProgressBar) findViewById(R.id.pb);
		tv_scan = (TextView) findViewById(R.id.tv_scan);
	}

	private void initData() {
		// TODO Auto-generated method stub
		pm = getPackageManager();
		dao = new AntivirusDao(this);
		setRotation();
		updateDB();
		scanPackage();
	}

	/**
	 * 升级数据库
	 */
	private void updateDB() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				try {
					URL url = new URL(getString(R.string.updateDB));
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					if(conn.getResponseCode()==200){
						InputStream is = conn.getInputStream();
						JSONObject jsonObject = new JSONObject(InputStringUtiles.getStringByInputStream(is));
						int newVersion = jsonObject.getInt("version");
						System.out.println(newVersion+":"+dao.getVersion());
						if(newVersion>dao.getVersion()){
							String md5 = jsonObject.getString("md5");
							String name = jsonObject.getString("name");
							String type = jsonObject.getString("type");
							String desc = jsonObject.getString("desc");
							dao.insertVirus(md5, type,name, desc, newVersion);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			};
		}.start();
	}

	/**
	 * 扫描
	 */
	private void scanPackage() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				List<PackageInfo> packageInfos = pm
						.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
								+ PackageManager.GET_SIGNATURES);
				pb.setMax(packageInfos.size());
				int progress = 0;
				for (PackageInfo packageInfo : packageInfos) {
					String appName = (String) packageInfo.applicationInfo
							.loadLabel(pm);
					String desc = isVirus(packageInfo);
					boolean flag = desc == null ? false : true;
					Message msg = handler.obtainMessage();
					msg.obj = flag ? (appName + "\t" + desc)
							: (appName + "\t" + "扫描安全");
					msg.what = 0;
					msg.arg1 = flag ? 1 : 0;// 1是病毒，0不是
					handler.sendMessage(msg);
					progress++;
					pb.setProgress(progress);
//					try {
//						sleep(5);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
				handler.sendEmptyMessage(1);
			};
		}.start();
	}

	/**
	 * 
	 * @param packageInfo
	 * @return 病毒描述信息
	 */
	protected String isVirus(PackageInfo packageInfo) {
		// TODO Auto-generated method stub
		String path = packageInfo.applicationInfo.sourceDir;
//		Signature[] signatures =  packageInfo.signatures;
//		System.out.println(signatures.length+"长度");
//		for (Signature signature : signatures) {
//			System.out.println("===="+signature.toCharsString());
//		}
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(new File(path));
			int len = 0;
			byte[] b = new byte[1024];
			while ((len = fis.read(b)) != -1) {
				digest.update(b, 0, len);
			}
			
//			digest.digest(packageInfo.signatures[0].toByteArray());//加签名
			byte[] result = digest.digest();
			StringBuilder builder = new StringBuilder();
			for (byte rs : result) {
				String s = Integer.toHexString(rs & 0xff);
				if (s.length() == 1) {
					builder.append("0");
				}
				builder.append(s); 
			}
			 System.out.println(packageInfo.packageName+"***"+builder.toString());
			return dao.getVirusDesc(builder.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 旋转动画
	 */
	private void setRotation() {
		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(2000);
		ra.setRepeatMode(Animation.RESTART);
		ra.setRepeatCount(Animation.INFINITE);
		// ra.setInterpolator(AnimationUtils.loadInterpolator(this,R.anim.interpolator));
		ra.setInterpolator(new Interpolator() {

			@Override
			public float getInterpolation(float input) {
				// TODO Auto-generated method stub
				return input;
			}
		});
		iv_scanning.startAnimation(ra);
	}

	private void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dao.closeDB();
	}
}
