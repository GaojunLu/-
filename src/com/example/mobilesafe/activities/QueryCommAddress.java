package com.example.mobilesafe.activities;

import com.example.mobilesafe.R;
import com.example.mobilesafe.dao.CommonnumDao;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListAdapter;
import android.widget.TextView;

public class QueryCommAddress extends Activity {
	private ExpandableListView elv_querycommaddress;
	private MyAdapter adapter;
	private CommonnumDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_querycommaddress);
		dao = new CommonnumDao(this);
		elv_querycommaddress = (ExpandableListView) findViewById(R.id.elv_querycommaddress);
		adapter = new MyAdapter();
		elv_querycommaddress.setAdapter(adapter);
		elv_querycommaddress.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				String[] s = dao.getChildNameAndNumber(groupPosition, childPosition);
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:"+s[1]));
				startActivity(intent);
				return false;
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dao.closeDB();
	}
	
	class MyAdapter extends BaseExpandableListAdapter{

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tv ;
			if(convertView!=null){
				tv = (TextView) convertView;
			}else{
				tv = new TextView(QueryCommAddress.this);
			}
			String[] s = dao.getChildNameAndNumber(groupPosition, childPosition);
			tv.setText("\t\t"+s[0]+"\n\t\t"+s[1]);
			tv.setTextSize(12);
			return tv;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return dao.getChildCount(groupPosition);
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return dao.getGroupCount();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tv ;
			if(convertView!=null){
				tv = (TextView) convertView;
			}else{
				tv = new TextView(QueryCommAddress.this);
			}
			tv.setText("\t"+dao.getGroupName(groupPosition));
			tv.setTextSize(16);
			tv.setTextColor(Color.BLACK);
			return tv;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}
}
