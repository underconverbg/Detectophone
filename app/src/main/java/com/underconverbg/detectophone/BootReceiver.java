package com.underconverbg.detectophone;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

/**
 * Created by underconverbg on 2017/4/9.
 */

public class BootReceiver extends BroadcastReceiver
{
    public static final String LOG_TAG = "DETECT_MYBOOTRECEIVER";

    private Intent phoneCallStateServiceIntent;

    @Override
    public void onReceive(Context context, Intent arg1)
    {
        Log.e(LOG_TAG, "-----MyBootReceiver:onReceive start");
        if (phoneCallStateServiceIntent == null)
        {
            phoneCallStateServiceIntent = new Intent(context, PhoneCallStateService.class);
            context.startService(phoneCallStateServiceIntent);
        }
        else
        {
            context.startService(phoneCallStateServiceIntent);
        }
//
        Log.e(LOG_TAG, "-----MyBootReceiver:onReceive end");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(context.getPackageName(), JobCastielService.class.getName()))
                    .setPeriodic(2000)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setRequiresCharging(true)// 设置是否充电的条件,默认false
                    .setRequiresDeviceIdle(true)// 设置手机是否空闲的条件,默认false
                    .setPersisted(true)//
                    .build();

            jobScheduler.schedule(jobInfo);
        }
    }
}