package com.frank.newapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ScreenshotView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap leftImage;
    private String topText = "上面一行文字";
    private String bottomText = "下面一行文字";

    public ScreenshotView(Context context) {
        super(context);
        init(context);
    }
    public ScreenshotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        leftImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        int padding = 32;
        int imgSize = 120;
        int width = getWidth();
        int height = getHeight();
        // 左侧图片
        int imgLeft = padding;
        int imgTop = (height - imgSize) / 2;
//        canvas.drawBitmap(leftImage, null, new android.graphics.Rect(imgLeft, imgTop, imgLeft + imgSize, imgTop + imgSize), paint);
        // 右侧区域
        int rightStart = imgLeft + imgSize + padding;
        int rightWidth = width - rightStart - padding;
        int centerY = height / 2;
        // 上面一行文字（大号）
        paint.setColor(Color.BLACK);
        paint.setTextSize(60); // 约20sp
        paint.setFakeBoldText(true);
        Paint.FontMetrics fmTop = paint.getFontMetrics();
        float topTextY = centerY - 10;
        canvas.drawText(topText, rightStart, topTextY, paint);
        // 下面一行文字（小号）
        paint.setTextSize(30); // 约10sp
        paint.setFakeBoldText(false);
        Paint.FontMetrics fmBottom = paint.getFontMetrics();
        float bottomTextY = topTextY + Math.abs(fmTop.bottom) + Math.abs(fmBottom.top) + 20;
        paint.setColor(Color.DKGRAY);
        canvas.drawText(bottomText, rightStart, bottomTextY, paint);
    }
}