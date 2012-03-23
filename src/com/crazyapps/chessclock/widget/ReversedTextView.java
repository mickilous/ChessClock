/**
 * 
 */
package com.crazyapps.chessclock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author id0
 * 
 */
public class ReversedTextView extends TextView {
	public ReversedTextView(Context paramContext) {
		super(paramContext);
	}

	public ReversedTextView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	public ReversedTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
	}

	public void onDraw(Canvas paramCanvas) {
		paramCanvas.save();
		paramCanvas.rotate(180.0F, getMeasuredWidth() / 2.0F, getMeasuredHeight() / 2.0F);
		super.onDraw(paramCanvas);
		paramCanvas.restore();
	}
}