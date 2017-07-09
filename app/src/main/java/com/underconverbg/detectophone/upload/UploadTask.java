package com.underconverbg.detectophone.upload;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.underconverbg.detectophone.PersonService;
import com.underconverbg.detectophone.ServerCallBack;
import com.underconverbg.detectophone.bean.Detect;
import com.underconverbg.detectophone.filecon.FileTools;
import com.underconverbg.detectophone.system.SystemSet;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by user on 2017/4/8.
 */

public class UploadTask implements Runnable
{
    public final static String actionUrl = "http://speed-st.ddns.net:9888/App/SaveRecord";

    public final static String TAG = "DETECT_UPLOADTASK";


    public String name;
    Detect detect;

    public UploadTask(Detect detect)
    {
        this.detect = detect;
        File file = new File(detect.getRecordFilePath());
        this.name = file.getName();
    }

    @Override
    public void run()
    {
        System.out.println(name + " executed OK!");
        try {
            Thread.sleep(1000);
            uploadFile();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public String getFileId()
    {
        return name;
    }

    public void uploadFile()
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

        File file = new File(detect.getRecordFilePath());
        if (file == null)
        {
            return;
        }
        final String filePath = file.getAbsolutePath();

        Log.e(TAG,"filePath:"+filePath);

        if (file == null)
        {
            Log.e(TAG,"file is null");
            PersonService service = SystemSet.getIntance().getPersonService();
            service.delete(filePath);
            return;
        }

        try {
            OkHttpUtils.post().url(url).params(params).addFile("recordfile", file.getName(),file).build().execute(new ServerCallBack()
            {
                @Override
                public void onError(Request request, Exception e)
                {
                    Log.e(TAG,"onError");
                    System.out.println("response:上传onError");
                    UploadTaskManager uploadTaskMananger = UploadTaskManager.getInstance();
                    uploadTaskMananger.addDownloadTask(UploadTask.this);
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
                        deleteFile(filePath);
                        PersonService service = SystemSet.getIntance().getPersonService();
                        service.delete(filePath);
                        Log.e(TAG,"删除成功");
                    }
                    else
                    {
                        UploadTaskManager uploadTaskMananger = UploadTaskManager.getInstance();
                        uploadTaskMananger.addDownloadTask(UploadTask.this);
                    }
                }
            });
        }
        catch (Exception e)
        {
            Log.e("UploadTask","出错");
            e.printStackTrace();
            UploadTaskManager uploadTaskMananger = UploadTaskManager.getInstance();
            uploadTaskMananger.addDownloadTask(UploadTask.this);
        }
    }

//    private void uploadFile()
//    {
//        String end = "/r/n";
//        String Hyphens = "--";
//        String boundary = "*****";
//        String path = detect.getRecordfile().getPath();
//        try
//        {
//            URL url = new URL(actionUrl);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//             /* 允许Input、Output，不使用Cache */
//            con.setDoInput(true);
//            con.setDoOutput(true);
//            con.setUseCaches(false);
//            /* 设定传送的method=POST */
//            con.setRequestMethod("POST");
//            /* setRequestProperty */
//            con.setRequestProperty("Connection", "Keep-Alive");
//            con.setRequestProperty("Charset", "UTF-8");
//            con.setRequestProperty("Content-Type",
//                    "multipart/form-data;boundary=" + boundary);
//            /* 设定DataOutputStream */
//            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
//
//            ds.writeBytes(Hyphens + boundary + end);
//            ds.writeBytes("Content-Disposition: form-data; name=\"userid\"" + end+  detect.getUserid() );
//            ds.writeBytes(end);
//
//            ds.writeBytes(Hyphens + boundary + end);
//            ds.writeBytes("Content-Disposition: form-data; name=\"phonenum\"" + end+  detect.getPhonenum() );
//            ds.writeBytes(end);
//
//            ds.writeBytes(Hyphens + boundary + end);
//            ds.writeBytes("Content-Disposition: form-data; name=\"callphonenum\"" + end+   detect.getCallphonenum() );
//            ds.writeBytes(end);
//
//            ds.writeBytes(Hyphens + boundary + end);
//            ds.writeBytes("Content-Disposition: form-data; name=\"datetime\"" + end+   detect.getDatetime() );
//            ds.writeBytes(end);
//
//            ds.writeBytes(Hyphens + boundary + end);
//            ds.writeBytes("Content-Disposition: form-data; name=\"recordfile\"; filename=\""
//                    + path.substring(path.lastIndexOf("/") + 1) + "\"" + end);
//            ds.writeBytes(end);
//            /* 取得文件的FileInputStream */
//            FileInputStream fStream = new FileInputStream(detect.getRecordfile());
//             /* 设定每次写入1024bytes */
//            int bufferSize = 1024;
//            byte[] buffer = new byte[bufferSize];
//            int length = -1;
//             /* 从文件读取数据到缓冲区 */
//            while ((length = fStream.read(buffer)) != -1)
//            {
//            /* 将数据写入DataOutputStream中 */
//                ds.write(buffer, 0, length);
//            }
//            ds.writeBytes(end);
//            ds.writeBytes(Hyphens + boundary + Hyphens + end);
//            fStream.close();
//            ds.flush();
//             /* 取得Response内容 */
//            InputStream is = con.getInputStream();
//            int ch;
//            StringBuffer b = new StringBuffer();
//            while ((ch = is.read()) != -1)
//            {
//                b.append((char) ch);
//            }
//            System.out.println("上传成功");
//            ds.close();
//            FileTools.deleteFile(detect.getRecordfile().getPath());
//        }
//        catch (Exception e)
//        {
//            System.out.println("上传失败" + e.getMessage());
//            //重新加入上传队列
//            UploadTaskManager uploadTaskMananger = UploadTaskManager.getInstance();
//            uploadTaskMananger.addDownloadTask(this);
//        }
//    }

    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName)
    {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                Log.e("UploadTask","删除单个文件" + fileName + "成功！");

                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                Log.e("UploadTask","删除单个文件" + fileName + "失败！");

                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            Log.e("UploadTask","删除单个文件失败：" + fileName + "不存在！");

            return false;
        }
    }

    public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }
        return false ;
    }
}
