package com.underconverbg.detectophone;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created by underconverbg on 2017/5/3.
 */

public class JobCastielService extends JobService
{
    private int kJobId = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e("castiel", "jobService启动");
        scheduleJob(getJobInfo());
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("castiel", "执行了onStartJob方法");
        boolean isLocalServiceWork = isServiceWork(this, "com.underconverbg.detectophone.PhoneCallStateService");
        if(!isLocalServiceWork)
        {
            this.startService(new Intent(this,PhoneCallStateService.class));
//            this.startService(new Intent(this,RemoteCastielService.class));
//            Toast.makeText(this, "进程启动", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params)
    {
        Log.e("castiel", "执行了onStopJob方法");
        scheduleJob(getJobInfo());
        return true;
    }

    //将任务作业发送到作业调度中去
    public void scheduleJob(JobInfo t)
    {
        Log.e("castiel", "调度job");
        JobScheduler tm =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.schedule(t);
    }

    public JobInfo getJobInfo(){
        JobInfo.Builder builder = new JobInfo.Builder(kJobId++, new ComponentName(this, JobCastielService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
//        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE); //设置需要的网络条件，默认NETWORK_TYPE_NONE
        builder.setPeriodic(3000);//设置间隔时间
        builder.setRequiresCharging(true);// 设置是否充电的条件,默认false
        builder.setRequiresDeviceIdle(true);// 设置手机是否空闲的条件,默认false
        builder.setPersisted(true);//设备重启之后你的任务是否还要继续执行
        return builder.build();
    }


    // 判断服务是否正在运行
    public boolean isServiceWork(Context mContext, String serviceName)
    {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}