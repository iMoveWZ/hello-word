package com.konka.floatingtouch;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.KeyEvent;

public class FloatingTouchProcesser
{
	public static final String TAG = "Processer";
	
	//返回
	public static final int DO_BACK = 1;
	//首页
	public static final int DO_HOME = 2;
	//最近运行
	public static final int DO_RECENT = 3;
	//声音 + 
	public static final int DO_VOL_UP = 4;
	//声音 -
	public static final int DO_VOL_DOWN = 5;
	//频道 +
	public static final int DO_CHANNEL_UP = 6;
	//频道 -
	public static final int DO_CHANNEL_DOWN = 7;
	//静音
	public static final int DO_MUTE = 8;
	//信源
	public static final int DO_INPUT = 9;
	//菜单
	public static final int DO_MENU = 10;
	//3D
	public static final int DO_3D = 11;
	//待机
	public static final int DO_STANDYBY = 12;
	
	
	private Instrumentation mInst = null;
	
	private Context mContext = null;
	
	private HandlerThread mKeyThread = null;
	
	private Handler	mKeyHandler = null;
	
	//构造函数 ， 参数为context
	public FloatingTouchProcesser(Context context)
	{
		Log.v(TAG, "create for sending the virtual key ");
		
		mKeyThread = new HandlerThread("FloatingTouch_SendKey");
		
		mKeyThread.start();
		
		mInst = new Instrumentation();
		
		mContext = context;
		
		mKeyHandler = new Handler(mKeyThread.getLooper());
	}
	
	public void quit()
	{
		mKeyHandler.removeCallbacksAndMessages(null);
		mKeyThread.quit();
	}
	
	//提供功能接口
	public void processTouch(int do_what)
	{
		switch(do_what)
		{
			case DO_BACK:
				sendKeyEvent(KeyEvent.KEYCODE_BACK);
				break;
			case DO_HOME:
				sendKeyEvent(KeyEvent.KEYCODE_HOME);
				break;
			case DO_RECENT:
			{
				Intent intent = new Intent("com.konka.systemui.action.SHOW_RECENT_DIALOG");
		        //intent.putExtra("isInTouchMode", isInTouchMode);//这个表示是否是鼠标模式，此选项值可以忽略，默认是false
				mContext.sendBroadcast(intent);
			}
				break;
			case DO_VOL_UP:
				sendKeyEvent(KeyEvent.KEYCODE_VOLUME_UP);				
				break;
			case DO_VOL_DOWN:
				sendKeyEvent(KeyEvent.KEYCODE_VOLUME_DOWN);
				break;
			case DO_CHANNEL_UP:
				sendKeyEvent(KeyEvent.KEYCODE_CHANNEL_UP);
				break;
			case DO_CHANNEL_DOWN:
				sendKeyEvent(KeyEvent.KEYCODE_CHANNEL_DOWN);
				break;
			case DO_MUTE:
				sendKeyEvent(KeyEvent.KEYCODE_VOLUME_MUTE);				
				break;
			case DO_INPUT:
				sendKeyEvent(KeyEvent.KEYCODE_TV_INPUT);	
				break;
			case DO_MENU:
				sendKeyEvent(KeyEvent.KEYCODE_MENU);
				break;
			case DO_3D:
				sendKeyEvent(KeyEvent.KEYCODE_3D_MODE);
				break;
			case DO_STANDYBY:
				sendKeyEvent(KeyEvent.KEYCODE_POWER);
				break;
			default:
				break;
				
		}
	}
	
	private void sendKeyEvent(int KeyCode)
	{
		sendKeyEvent1(KeyCode);
	}
	
	private void sendKeyEvent1(int KeyCode)
	{
		final int keyCode = KeyCode;

		mKeyHandler.post(new Runnable()
		{			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				mInst.sendKeyDownUpSync(keyCode);
			}
		});
	}
	
	
	
//	private void sendKeyEvent2(int KeyCode)
//	{
//		long mDownTime = SystemClock.uptimeMillis();  
//	    long when = mDownTime;  
//	    final KeyEvent ev = new KeyEvent(mDownTime, when, KeyEvent.ACTION_DOWN, KeyCode, 0,  
//	            0, -1, 0, KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY, InputDevice.SOURCE_KEYBOARD);  
//	    IWindowManager mWindowManager = IWindowManager.Stub.asInterface(  
//	            ServiceManager.getService(Context.WINDOW_SERVICE));  
//	    try 
//	    {  
//	        mWindowManager.injectInputEventNoWait(ev);  
//	    } 
//	    catch (RemoteException ex) 
//	    {  
//	    }  
//	}
}
