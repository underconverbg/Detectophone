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
    String userid;
    String phonenum;
    String callphonenum;
    String datetime;
    File recordfile;
    String type = "out";
    String recordtime = "00:00:00";
    private String recordFilePath;

    @Override
    public String toString()
    {
        return "Detect{" +
                "userid='" + userid + '\'' +
                ", phonenum='" + phonenum + '\'' +
                ", callphonenum='" + callphonenum + '\'' +
                ", datetime='" + datetime + '\'' +
                ", recordfile=" + recordfile +
                ", type='" + type + '\'' +
                ", recordtime='" + recordtime + '\'' +
                '}';
    }

    public  Detect()
    {
        userid = SystemSet.getIntance().getDeviceid();
        phonenum = SystemSet.getIntance().getTel();
    }

    public String getRecordtime() {
        return recordtime;
    }
    public void setRecordtime(String recordtime)
    {
        this.recordtime = recordtime;
    }

    public String getType() {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid)
    {
        this.userid = userid;
    }

    public String getPhonenum()
    {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getCallphonenum() {
        return callphonenum;
    }

    public void setCallphonenum(String callphonenum) {
        this.callphonenum = callphonenum;
    }

    public String getDatetime()
    {
        return datetime;
    }

    public void setDatetime(String datetime)
    {
        this.datetime = datetime;
    }

    public File getRecordfile() {
        return recordfile;
    }

    public String getRecordFilePath() {
        return recordFilePath;
    }

    public void setRecordFilePath(String recordFilePath ) {
        this.recordFilePath = recordFilePath;
        recordfile = new File(recordFilePath);
    }


    public void setRecordfile(File recordfile)
    {
        this.recordfile = recordfile;

        recordFilePath = recordfile.getAbsolutePath();

        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(recordfile.getAbsolutePath());  //recordingFilePath（）为音频文件的路径
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
