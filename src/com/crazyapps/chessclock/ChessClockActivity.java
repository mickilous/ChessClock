package com.crazyapps.chessclock;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.crazyapps.chessclock.CountDown.CountDownListener;

public class ChessClockActivity extends Activity {

	private static final int	ACTIVITY_PREFS	= 1;
	private static final int	MENU_SETTINGS	= 1;
	private static final int	MENU_RESET		= 2;
	private static final int	MENU_ABOUT		= 3;
	private static final int	MENU_EXIT		= 4;

	private CountDown			countDown1;
	private CountDown			countDown2;
	private SharedPreferences	prefs;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);

		countDown1 = (CountDown) findViewById(R.id.countdown1);
		countDown2 = (CountDown) findViewById(R.id.countdown2);

		defineCountDown(countDown1, countDown2);
		defineCountDown(countDown2, countDown1);

		initializeCountDowns();

		defineButton();
	}

	private void initializeCountDowns() {
		countDown1.pause();
		countDown2.pause();
		countDown1.setTime(prefs.getInt(Constants.TIME_P1, Constants.TIME_DEFAULT));
		countDown2.setTime(prefs.getInt(Constants.TIME_P2, Constants.TIME_DEFAULT));
	}

	private void defineCountDown(final CountDown mainCountDown, final CountDown adverseCountDown) {
		// mainCountDown.setTotal(INITIAL_TIME);

		mainCountDown.setCountDownListener(new CountDownListener() {

			public void onClick(View v) {
				mainCountDown.pause();
				adverseCountDown.start();
			}

			public void onFinish() {
				toast("Done");
			}
		});
	}

	private void defineButton() {
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				toast("Todo");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_SETTINGS, Menu.NONE, R.string.settings);
		menu.add(0, MENU_RESET, Menu.NONE, R.string.reset);
		menu.add(0, MENU_ABOUT, Menu.NONE, R.string.about);
		menu.add(0, MENU_EXIT, Menu.NONE, R.string.exit);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_SETTINGS:
				// toast("Settings");
				startActivityForResult(new Intent(this, PreferencesActivity.class), ACTIVITY_PREFS);
				break;
			case MENU_RESET:
				toast("Reset");
				break;
			case MENU_ABOUT:
				toast("www.crazy-apps.com");
				break;
			case MENU_EXIT:
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