package com.example.syncbarrierdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.frank.newapplication.R;

/**
 * 一个禁止截屏的安全Activity，使用FLAG_SECURE标记。
 */
public class SecureActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置FLAG_SECURE，禁止截屏和录屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_secure);
    }
} 