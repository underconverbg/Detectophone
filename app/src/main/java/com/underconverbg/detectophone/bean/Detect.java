package com.underconverbg.detectophone.bean;

import com.underconverbg.detectophone.system.SystemSet;

import java.io.File;

/**
 * Created by user on 2017/4/10.
 */

public class Detect
{
    String userid;
    String phonenum;

    @Override
    public String toString() {
        return "Detect{" +
                "userid='" + userid + '\'' +
                ", phonenum='" + phonenum + '\'' +
                ", callphonenum='" + callphonenum + '\'' +
                ", datetime='" + datetime + '\'' +
                ", recordfile=" + recordfile +
                '}';
    }

    String callphonenum;
    String datetime;
    File recordfile;

    public  Detect()
    {
        userid = SystemSet.getIntance().getDeviceid();
        phonenum = SystemSet.getIntance().getTel();
    };


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPhonenum() {
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public File getRecordfile() {
        return recordfile;
    }

    public void setRecordfile(File recordfile) {
        this.recordfile = recordfile;
    }
}
