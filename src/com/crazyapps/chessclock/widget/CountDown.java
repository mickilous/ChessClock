package com.crazyapps.chessclock.widget;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

public class CountDown extends TextView {

	private long				timeTotal;
	protected long				timeIncrement;
	protected long				timeCredit;
	private boolean				appendTimeIncrement;
	private CountDownTimer		timer;
	private CountDownListener	listener;
	private final long			INTERVAL_TIME	= 1000;
	private final NumberFormat	formatter		= new DecimalFormat("##00");

	public enum Status {
		ACTIVE, INACTIVE;
	}

	protected Status	viewStatus	= Status.INACTIVE;

	public Status getViewStatus() {
		return viewStatus;
	}

	public void setViewStatus(Status viewStatus) {
		this.viewStatus = viewStatus;
	}

	public CountDown(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public CountDown(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CountDown(Context context) {
		this(context, null, 0);
	}

	protected void initView() {

		updateTextAttributes();
	}

	public void start() {
		System.out.println("start !!!!!");
		launchTimer();
		setStatusOn();
	}

	public void stop() {
		appendTimeIncrement();
		terminate();
	}

	private void appendTimeIncrement() {
		if (appendTimeIncrement) {
			System.out.println("Appending !!!!!!!!!!!!!!!!");
			timeTotal += timeCredit;
			timeCredit = 0;
			setText(formatTime(timeTotal));
		}
	}

	public void terminate() {
		cancelTimer();
		setStatusOff();
	}

	public void pause() {
		cancelTimer();
		setStatusPaused();
	}

	private void cancelTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

	private void launchTimer() {
		if (timeIncrement > 0) {
			timeCredit = timeIncrement;
			launchPreTimer();
		} else {
			launchMainTimer();
		}
	}

	private void launchPreTimer() {
		timer = new CountDownTimer(timeIncrement, 1) {

			@Override
			public void onTick(long millisUntilFinished) {
				timeCredit = millisUntilFinished;
				decrementPreTimer(millisUntilFinished);
			}

			@Override
			public void onFinish() {
				timeCredit = 0;
				launchMainTimer();
			}

		}.start();
	}

	protected void decrementPreTimer(long millisUntilFinished) {
		String time = formatTime(millisUntilFinished).toString();
		this.postInvalidate();
		System.out.println(time + " : tick !!!!!");
	}

	protected void launchMainTimer() {
		timer = new CountDownTimer(timeTotal, INTERVAL_TIME) {

			@Override
			public void onTick(long millisUntilFinished) {
				timeTotal = millisUntilFinished;
				decrementTimer(millisUntilFinished);
			}

			@Override
			public void onFinish() {
				setText(formatTime(0L));
				listener.onFinish();
			}
		}.start();
	}

	protected void decrementTimer(long millisUntilFinished) {
		String time = formatTime(millisUntilFinished).toString();
		setText(time);
		System.out.println(time + " : tack !!!!!");
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

	protected void setStatusOn() {
		viewStatus = Status.ACTIVE;
		updateTextAttributes();
		setClickable(true);
	}

	protected void setStatusOff() {
		viewStatus = Status.INACTIVE;
		updateTextAttributes();
		setClickable(false);
	}

	protected void setStatusPaused() {
		viewStatus = Status.INACTIVE;
		updateTextAttributes();
		setClickable(true);
	}

	protected void updateTextAttributes() {
		switch (viewStatus) {
			case ACTIVE:
				setTextSize(35f);
				setTextColor(Color.WHITE);
				setShadowLayer(12, 0, 0, Color.WHITE);
				break;
			case INACTIVE:
				setTextSize(25f);
				setTextColor(Color.GRAY);
				setShadowLayer(12, 0, 0, Color.GRAY);
				break;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public void setTime(int time) {
		timeTotal = milliSeconds(time);
		setText(formatTime(timeTotal));
	}

	public int getTime() {
		return seconds(timeTotal);
	}

	public int getTimeIncrement() {
		return seconds(timeIncrement);
	}

	public void setTimeIncrement(int preTime) {
		this.timeIncrement = milliSeconds(preTime);
	}

	public void setAppendTimeIncrement(boolean appendTimeIncrement) {
		this.appendTimeIncrement = appendTimeIncrement;
	}

	private int seconds(long milliSeconds) {
		return (int) (milliSeconds / 1000);
	}

	private long milliSeconds(int seconds) {
		return seconds * 1000;
	}

}
