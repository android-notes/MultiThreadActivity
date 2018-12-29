package com.wanjian.singlethreadact.demo;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv);
        msg("onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        msg("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        msg("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        msg("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        msg("onStop");
    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        msg("onNewIntent");
//    }
//
//    @Override
//    public void onContentChanged() {
//        super.onContentChanged();
//        msg("onContentChanged");
//    }


    private void msg(String s) {


        textView.append(s);
        textView.append("\n");
        textView.append("main thread ? " + (Looper.getMainLooper().getThread() == Thread.currentThread()));
        textView.append("\n");
        textView.append("main Thread:" + Looper.getMainLooper().getThread().getId());
        textView.append("\n");
        textView.append("current thread:" + Thread.currentThread().getId());
        textView.append("\n");
        textView.append("\n");

    }
}
