package com.frank.newapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ThreadStateActivity extends AppCompatActivity {
    private TextView logView;
    private TextView stateView;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Thread demoThread;
    private final Object lock = new Object();
    private final Object blockLock = new Object();
    private Thread blockThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_state);
        logView = findViewById(R.id.thread_state_log);
        stateView = findViewById(R.id.tv_current_state);
        Button btnNew = findViewById(R.id.btn_new);
        Button btnRunnable = findViewById(R.id.btn_runnable);
        Button btnBlocked = findViewById(R.id.btn_blocked);
        Button btnWaiting = findViewById(R.id.btn_waiting);
        Button btnTimedWaiting = findViewById(R.id.btn_timed_waiting);
        Button btnTerminated = findViewById(R.id.btn_terminated);
        Button btnReset = findViewById(R.id.btn_reset);

        btnNew.setOnClickListener(v -> showNewState());
        btnRunnable.setOnClickListener(v -> showRunnableState());
        btnBlocked.setOnClickListener(v -> showBlockedState());
        btnWaiting.setOnClickListener(v -> showWaitingState());
        btnTimedWaiting.setOnClickListener(v -> showTimedWaitingState());
        btnTerminated.setOnClickListener(v -> showTerminatedState());
        btnReset.setOnClickListener(v -> resetDemo());

        resetDemo();
    }

    private void showNewState() {
        demoThread = new Thread(() -> {});
        logAndState("[NEW] 新建线程，尚未start。", demoThread);
    }

    private void showRunnableState() {
        demoThread = new Thread(() -> {
            logOnUi("[RUNNABLE] 线程正在运行，等待CPU调度。\n");
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        });
        demoThread.start();
        mainHandler.postDelayed(() -> logAndState("[RUNNABLE] 已start，处于可运行状态。", demoThread), 200);
    }

    private void showBlockedState() {
        blockThread = new Thread(() -> {
            logOnUi("[BLOCKED] 子线程尝试获取blockLock\n");
            synchronized (blockLock) {
                logOnUi("[BLOCKED] 子线程获得锁，进入临界区\n");
            }
        });
        new Thread(() -> {
            synchronized (blockLock) {
                logOnUi("[BLOCKED] 主线程持有blockLock 2秒\n");
                blockThread.start();
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
                logOnUi("[BLOCKED] 主线程释放blockLock\n");
            }
            mainHandler.postDelayed(() -> logAndState("[BLOCKED] 子线程等待锁，处于BLOCKED状态。", blockThread), 500);
        }).start();
    }

    private void showWaitingState() {
        demoThread = new Thread(() -> {
            synchronized (lock) {
                try {
                    logOnUi("[WAITING] 线程进入wait，等待唤醒\n");
                    lock.wait();
                } catch (InterruptedException ignored) {}
            }
            logOnUi("[WAITING] 线程被唤醒，继续执行\n");
        });
        demoThread.start();
        mainHandler.postDelayed(() -> logAndState("[WAITING] 线程wait()，无限期等待。", demoThread), 300);
        mainHandler.postDelayed(() -> {
            synchronized (lock) { lock.notify(); }
        }, 1200);
    }

    private void showTimedWaitingState() {
        demoThread = new Thread(() -> {
            try {
                logOnUi("[TIMED_WAITING] 线程sleep 2秒\n");
                Thread.sleep(2000);
                logOnUi("[TIMED_WAITING] 线程sleep结束\n");
            } catch (InterruptedException ignored) {}
        });
        demoThread.start();
        mainHandler.postDelayed(() -> logAndState("[TIMED_WAITING] 线程sleep(n)或wait(n)，限时等待。", demoThread), 300);
    }

    private void showTerminatedState() {
        demoThread = new Thread(() -> logOnUi("[TERMINATED] 线程run完毕\n"));
        demoThread.start();
        mainHandler.postDelayed(() -> {
            logAndState("[TERMINATED] 线程已结束。", demoThread);
        }, 400);
    }

    private void resetDemo() {
        logView.setText("Thread 状态日志：\n");
        stateView.setText("当前线程状态：-");
        demoThread = null;
        blockThread = null;
    }

    private void logAndState(String msg, Thread t) {
        Thread.State state = t != null ? t.getState() : null;
        String stateStr = state != null ? state.name() : "-";
        logOnUi(msg + " 当前状态: " + stateStr + "\n");
        stateView.setText("当前线程状态：" + stateStr + explainState(state));
    }

    private void logOnUi(String msg) {
        runOnUiThread(() -> logView.append(msg));
    }

    private String explainState(Thread.State state) {
        if (state == null) return "";
        switch (state) {
            case NEW: return "（新建，未start）";
            case RUNNABLE: return "（可运行/运行中）";
            case BLOCKED: return "（阻塞，等待锁）";
            case WAITING: return "（无限期等待）";
            case TIMED_WAITING: return "（限时等待）";
            case TERMINATED: return "（已终止）";
            default: return "";
        }
    }
} 