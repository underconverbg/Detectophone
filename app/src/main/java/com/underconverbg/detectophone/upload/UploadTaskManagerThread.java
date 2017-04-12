package com.underconverbg.detectophone.upload;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by user on 2017/4/8.
 */

public class UploadTaskManagerThread  implements Runnable
{
    private UploadTaskManager uploadTaskManager;

    // 创建一个可重用固定线程数的线程池
    private ExecutorService pool;
    // 线程池大小
    private final int POOL_SIZE = 5;
    // 轮询时间
    private final int SLEEP_TIME = 1000;
    // 是否停止
    private boolean isStop = false;

    public UploadTaskManagerThread()
    {
        uploadTaskManager = UploadTaskManager.getInstance();
        pool = Executors.newFixedThreadPool(POOL_SIZE);
    }


    @Override
    public void run()
    {
        while (!isStop)
        {
            UploadTask uploadTask = uploadTaskManager.getUploadTask();
            if (uploadTask != null)
            {
                System.out.println("pool.execute");
                pool.execute(uploadTask);
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
            pool.shutdown();
        }
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }
}
