package com.wanjian.singlethreadact.demo;

import android.os.IBinder;
import android.os.Message;

import java.lang.reflect.Field;

public class ActivityLifecycleV24Compact implements IActivityLifecycleCompact {
    @Override
    public IBinder onLaunchActivity(Message message) {
        try {
            Object activityClientRecord = message.obj;

            Field tokenField = activityClientRecord.getClass().getDeclaredField("token");

            tokenField.setAccessible(true);
            IBinder binder = (IBinder) tokenField.get(activityClientRecord);
            return binder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IBinder onReLaunchActivity(Message message) {
        return onLaunchActivity(message);
    }

    @Override
    public IBinder onResumeActivity(Message message) {
        return someArgs(message);
    }

    @Override
    public IBinder onPauseActivity(Message message) {
        return someArgs(message);
    }

    @Override
    public IBinder onPauseActivityFinish(Message message) {
        return someArgs(message);
    }

    @Override
    public IBinder onStopActivity(Message message) {
        return someArgs(message);
    }

    @Override
    public IBinder onStopActivityHide(Message message) {
        return someArgs(message);
    }

    @Override
    public IBinder onDestroyActivity(Message message) {
        return (IBinder) message.obj;
    }

    @Override
    public IBinder onConfigChanged(Message message) {
        return null;
    }

    private IBinder someArgs(Message message) {
        try {
            Object someArgs = message.obj;
            Field arg1Field = someArgs.getClass().getDeclaredField("arg1");
            arg1Field.setAccessible(true);
            IBinder binder = (IBinder) arg1Field.get(someArgs);
            return binder;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
