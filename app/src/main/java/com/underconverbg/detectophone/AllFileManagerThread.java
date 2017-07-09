package com.underconverbg.detectophone;

import android.util.Log;

import com.underconverbg.detectophone.bean.Detect;
import com.underconverbg.detectophone.system.SystemSet;
import com.underconverbg.detectophone.upload.UploadTask;
import com.underconverbg.detectophone.upload.UploadTaskManager;
import com.underconverbg.detectophone.upload.UploadTools;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by underconverbg on 2017/5/19.
 */

public class AllFileManagerThread implements Runnable
{
    private boolean isStop = false;

    private final int SLEEP_TIME = 1000*60;


    public AllFileManagerThread()
    {

    }


    @Override
    public void run()
    {
        while (!isStop)
        {
            PersonService service = SystemSet.getIntance().getPersonService();
            List<Detect> list = service.findAll();

            if (list != null)
            {
                if (list.size()>0)
                {
                    Log.e("AllFileManagerThread", "AllFileManagerThread轮询");
                    for (int i = 0;i<list.size();i++)
                    {
                        Detect detect = list.get(i);
                        UploadTools.upload(detect);
                    }
                }
            }
            else
            {  //如果当前未有downloadTask在任务队列中
                try
                {
                    // 查询任务完成失败的,重新加载任务队列
                    // 轮询,
                    Thread.sleep(SLEEP_TIME);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (isStop)
        {
            System.out.println("轮询结束");
        }
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }
}
