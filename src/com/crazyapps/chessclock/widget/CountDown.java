package com.crazyapps.chessclock.widget;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazyapps.chessclock.R;
import com.crazyapps.chessclock.util.Xlog;

public class CountDown extends RelativeLayout {

	protected long				baseTime;
	protected long				timeTotal;

	protected long				timeIncrement;
	protected long				timeCredit;
	private boolean				isAppendIncrementToTotal;

	private CountDownTimer		timer;
	private CountDownListener	listener;

	protected Status			viewStatus		= Status.INACTIVE;

	private final long			INTERVAL_TIME	= 1;
	private final NumberFormat	formatter		= new DecimalFormat("##00");

	protected TextView			timerView;
	protected TextView			incrementTimerView;

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

		this.setWillNotDraw(false);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflateView(inflater);

		initView();
	}

	/**
	 * @param inflater
	 */
	protected void inflateView(LayoutInflater inflater) {
		inflater.inflate(R.layout.countdown, this, true);

		timerView = (TextView) findViewById(R.id.timer);
		incrementTimerView = (TextView) findViewById(R.id.incrementtimer);
		refreshTimer();
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
		Xlog.debug("Start Countdown");
		launchTimer();
		setStatusOn();
	}

	public void pause() {
		Xlog.debug("Pause Countdown");
		cancelTimer();
		setStatusPaused();
	}

	public void stop() {
		Xlog.debug("Stop Countdown");
		appendAndResetTimeIncrement();
		cancelTimer();
		setStatusOff();
	}

	private void appendAndResetTimeIncrement() {
		if (isAppendIncrementToTotal) {
			Xlog.debug("Appending time : %d", timeCredit);
			timeTotal += timeCredit;
			this.postInvalidate();
			refreshTimer();
		}
		timeCredit = 0;
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
		if (timeCredit == 0)
			timeCredit = timeIncrement;

		timer = new CountDownTimer(timeCredit, INTERVAL_TIME) {
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
		this.postInvalidate();
		refreshTimer();
		Xlog.debug("Tick : %s", formatTime(millisUntilFinished).toString());
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
		this.postInvalidate();
		refreshTimer();
		Xlog.debug("Tack : %s", formatTime(millisUntilFinished).toString());
	}

	protected CharSequence formatTime(long millisUntilFinished) {
		int h = (int) ((millisUntilFinished / 1000) / 3600);
		int m = (int) (((millisUntilFinished / 1000) / 60) % 60);
		int s = (int) ((millisUntilFinished / 1000) % 60);
		return formatter.format(h) + ":" + formatter.format(m) + ":" + formatter.format(s);
		// return String.format("%s:%s:%s", formatter.format(h), formatter.format(m), formatter.format(s));
	}

	public void setCountDownListener(CountDownListener listener) {
		super.setOnClickListener(listener);
		this.listener = listener;
	}

	public interface CountDownListener extends OnClickListener {
		public void onFinish();
	}

	private void refreshTimer() {
		timerView.setText(formatTime(timeTotal));
		incrementTimerView.setText((timeCredit > 0) ? getMsWithSeparator(timeCredit) : getMsAsText(timeTotal % 1000));
	}

	protected void setStatusOn() {
		viewStatus = Status.ACTIVE;
		incrementTimerView.setVisibility(VISIBLE);
		updateTextAttributes();
		setClickable(true);
	}

	protected void setStatusOff() {
		viewStatus = Status.INACTIVE;
		incrementTimerView.setVisibility(INVISIBLE);
		updateTextAttributes();
		setClickable(false);
	}

	protected void setStatusPaused() {
		viewStatus = Status.INACTIVE;
		incrementTimerView.setVisibility(INVISIBLE);
		updateTextAttributes();
		setClickable(true);
	}

	protected void updateTextAttributes() {
		switch (viewStatus) {
			case ACTIVE:
				timerView.setTextSize(getResources().getDimensionPixelSize(R.dimen.timerTextSize_Active));
				timerView.setTextColor(Color.WHITE);
				timerView.setShadowLayer(12, 0, 0, Color.WHITE);
				incrementTimerView.setTextSize(getResources().getDimensionPixelSize(R.dimen.incrementTimerTextSize));
				incrementTimerView.setTextColor(Color.WHITE);
				incrementTimerView.setShadowLayer(12, 0, 0, Color.WHITE);
				break;
			case INACTIVE:
				timerView.setTextSize(getResources().getDimensionPixelSize(R.dimen.timerTextSize_Inactive));
				timerView.setTextColor(Color.GRAY);
				timerView.setShadowLayer(12, 0, 0, Color.GRAY);
				break;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public void setTime(long time) {
		timeTotal = time;
		baseTime = time;
		this.postInvalidate();
		refreshTimer();
	}

	private String getMsAsText(long ms) {
		if (ms < 10)
			return ".00" + ms;
		if (ms < 100)
			return ".0" + ms;
		return "." + String.valueOf(ms);
	}

	private String getMsWithSeparator(long time) {
		if (time < 1000)
			return "0" + getMsAsText(time);
		return time / 1000 + "." + time % 1000;
	}

	public long getTime() {
		return timeTotal;
	}

	public long getTimeIncrement() {
		return timeIncrement;
	}

	public void setTimeIncrement(long timeIncrement) {
		this.timeIncrement = timeIncrement;
	}

	public void setAppendTimeIncrement(boolean appendTimeIncrement) {
		this.isAppendIncrementToTotal = appendTimeIncrement;
	}

	public long getTimeCredit() {
		return timeCredit;
	}

	public void setTimeCredit(long timeCredit) {
		this.timeCredit = timeCredit;
	}
}
