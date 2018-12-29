package com.wanjian.singlethreadact.demo;

import android.content.Context;
import android.os.IBinder;
import android.os.Message;

import java.lang.reflect.Method;

import me.weishu.reflection.Reflection;

public class ActivityLifecycleV28Compact implements IActivityLifecycleCompact {
    public ActivityLifecycleV28Compact(Context context) {
        Reflection.unseal(context);
    }

    @Override
    public IBinder onLaunchActivity(Message message) {
        try {
            Object clientTransaction = message.obj;
            Method getActivityTokenMethod = clientTransaction.getClass().getDeclaredMethod("getActivityToken");
            IBinder binder = (IBinder) getActivityTokenMethod.invoke(clientTransaction);
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
        return null;
    }

    @Override
    public IBinder onPauseActivity(Message message) {
        return null;
    }

    @Override
    public IBinder onPauseActivityFinish(Message message) {
        return null;
    }

    @Override
    public IBinder onStopActivity(Message message) {
        return null;
    }

    @Override
    public IBinder onStopActivityHide(Message message) {
        return null;
    }

    @Override
    public IBinder onDestroyActivity(Message message) {
        return null;
    }

    @Override
    public IBinder onConfigChanged(Message message) {
        return null;
    }

}
