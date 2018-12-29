package com.wanjian.singlethreadact.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv);
        msg("onCreate");

        Toast.makeText(this, "当前线程" + Thread.currentThread().getId(), Toast.LENGTH_SHORT).show();
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SecondActivity.class));
            }
        });


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
