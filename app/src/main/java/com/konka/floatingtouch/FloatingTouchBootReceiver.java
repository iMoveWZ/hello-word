package com.konka.floatingtouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class FloatingTouchBootReceiver extends BroadcastReceiver
{
	public static final String TAG = "FloatingTouchBootReceiver";
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
		{
			Log.v(TAG, "---------------------------------FlotaingTouchService start");
			Intent myIntent = new Intent();
			myIntent.setAction("com.konka.FloatingTouchService");
			context.startService(myIntent);
		}
	}

}
