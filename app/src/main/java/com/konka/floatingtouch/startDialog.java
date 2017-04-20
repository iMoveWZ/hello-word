package com.konka.floatingtouch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class startDialog extends Activity {

	AlertDialog.Builder builder;
	private FloatingTouchProcesser dialogProcesser = new FloatingTouchProcesser(this);

		protected void onCreate(Bundle savedInstanceState)
		{			// TODO Auto-generated method stub
			
			super.onCreate(savedInstanceState);
			
			builder = new Builder(this);
			builder.setMessage(R.string.prompt);
			builder.setTitle("");
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							dialogProcesser.processTouch(FloatingTouchProcesser.DO_STANDYBY);
							finish();
						}
					});
			builder.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent mIntent =new Intent();
							mIntent.setAction("com.konka.floatingtouchservice.dialogcanceled");
							sendBroadcast(mIntent);
							finish();
						}
					});

			builder.setCancelable(false);
			builder.create();
			builder.show();
			
		}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Intent mIntent = new Intent();
		mIntent.setAction("com.konka.floatingtouchservice.dialogcanceled");
		sendBroadcast(mIntent);
		onDestroy();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
