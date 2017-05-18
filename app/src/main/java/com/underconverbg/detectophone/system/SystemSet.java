package com.underconverbg.detectophone.system;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.underconverbg.detectophone.PersonService;

/**
 * Created by user on 2017/4/10.
 */

public class SystemSet
{
    static  SystemSet systemSet;

    private PersonService mgr;

    public static SystemSet getIntance()
    {
        if (systemSet == null)
        {
            synchronized(SystemSet.class) {
                systemSet = new SystemSet();
            }
        }
        return  systemSet;
    }

    public void init(Context context)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        deviceid = tm.getDeviceId();
        tel = tm.getLine1Number();//手机号码
        imei = tm.getSimSerialNumber();
        imsi = tm.getSubscriberId();
        mgr = new PersonService(context);
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String deviceid;
    public String tel;
    public String imei;
    public String imsi;

    public PersonService getPersonService()
    {
        return mgr;
    }

}
