package com.crazyapps.chessclock;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class CountDown extends TextView {

	private long				total;
	private final long			INTERVAL_TIME	= 1000;
	private CountDownTimer		timer;
	private CountDownListener	listener;
	private NumberFormat		formatter		= new DecimalFormat("##00");

	private enum Status {
		ACTIVE, INACTIVE;
	}

	private Status	viewStatus	= Status.INACTIVE;
	private int[]	activeGradientColors;
	private int[]	inactiveGradientColors;
	private float[]	gradientPositions;

	private Paint	circlePaint;

	public CountDown(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initCountDonwView();
	}

	public CountDown(Context context, AttributeSet attrs) {
		super(context, attrs);
		initCountDonwView();
	}

	public CountDown(Context context) {
		super(context);
		initCountDonwView();
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

	private void launchTimer() {
		timer = new CountDownTimer(total, INTERVAL_TIME) {

			@Override
			public void onTick(long millisUntilFinished) {
				setText(formatTime(millisUntilFinished));
				total = millisUntilFinished;
				Log.i("time", formatTime(millisUntilFinished).toString());
			}

			@Override
			public void onFinish() {
				setText(formatTime(0L));
				listener.onFinish();
			}
		}.start();
	}

	private void initCountDonwView() {
		setFocusable(true);
		// Récupère les ressources externes
		Resources r = this.getResources();

		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setColor(Color.BLACK);
		circlePaint.setStrokeWidth(1);
		circlePaint.setStyle(Paint.Style.STROKE);

		// create the tronlike gradient
		activeGradientColors = new int[4];
		inactiveGradientColors = new int[4];
		gradientPositions = new float[4];

		inactiveGradientColors[3] = Color.argb(0, 0, 255, 255);
		inactiveGradientColors[2] = Color.argb(100, 255, 255, 255);
		inactiveGradientColors[1] = Color.argb(60, 0, 255, 255);
		inactiveGradientColors[0] = Color.argb(0, 0, 255, 255);

		activeGradientColors[3] = Color.argb(0, 0, 255, 255);
		activeGradientColors[2] = Color.argb(255, 255, 255, 255);
		activeGradientColors[1] = Color.argb(123, 0, 255, 255);
		activeGradientColors[0] = Color.argb(0, 0, 255, 255);

		gradientPositions[3] = 1 - 0.0f;
		gradientPositions[2] = 1 - 0.1f;
		gradientPositions[1] = 1 - 0.15f;
		gradientPositions[0] = 1 - 0.3f;

		Typeface tf = Typeface.createFromAsset(r.getAssets(), "fonts/Neutronium.ttf");
		this.setTypeface(tf);

		setText("00:00:00");
		setTextSize(25);
		setTextColor(Color.GRAY);
		setShadowLayer(12, 0, 0, Color.GRAY);
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

	private int[] getGradientColors() {
		if (viewStatus == Status.ACTIVE)
			return activeGradientColors;
		return inactiveGradientColors;
	}

	private void setStatusOn() {
		this.viewStatus = Status.ACTIVE;
		setClickable(true);
		setTextSize(35f);
		setTextColor(Color.WHITE);
		setShadowLayer(12, 0, 0, Color.CYAN);
	}

	private void setStatusOff() {
		setStatusPaused();
		setClickable(false);
	}

	private void setStatusPaused() {
		setStatusInactive();
		setClickable(true);
	}

	private void setStatusInactive() {
		this.viewStatus = Status.INACTIVE;
		setTextSize(25);
		setTextColor(Color.GRAY);
		setShadowLayer(12, 0, 0, Color.GRAY);
	}

	// private void switchStatus() {
	// if (this.viewStatus == Status.ACTIVE) {
	// this.viewStatus = Status.INACTIVE;
	// setClickable(false);
	// setTextSize(25);
	// setTextColor(Color.GRAY);
	// setShadowLayer(12, 0, 0, Color.GRAY);
	// } else {
	// this.viewStatus = Status.ACTIVE;
	// setClickable(true);
	// setTextSize(35f);
	// setTextColor(Color.WHITE);
	// setShadowLayer(12, 0, 0, Color.CYAN);
	// }
	// }

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// get the view size
		int height = getMeasuredHeight();
		int width = getMeasuredWidth();

		// calculate center of view
		int px = width / 2;
		int py = height / 2;
		Point center = new Point(px, py);

		int radius = Math.min(px, py) - 2;

		RectF boundingBox = new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius);

		RadialGradient activeGradient = new RadialGradient(px, py, radius, getGradientColors(), gradientPositions,
				TileMode.CLAMP);

		Paint pgb = new Paint();
		pgb.setShader(activeGradient);

		Path outerRingPath = new Path();
		outerRingPath.addOval(boundingBox, Direction.CW);

		canvas.drawPath(outerRingPath, pgb);

		canvas.restore();

		canvas.save();
	}
}
