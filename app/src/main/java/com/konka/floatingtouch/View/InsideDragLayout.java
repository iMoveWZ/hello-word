package com.konka.floatingtouch.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class InsideDragLayout extends LinearLayout {
	
	private static final String TAG = "InsideDraggableLayout";
	
	public InsideDragLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public InsideDragLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public InsideDragLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO Auto-generated method stub
		//Log.v(TAG, "========onTouchEvent============getaction  =  " + event.getAction());
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			return true;
		}
		else
			return super.onTouchEvent(event);  
	}
}
