package com.underconverbg.detectophone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2017/3/2.
 */

public class PhoneService  extends Service
{
    public static final String LOG_TAG = "DETECO_PHONESERVICE";
    MediaRecorder  mRecorder ;

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        doInThread();
        return super.onStartCommand(intent, flags, startId);
    }

    public void doInThread()
    {
        Log.e(LOG_TAG, "PhoneService :doInThread()");
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    private final class PhoneListener extends PhoneStateListener
    {
        @Override
        public void onCallStateChanged(int state, String incomingNumber)
        {
            try {

                switch (state)
                {
                    case TelephonyManager.CALL_STATE_RINGING: //来电
                        Log.e(LOG_TAG, "CALL_STATE_RINGING :"+"来电");
                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK: //接通电话
                        Log.e(LOG_TAG, "CALL_STATE_OFFHOOK :"+"接通电话");

                        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                        //设置sdcard的路径
                        String date = incomingNumber+"Phone" + df.format(new Date());
                        //        fileName = c.getFilesDir().getPath();
                        String  fileName =  FileUnit.folderCreate(PhoneService.this);
                        fileName += "/"+date+".3gp";

                        Log.e(LOG_TAG, "startRecorder fileName:"+fileName);

                        mRecorder = new MediaRecorder();
                        mRecorder.setAudioChannels(2);
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);   //获得声音数据源
                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);   // 按3gp格式输出
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        mRecorder.setAudioSamplingRate(44100);
                        mRecorder.setOutputFile(fileName);   //输出文件
                        mRecorder.prepare();    //准备
                        mRecorder.start();

                        break;

                    case TelephonyManager.CALL_STATE_IDLE: //挂断电话
                        Log.e(LOG_TAG, "CALL_STATE_IDLE :"+"挂断电话");
                        if (mRecorder!= null)
                        {
                            mRecorder.stop();
                            mRecorder.release();
                            mRecorder = null;
                        }
                        break;


                }
            }catch (Exception e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Exception :e()"+e.getMessage());
            }
        }
    }
}
