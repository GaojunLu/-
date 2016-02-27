package com.example.mobilesafe.activities;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

public class TrafficActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic);
		TextView tv_traffic_info = (TextView) findViewById(R.id.tv_traffic_info);
		
		long totalRxBytes = TrafficStats.getTotalRxBytes();
		long totalTxBytes = TrafficStats.getTotalTxBytes();
		
		tv_traffic_info.setText("上传流量:"+Formatter.formatFileSize(this, totalTxBytes)+"\n下载流量:"+Formatter.formatFileSize(this, totalRxBytes));
		
	}
}
