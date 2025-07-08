package com.frank.newapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CanvasScreenshotActivity extends AppCompatActivity {
    private ScreenshotView screenshotView;
    private ImageView resultView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_screenshot);
        screenshotView = findViewById(R.id.screenshot_view);
        resultView = findViewById(R.id.screenshot_result);
        Button btnShot = findViewById(R.id.btn_screenshot);
        btnShot.setOnClickListener(v -> doScreenshot());
    }

    private void doScreenshot() {
        Bitmap bmp = Bitmap.createBitmap(screenshotView.getWidth(), screenshotView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        screenshotView.draw(canvas);
        resultView.setImageBitmap(bmp);
    }
} 