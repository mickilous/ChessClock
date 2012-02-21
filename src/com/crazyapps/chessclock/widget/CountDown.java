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
	private long				timeIncrement;
	private long				timeCredit;
	private boolean				appendTimeIncrement;

	private CountDownTimer		timer;
	private CountDownListener	listener;

	protected Status			viewStatus		= Status.INACTIVE;

	private final long			INTERVAL_TIME	= 1000;
	private final NumberFormat	formatter		= new DecimalFormat("##00");

	public enum Status {
		ACTIVE, INACTIVE;
	}

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

	public void pause() {
		cancelTimer();
		setStatusPaused();
	}

	public void stop() {
		appendTimeIncrement();
		terminate();
	}

	public void terminate() {
		cancelTimer();
		setStatusOff();
	}

	private void appendTimeIncrement() {
		if (appendTimeIncrement) {
			System.out.println("Appending !!!!!!!!!!!!!!!!");
			timeTotal += timeCredit;
			setText(formatTime(timeTotal));
		}
	}

	private void cancelTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

	private void launchTimer() {
		if (timeIncrement > 0)
			launchPreTimer();
		else
			launchMainTimer();
	}

	private void launchPreTimer() {
		timer = new CountDownTimer(timeIncrement, INTERVAL_TIME) {

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
				timeTotal = 0L;
				decrementTimer(timeTotal);
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
		if (viewStatus != Status.INACTIVE) {
			viewStatus = Status.INACTIVE;
			updateTextAttributes();
			setClickable(true);
		}
	}

	protected void updateTextAttributes() {
		switch (viewStatus) {
			case ACTIVE:
				setTextSize(35f);
				setTextColor(Color.WHITE);
				setShadowLayer(12, 0, 0, Color.WHITE);
				break;
			case INACTIVE:
				setTextSize(25);
				setTextColor(Color.GRAY);
				setShadowLayer(12, 0, 0, Color.GRAY);
				break;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public void setTime(long time) {
		timeTotal = time;
		setText(formatTime(timeTotal));
	}

	public long getTime() {
		return timeTotal;
	}

	public void setTimeIncrement(long timeIncrement) {
		this.timeIncrement = timeIncrement;
	}

	public void setAppendTimeIncrement(boolean appendTimeIncrement) {
		this.appendTimeIncrement = appendTimeIncrement;
	}
}
