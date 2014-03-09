package com.hiesenbery.musicplayerproject;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class TimeView extends SurfaceView {

	public TimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public void onDraw(Canvas canvas) {
	}
	
}
