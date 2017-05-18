package com.underconverbg.detectophone;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.underconverbg.detectophone.bean.Detect;
import com.underconverbg.detectophone.system.SystemSet;
import com.underconverbg.detectophone.upload.UploadTools;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2017/3/2.
 */

public class MyRecorder
{
    private File recordFile;

    private String dateSth;
    private String phoneCallNumber = "unknow";

    private MediaRecorder mrecorder;
    private boolean isCommingNumber = false;//是否是来电
    private String TAG = "Recorder";
    private String type = "out";
//    private String recordtime = "00:00:00";


    public synchronized void start()
    {
        File recordPath = new File(Environment.getExternalStorageDirectory()
                , "/mydetectophone");
        if (!recordPath.exists()) {
            recordPath.mkdirs();
            Log.e("recorder", "创建目录");
        }
        else if( !recordPath.isDirectory() && recordPath.canWrite() )
        {
            Log.e("recorder", "删除创建目录");
            recordPath.delete();
            recordPath.mkdirs();
        }
        else
        {
            //you can't access there with write permission.
            //Try other way.
        }

        type = "out";
        if (isCommingNumber)
        {
            type = "in";

        }

        Date  myDate = new Date(System.currentTimeMillis());
        dateSth =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(myDate);

        String  dateName =  new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
                .format(myDate);

        String accName = type + "-" + phoneCallNumber + "-"
                + dateName + ".aac";//实际是3gp

        recordFile = new File(recordPath, accName);

        mrecorder = new MediaRecorder();

        try {
            mrecorder.stop();
            mrecorder.release();

        }catch (Exception e)
        {
            e.printStackTrace();
            Log.e("recorder", "录音开始停止错误IllegalStateException" + e.getMessage());
        }

        //2.指定录音机的声音源
        mrecorder.setAudioSource(
                MediaRecorder.AudioSource.MIC|MediaRecorder.AudioSource.VOICE_CALL|
                        MediaRecorder.AudioSource.DEFAULT|MediaRecorder.AudioSource.CAMCORDER
                        |MediaRecorder.AudioSource.VOICE_UPLINK|MediaRecorder.AudioSource.VOICE_DOWNLINK
        );

        mrecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);

        mrecorder.setOutputFile(recordFile.getPath());

        mrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //设置所录制的声音的编码位率。
        mrecorder.setAudioEncodingBitRate(16);
        //设置所录制的声音的采样率。
        mrecorder.setAudioSamplingRate(44100);


        try {
            mrecorder.prepare();
            mrecorder.start();
            Log.e(TAG , "录音开始");
            Log.e(TAG , "录音并设置开始");

        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e("recorder", "录音开始错误IllegalStateException" + e.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("recorder", "录音开始错误IOException" + e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("recorder", "录音开始错误Exception" + e.getMessage());
        }
    }

    public void stop() {
        try {
            if (mrecorder!=null)
            {
                mrecorder.stop();
                mrecorder.release();


                setIsCommingNumber(false);

                Log.e(TAG , "录音停止");

                if (recordFile != null)
                {
                    Log.e(TAG , "UploadTools 上传");

                    Detect detect = new Detect();
                    Log.e(TAG , "date:"+dateSth);
                    detect.setDatetime(dateSth);
                    detect.setCallphonenum(getPhoneCallNumber());
                    detect.setRecordfile(recordFile);
                    detect.setType(type);
//                  detect.setRecordtime(recordtime);

                    Log.e(TAG , detect.toString());
                    UploadTools.upload(detect);

                    PersonService service = SystemSet.getIntance().getPersonService();
                    service.save(detect);
                }
                mrecorder = null;
            }
        } catch (IllegalStateException e)
        {
            Log.e(TAG , "录音错误结束");
            mrecorder = null;
            e.printStackTrace();
        }

        Log.e(TAG , "录音结束");
    }

    public String getPhoneCallNumber() {
        return phoneCallNumber;
    }

    public void setPhoneCallNumber(String phoneNumber) {
        Log.e("-------", "设置来电号码为："+phoneNumber);
        if (phoneNumber == null || ("").equals(phoneNumber) || ("null").equals(phoneNumber))
        {
            this.phoneCallNumber = "unknow";

        }
        else
        {
            this.phoneCallNumber = phoneNumber;
        }
    }

    public synchronized boolean isCommingNumber() {
        return isCommingNumber;
    }

    public synchronized void setIsCommingNumber(boolean isCommingNumber) {
        this.isCommingNumber = isCommingNumber;
    }
}
