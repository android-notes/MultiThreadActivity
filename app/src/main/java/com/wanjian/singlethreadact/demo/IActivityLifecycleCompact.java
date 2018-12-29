package com.wanjian.singlethreadact.demo;

import android.os.IBinder;
import android.os.Message;

public interface IActivityLifecycleCompact {

    IBinder onLaunchActivity(Message message);

    IBinder onReLaunchActivity(Message message);

    IBinder onResumeActivity(Message message);

    IBinder onPauseActivity(Message message);

    IBinder onPauseActivityFinish(Message message);

    IBinder onStopActivity(Message message);

    IBinder onStopActivityHide(Message message);

    IBinder onDestroyActivity(Message message);

    // TODO: 2018/12/29  
    IBinder onConfigChanged(Message message);
}
