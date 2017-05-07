package com.underconverbg.detectophone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.underconverbg.detectophone.bean.Detect;
import com.underconverbg.detectophone.upload.UploadTools;

import java.io.File;

/**
 * Created by user on 2017/3/2.
 */

public class OutgoingCallReciver extends BroadcastReceiver
{
    static final String TAG = "OutgoingCallReciver";
    private MyRecorder recorder;

    public  OutgoingCallReciver (MyRecorder recorder)
    {
        this.recorder = recorder;
    }

    @Override
    public void onReceive(Context ctx, Intent intent)
    {
        String phoneState = intent.getAction();
        Log.e(TAG, "广播接收者的Action："+phoneState);
        if (phoneState.equals(Intent.ACTION_NEW_OUTGOING_CALL))
        {
//            String phoneNum = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);//拨出号码
            String outgoing_number = intent.getStringExtra("android.intent.extra.PHONE_NUMBER");

            if (outgoing_number != null)
            {
                outgoing_number = outgoing_number.trim();
            }

            if (outgoing_number != null && !("").equals(outgoing_number) && !("null").equals(outgoing_number))
            {
                recorder.setPhoneCallNumber(""+outgoing_number);
                Log.e(TAG, "设置为去电状态");
                Log.e(TAG, "去电状态 呼叫：" + outgoing_number);
            }

            recorder.setIsCommingNumber(false);

        }

//        if (phoneState.equals(OutgoingCallState.ForeGroundCallState.DIALING)) {
//            Log.e(TAG, "正在拨号...");
//        }
//
//        if (phoneState.equals(OutgoingCallState.ForeGroundCallState.ALERTING)) {
//            Log.e(TAG, "正在呼叫...");
//        }
//
//        if (phoneState.equals(OutgoingCallState.ForeGroundCallState.ACTIVE)) {
//            if (!recorder.isCommingNumber() && !recorder.isStarted())
//            {
//                Log.e(TAG, "去电已接通 启动录音机");
//                recorder.start();
//            }
//        }
//
//        if (phoneState.equals(OutgoingCallState.ForeGroundCallState.DISCONNECTED)) {
//            if (!recorder.isCommingNumber() && recorder.isStarted()) {
//                Log.e(TAG, "已挂断 关闭录音机");
//                recorder.stop();
//            }
//        }
    }
}
















