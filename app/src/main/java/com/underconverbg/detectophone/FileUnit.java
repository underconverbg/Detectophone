package com.underconverbg.detectophone;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by user on 2017/3/2.
 */

public class FileUnit
{
    public static final String LOG_TAG = "DETECT_FILEUNIT";

//    public static String FirstFolder = Environment.getExternalStorageDirectory().getPath()+"/detectophone";//定义需要创建的根目录文件夹名字
//    public static String SecondFolder = "SecondFolder";//定义需要创建的二级目录文件夹，根据需要进行设置

    public static String folderCreate(Context c)
    {
       String path = c.getFilesDir().getPath()+"/luyin";
        File file = new File(path) ;

        if(!file.exists())
        {
            try {
                file.mkdirs() ;
                //file is create
                Log.e(LOG_TAG, "file is create");

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(LOG_TAG, "file is fail");
            }
        }

        return  path;
    }
}
