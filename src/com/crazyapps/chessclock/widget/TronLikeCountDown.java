/**
 * 
 */
package com.crazyapps.chessclock.widget;

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
import android.util.AttributeSet;

import com.crazyapps.chessclock.R;

/**
 * @author id0
 * 
 */
public class TronLikeCountDown extends CountDown {

	private int[]			activeGradientColors;
	private int[]			inactiveGradientColors;
	private float[]			gradientPositions;

	private Paint			circlePaint;
	private Paint			pgb;
	private Paint			timerPaint;

	private Paint			pulsationPaint;

	private Paint			backgroundPaint;
	private Paint			bonusText;

	private Path			outerRingPath;

	private RectF			boundingBox;
	private RectF			preTimerBox;

	private RadialGradient	gradient;

	public TronLikeCountDown(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public TronLikeCountDown(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public TronLikeCountDown(Context context) {
		super(context);
		initView();
	}

	@Override
	protected void initView() {
		setFocusable(true);
		// Récupère les ressources externes
		Resources r = this.getResources();

		Typeface tf = Typeface.createFromAsset(r.getAssets(), "fonts/Neutronium.ttf");
		this.setTypeface(tf);

		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setColor(Color.BLACK);
		circlePaint.setStrokeWidth(1);
		circlePaint.setStyle(Paint.Style.STROKE);

		timerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		timerPaint.setColor(Color.WHITE);
		timerPaint.setStyle(Paint.Style.STROKE);
		timerPaint.setStrokeWidth(2);

		pulsationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		pulsationPaint.setColor(Color.WHITE);
		pulsationPaint.setStyle(Paint.Style.STROKE);
		pulsationPaint.setStrokeWidth(4);

		backgroundPaint = new Paint();
		backgroundPaint.setARGB(120, 0, 0, 0);

		pgb = new Paint();

		bonusText = new Paint();
		bonusText.setTypeface(tf);
		bonusText.setTextSize(getResources().getDimensionPixelSize(R.dimen.incrementTimerTextSize));
		bonusText.setColor(Color.WHITE);
		bonusText.setShadowLayer(12, 0, 0, Color.CYAN);

		outerRingPath = new Path();

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

		setText("00:00:00");
		updateTextAttributes();
	}

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

		boundingBox = new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius);

		gradient = new RadialGradient(px, py, radius, getGradientColors(), gradientPositions, TileMode.CLAMP);

		pgb.setShader(gradient);

		outerRingPath.addOval(boundingBox, Direction.CW);

		canvas.drawPath(outerRingPath, pgb);

		radius = radius - radius / 4;
		preTimerBox = new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius);

		if (timeIncrement > 0 && timeCredit > 0) {
			float angle = getAngle(timeIncrement, timeCredit);
			canvas.drawArc(preTimerBox, -90 + angle / 2, angle, false, timerPaint);
			bonusText.setColor(getAlphaBasedOnTime(Color.WHITE));
			bonusText.setShadowLayer(12, 0, 0, getAlphaBasedOnTime(Color.CYAN));
			canvas.drawText(getTimeWithSeparator(timeCredit), center.x, center.y + getTextSize() + 3, bonusText);
		} else {
			if (viewStatus.equals(Status.ACTIVE)) {
				canvas.drawArc(preTimerBox, 90 + getAngle(baseTime, timeTotal) / 2, getAngle(baseTime, timeTotal),
						false, timerPaint);
			}
		}
		canvas.save();
	}

	private String getTimeWithSeparator(long time) {
		return (time / 1000) + "." + time % 1000;
	}

	private float getAngle(long a, long b) {
		float fa = (float) a;
		float fb = (float) b;
		return -360 / (fa / fb);
	}

	private int getAlphaBasedOnTime(int c) {
		int r = Color.red(c);
		int g = Color.green(c);
		int b = Color.blue(c);

		return Color.argb((int) (((float) timeCredit / (float) timeIncrement) * (float) 255), r, g, b);
	}

	private int[] getGradientColors() {
		if (viewStatus == Status.ACTIVE)
			return activeGradientColors;
		return inactiveGradientColors;
	}

	@Override
	protected void updateTextAttributes() {
		switch (this.viewStatus) {
			case ACTIVE:
				setTextSize(getResources().getDimensionPixelSize(R.dimen.timerTextSize_Active));
				setTextColor(Color.WHITE);
				setShadowLayer(12, 0, 0, Color.CYAN);
				break;
			case INACTIVE:
				setTextSize(getResources().getDimensionPixelSize(R.dimen.timerTextSize_Inactive));
				setTextColor(Color.GRAY);
				setShadowLayer(12, 0, 0, Color.GRAY);
				break;
		}
	}
}
