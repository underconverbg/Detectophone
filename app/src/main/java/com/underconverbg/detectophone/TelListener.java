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
    private  MyRecorder2 recorder2;

    boolean isStart = false;


    public TelListener(MyRecorder recorder)
    {
        this.recorder = recorder;
    }


    @Override
    public void onCallStateChanged(int state, String incomingNumber)
    {
        super.onCallStateChanged(state, incomingNumber);


        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING: // 来电响铃
                Log.e(LOG_TAG, "CALL_STATE_RINGING :"+"来电号码："+incomingNumber);
                if (recorder != null)
                {
                    recorder.stop();
                    Log.e(LOG_TAG, "来电CALL_STATE_RINGING :"+"录音停止");
                }

                if (incomingNumber != null)
                {
                    incomingNumber = incomingNumber.trim();
                }

                if (incomingNumber != null && !("").equals(incomingNumber) && !("null").equals(incomingNumber))
                {
                    recorder.setPhoneCallNumber(incomingNumber);
                }
                recorder.setPhoneCallNumber(incomingNumber);
                recorder.setIsCommingNumber(true);

                break;
            case TelephonyManager.CALL_STATE_IDLE: // 空闲状态，即无来电也无去电
                if (recorder != null)
                {
                    if (isStart)
                    {
                        recorder.stop();
                        isStart = false;
                        Log.e(LOG_TAG, "CALL_STATE_IDLE :" + "录音停止");
                    }
                    else
                    {
                        Log.e(LOG_TAG, "CALL_STATE_IDLE :"+"空挂无聊");
                    }
                }

                break;

            case TelephonyManager.CALL_STATE_OFFHOOK: // 摘机，即接通
                if (recorder != null)
                {
                    if (!isStart)
                    {
                        Log.e(LOG_TAG, "CALL_STATE_OFFHOOK :"+"接通电话");

                        recorder.start();
                        isStart = true;
                    }
                    else
                    {
                        Log.e(LOG_TAG, "CALL_STATE_OFFHOOK :"+"空挂无聊");
                    }
                }
                else
                {
                    Log.e(LOG_TAG, "CALL_STATE_OFFHOOK :" + "recorder为空");
                }

                break;
        }

//        Log.e("TelephoneState", String.valueOf(incomingNumber));
    }
}