package com.underconverbg.detectophone.down;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by underconverbg on 2017/5/9.
 */

public class ApkInstallReceiver extends BroadcastReceiver
{
    public final static String TAG = "ApkInstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.e("ApkInstallReceiver", "onReceive.......");

        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        {
            Log.e("ApkInstallReceiver", "ACTION_DOWNLOAD_COMPLETE.......");
            long downloadApkId =intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            installApk(context, downloadApkId);

        }
    }

    /**
     * 安装apk
     */
    private void installApk(Context context,long downloadApkId) {
        Log.e("ApkInstallReceiver", "installApk.......");

        // 获取存储ID
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        long downId =sp.getLong(DownloadManager.EXTRA_DOWNLOAD_ID,-1L);
        Log.e("ApkInstallReceiver", "downloadApkId："+downloadApkId+"");
        Log.e("ApkInstallReceiver", "downId："+downId+"");

        if(downloadApkId == downId){
            DownloadManager downManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadFileUri = downManager.getUriForDownloadedFile(downloadApkId);
            if (downloadFileUri != null)
            {
                Log.e("ApkInstallReceiver", "下载成功.......");
                Log.e("downloadFileUri", "downloadFileUri:"+downloadFileUri);
                String uriSth = downloadFileUri.getAuthority();

                Intent install= new Intent(Intent.ACTION_VIEW);
                install.setDataAndType(Uri.parse("file://" + uriSth), "application/vnd.android.package-archive");
//                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");

                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);

            }else{
//                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                Log.e("ApkInstallReceiver", "下载失败.......");

            }
        }
    }
}