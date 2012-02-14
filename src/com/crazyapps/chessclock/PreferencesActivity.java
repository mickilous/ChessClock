package com.crazyapps.chessclock;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TabHost;

import com.crazyapps.chessclock.widget.TimePicker;

public class PreferencesActivity extends Activity {

	private SharedPreferences	prefs;
	private TimePicker			timeP1;
	private TimePicker			timeP2;
	private CheckBox			isTimeEquals;
	private CheckBox			isSoundOnClick;
	private CheckBox			isVibrateOnClick;
	private CheckBox			isSoundOnGameOver;
	private CheckBox			isVibrateOnGameOver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);

		prefs = getSharedPreferences(C.PREFERENCES, MODE_PRIVATE);

		defineTabs();

		defineButton();

		defineTimeCheckBox();

		defineTimePicker1();
		defineTimePicker2();

		defineSoundsCheckBoxes();

	}

	private void defineSoundsCheckBoxes() {

		isSoundOnClick = (CheckBox) findViewById(R.id.pref_sound_onclick);
		isVibrateOnClick = (CheckBox) findViewById(R.id.pref_vibrate_onclick);
		isSoundOnGameOver = (CheckBox) findViewById(R.id.pref_sound_ongameover);
		isVibrateOnGameOver = (CheckBox) findViewById(R.id.pref_vibrate_ongameover);

		isSoundOnClick.setChecked(prefs.getBoolean(C.SOUNDS_ON_CLICK, true));
		isVibrateOnClick.setChecked(prefs.getBoolean(C.VIBRATE_ON_CLICK, true));
		isSoundOnGameOver.setChecked(prefs.getBoolean(C.SOUNDS_ON_GAMEOVER, true));
		isVibrateOnGameOver.setChecked(prefs.getBoolean(C.VIBRATE_ON_CLICK, true));

	}

	private void defineTimePicker1() {
		timeP1 = defineTimePicker(R.id.pref_time_p1, C.TIME_P1);
	}

	private void defineTimePicker2() {
		timeP2 = defineTimePicker(R.id.pref_time_p2, C.TIME_P2);
		timeP2.setVisibility(visibilityIntValue(!isTimeEquals.isChecked()));
	}

	private TimePicker defineTimePicker(int id, String pref) {
		TimePicker picker = (TimePicker) findViewById(id);
		picker.setTime(prefs.getInt(pref, C.TIME_DEFAULT));
		return picker;
	}

	private void defineTimeCheckBox() {
		isTimeEquals = (CheckBox) findViewById(R.id.pref_time_equals);
		isTimeEquals.setChecked(prefs.getBoolean(C.TIME_EQUALS, C.TIME_EQUALS_DEFAULT));
		isTimeEquals.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				timeP2.setVisibility(visibilityIntValue(!isTimeEquals.isChecked()));
			}
		});
	}

	private void defineButton() {

		((Button) findViewById(R.id.pref_time_done)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				save();
				finish();
			}

		});
	}

	private void save() {
		Editor editor = prefs.edit();
		saveTime(editor);
		saveSounds(editor);
		editor.commit();
	}

	private void saveTime(Editor editor) {
		editor.putInt(C.TIME_P1, timeP1.getTime());
		editor.putInt(C.TIME_P2, isTimeEquals.isChecked() ? timeP1.getTime() : timeP2.getTime());
		editor.putBoolean(C.TIME_EQUALS, isTimeEquals.isChecked());
	}

	private void saveSounds(Editor editor) {
		editor.putBoolean(C.SOUNDS_ON_CLICK, isSoundOnClick.isChecked());
		editor.putBoolean(C.VIBRATE_ON_CLICK, isVibrateOnClick.isChecked());
		editor.putBoolean(C.SOUNDS_ON_GAMEOVER, isSoundOnGameOver.isChecked());
		editor.putBoolean(C.VIBRATE_ON_CLICK, isVibrateOnGameOver.isChecked());
	}

	private void defineTabs() {
		TabHost tabHost = (TabHost) findViewById(R.id.pref_tab_host);
		tabHost.setup();

		tabHost.addTab(tabHost.newTabSpec("Tralala_Time").setIndicator(getString(R.string.pref_tab_time))
				.setContent(R.id.pref_tab_1));

		tabHost.addTab(tabHost.newTabSpec("Tralala_Sounds").setIndicator(getString(R.string.pref_tab_sounds))
				.setContent(R.id.pref_tab_2));

	}

	private int visibilityIntValue(boolean visible) {
		if (visible)
			return View.VISIBLE;
		else
			return View.INVISIBLE;
	}
}
