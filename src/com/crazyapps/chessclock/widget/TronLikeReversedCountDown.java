/**
 * 
 */
package com.crazyapps.chessclock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * @author id0
 * 
 */
public class TronLikeReversedCountDown extends TronLikeCountDown {
	public TronLikeReversedCountDown(Context paramContext) {
		super(paramContext);
	}

	public TronLikeReversedCountDown(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	public TronLikeReversedCountDown(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
	}

	public void onDraw(Canvas canvas) {
		canvas.save();
		canvas.rotate(180.0F, getMeasuredWidth() / 2.0F, getMeasuredHeight() / 2.0F);
		super.onDraw(canvas);
		canvas.restore();
	}
}
