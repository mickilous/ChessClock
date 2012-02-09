package com.crazyapps.chessclock;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

public class CountDown extends TextView {

	private long				total;
	private final long			INTERVAL_TIME	= 1000;

	private CountDownTimer		timer;

	private CountDownListener	listener;

	private NumberFormat		formatter		= new DecimalFormat("##00");

	public CountDown(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CountDown(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CountDown(Context context) {
		super(context);
	}

	public void start() {
		launchTimer();
	}

	public void pause() {
		if (timer != null)
			timer.cancel();
	}

	public void resume() {
		launchTimer();
	}

	private void launchTimer() {
		timer = new CountDownTimer(total, INTERVAL_TIME) {

			@Override
			public void onTick(long millisUntilFinished) {
				setText(formatTime(millisUntilFinished));
				total = millisUntilFinished;
			}

			@Override
			public void onFinish() {
				setText(formatTime(0L));
				listener.onFinish();
			}
		}.start();
	}

	protected CharSequence formatTime(long millisUntilFinished) {

		int h = (int) ((millisUntilFinished / 1000) / 3600);
		int m = (int) (((millisUntilFinished / 1000) / 60) % 60);
		int s = (int) ((millisUntilFinished / 1000) % 60);
		return String.format("%s:%s:%s", formatter.format(h), formatter.format(m), formatter.format(s));

	}

	public void setCountDownListener(CountDownListener listener) {
		super.setOnClickListener(listener);
		this.listener = listener;
	}

	public interface CountDownListener extends OnClickListener {
		public void onFinish();
	}

	public void setTime(int time) {
		total = time * 1000;
		setText(formatTime(total));
	}

}
