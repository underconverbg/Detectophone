package com.underconverbg.detectophone.bean;

import android.media.MediaPlayer;
import android.util.Log;

import com.underconverbg.detectophone.system.SystemSet;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by user on 2017/4/10.
 */

public class Detect
{
    private String userid="unknow";
    private String phonenum="unknow";
    private String callphonenum="unknow";
    private String datetime="unknow";
    private String type = "out";
    private String recordtime = "00:00:00";
    private String recordFilePath ="unknow";

    @Override
    public String toString()
    {
        return "Detect{" +
                "userid='" + userid + '\'' +
                ", phonenum='" + phonenum + '\'' +
                ", callphonenum='" + callphonenum + '\'' +
                ", datetime='" + datetime + '\'' +
                ", type='" + type + '\'' +
                ", recordtime='" + recordtime + '\'' +
                ", recordFilePath=" + recordFilePath +
                '}';
    }

    public  Detect()
    {
        userid = SystemSet.getIntance().getDeviceid();
        phonenum = ""+SystemSet.getIntance().getTel();
    }

    public String getRecordtime() {
        return recordtime;
    }
    public void setRecordtime(String recordtime)
    {
            if (recordtime != null) {
            this.recordtime = recordtime;
        }
    }

    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        if (type != null)
        {
            this.type = type;
        }
    }


    public String getUserid()
    {
        String  nowuserid = SystemSet.getIntance().getDeviceid();
        if (null == nowuserid ||nowuserid.equals("")||nowuserid.equals("null")||nowuserid.length()<=0)
        {
            userid = "unknow";
        }
        else
        {
            userid = nowuserid;
        }return userid;
    }

    public void setUserid(String userid)
    {
        if (userid != null && !userid.equals("")&& !userid.equals("null")&& userid.length()>0)
        {
            this.userid = userid;
        }
        else
        {
            this.phonenum = "unknow";
        }
    }

    public String getPhonenum()
    {
            String nowTel = SystemSet.getIntance().getTel();
            if (null == nowTel ||nowTel.equals("")||nowTel.equals("null")||nowTel.length()<=0)
            {
                this.phonenum = "unknow";
            }
            else
            {
                this.phonenum = nowTel;
            }
            return phonenum;
    }

    public void setPhonenum(String phonenum)
    {
        if (phonenum != null && !phonenum.equals("")&& !phonenum.equals("null")&& phonenum.length()>0)
        {
            this.phonenum = ""+phonenum;
        }
        else
        {
            this.phonenum = "unknow";
        }
    }

    public String getCallphonenum() {
        return callphonenum;
    }

    public void setCallphonenum(String callphonenumsth)
    {
        if (callphonenumsth != null && !callphonenumsth.equals("")&& !callphonenumsth.equals("null")&& callphonenumsth.length()>0)
        {
            this.callphonenum = callphonenumsth;
        }
    }

    public String getDatetime()
    {
        return datetime;
    }

    public void setDatetime(String datetimesth)
    {
        if (datetimesth != null && !datetimesth.equals("")&& !datetimesth.equals("null")&& datetimesth.length()>0)
        {
            this.datetime = datetimesth;
        }
    }


    public String getRecordFilePath() {
        return recordFilePath;
    }

    public void setRecordFilePath(String recordFilePath )
    {
        if (recordFilePath == null)
        {
            return;
        }
        Log.e("Detect","setRecordFilePath:"+recordFilePath);

        this.recordFilePath = recordFilePath;

        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);  //recordingFilePath（）为音频文件的路径
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", "Exception");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "Exception");

        }
        int duration= player.getDuration();
        int mm = duration/1000;
        int m = mm/60;
        int s = mm%60;

        Log.e("ACETEST", "### duration: " + duration);
        Log.e("ACETEST", m + ":" + s);

        player.release();//记得释放资源

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        recordtime = df.format(duration);
    }



}
