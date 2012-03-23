/**
 * 
 */
package com.crazyapps.chessclock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.crazyapps.chessclock.R;

/**
 * @author id0
 * 
 */
public class ReversedCountDown extends CountDown {
	public ReversedCountDown(Context paramContext) {
		super(paramContext);
	}

	public ReversedCountDown(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	public ReversedCountDown(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
	}

	/**
	 * @param inflater
	 */
	protected void inflateView(LayoutInflater inflater) {
		inflater.inflate(R.layout.reversed_countdown, this, true);

		timerView = (TextView) findViewById(R.id.timer);
		timerView.setText(formatTime(timeTotal));
		incrementTimerView = (TextView) findViewById(R.id.incrementtimer);
	}

	public void onDraw(Canvas canvas) {
		canvas.save();
		canvas.rotate(180.0F, (getLeft() + getRight()) / 2.0F, (getTop() + getBottom()) / 2.0F);
		super.onDraw(canvas);
		canvas.restore();
	}
}
