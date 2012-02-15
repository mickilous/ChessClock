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

	private long				totalTime;
	private long				preTime;
	private boolean				appendPreTime;
	private CountDownTimer		preTimer;
	private CountDownTimer		timer;
	private CountDownListener	listener;
	private final long			INTERVAL_TIME	= 1000;
	private final NumberFormat	formatter		= new DecimalFormat("##00");

	public enum Status {
		ACTIVE, INACTIVE;
	}

	protected Status	viewStatus	= Status.INACTIVE;

	/**
	 * @return the viewStatus
	 */
	public Status getViewStatus() {
		return viewStatus;
	}

	/**
	 * @param viewStatus
	 *            the viewStatus to set
	 */
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
		if (preTime > 0)
			launchPreTimer();
		else
			launchMainTimer();

	}

	private void launchPreTimer() {
		timer = new CountDownTimer(preTime, INTERVAL_TIME) {

			@Override
			public void onTick(long millisUntilFinished) {
				String time = formatTime(millisUntilFinished).toString();
				System.out.println(time + " : tick !!!!!");
				preTime = millisUntilFinished;
			}

			@Override
			public void onFinish() {
				launchMainTimer();
			}

		}.start();
	}

	protected void launchMainTimer() {
		timer = new CountDownTimer(totalTime, INTERVAL_TIME) {

			@Override
			public void onTick(long millisUntilFinished) {
				String time = formatTime(millisUntilFinished).toString();
				System.out.println(time + " : tack !!!!!");
				setText(time);
				totalTime = millisUntilFinished;
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

	public void setTime(int time) {
		totalTime = milliSeconds(time);
		setText(formatTime(totalTime));
	}

	public int getTime() {
		return seconds(totalTime);
	}

	public int getPreTime() {
		return seconds(preTime);
	}

	public void setPreTime(int preTime) {
		this.preTime = milliSeconds(preTime);
	}

	public void setAppendPreTime(boolean appendPreTime) {
		this.appendPreTime = appendPreTime;
	}

	private int seconds(long milliSeconds) {
		return (int) (milliSeconds / 1000);
	}

	private long milliSeconds(int seconds) {
		return seconds * 1000;
	}

}
