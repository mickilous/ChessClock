package com.crazyapps.chessclock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.crazyapps.chessclock.widget.CountDown;
import com.crazyapps.chessclock.widget.CountDown.CountDownListener;
import com.crazyapps.chessclock.widget.CountDown.Status;

public class ChessClockActivity extends Activity {

	private static final int	ACTIVITY_PREFS	= 1;

	private CountDown			countDown1;
	private CountDown			countDown2;
	private SharedPreferences	prefs;

	private Vibrator			v;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		prefs = getSharedPreferences(C.PREFERENCES, MODE_PRIVATE);

		countDown1 = (CountDown) findViewById(R.id.countdown1);
		countDown2 = (CountDown) findViewById(R.id.countdown2);

		defineCountDown(countDown1, countDown2);
		defineCountDown(countDown2, countDown1);

		if (savedInstanceState != null) {
			countDown1.setTime((Integer) savedInstanceState.getSerializable("countDown1_Time"));
			countDown2.setTime((Integer) savedInstanceState.getSerializable("countDown2_Time"));
			countDown1.setViewStatus((Status) savedInstanceState.getSerializable("countDown1_Status"));
			countDown2.setViewStatus((Status) savedInstanceState.getSerializable("countDown2_Status"));
			if ((Status) savedInstanceState.getSerializable("countDown1_Status") == Status.ACTIVE)
				countDown1.start();
			if ((Status) savedInstanceState.getSerializable("countDown2_Status") == Status.ACTIVE)
				countDown2.start();
		} else {
			initializeCountDowns();
		}
	}

	private void initializeCountDowns() {
		countDown1.setTime(prefs.getInt(C.TIME_P1, C.TIME_DEFAULT));
		countDown2.setTime(prefs.getInt(C.TIME_P2, C.TIME_DEFAULT));
	}

	private void defineCountDown(final CountDown mainCountDown, final CountDown adverseCountDown) {
		// mainCountDown.setTotal(INITIAL_TIME);

		mainCountDown.setCountDownListener(new CountDownListener() {

			public void onClick(View view) {
				v.vibrate(100);
				mainCountDown.stop();
				adverseCountDown.start();
			}

			public void onFinish() {
				toast("Done");
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("countDown1_Time", countDown1.getTime());
		outState.putSerializable("countDown2_Time", countDown2.getTime());
		outState.putSerializable("countDown1_Status", countDown1.getViewStatus());
		outState.putSerializable("countDown2_Status", countDown2.getViewStatus());
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		pause();
		toast("Pause");
		return super.onMenuOpened(featureId, menu);
	}

	private void pause() {
		countDown1.pause();
		countDown2.pause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:
				startActivityForResult(new Intent(this, PreferencesActivity.class), ACTIVITY_PREFS);
				break;
			case R.id.reset:
				initializeCountDowns();
				break;
			case R.id.about:
				toast("www.crazy-apps.com");
				break;
			case R.id.exit:
				finish();
				break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case ACTIVITY_PREFS:
				initializeCountDowns();
				break;

		}
	}

	private void toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

}
