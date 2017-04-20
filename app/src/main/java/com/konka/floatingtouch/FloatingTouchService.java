package com.konka.floatingtouch;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.konka.floatingtouch.View.DragLayout;

public class FloatingTouchService extends Service {
	public static final String TAG = "FloatingTouchService";
	
	private static final  int TOP = 1;
	private static final  int LEFT= 2;
	private static final  int BOTTOM= 3;
	private static final  int RIGHT= 4;
	
	private static final  int MSG_CHANGE_TO_HV = 5;
	private static final  int MSG_OPERATE_ADVANCEDVIEW = 6;
	private static final  int MSG_STANDARDVIEW_BACK = 7;
	
	private static final  int MSG_THREE_SECONDS= 8;
	private static final  int MSG_TEN_SECONDS = 9;
	
	private static final int MSG_TIME_THREE = 1;
	private static final int MSG_TIME_TEN = 2;
	
	// WindowManager and LayoutParams
	private WindowManager mWM = null;
	private WindowManager.LayoutParams mWMParams = null;

	// ViewGroup
	private View mCurView = null;
	private View mHandleView = null;
	private View mStandardView = null;
	private View mUpView = null;

	// 拖拽的layout
	private DragLayout mHandleDragLayout = null;
	private DragLayout mStandardDragLayout = null;

	// 最左侧和最右侧button， 根据位置变换为手柄 或者 更多
	private Button mSVLeftButton = null;
	private Button mSVRightButton = null;
	private Button btnhandle = null;

	private float x = 0;
	private float y = 0;
	private float startX = 0;
	private float startY = 0;

	private int screenWidth = 0;
	private int screenHeight = 0;

	private int mHandleWidth = 0;
	private int mHandleHeight = 0;
	private int mStandardWidth = 0;
	private int mStandardHeight = 0;
	private int mAdvancedHeight = 0;

	private int mPosition = -1;

	private FloatingTouchAnimation myTouchAnimation;
	private Handler myTimerHander = null;
	private View myStandard_layout = null;
	private View myAdvanced_layout = null;
	private Boolean more_flag = false;
	private Boolean auto_flag = false;
	private Boolean move_flag = false;
	public int id = 0;
	// lihu
	private Handler myHandler = null;
	
	
	private FloatingTouchProcesser mProcesser = new FloatingTouchProcesser(this);

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Notification notification = new Notification();
		this.startForeground(0, notification);

		// get layout
		mHandleView = LayoutInflater.from(this).inflate(R.layout.handle, null);
		mStandardView = LayoutInflater.from(this).inflate(R.layout.standard,
				null);
		mUpView = (View) mStandardView.findViewById(R.id.standard_layout);
		mHandleDragLayout = (DragLayout) mHandleView
				.findViewById(R.id.handle_draglayout);
		mHandleDragLayout.setOnTouchListener(mViewDragListener);

		mStandardDragLayout = (DragLayout) mStandardView
				.findViewById(R.id.standard_draglayout);
		mStandardDragLayout.setOnTouchListener(mViewDragListener);

		// 最左边按钮 ，与其 listener
		mSVLeftButton = (Button) mStandardView.findViewById(R.id.leftbutton);
		mSVLeftButton.setOnClickListener(mSV_HandleListener);

		// 最右边按钮 ，与其 listener
		mSVRightButton = (Button) mStandardView.findViewById(R.id.rightbutton);
		mSVRightButton.setOnClickListener(mSV_MoreListener);

		// 手柄与其listener
		btnhandle = (Button) mHandleView.findViewById(R.id.handlebutton);
		btnhandle.setOnClickListener(mHV_HandleListener);

		myTouchAnimation = new FloatingTouchAnimation();

		initFuncButtonListener();

		mCurView = mHandleView;
		myStandard_layout = (View) mStandardView
				.findViewById(R.id.standard_layout);
		myAdvanced_layout = (View) mStandardView
				.findViewById(R.id.advanced_layout);
		
		createview();
		
		myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				HandleMessage(msg);
			}
		};
		myTimerHander = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				HandleTimeMessage(msg);				
			}
		};
	}
	protected void HandleTimeMessage(Message msg)
	{
		switch(msg.what)
		{
		case MSG_TIME_THREE:
		{
		switch (mPosition) {
		case TOP: {

			btnhandle.setBackgroundDrawable(this.getResources()
					.getDrawable(R.drawable.handle_down_select_s));

			break;
		}
		case LEFT: {
           Log.v("","=======================================ggggggggggggggggggggggggg");
			btnhandle.setBackgroundDrawable(this.getResources()
					.getDrawable(R.drawable.handle_right_select_s));

			break;
		}
		case BOTTOM: {

			btnhandle.setBackgroundDrawable(this.getResources()
					.getDrawable(R.drawable.handle_up_select_s));

			break;
		}
		case RIGHT: {

			btnhandle.setBackgroundDrawable(this.getResources()
					.getDrawable(R.drawable.handle_left_select_s));

			break;
		}
		default:
			break;
		}
		}
		break;
		case MSG_TIME_TEN:
		{
			auto_flag = true;
			if (!more_flag) {

				// “更多”未展开时
				myHandler.sendEmptyMessage(MSG_STANDARDVIEW_BACK );

			} else {

				// "更多" 已展开
				more_flag = false;
				myHandler.sendEmptyMessage(MSG_OPERATE_ADVANCEDVIEW);
			}
		}
		break;
		default:
			break;
			
		}
	}
	protected void HandleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case MSG_CHANGE_TO_HV :// 返回手柄状态
			Log.v("", "=======================case1");
			if (true) {
				Log.v("case 1", "================================DONE"
						+ myTouchAnimation.translateAnimaiton_done);
				mWM.removeView(mCurView);
				switch (mPosition) {
				case TOP: {
					mWMParams.width = mHandleHeight;
					mWMParams.height = mHandleWidth;
					mWMParams.x += (mStandardWidth - mHandleWidth) / 2;
					if (auto_flag == false) {
						btnhandle
								.setBackgroundDrawable(this
										.getResources()
										.getDrawable(
												R.drawable.handle_down_select_uns));
						myTimerHander.removeMessages(MSG_TIME_TEN);
						myTimerHander.sendEmptyMessageDelayed(MSG_TIME_THREE,3000);
					} else {
						auto_flag = false;
						btnhandle.setBackgroundDrawable(this.getResources()
								.getDrawable(R.drawable.handle_down_select_s));
					}
					break;
				}
				case LEFT: {
					mWMParams.width = mHandleWidth;
					mWMParams.height = mHandleHeight;
					if (auto_flag == false) {
						myTimerHander.removeMessages(MSG_TIME_TEN);
						myTimerHander.sendEmptyMessageDelayed(MSG_TIME_THREE,3000);
						btnhandle
								.setBackgroundDrawable(this
										.getResources()
										.getDrawable(
												R.drawable.handle_right_select_uns));
					} else {
						auto_flag = false;
						btnhandle.setBackgroundDrawable(this.getResources()
								.getDrawable(R.drawable.handle_right_select_s));
					}
					break;
				}
				case BOTTOM: {
					mWMParams.width = mHandleHeight;
					mWMParams.height = mHandleWidth;
					mWMParams.x += (mStandardWidth - mHandleWidth) / 2;
					mWMParams.y = screenHeight - mHandleWidth;
					if (auto_flag == false) {
						myTimerHander.removeMessages(MSG_TIME_TEN);
						myTimerHander.sendEmptyMessageDelayed(MSG_TIME_THREE,3000);
						btnhandle.setBackgroundDrawable(this.getResources()
								.getDrawable(R.drawable.handle_up_select_uns));
					} else {
						auto_flag = false;
						btnhandle.setBackgroundDrawable(this.getResources()
								.getDrawable(R.drawable.handle_up_select_s));
					}
					break;
				}
				case RIGHT: {
					mWMParams.width = mHandleWidth;
					mWMParams.height = mHandleHeight;
					mWMParams.x = screenWidth - mHandleWidth;
					if (auto_flag == false) {
						myTimerHander.removeMessages(MSG_TIME_TEN);
						myTimerHander.sendEmptyMessageDelayed(MSG_TIME_THREE,3000);
						btnhandle
								.setBackgroundDrawable(this
										.getResources()
										.getDrawable(
												R.drawable.handle_left_select_uns));
					} else {
						auto_flag = false;
						btnhandle.setBackgroundDrawable(this.getResources()
								.getDrawable(R.drawable.handle_left_select_s));
					}
					break;
				}
				default:
					break;
				}
				mCurView = mHandleView;
				mWM.addView(mCurView, mWMParams);

				Log.v("case 1", "================================HV added");
			}
			break;
		case MSG_OPERATE_ADVANCEDVIEW: {// 收起
			Log.v("", "=======================case2");
			if (more_flag) {
				// 展开状态下，点击“更多”
				more_flag = false;
				if (mPosition != BOTTOM) {
					Log.v("", "--------------------------=="
							+ myTouchAnimation.scaleAnimaiton_done);
					mSVRightButton.setBackgroundDrawable(mStandardView
							.getResources().getDrawable(
									R.drawable.standard_advanced));
					myTouchAnimation.scaleAnimaiton_done = false;
					mWMParams.width = mStandardWidth;
					mWMParams.height = mStandardHeight;
					mWM.updateViewLayout(mCurView, mWMParams);
				} else {
					mSVRightButton.setBackgroundDrawable(mStandardView
							.getResources().getDrawable(
									R.drawable.standard_advanced));
					myTouchAnimation.scaleAnimaiton_done = false;
					mWMParams.width = mStandardWidth;
					mWMParams.height = mStandardHeight;
					mWMParams.y = screenHeight - mStandardHeight;
					mWM.updateViewLayout(mCurView, mWMParams);
				}
				Log.v("case 2", "================================SV updated");
			} else {
				// 展开状态下，点击“返回”
				more_flag = true;

				myTouchAnimation.AdvancedAnimaiton(myAdvanced_layout, 10);
				myHandler.sendEmptyMessageDelayed(MSG_OPERATE_ADVANCEDVIEW, 200);
				myHandler.sendEmptyMessageDelayed(MSG_STANDARDVIEW_BACK , 300);

			}
		}
			break;
		case MSG_STANDARDVIEW_BACK : {
			// 返回手柄状态
			Log.v("", "=======================case3");
			more_flag = false;
			switch (mPosition) {
			case TOP: {

				// mWMParams.x += (mCurView.getWidth() / 2 - 27);

				myTouchAnimation.StandardAnimation(myStandard_layout, 2);
				break;
			}
			case LEFT: {

				myTouchAnimation.StandardAnimation(myStandard_layout, 6);
				break;
			}
			case BOTTOM: {
				// mWMParams.x += (mCurView.getWidth() / 2 - 27);

				myTouchAnimation.StandardAnimation(myStandard_layout, 4);
				break;
			}
			case RIGHT: {

				myTouchAnimation.StandardAnimation(myStandard_layout, 8);
				break;
			}
			default:
				myTouchAnimation.StandardAnimation(myStandard_layout, 6);
			}
			myHandler.sendEmptyMessageDelayed(MSG_CHANGE_TO_HV , 200);
		}
			// else
			// sendEmptyMessage(1);
			break;
		default:
			break;
		}
	}

	// 手柄状态下的手柄的 onClickListener
	private OnClickListener mHV_HandleListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			mWM.removeView(mCurView);

			mCurView = mStandardView;
			mWMParams.width = mStandardWidth;
			mWMParams.height = mStandardHeight;
			Log.v("W/H", "------------------------W=" + mWMParams.width
					+ "   H=" + mWMParams.height);

			// checkEdge();
			Log.v("mHVcheck", "=================================  mPosition="
					+ mPosition);
			switch (mPosition) {
			case TOP: {
				mWMParams.x -= (mStandardWidth - mHandleWidth) / 2;
				mWM.addView(mCurView, mWMParams);
				Log.v("case 1",
						"----------------------------------------------------------added");
				myTouchAnimation.StandardAnimation(myStandard_layout, 1);
				break;
			}
			case LEFT: {
				mWM.addView(mCurView, mWMParams);
				Log.v("case 2",
						"----------------------------------------------------------added");
				myTouchAnimation.StandardAnimation(myStandard_layout, 5);
				break;
			}
			case BOTTOM: {
				mWMParams.x -= (mStandardWidth - mHandleWidth) / 2;
				Log.v("case 3",
						"----------------------------------------------------------X="
								+ mWMParams.x);
				mWM.addView(mCurView, mWMParams);
				Log.v("case 3",
						"----------------------------------------------------------added");
				myTouchAnimation.StandardAnimation(myStandard_layout, 3);
				break;
			}
			case RIGHT: {
				mWM.addView(mCurView, mWMParams);
				Log.v("case 4",
						"----------------------------------------------------------added");
				myTouchAnimation.StandardAnimation(myStandard_layout, 7);
				break;
			}
			default:
				mWM.addView(mCurView, mWMParams);
				myTouchAnimation.StandardAnimation(myStandard_layout, 5);
			}
			myTimerHander.removeMessages(MSG_TIME_THREE);
			myTimerHander.sendEmptyMessageDelayed(MSG_TIME_TEN,10000);
		}
	};

	// 标准状态下的手柄 onClickListener
	private OnClickListener mSV_HandleListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (!more_flag) {

				// “更多”未展开时
				myHandler.sendEmptyMessage(MSG_STANDARDVIEW_BACK );

			} else {

				// "更多" 已展开
				more_flag = false;
				myHandler.sendEmptyMessage(MSG_OPERATE_ADVANCEDVIEW);
			}
		}
	};

	// 标准状态下更多的OnClickListener
	private OnClickListener mSV_MoreListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.v(TAG,"==============================ONCLICK  MORE  BUTTON======================");
			myHandler.removeMessages(MSG_OPERATE_ADVANCEDVIEW);
			myTimerHander.removeMessages(MSG_TIME_TEN);
			myTimerHander.sendEmptyMessageDelayed(MSG_TIME_TEN,10000);
			if (!more_flag) {
				more_flag = true;
				mSVRightButton.setBackgroundDrawable(mStandardView
						.getResources().getDrawable(
								R.drawable.standard_advanced_2));
				mWMParams.width = mStandardWidth;
				mWMParams.height = mAdvancedHeight;
				mWM.updateViewLayout(mCurView, mWMParams);
				if (mPosition == BOTTOM) {
					// myTouchAnimation.translateAnimation(mUpView, 0, 0, -1,
					// 1);
					myTouchAnimation.AdvancedAnimaiton(myAdvanced_layout, 9);
				} else {
					myTouchAnimation.AdvancedAnimaiton(myAdvanced_layout, 9);
				}

			} else {
				mSVRightButton.setBackgroundDrawable(mStandardView
						.getResources().getDrawable(
								R.drawable.standard_advanced));
				// myTouchAnimation.translateAnimation(mUpView, 0, 0, 1, -1);
				myTouchAnimation.AdvancedAnimaiton(myAdvanced_layout, 10);
				myHandler.sendEmptyMessageDelayed(MSG_OPERATE_ADVANCEDVIEW, 200);

			}
		}
	};

	// 标准状态下的 各个功能button的OnClickListener
	private OnClickListener mSV_ButtonFuncListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (move_flag == false) {
				myTimerHander.removeMessages(MSG_TIME_TEN);
				myTimerHander.sendEmptyMessageDelayed(MSG_TIME_TEN,10000);
				switch (v.getId()) {
				case R.id.home:
					mProcesser.processTouch(FloatingTouchProcesser.DO_HOME);
					break;
				case R.id.back:
					mProcesser.processTouch(FloatingTouchProcesser.DO_BACK);
					break;
//				case R.id.recent:
//					mProcesser.processTouch(FloatingTouchProcesser.DO_RECENT);
//					break;
				case R.id.vol_up:
					mProcesser.processTouch(FloatingTouchProcesser.DO_VOL_UP);
					break;
				case R.id.vol_down:
					mProcesser.processTouch(FloatingTouchProcesser.DO_VOL_DOWN);
					break;
				case R.id.channel_up:
					mProcesser
							.processTouch(FloatingTouchProcesser.DO_CHANNEL_UP);
					break;
				case R.id.channel_down:
					mProcesser
							.processTouch(FloatingTouchProcesser.DO_CHANNEL_DOWN);
					break;
				case R.id.silence:
					mProcesser.processTouch(FloatingTouchProcesser.DO_MUTE);
					break;
				case R.id.source:
					mProcesser.processTouch(FloatingTouchProcesser.DO_INPUT);
					break;
				case R.id.menu:
					mProcesser.processTouch(FloatingTouchProcesser.DO_MENU);
					break;
//				case R.id.ddd:
//					mProcesser.processTouch(FloatingTouchProcesser.DO_3D);
//					break;
				case R.id.power:	
					myTimerHander.removeMessages(MSG_TIME_TEN);
					mWM.removeView(mCurView);
					showDialog();
					break;
				default:
					break;
				}
			}
		}
	};
	

	private OnTouchListener mViewDragListener = new OnTouchListener() {
		float lastX = -1.0f;
		float lastY = -1.0f;

		float relaX = -1.0f;
		float relaY = -1.0f;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			// 获取当前拖拽的DragLayout
			DragLayout dl = (DragLayout) v;
			boolean ret = false;

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {

			}
				break;
			case MotionEvent.ACTION_MOVE: {
				if (dl.getTouchState() == DragLayout.TOUCH_STATE_DRAG) {
					// Log.v(TAG, " MotionEvent.ACTION_MOVE " );
			    	//	mTimer = new myTimer();
					//mTimer.timer.cancel();
					myTimerHander.removeMessages(MSG_TIME_TEN);
					myTimerHander.removeMessages(MSG_TIME_THREE);
					move_flag = true;
					// 无起始位置

					if (dl.getStartX() == -1 || dl.getStartY() == -1)
						break;

					// 第一次收到move
					if (lastX == -1.0f || lastY == -1.0f) {
						lastX = dl.getStartX();
						lastY = dl.getStartY();

						relaX = dl.getStartX() - mWMParams.x;
						relaY = dl.getStartY() - mWMParams.y;
						if (mCurView == mHandleView) {
							changeBackground();
						}
					}

					// 移动界面位置
					if (mWMParams.x < 0) {
						mWMParams.x = 0;
					} else if (mWMParams.x > screenWidth - mWMParams.width) {
						mWMParams.x = screenWidth - mWMParams.width;
					} else {
						mWMParams.x = (int) (event.getRawX() - relaX);
					}
					if (mWMParams.y < 0) {
						mWMParams.y = 0;
					} else if (mWMParams.y > screenHeight - mWMParams.height) {
						mWMParams.y = screenHeight - mWMParams.height;
					} else {
						mWMParams.y = (int) (event.getRawY() - relaY);
					}
					mWM.updateViewLayout(mCurView, mWMParams);
					mCurView.invalidate();
					lastX = event.getRawX();
					lastY = event.getRawY();

					ret = true;
				}
			}
				break;
			case MotionEvent.ACTION_UP: {
				// 拖拽结束松手时
				dl.setTouchState(DragLayout.TOUCH_STATE_IDLE);
				// Log.v(TAG, " MotionEvent.ACTION_UP " );
				checkPosition();
				changeBackground();
				// 自动停靠
				myTouchAnimation.BackAnimation(mWM, mWMParams, mCurView,
						mPosition, screenWidth, screenHeight);
				move_flag = false;
				// checkEdge();

				if (mCurView == mHandleView) {
					myTimerHander.removeMessages(MSG_TIME_TEN);
					myTimerHander.sendEmptyMessageDelayed(MSG_TIME_THREE,3000);
				} else {
					myTimerHander.removeMessages(MSG_TIME_TEN);
					myTimerHander.sendEmptyMessageDelayed(MSG_TIME_TEN,10000);
				}
				

				Log.v("movecheck",
						"=================================  mPosition="
								+ mPosition);

				lastX = -1.0f;
				lastY = -1.0f;
				relaX = -1.0f;
				relaY = -1.0f;

				ret = true;
			}

			default:
				break;
			}
			return ret;
		}

	};

	private void createview() {
		mWM = (WindowManager) this.getSystemService(WINDOW_SERVICE);
		mesureScreenSize();
		Log.v("screen", screenWidth + "X" + screenHeight);
		mesureViewSize();
		mWMParams = new WindowManager.LayoutParams();
		mWMParams.type = LayoutParams.TYPE_SYSTEM_ERROR;
		mWMParams.format = PixelFormat.RGBA_8888;
		mWMParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		mWMParams.gravity = Gravity.TOP | Gravity.LEFT;
		mWMParams.x = 0;
		mWMParams.y = screenHeight / 2;
		mWMParams.width = mHandleWidth;
		mWMParams.height = mHandleHeight;

		mWM.addView(mCurView, mWMParams);

		checkPosition();
	}

	private void mesureScreenSize() {
		// 获得屏幕大小
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager WM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		WM.getDefaultDisplay().getMetrics(metrics);

		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
	}

	private void mesureViewSize() {
		// TODO Auto-generated method stub
		if (screenWidth == 1920) {
			mHandleWidth = 45;
			mHandleHeight = 85;
			mStandardWidth = 372;
			mStandardHeight = 67;
			mAdvancedHeight = 212;

		} else if (screenWidth == 1280) {

			mHandleWidth = 45 * 1280 / 1920;
			mHandleHeight = 85 * 1280 / 1920;
			mStandardWidth = 372 * 1280 / 1920;
			mStandardHeight = 67 * 1280 / 1920;
			mAdvancedHeight = 212 * 1280 / 1920;
		} else {
			mHandleWidth = 45;
			mHandleHeight = 85;
			mStandardWidth = 372;
			mStandardHeight = 67;
			mAdvancedHeight = 212;
		}
	}

	protected void changeBackground() {
		// TODO Auto-generated method stub
		// checkEdge();

		if (mCurView == mHandleView) {
			switch (mPosition) {
			case TOP: {
				mWMParams.width = mHandleHeight;
				mWMParams.height = mHandleWidth;
				btnhandle.setBackgroundDrawable(this.getResources()
						.getDrawable(R.drawable.handle_down_select_uns));

				break;
			}
			case LEFT: {
				mWMParams.width = mHandleWidth;
				mWMParams.height = mHandleHeight;
				btnhandle.setBackgroundDrawable(this.getResources()
						.getDrawable(R.drawable.handle_right_select_uns));
				break;
			}
			case BOTTOM: {
				mWMParams.width = mHandleHeight;
				mWMParams.height = mHandleWidth;
				if(mCurView == mHandleView && mPosition == BOTTOM 
						&& mWMParams.y >= screenHeight - mHandleHeight)
					mWMParams.y = screenHeight - mHandleWidth;
				btnhandle.setBackgroundDrawable(this.getResources()
						.getDrawable(R.drawable.handle_up_select_uns));
				break;
			}
			case RIGHT: {
				mWMParams.width = mHandleWidth;
				mWMParams.height = mHandleHeight;
				if(mCurView == mHandleView && mPosition == RIGHT 
						&& mWMParams.x >= screenWidth - mHandleHeight)
					mWMParams.x = screenWidth - mHandleWidth;
				btnhandle.setBackgroundDrawable(this.getResources()
						.getDrawable(R.drawable.handle_left_select_uns));
				break;
			}
			default:
				break;
			}
		}

		switch (mPosition) {
		case TOP: {
			mSVLeftButton.setBackgroundDrawable(this.getResources()
					.getDrawable(R.drawable.standard_direction_up));
			// Log.v("change",
			// "-------------------------------------------------------standardview1");
			break;
		}
		case LEFT: {
			mSVLeftButton.setBackgroundDrawable(this.getResources()
					.getDrawable(R.drawable.standard_direction_left));
			// Log.v("change",
			// "-------------------------------------------------------standardview2");
			break;
		}
		case BOTTOM: {
			mSVLeftButton.setBackgroundDrawable(this.getResources()
					.getDrawable(R.drawable.standard_direction_down));
			// Log.v("change",
			// "-------------------------------------------------------standardview3");
			break;
		}
		case RIGHT: {
			mSVLeftButton.setBackgroundDrawable(this.getResources()
					.getDrawable(R.drawable.standard_direction_right));
			// Log.v("change",
			// "-------------------------------------------------------standardview4");
			break;
		}
		default:
			break;

		}
		mWM.updateViewLayout(mCurView, mWMParams);
	}

	private void checkPosition() {

		float currentX = mWMParams.x + mCurView.getWidth() / 2;
		// Log.v("checkposition",
		// "  mCurView.getWidth() / 2="+mCurView.getWidth() / 2);

		float currentY = mWMParams.y + mCurView.getHeight() / 2;

		float relativeX = currentX - screenWidth / 2;
		// Log.v("checkposition",
		// " gradient = relativeY / relativeX="+relativeX);

		float relativeY = currentY - screenHeight / 2;
		// Log.v("checkposition",
		// " gradient = relativeY / relativeX="+relativeY);

		float gradient = relativeY / relativeX;
		// Log.v("checkposition",
		// " gradient = relativeY / relativeX="+gradient);

		float scrgradient = (float) screenHeight / (float) screenWidth;
		// Log.v("checkposition", " scrgradient = scrH / scrW="+scrgradient);
		/*
		 * mPosition: 1 : 上边 2 : 左边 3 : 下边 4 : 右边
		 */

		if (relativeX == 0) {
			if (relativeY < 0) {

				mPosition = TOP;

			} else if (relativeY > 0) {

				mPosition = BOTTOM;

			} else {

				mPosition = LEFT;
			}
		} else if ((relativeY > 0 && relativeX > 0 && gradient >= scrgradient)
				|| (relativeY > 0 && relativeX < 0 && gradient <= (-scrgradient))) {

			mPosition = BOTTOM;

		} else if ((relativeY < 0 && relativeX > 0 && gradient >= (-scrgradient))
				|| (relativeY > 0 && relativeX > 0 && gradient <= scrgradient)) {

			mPosition = RIGHT;
		} else if (relativeX < 0
				&& (gradient <= scrgradient && gradient >= (-scrgradient))) {

			mPosition = LEFT;
		} else if (relativeY < 0
				&& (gradient > scrgradient || gradient < (-scrgradient))) {

			mPosition = TOP;
		}
		Log.v("checkPosition", "------------------mPosition=" + mPosition);
	}

	MyDialogCanceledReceiver myReceiver = new MyDialogCanceledReceiver(); 
	private void showDialog() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(FloatingTouchService.this, startDialog.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.konka.floatingtouchservice.dialogcanceled"); 
		registerReceiver(myReceiver, filter); 
	}
	
	private class MyDialogCanceledReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			mWM.addView(mCurView, mWMParams);
			myTimerHander.sendEmptyMessageDelayed(MSG_TIME_TEN,10000);
			unregisterReceiver(myReceiver); 
		}
		
	}

	public void onDestroy() {

		super.onDestroy();
		mWM.removeView(mCurView);

		mProcesser.quit();
	}

	protected void initFuncButtonListener() {
		// TODO Auto-generated method stub

		Button btnback = (Button) mStandardView.findViewById(R.id.back);
		Button btnhome = (Button) mStandardView.findViewById(R.id.home);
		Button btnrecent = (Button) mStandardView.findViewById(R.id.recent);

		Button btnvoldown = (Button) mStandardView.findViewById(R.id.vol_down);
		Button btnvolup = (Button) mStandardView.findViewById(R.id.vol_up);
		Button btnchdown = (Button) mStandardView
				.findViewById(R.id.channel_down);
		Button btnchup = (Button) mStandardView.findViewById(R.id.channel_up);
		Button btnsilence = (Button) mStandardView.findViewById(R.id.silence);
		Button btnsource = (Button) mStandardView.findViewById(R.id.source);
		Button btnmenu = (Button) mStandardView.findViewById(R.id.menu);
		Button btn3d = (Button) mStandardView.findViewById(R.id.ddd);
		Button btnpower = (Button) mStandardView.findViewById(R.id.power);

		btnback.setOnClickListener(mSV_ButtonFuncListener);
		btnhome.setOnClickListener(mSV_ButtonFuncListener);
		btnrecent.setOnClickListener(mSV_ButtonFuncListener);

		btnvoldown.setOnClickListener(mSV_ButtonFuncListener);
		btnvolup.setOnClickListener(mSV_ButtonFuncListener);
		btnchdown.setOnClickListener(mSV_ButtonFuncListener);
		btnchup.setOnClickListener(mSV_ButtonFuncListener);
		btnsilence.setOnClickListener(mSV_ButtonFuncListener);
		btnsource.setOnClickListener(mSV_ButtonFuncListener);
		btnmenu.setOnClickListener(mSV_ButtonFuncListener);
		btn3d.setOnClickListener(mSV_ButtonFuncListener);
		btnpower.setOnClickListener(mSV_ButtonFuncListener);

	}

}