package com.wanjian.singlethreadact.demo;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        new MultiThreadActivity(this);
    }
}
