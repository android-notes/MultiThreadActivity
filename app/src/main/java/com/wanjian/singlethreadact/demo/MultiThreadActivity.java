package com.wanjian.singlethreadact.demo;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试通过
 * api 18
 * api 19
 * api 21
 * api 24
 * api 26
 * api 28
 */
public class MultiThreadActivity {
    private Map<IBinder, HandlerThread> handlerThreadMap = new HashMap<>();

    private IActivityLifecycleCompact lifecycleCompact;


    public MultiThreadActivity(Context context) {
        if (Build.VERSION.SDK_INT >= 28) {
            lifecycleCompact = new ActivityLifecycleV28Compact(context);
        } else if (Build.VERSION.SDK_INT >= 26) {
            lifecycleCompact = new ActivityLifecycleV26Compact();
        } else if (Build.VERSION.SDK_INT >= 24) {
            lifecycleCompact = new ActivityLifecycleV24Compact();
        } else if (Build.VERSION.SDK_INT >= 21) {
            lifecycleCompact = new ActivityLifecycleV21Compact();
        } else if (Build.VERSION.SDK_INT >= 15) {
            lifecycleCompact = new ActivityLifecycleV15Compact();
        } else {
            lifecycleCompact = new ActivityLifecycleV0Compact();
        }
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void init() throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {

        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getDeclaredMethod("currentActivityThread").invoke(null);

        final Field mhField = activityThreadClass.getDeclaredField("mH");
        mhField.setAccessible(true);
        final Handler mhHandler = (Handler) mhField.get(activityThread);
        Field callbackField = Handler.class.getDeclaredField("mCallback");
        callbackField.setAccessible(true);
        final Handler.Callback originCallback = (Handler.Callback) callbackField.get(mhHandler);
        callbackField.set(mhHandler, new Handler.Callback() {

            @Override
            public boolean handleMessage(final Message msg) {
                final Message message = new Message();
                message.copyFrom(msg);
                if (Build.VERSION.SDK_INT >= 28) {//android P 生命周期全部走这
                    return deliverAboveApi28(msg, message, mhHandler);
                } else {
                    return deliverBeforeApi28(msg, message, mhHandler);
                }
            }
        });
    }

    private boolean deliverBeforeApi28(Message msg, Message message, Handler mhHandler) {
        final int LAUNCH_ACTIVITY = 100;
        final int PAUSE_ACTIVITY = 101;
        final int PAUSE_ACTIVITY_FINISHING = 102;
        final int STOP_ACTIVITY_HIDE = 104;
        final int RESUME_ACTIVITY = 107;
        final int DESTROY_ACTIVITY = 109;
        final int NEW_INTENT = 112;
        final int RELAUNCH_ACTIVITY = 126;
        final int CONFIGURATION_CHANGED = 118;

        switch (msg.what) {
            case LAUNCH_ACTIVITY:// startActivity--> activity.attach  activity.onCreate  r.activity!=null  activity.onStart  activity.onResume
                IBinder iBinder = lifecycleCompact.onLaunchActivity(msg);

                deliverOnChildThread(message, iBinder, mhHandler);
                return true;
            case RESUME_ACTIVITY://回到activity onRestart onStart onResume
                iBinder = lifecycleCompact.onResumeActivity(msg);
                deliverOnChildThread(message, iBinder, mhHandler);
                return true;
            case PAUSE_ACTIVITY_FINISHING://按返回键 onPause
                iBinder = lifecycleCompact.onPauseActivityFinish(msg);
                deliverOnChildThread(message, iBinder, mhHandler);
                return true;
            case PAUSE_ACTIVITY://开启新页面时，旧页面执行 activity.onPause
                iBinder = lifecycleCompact.onPauseActivity(msg);
                deliverOnChildThread(message, iBinder, mhHandler);
                return true;
            case STOP_ACTIVITY_HIDE://开启新页面时，旧页面执行 activity.onStop
                iBinder = lifecycleCompact.onStopActivityHide(msg);
                deliverOnChildThread(message, iBinder, mhHandler);
                return true;
            case DESTROY_ACTIVITY:// 关闭activity onStop  onDestroy
                iBinder = lifecycleCompact.onDestroyActivity(msg);
                deliverOnChildThread(message, iBinder, mhHandler);
                return true;
            case NEW_INTENT:// TODO: 2018/12/29

                return false;
            case CONFIGURATION_CHANGED:// TODO: 2018/12/29

                return false;
            case RELAUNCH_ACTIVITY:
                iBinder = lifecycleCompact.onReLaunchActivity(msg);
                deliverOnChildThread(message, iBinder, mhHandler);
                return true;
        }
//                if (originCallback != null) {
//                    return originCallback.handleMessage(msg);
//                }
        return false;
    }

    private boolean deliverAboveApi28(Message msg, Message message, Handler mhHandler) {
        final int EXECUTE_TRANSACTION = 159;
        final int ENTER_ANIMATION_COMPLETE = 149;
        final int CONFIGURATION_CHANGED = 118;
        final int DUMP_ACTIVITY = 136;
        final int RELAUNCH_ACTIVITY = 160;
        if (msg.what == EXECUTE_TRANSACTION) {
            IBinder iBinder = lifecycleCompact.onLaunchActivity(msg);
            deliverOnChildThread(message, iBinder, mhHandler);
            return true;
        } else if (msg.what == ENTER_ANIMATION_COMPLETE) {
            deliverOnChildThread(message, (IBinder) message.obj, mhHandler);
            return true;
        }
//      else if (){   // TODO: 2018/12/29
//          final int CONFIGURATION_CHANGED = 118;
//          final int DUMP_ACTIVITY = 136;
//          final int RELAUNCH_ACTIVITY = 160;
//      }
//      if (originCallback != null) {
//          return originCallback.handleMessage(msg);
//      }
        return false;
    }

    private void deliverOnChildThread(final Message message, IBinder iBinder, final Handler mhHandler) {
        HandlerThread handlerThread = handlerThreadMap.get(iBinder);

        if (handlerThread == null) {
            handlerThread = new HandlerThread("");
            handlerThread.start();
            handlerThreadMap.put(iBinder, handlerThread);
        }
        new Handler(handlerThread.getLooper()).post(new Runnable() {
            @Override
            public void run() {
                mhHandler.handleMessage(message);
            }
        });
    }

}
