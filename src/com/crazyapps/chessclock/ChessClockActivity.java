package com.crazyapps.chessclock;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.crazyapps.chessclock.CountDown.CountDownListener;

public class ChessClockActivity extends Activity {

	private final long	INITIAL_TIME	= 10000;

	private CountDown	countDown1;
	private CountDown	countDown2;

	private Vibrator	v;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);

		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		countDown1 = (CountDown) findViewById(R.id.countdown1);
		countDown2 = (CountDown) findViewById(R.id.countdown2);

		defCountDownBehavior(countDown1, countDown2);
		defCountDownBehavior(countDown2, countDown1);
	}

	private void defCountDownBehavior(final CountDown mainCountDown, final CountDown adverseCountDown) {
		mainCountDown.setTotal(INITIAL_TIME);

		mainCountDown.setCountDownListener(new CountDownListener() {

			public void onClick(View view) {
				v.vibrate(100);
				mainCountDown.pause();
				adverseCountDown.start();
			}

			public void onFinish() {
				toast("Done");
			}
		});
	}

	private void toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

}