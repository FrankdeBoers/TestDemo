package com.frank.newapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.reflect.Field;
import java.util.Map;
import android.os.IBinder;

public class ServiceManagerCacheActivity extends AppCompatActivity {
    private TextView logView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_manager_cache);
        logView = findViewById(R.id.smc_log);
        Button btnRefresh = findViewById(R.id.smc_refresh);
        btnRefresh.setOnClickListener(v -> showCache());
        showCache();
    }

    private void showCache() {
        StringBuilder sb = new StringBuilder();
        try {
            Class<?> serviceManagerCls = Class.forName("android.os.ServiceManager");
            Field cacheField = serviceManagerCls.getDeclaredField("sCache");
            cacheField.setAccessible(true);
            Map<String, IBinder> cache = (Map<String, IBinder>) cacheField.get(null);
            sb.append("ServiceManager.sCache 内容如下:\n");
            for (Map.Entry<String, IBinder> entry : cache.entrySet()) {
                sb.append(entry.getKey()).append(" => ").append(entry.getValue()).append("\n");
            }
        } catch (Throwable e) {
            sb.append("读取失败: ").append(e.getMessage());
        }
        logView.setText(sb.toString());
    }
} 