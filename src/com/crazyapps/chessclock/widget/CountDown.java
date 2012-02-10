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

	protected long				total;
	protected final long		INTERVAL_TIME	= 1000;
	protected CountDownTimer	timer;
	protected CountDownListener	listener;
	protected NumberFormat		formatter		= new DecimalFormat("##00");

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
		super(context, attrs);
		initView();
	}

	public CountDown(Context context) {
		super(context);
		initView();
	}

	public void start() {
		launchTimer();
		setStatusOn();
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
			setStatusOff();
		}
	}

	public void pause() {
		if (timer != null) {
			timer.cancel();
			setStatusPaused();
		}
	}

	protected void launchTimer() {
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

	protected void initView() {
		updateTextAttributes();
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

	protected void setStatusOn() {
		this.viewStatus = Status.ACTIVE;
		updateTextAttributes();
		setClickable(true);
	}

	protected void setStatusOff() {
		setStatusInactive();
		updateTextAttributes();
		setClickable(false);
	}

	protected void setStatusPaused() {
		setStatusInactive();
		updateTextAttributes();
		setClickable(true);
	}

	protected void updateTextAttributes() {
		switch (this.viewStatus) {
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

	protected void setStatusInactive() {
		this.viewStatus = Status.INACTIVE;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	/**
	 * @return
	 */
	public int getTime() {
		return ((Long) total).intValue() / 1000;
	}
}
