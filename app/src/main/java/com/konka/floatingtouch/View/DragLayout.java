package com.konka.floatingtouch.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class DragLayout extends LinearLayout
{
	private static final String TAG = "DragLayout";

	//touch空闲状态 
	public static final int TOUCH_STATE_IDLE = 0;
	
	//touch拖拽状态
	public static final int TOUCH_STATE_DRAG = 1;	
	
	//定义全局状态  touch state
	private int mTouchState = TOUCH_STATE_IDLE;
	
	//定义touch的起始位置 	
	private float mStart_X = -1.0f ;	 
	private float mStart_Y = -1.0f ;
	
	private long mTouchDownTime = 0;
    
	public DragLayout(Context context) 
	{ 
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DragLayout(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public DragLayout(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	//获取当前touch的state
	public int getTouchState()
	{
		return mTouchState;
	}
	
	//设置当前touch的state
	public void setTouchState(int state)
	{
		mTouchState = state;
		
		if(state == TOUCH_STATE_IDLE)
		{
			mTouchDownTime = 0;
			mStart_X = -1;
			mStart_Y = -1;
		}
	}
	
	//获取拖动起始位置 X
	public float getStartX()
	{
		return mStart_X;
	}
	
	public float getStartY()
	{
		return mStart_Y;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		// TODO Auto-generated method stub
		super.onInterceptTouchEvent(ev);		
		//Log.v(TAG, "========222============getaction  =  " + ev.getAction());
		
		switch (ev.getAction())
		{
			case MotionEvent.ACTION_DOWN:	
			{
				mTouchDownTime = System.currentTimeMillis();
				mStart_X = ev.getRawX();
				mStart_Y = ev.getRawY();		
						
			}
				break;
			case MotionEvent.ACTION_MOVE:
			{
				if( mTouchDownTime != 0 && (System.currentTimeMillis() - mTouchDownTime ) < 300L )
				{
					break;
				}
				else
				{
					if(mTouchState == TOUCH_STATE_IDLE)
					{
						mTouchState = TOUCH_STATE_DRAG;
					}
				}
			}
				break;
			case MotionEvent.ACTION_UP:
			{
				mStart_X = -1;
				mStart_Y = -1;
				
				mTouchDownTime = 0; 
				
				//好像没跑这里
				if(mTouchState == TOUCH_STATE_DRAG)
				{
					mTouchState = TOUCH_STATE_IDLE;
					Log.v(TAG, "=========ACTION_UP============mTouchState =  " + mTouchState);
					return true;
				}
			}				
				break;
	
			default:
				break;
		}
		
		//Log.v(TAG, "=====================mTouchState =  " + mTouchState);
		return mTouchState != TOUCH_STATE_IDLE;		
	}
	
}
