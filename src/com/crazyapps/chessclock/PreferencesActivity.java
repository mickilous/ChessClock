package com.crazyapps.chessclock;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

import com.crazyapps.chessclock.widget.TimePicker;

public class PreferencesActivity extends Activity {

	private SharedPreferences	prefs;
	private TimePicker			timeP1;
	private TimePicker			timeP2;
	private CheckBox			isTimeEquals;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);

		prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);

		defineButton();

		defineCheckBox();

		defineTimePicker1();
		defineTimePicker2();

	}

	private void defineTimePicker1() {
		timeP1 = initializeTimePicker(R.id.pref_time_p1, Constants.TIME_P1);
	}

	private void defineTimePicker2() {
		timeP2 = initializeTimePicker(R.id.pref_time_p2, Constants.TIME_P2);
		timeP2.setVisibility(visibilityIntValue(!isTimeEquals.isChecked()));
	}

	private TimePicker initializeTimePicker(int id, String pref) {
		TimePicker picker = (TimePicker) findViewById(id);
		picker.setTime(prefs.getInt(pref, Constants.TIME_DEFAULT));
		return picker;
	}

	private void defineCheckBox() {
		isTimeEquals = (CheckBox) findViewById(R.id.pref_time_equals);
		isTimeEquals.setChecked(prefs.getBoolean(Constants.TIME_EQUALS, Constants.TIME_EQUALS_DEFAULT));
		isTimeEquals.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				timeP2.setVisibility(visibilityIntValue(!isTimeEquals.isChecked()));
			}
		});
	}

	private void defineButton() {

		((Button) findViewById(R.id.pref_time_done)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Editor editor = prefs.edit();
				editor.putInt(Constants.TIME_P1, timeP1.getTime());
				if (isTimeEquals.isChecked())
					editor.putInt(Constants.TIME_P2, timeP1.getTime());
				else
					editor.putInt(Constants.TIME_P2, timeP2.getTime());
				editor.putBoolean(Constants.TIME_EQUALS, isTimeEquals.isChecked());
				editor.commit();
				finish();
			}
		});
	}

	private int visibilityIntValue(boolean visible) {
		if (visible)
			return View.VISIBLE;
		else
			return View.INVISIBLE;
	}
}
