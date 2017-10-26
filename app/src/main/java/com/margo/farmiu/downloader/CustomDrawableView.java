package com.margo.farmiu.downloader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by User on 011 11.10.17.
 */
public class CustomDrawableView extends View {
    private Paint mPaint;
    private float[] data = { 292.36545f};
    public CustomDrawableView(Context context, AttributeSet attributeSet) {
        super(context);

        int x = 10;
        int y = 10;
        int width = 300;
        int height = 50;

        mPaint = new Paint();
        mPaint.setColor(Color.RED);

    }

    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0, 0, 20, 20, mPaint);
        canvas.drawLine(20, 0, 0, 20, mPaint );
    }


}