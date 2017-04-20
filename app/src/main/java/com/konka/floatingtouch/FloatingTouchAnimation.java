package com.konka.floatingtouch;

import java.util.HashMap;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class FloatingTouchAnimation {
	private static final int standard_up = 1;
	private static final int standard_up_back = 2;
	private static final int standard_down = 3;
	private static final int standard_down_back = 4;
	private static final int standard_left = 5;
	private static final int standard_left_back = 6;
	private static final int standard_right = 7;
	private static final int standard_right_back = 8;
	private static final int advanced_up = 9;
	private static final int advanced_up_back = 10;
	private static final int advanced_down = 11;
	private static final int advanced_down_back = 12;
	public Boolean scaleAnimaiton_done = false;
	public Boolean translateAnimaiton_done = false;
	
	//松手的动画步长
	public static final int BACK_STEP_DISTANCE1= 100;
	public static final int BACK_STEP_DISTANCE2= 10;
	public static final int BACK_STEP_TIME = 0;

	public void StandardAnimation(View view, int id) {
		translateAnimaiton_done = false;
		switch (id) {
		case standard_up:
			scaleAnimation(view, 0, 1, 0.5f,0 , 1, 0);
			break;
		case standard_up_back:
			scaleAnimation(view, 1, 0,0.5f,1, 0, 0);
			break;
		case standard_down:
			scaleAnimation(view, 0, 1, 0.5f, 0,1,1);
			break;
		case standard_down_back:
			scaleAnimation(view, 1, 0,0.5f,1, 0, 1);
			break;
		case standard_left:
			scaleAnimation(view, 0,1, 0, 0, 1,0.5f);
			break;
		case standard_left_back:
			scaleAnimation(view, 1,0, 0, 1, 0,0.5f);
			break;
		case standard_right:
			scaleAnimation(view, 0,1, 1, 0, 1,0.5f);
			break;
		case standard_right_back:
			scaleAnimation(view, 1,0, 1, 1, 0,0.5f);
			break;
		default:
			break;
		}
	}

	public void AdvancedAnimaiton(View view, int id) {
		switch (id) {
		case advanced_up:
			scaleAnimation(view, 0, 1,0.9f,0,1,0);
			break;
		case advanced_up_back:
			scaleAnimation(view, 1, 0,0.9f,1,0,0);
			break;
		case advanced_down:
			//scaleAnimation(view, 0, 1, 1);
			break;
		case advanced_down_back:
			//scaleAnimation(view, 1, 0, 1);
			break;
		default:
			break;

		}
	}

	Handler myHanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		
				HashMap<String, Object> h = new HashMap<String, Object>();
				h = (HashMap<String, Object>) msg.obj;
				View view = (View) h.get("view");
				WindowManager mwm = (WindowManager) h.get("mwm");
				WindowManager.LayoutParams mparams = (WindowManager.LayoutParams) h
						.get("mparams");
				BackAnimation(mwm, mparams, view, msg.what,msg.arg1,msg.arg2);
					
		}
	};

	
	public void BackAnimation(WindowManager mWm,
			WindowManager.LayoutParams mParams, View view, int position,int screenWidth,int screenHeight) {
		switch (position) {
		case 1:
		{
			if (mParams.y > 100) 
			{
				mParams.y = mParams.y - BACK_STEP_DISTANCE1;
				mWm.updateViewLayout(view, mParams);

				sendMessage( mWm, mParams, view,  position,screenWidth,screenHeight);
			}
			else if (mParams.y > 0)
			{
				mParams.y = mParams.y - BACK_STEP_DISTANCE2;
				mWm.updateViewLayout(view, mParams);

				sendMessage( mWm, mParams, view,  position,screenWidth,screenHeight);
			}
				


			
		}
			break;
		case 2:
		{
			if (mParams.x > 100) 
			{
				mParams.x = mParams.x - BACK_STEP_DISTANCE1;
				mWm.updateViewLayout(view, mParams);

				sendMessage( mWm, mParams, view,  position,screenWidth,screenHeight);
			}
			else if (mParams.x > 0)
			{
				mParams.x = mParams.x - BACK_STEP_DISTANCE2;
				mWm.updateViewLayout(view, mParams);
				sendMessage( mWm, mParams, view,  position,screenWidth,screenHeight);
				
			}
		


			}
			break;
		case 3:
		{
			if (mParams.y < screenHeight-100-view.getHeight())
			{
				mParams.y = mParams.y + BACK_STEP_DISTANCE1;
				mWm.updateViewLayout(view, mParams);
				sendMessage( mWm, mParams, view,  position,screenWidth,screenHeight);
			}
			else if (mParams.y < screenHeight-view.getHeight())
			{
				mParams.y = mParams.y + BACK_STEP_DISTANCE2;
				mWm.updateViewLayout(view, mParams);
				sendMessage( mWm, mParams, view,  position,screenWidth,screenHeight);
			}
			
				

			}
			break;
		case 4:
		{
			if (mParams.x <screenWidth-100-view.getWidth()) 
			{
				mParams.x = mParams.x + BACK_STEP_DISTANCE1;
				mWm.updateViewLayout(view, mParams);

				sendMessage( mWm, mParams, view,  position,screenWidth,screenHeight);

			}
			else if (mParams.x < screenWidth-view.getWidth())
			{
				mParams.x = mParams.x + BACK_STEP_DISTANCE2;
				mWm.updateViewLayout(view, mParams);
				sendMessage( mWm, mParams, view,  position,screenWidth,screenHeight);
			}
				


			}
			break;
		default:
			break;
		}
		view.invalidate();
	}
    public void sendMessage(WindowManager mWm,
			WindowManager.LayoutParams mParams, View view, int position,int screenWidth,int screenHeight)
    {
    	Message msg = new Message();
		msg.what = position;
		msg.arg1 = screenWidth;
		msg.arg2 = screenHeight;
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		hashmap.put("view", view);
		hashmap.put("mwm", mWm);
		hashmap.put("mparams", mParams);
		msg.obj = hashmap;
		myHanlder.sendMessageDelayed(msg, BACK_STEP_TIME);
    	
    }
	public void translateAnimation(View view, float x1, float x2, float y1,
			float y2) {

		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, x1, Animation.RELATIVE_TO_SELF, x2,
				Animation.RELATIVE_TO_SELF, y1, Animation.RELATIVE_TO_SELF, y2);
		translateAnimation.setDuration(200);
		view.startAnimation(translateAnimation);
		translateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				// translateAnimaiton_done = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				translateAnimaiton_done = true;
			}
		});
	}

	public void scaleAnimation(View view,float x1,float x2,float x, float y1, float y2, float y) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(x1, x2, y1, y2,
				Animation.RELATIVE_TO_SELF, x, Animation.RELATIVE_TO_SELF, y);
		scaleAnimation.setDuration(200);
		view.startAnimation(scaleAnimation);

		scaleAnimation.setAnimationListener(new AnimationListener() {

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
				scaleAnimaiton_done = true;
			}
		});
	}
}
