package com.crazyapps.chessclock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.crazyapps.chessclock.manager.Notifier;
import com.crazyapps.chessclock.widget.CountDown;
import com.crazyapps.chessclock.widget.CountDown.CountDownListener;
import com.crazyapps.chessclock.widget.CountDown.Status;

public class ChessClockActivity extends Activity {

	private static final int	ACTIVITY_PREFS	= 1;
	private final Notifier		notifier		= new Notifier(this);

	private CountDown			countDown1;
	private CountDown			countDown2;

	private SharedPreferences	prefs;
	private WakeLock			wakeLock;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"DoNotDimScreen");

		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		prefs = getSharedPreferences(C.prefs.PREFERENCES, MODE_PRIVATE);

		defineCountDowns(savedInstanceState);

		defineNotifier();
	}

	private void defineCountDowns(Bundle savedInstanceState) {
		countDown1 = (CountDown) findViewById(R.id.countdown1);
		countDown2 = (CountDown) findViewById(R.id.countdown2);

		defineCountDownBehavior(countDown1, countDown2);
		defineCountDownBehavior(countDown2, countDown1);

		if (savedInstanceState != null) {
			restoreCountDownsState(savedInstanceState);
		} else {
			defineCountDownsTime();
		}
	}

	private void defineCountDownBehavior(final CountDown mainCountDown, final CountDown adverseCountDown) {

		mainCountDown.setCountDownListener(new CountDownListener() {

			public void onClick(View view) {
				switchPlayer(mainCountDown, adverseCountDown);
			}

			public void onFinish() {
				System.out.println("Game Over !!!!!");
				notifier.gameOver();
			}

		});
	}

	private void defineCountDownsTime() {
		boolean appendTimeIncrement = isAppendTimeIncrement();
		countDown1.setTime(prefs.getInt(C.prefs.TIME_P1, C.prefs.TIME_DEFAULT));
		countDown1.setTimeIncrement(prefs.getInt(C.prefs.MODE_TIME, 0));
		countDown1.setAppendTimeIncrement(appendTimeIncrement);
		countDown2.setTime(prefs.getInt(C.prefs.TIME_P2, C.prefs.TIME_DEFAULT));
		countDown2.setTimeIncrement(prefs.getInt(C.prefs.MODE_TIME, 0));
		countDown2.setAppendTimeIncrement(appendTimeIncrement);
	}

	private boolean isAppendTimeIncrement() {
		return (prefs.getInt(C.prefs.MODE, 0) == C.MODE_FISHER) ? true : false;
	}

	private void switchPlayer(final CountDown mainCountDown, final CountDown adverseCountDown) {
		notifier.click();
		mainCountDown.stop();
		adverseCountDown.start();
	}

	private void pause() {
		countDown1.pause();
		countDown2.pause();
	}

	private void defineNotifier() {
		notifier.setSoundOnClick(prefs.getBoolean(C.prefs.SOUNDS_ON_CLICK, true));
		notifier.setVibrateOnClick(prefs.getBoolean(C.prefs.VIBRATE_ON_CLICK, true));
		notifier.setSoundOnGameOver(prefs.getBoolean(C.prefs.SOUNDS_ON_GAMEOVER, true));
		notifier.setVibrateOnGameOver(prefs.getBoolean(C.prefs.VIBRATE_ON_CLICK, true));
	}

	private void saveCountDownsState(Bundle outState) {
		outState.putSerializable(C.prefs.TIME_P1, countDown1.getTime());
		outState.putSerializable(C.prefs.TIME_P2, countDown2.getTime());
		outState.putSerializable(C.prefs.STATUS_P1, countDown1.getViewStatus());
		outState.putSerializable(C.prefs.STATUS_P2, countDown2.getViewStatus());
		countDown1.terminate();
		countDown2.terminate();
	}

	private void restoreCountDownsState(Bundle savedInstanceState) {
		countDown1.setTime((Integer) savedInstanceState.getSerializable(C.prefs.TIME_P1));
		countDown2.setTime((Integer) savedInstanceState.getSerializable(C.prefs.TIME_P2));
		countDown1.setViewStatus((Status) savedInstanceState.getSerializable(C.prefs.STATUS_P1));
		countDown2.setViewStatus((Status) savedInstanceState.getSerializable(C.prefs.STATUS_P2));
		CountDown activeCountDown = countDown1.getViewStatus() == Status.ACTIVE ? countDown1 : countDown2;
		activeCountDown.start();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		saveCountDownsState(outState);
	}

	@Override
	protected void onPause() {
		notifier.release();
		wakeLock.release();
		pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		notifier.aquire();
		wakeLock.acquire();
		super.onResume();
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		pause();
		toast("Pause");
		return super.onMenuOpened(featureId, menu);
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
				defineCountDownsTime();
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
				defineCountDownsTime();
				defineNotifier();
				break;

		}
	}

	private void toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

}
