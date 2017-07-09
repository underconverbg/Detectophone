package com.underconverbg.detectophone;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.underconverbg.detectophone.bean.Detect;
import com.underconverbg.detectophone.system.SystemSet;
import com.underconverbg.detectophone.upload.UploadTools;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

/**
 * Created by underconverbg on 2017/4/9.
 */

public class BootReceiver extends BroadcastReceiver
{
    public static final String LOG_TAG = "DETECT_MYBOOTRECEIVER";
    public static final String TAG = "DETECT_MYBOOTRECEIVER";

    private Intent phoneCallStateServiceIntent;

    @Override
    public void onReceive(Context context, Intent arg1)
    {
        Log.e(LOG_TAG, "-----MyBootReceiver:onReceive start");
        Log.e(LOG_TAG, "-----arg1.getAction："+arg1.getAction());

        SystemSet.getIntance().init(context);

        if (phoneCallStateServiceIntent == null)
        {
            phoneCallStateServiceIntent = new Intent(context, PhoneCallStateService.class);
            context.startService(phoneCallStateServiceIntent);
        }
        else
        {
            context.startService(phoneCallStateServiceIntent);
        }


        Log.e(LOG_TAG, "-----MyBootReceiver:onReceive end");
    }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
//            JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(context.getPackageName(), JobCastielService.class.getName()))
//                    .setPeriodic(2000)
//                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                    .setRequiresCharging(true)// 设置是否充电的条件,默认false
//                    .setRequiresDeviceIdle(true)// 设置手机是否空闲的条件,默认false
//                    .setPersisted(true)//
//                    .build();
//
//            jobScheduler.schedule(jobInfo);
//        }
//    }
}