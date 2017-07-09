package com.underconverbg.detectophone.system;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.underconverbg.detectophone.PersonService;
import com.underconverbg.detectophone.ServerCallBack;
import com.underconverbg.detectophone.bean.Detect;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by user on 2017/4/10.
 */

public class SystemSet
{
    public final static String actionUrl = "http://mobile.davidgroup.asia/api/app/SaveRecord";
    private String TAG = "SystemSet";

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
        mycontext = context;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        deviceid = tm.getDeviceId();
        tel = tm.getLine1Number();//手机号码
        imei = tm.getSimSerialNumber();
        imsi = tm.getSubscriberId();
        mgr = new PersonService(context);
    }

    public String getDeviceid()
    {
        if (null == deviceid ||deviceid.equals("")||deviceid.equals("null")||deviceid.equals("unknow")||deviceid.length()<=0)
        {
            if(mycontext != null)
            {
                TelephonyManager tm = (TelephonyManager) mycontext.getSystemService(Context.TELEPHONY_SERVICE);
                String iddeviceid = tm.getDeviceId();
                if (null == deviceid ||deviceid.equals("")||deviceid.equals("null")||deviceid.length()<=0)
                {
                    deviceid = "unknow";
                }
                else
                {
                    if (null != iddeviceid && !iddeviceid.equals("")&& !iddeviceid.equals("null") && iddeviceid.length()>0) {
                        deviceid = new String(iddeviceid);
                    }
                    else
                    {
                        deviceid = "unknow";
                    }
                }
            }
            else
            {
                deviceid = "unknow";
            }
        }
        return deviceid;
    }

    public String getTel()
    {
        if (null == tel ||tel.equals("")||tel.equals("null")||tel.equals("unknow")||tel.length()<=0)
        {
            if(mycontext != null)
            {
                TelephonyManager tm = (TelephonyManager) mycontext.getSystemService(Context.TELEPHONY_SERVICE);
                String idtel = tm.getLine1Number();
                if (null == tel ||tel.equals("")||tel.equals("null")||tel.length()<=0)
                {
                    tel = "unknow";
                }
                else
                {
                    if (null != idtel && !idtel.equals("")&& !idtel.equals("null")||idtel.length()>0) {
                        tel = new String(idtel);
                    }
                    else
                    {
                        tel = "unknow";
                    }
                }
            }
            else
            {
                tel = "unknow";
            }
        }
        return tel;
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

    public Context mycontext;
    public String deviceid;
    private String tel;
    public String imei;
    public String imsi;

    public PersonService getPersonService()
    {
        return mgr;
    }

    public void uploadFromDB()
    {
        if(mgr == null)
        {
            Log.e("uploadFromDB", "uploadFromDB...mgr is null");
            return;
        }
        List<Detect> list = mgr.findAll();
        for (int i = 0;i<list.size();i++)
        {
            Detect detect = list.get(i);
            upload(detect);
        }
    }

    private void upload(final Detect detect)
    {
        String url = actionUrl;
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", detect.getUserid());
        params.put("phonenum", detect.getPhonenum());
        params.put("callphonenum",detect.getCallphonenum());
        params.put("datetime", detect.getDatetime());
        params.put("recordtime", detect.getRecordtime());
        params.put("type", detect.getType());

        Log.e("params",params.toString());

        Log.e(TAG,"filePath:detect.getRecordFilePath():"+detect.getRecordFilePath());
        File file = new File(detect.getRecordFilePath());

        if (file == null)
        {
            Log.e(TAG,"file is null");
            PersonService service = SystemSet.getIntance().getPersonService();
            service.delete(detect.getRecordFilePath());
            return;
        }

            File uploadFile = new File(detect.getRecordFilePath());
            Log.e(TAG,"uploadFile:detect.getRecordFilePath():"+detect.getRecordFilePath());


        OkHttpUtils.post().url(url).params(params).addFile("recordfile", uploadFile.getName(),uploadFile).build().execute(new ServerCallBack()
        {
            @Override
            public void onError(Request request, Exception e)
            {
                Log.e(TAG,"onError");
                System.out.println("response:上传onError");
            }

            @Override
            public void onResponse(String response) throws JSONException
            {
                Log.e(TAG,"response:"+response.toString());
                System.out.println("response:"+response.toString());
                JSONObject jsonObject  = new JSONObject(response);
                String satae = jsonObject.optString("state");
                if ("success".equals(satae))
                {
                    deleteFile(detect.getRecordFilePath());
                    PersonService service = SystemSet.getIntance().getPersonService();
                    service.delete(detect.getRecordFilePath());
                    Log.e(TAG,"删除成功");

                    SystemSet.getIntance().uploadFromDB();
                }
                else if ("repeat".equals(satae))
                {
                    deleteFile(detect.getRecordFilePath());
                    PersonService service = SystemSet.getIntance().getPersonService();
                    service.delete(detect.getRecordFilePath());
                    Log.e(TAG,"删除成功");
                    SystemSet.getIntance().uploadFromDB();
                }
                else
                {

                }
            }
        });

    }

    public  boolean deleteFile(String fileName)
    {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                Log.e("UploadTask","删除单个文件" + fileName + "成功！");
                deleteFromeDB(fileName);
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                Log.e("UploadTask","删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            Log.e("UploadTask","删除单个文件失败：" + fileName + "不存在！");
            deleteFromeDB(fileName);
            return false;
        }
    }

    private void deleteFromeDB(String path)
    {
        if (mgr != null)
        {
            mgr.delete(path);
            Log.e("deleteFile","数据库删除成功");
        }
    }

}
