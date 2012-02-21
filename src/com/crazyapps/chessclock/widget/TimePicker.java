package com.crazyapps.chessclock.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.crazyapps.chessclock.R;

public class TimePicker extends LinearLayout {

	private NumberPicker	hoursPicker;
	private NumberPicker	minutesPicker;
	private NumberPicker	secondsPicker;

	public TimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.time_picker, this, true);

		hoursPicker = (NumberPicker) findViewById(R.id.hours);
		minutesPicker = (NumberPicker) findViewById(R.id.minutes);
		secondsPicker = (NumberPicker) findViewById(R.id.seconds);

		hoursPicker.setRange(0, 23);
		minutesPicker.setRange(0, 59);
		secondsPicker.setRange(0, 59);
	}

	public TimePicker(Context context) {
		this(context, null);
	}

	public long getTime() {
		return (hoursPicker.getCurrent() * 3600 + minutesPicker.getCurrent() * 60 + secondsPicker.getCurrent()) * 1000;
	}

	public void setTime(long time) {
		int timeInSeconds = (int) (time / 1000);
		hoursPicker.setCurrent(timeInSeconds / 3600);
		minutesPicker.setCurrent((timeInSeconds / 60) % 60);
		secondsPicker.setCurrent(timeInSeconds % 60);
	}
}
