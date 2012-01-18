package com.crazyapps.chessclock;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChessClockActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Starting", Toast.LENGTH_SHORT).show();
				new CountDownTimer(5000, 1000) {

					public void onTick(long millisUntilFinished) {
						((TextView) findViewById(R.id.countdown1)).setText(format(millisUntilFinished));

					}

					public void onFinish() {
						Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
					}
				}.start();
			}
		});
	}

	private CharSequence format(long millisUntilFinished) {
		// TODO Auto-generated method stub
		// return new SimpleDateFormat("hh:mm:ss").format(millisUntilFinished);
		int h = (int) ((millisUntilFinished / 1000) / 3600);
		int m = (int) (((millisUntilFinished / 1000) / 60) % 60);
		int s = (int) ((millisUntilFinished / 1000) % 60);
		return String.format("%d:%d:%d", h, m, s);
	}
}