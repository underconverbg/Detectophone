package com.underconverbg.detectophone;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.underconverbg.detectophone.bean.Detect;
import com.underconverbg.detectophone.upload.UploadTools;

import java.io.File;

/**
 * Created by user on 2017/3/2.
 */

public class TelListener  extends PhoneStateListener
{
    public static final String LOG_TAG = "TelListener";

    private  MyRecorder recorder;

    public TelListener()
    {

    }


    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE: // 空闲状态，即无来电也无去电
                Log.e(LOG_TAG, "CALL_STATE_IDLE :"+"挂断电话");
                if (recorder != null){
                    recorder.stop();
                    Log.e(LOG_TAG, "CALL_STATE_IDLE :"+"录音停止");
                }
                break;
            case TelephonyManager.CALL_STATE_RINGING: // 来电响铃
                Log.e(LOG_TAG, "CALL_STATE_RINGING :"+"来电");
                //此处添加一系列功能代码
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK: // 摘机，即接通
                Log.e(LOG_TAG, "CALL_STATE_OFFHOOK :"+"接通电话");
                recorder   = new MyRecorder(incomingNumber);
                recorder.start();
                Log.e(LOG_TAG, "CALL_STATE_IDLE :"+"录音开始");
                break;
        }

//        Log.e("TelephoneState", String.valueOf(incomingNumber));
    }
}