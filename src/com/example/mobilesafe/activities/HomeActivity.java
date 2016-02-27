package com.example.mobilesafe.activities;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SpotManager;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utiles.IntentUtiles;
import com.example.mobilesafe.utiles.MD5Utiles;
import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends Activity {
	private GridView gv_activity_home;
	private static final String[] functionNames = { "�ֻ�����", "ͨѶ��ʿ", "�������",
			"���̹���", "����ͳ��", "�ֻ�ɱ��", "��������", "�߼�����", "��������" };
	private static final int[] icons = { R.drawable.safe,
			R.drawable.callmsgsafe, R.drawable.app, R.drawable.taskmanager,
			R.drawable.netmanager, R.drawable.trojan, R.drawable.sysoptimize,
			R.drawable.atools, R.drawable.settings };
	private SharedPreferences sp;
	private AlertDialog.Builder builder;
	private EditText et_edittext_dialog_password;
	private EditText et_edittext_dialog_password_confirm;
	private Button bt_cancel_edittext_dialog;
	private Button bt_confirm_edittext_dialog;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		gv_activity_home = (GridView) findViewById(R.id.gv_activity_home);
		gv_activity_home.setAdapter(new MyAdapter());
		gv_activity_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0://�ֻ���ʿ
					String password = sp.getString("password", null);
					if (password == null) {
						// ���������öԻ���
						showSetupPassword();
					} else {
						// ����������Ի���
						showInputPassword();
					}
					break;
				case 1://ͨѶ��ʿ
					IntentUtiles.startActivityNotFinish(HomeActivity.this, ContactActivity.class);
					break;
				case 2:
					IntentUtiles.startActivityNotFinish(HomeActivity.this, AppManageActivity.class);
					break;
				case 3:
					IntentUtiles.startActivityNotFinish(HomeActivity.this, TaskManageActivity.class);
					break;
				case 4:
					IntentUtiles.startActivityNotFinish(HomeActivity.this, TrafficActivity.class);
					break;
				case 5:
					IntentUtiles.startActivityNotFinish(HomeActivity.this, TrojanActivity.class);
					break;
				case 6:
					IntentUtiles.startActivityNotFinish(HomeActivity.this, ClearCacheActivity.class);
					break;
				case 7://�߼�����
					IntentUtiles.startActivityNotFinish(HomeActivity.this, AToolsActivity.class);
					break;
				case 8://����
					Intent intent = new Intent(HomeActivity.this,
							SettingActivity.class);
					startActivity(intent);
					break;
				}
			}
		});
		//���
		// ʵ���������
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// ��ȡҪǶ�������Ĳ���
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
		// ����������뵽������
		adLayout.addView(adView);
		SpotManager.getInstance(this).showSpotAds(this);
	}

	protected void showInputPassword() {
		// TODO Auto-generated method stub
		builder = new AlertDialog.Builder(this);
		View view = View.inflate(this, R.layout.edittextdialog, null);
		((TextView) view.findViewById(R.id.tv_title_edittext_dialog))
				.setText("����������");
		et_edittext_dialog_password = (EditText) view
				.findViewById(R.id.et_edittext_dialog_password);
		et_edittext_dialog_password_confirm = (EditText) view
				.findViewById(R.id.et_edittext_dialog_password_confirm);
		bt_cancel_edittext_dialog = (Button) view
				.findViewById(R.id.bt_cancel_edittext_dialog);
		bt_confirm_edittext_dialog = (Button) view
				.findViewById(R.id.bt_confirm_edittext_dialog);
		et_edittext_dialog_password_confirm.setVisibility(View.GONE);
		bt_cancel_edittext_dialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		bt_confirm_edittext_dialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password = et_edittext_dialog_password.getText()
						.toString().trim();
				if (sp.getString("password", null).equals(
						MD5Utiles.encode(password))) {
					// ��ת������
					if (!sp.getBoolean("initSetup", false)) {// û�н��й���ʼ����
						// ����������
						IntentUtiles.startActivityNotFinish(HomeActivity.this,
								Setup1Activity.class);
						System.out.println("������");
					} else {
						// �������
						IntentUtiles.startActivityNotFinish(HomeActivity.this,
								SafeActivity.class);
						System.out.println("�������");
					}
				} else {
					ToastUtiles.makeToast(HomeActivity.this, "�������");
				}
				dialog.dismiss();
			}

		});
		builder.setView(view);
		dialog = builder.create();
		dialog.show();
	}

	protected void showSetupPassword() {
		builder = new AlertDialog.Builder(this);
		View view = View.inflate(this, R.layout.edittextdialog, null);
		((TextView) view.findViewById(R.id.tv_title_edittext_dialog))
				.setText("����������");
		et_edittext_dialog_password = (EditText) view
				.findViewById(R.id.et_edittext_dialog_password);
		et_edittext_dialog_password_confirm = (EditText) view
				.findViewById(R.id.et_edittext_dialog_password_confirm);
		bt_cancel_edittext_dialog = (Button) view
				.findViewById(R.id.bt_cancel_edittext_dialog);
		bt_confirm_edittext_dialog = (Button) view
				.findViewById(R.id.bt_confirm_edittext_dialog);
		bt_cancel_edittext_dialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		bt_confirm_edittext_dialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password = et_edittext_dialog_password.getText()
						.toString().trim();
				String password_confirm = et_edittext_dialog_password_confirm
						.getText().toString().trim();
				if (TextUtils.isEmpty(password)) {
					ToastUtiles.makeToast(HomeActivity.this, "���벻��Ϊ��");
				}
				if (password.equals(password_confirm)) {
					Editor editor = sp.edit();
					editor.putString("password", MD5Utiles.encode(password));
					editor.commit();
					dialog.dismiss();
					// ��ת������
					if (!sp.getBoolean("initSetup", false)) {// û�н��й���ʼ����
						// ����������
						IntentUtiles.startActivityNotFinish(HomeActivity.this,
								Setup1Activity.class);
					} else {
						// �������
						IntentUtiles.startActivityNotFinish(HomeActivity.this,
								SafeActivity.class);
					}
				} else {
					ToastUtiles.makeToast(HomeActivity.this, "�����������벻��ͬ������������");
				}
			}
		});
		builder.setView(view);
		dialog = builder.create();
		dialog.show();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return functionNames.length;
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
			View view = View.inflate(HomeActivity.this, R.layout.item_gridview,
					null);
			((ImageView) view.findViewById(R.id.iv_item_gridview_home))
					.setImageResource(icons[position]);
			((TextView) view.findViewById(R.id.tv_item_gridview_home))
					.setText(functionNames[position]);
			return view;
		}

	}
}
