package com.example.mobilesafe.base;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utiles.ToastUtiles;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetupActivity extends Activity {
	protected SharedPreferences sp;
	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// 事件逻辑
				if(Math.abs(e1.getY()-e2.getY())>100){
					ToastUtiles.makeToast(BaseSetupActivity.this, "手势错误");
					return true;
				}
				if(e2.getX()-e1.getX()>100){//右滑
					pre();
					overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
				}else if (e1.getX()-e2.getX()>100) {
					next();
					overridePendingTransition(R.anim.next_in, R.anim.next_out);
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
			
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 处理触摸事件
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	public  void pre(View view){
		pre();
		overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
	};
	public  void next(View view){
		next();
		overridePendingTransition(R.anim.next_in, R.anim.next_out);
	};
	public abstract void pre();
	public abstract void next();
}
