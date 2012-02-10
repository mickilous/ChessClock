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

/**
 * @author id0
 * 
 */
public class TronLikeCountDown extends CountDown {

	private int[]	activeGradientColors;
	private int[]	inactiveGradientColors;
	private float[]	gradientPositions;

	private Paint	circlePaint;

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

	private int[] getGradientColors() {
		if (viewStatus == Status.ACTIVE)
			return activeGradientColors;
		return inactiveGradientColors;
	}

	@Override
	protected void updateTextAttributes() {
		switch (this.viewStatus) {
			case ACTIVE:
				setTextSize(35f);
				setTextColor(Color.WHITE);
				setShadowLayer(12, 0, 0, Color.CYAN);
				break;
			case INACTIVE:
				setTextSize(25);
				setTextColor(Color.GRAY);
				setShadowLayer(12, 0, 0, Color.GRAY);
				break;
		}
	}
}
