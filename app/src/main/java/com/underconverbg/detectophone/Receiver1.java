package com.underconverbg.detectophone;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * DO NOT do anything in this Receiver!<br/>
 *
 * Created by Mars on 12/24/15.
 */
public class Receiver1 extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        try {
            if (!isServiceRunning(context)) {
                intent.setComponent(new ComponentName("com.underconverbg.detectophonesecond",
                        "com.underconverbg.detectophonesecond.Service1"));
                context.startService(intent);
            }
        }catch (Exception e)
        {

        }
    }

    private boolean isServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.underconverbg.detectophonesecond.Service1".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
