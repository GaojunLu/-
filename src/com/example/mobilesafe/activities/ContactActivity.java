package com.example.mobilesafe.activities;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.BlackInfo;
import com.example.mobilesafe.utiles.ContactDbUtiles;
import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ContactActivity extends Activity {
	private Button bt_addbalck_activity_contact;
	private ListView listview_activity_contact;
	private MyAdapter adapter;
	private static final int LOADING = 0;
	private static final int LOAD_COMPLET = 1;
	List<BlackInfo> blackInfos = new ArrayList<BlackInfo>();
	private int blackInfoCount;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			adapter.notifyDataSetChanged();
			switch (msg.what) {
			case LOAD_COMPLET:
				pb_activity_contact.setVisibility(View.INVISIBLE);
				break;
			case LOADING:
				pb_activity_contact.setVisibility(View.VISIBLE);
				break;
			}
		};
	};
	private LinearLayout pb_activity_contact;
	private int limit = 20;
	private int offset = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		bt_addbalck_activity_contact = (Button) findViewById(R.id.bt_addbalck_activity_contact);
		listview_activity_contact = (ListView) findViewById(R.id.listview_activity_contact);
		pb_activity_contact = (LinearLayout) findViewById(R.id.pb_activity_contact);
		blackInfoCount = ContactDbUtiles.getBlackInfoCount(this);
		adapter = new MyAdapter();
		listview_activity_contact.setAdapter(adapter);
		bt_addbalck_activity_contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ContactActivity.this);
				final AlertDialog dialog = builder.create();
				View view = View.inflate(ContactActivity.this,
						R.layout.add_black_dialog, null);
				final EditText et_phone_black_dialog = (EditText) view
						.findViewById(R.id.et_phone_black_dialog);
				final RadioGroup rg_type_contact = (RadioGroup) view
						.findViewById(R.id.rg_type_contact);
				Button bt_cancel_dialog = (Button) view
						.findViewById(R.id.bt_cancel_dialog);
				Button bt_confirm_dialog = (Button) view
						.findViewById(R.id.bt_confirm_dialog);
				bt_cancel_dialog.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				bt_confirm_dialog.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String phone = et_phone_black_dialog.getText()
								.toString().trim();
						String type = "3";
						int id = rg_type_contact.getCheckedRadioButtonId();
						if (TextUtils.isEmpty(phone)) {
							ToastUtiles.makeToast(ContactActivity.this,
									"号码不能为空");
							return;
						} else {
							switch (id) {
							case R.id.rd_phone:
								type = "1";
								break;
							case R.id.rd_msg:
								type = "2";
								break;
							case R.id.rd_all:
								type = "3";
								break;
							}
						}
						ContactDbUtiles.insert(ContactActivity.this, phone,
								type);
						BlackInfo blackInfo = new BlackInfo();
						blackInfo.setPhone(phone);
						blackInfo.setType(type);
						blackInfos.add(0, blackInfo);
						adapter.notifyDataSetChanged();
						dialog.dismiss();
						ToastUtiles.makeToast(ContactActivity.this, "添加成功");
					}
				});
				dialog.setView(view, 0, 0, 0, 0);// 无边框
				dialog.show();
			}
		});

		loadBlackInfos(limit, offset);
		
		listview_activity_contact.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){//静止状态
					if(listview_activity_contact.getLastVisiblePosition() == (adapter.getCount()-1)){
						System.out.println("到底了========");
						//加载
						offset+=limit;
						if(offset>=blackInfoCount){
							ToastUtiles.makeToast(ContactActivity.this, "没有数据了");
						}
						else {
							loadBlackInfos(limit, offset);
						}
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
	}
/**
 *  子线程获取数据
 */
	private void loadBlackInfos(final int limit, final int offset) {
		new Thread() {
			public void run() {
				handler.sendEmptyMessage(LOADING);
				blackInfos.addAll(ContactDbUtiles.getLimitBlackInfo(ContactActivity.this, limit, offset));
				handler.sendEmptyMessage(LOAD_COMPLET);
			};
		}.start();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return blackInfos.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(ContactActivity.this,
						R.layout.item_listview_activity_contact, null);
				holder = new ViewHolder();
				holder.tv_phone_item_listview_activity_contact = (TextView) view
						.findViewById(R.id.tv_phone_item_listview_activity_contact);
				holder.tv_type_item_listview_activity_contact = (TextView) view
						.findViewById(R.id.tv_type_item_listview_activity_contact);
				holder.iv_item_listview_activity_contact = (ImageView) view
						.findViewById(R.id.iv_item_listview_activity_contact);
				view.setTag(holder);
			}

			final String phone = blackInfos.get(position).getPhone();
			String type = blackInfos.get(position).getType();
			holder.tv_phone_item_listview_activity_contact.setText(phone);
			if ("1".equals(type)) {
				holder.tv_type_item_listview_activity_contact.setText("拦截电话");
			} else if ("2".equals(type)) {
				holder.tv_type_item_listview_activity_contact.setText("拦截短信");
			} else if ("3".equals(type)) {
				holder.tv_type_item_listview_activity_contact.setText("全部拦截");
			}
			holder.iv_item_listview_activity_contact
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							AlertDialog.Builder builder = new AlertDialog.Builder(
									ContactActivity.this);
							builder.setTitle("提示").setMessage("确认删除吗？");
							builder.setNegativeButton("取消", null);
							builder.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											ContactDbUtiles
													.deleteByPhone(
															ContactActivity.this,
															phone);
											blackInfos.remove(position);
											ToastUtiles.makeToast(
													ContactActivity.this,
													"删除成功");
											adapter.notifyDataSetChanged();
										}

									});
							builder.show();
						}
					});
			return view;
		}

		class ViewHolder {
			TextView tv_phone_item_listview_activity_contact;
			TextView tv_type_item_listview_activity_contact;
			ImageView iv_item_listview_activity_contact;
		}

	}
}
