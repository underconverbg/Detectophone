package com.underconverbg.detectophone.upload;

/**
 * Created by user on 2017/4/8.
 */

public class UploadDemo
{
    public static void main(String[] args)
    {
        //1.new一个线程管理队列
        UploadTaskManager.getInstance();

        //2.new一个线程池，并启动
        UploadTaskManagerThread downloadTaskManagerThread = new UploadTaskManagerThread();
        new Thread(downloadTaskManagerThread).start();


//        for(int i=0;i<items.length;i++)
//        {
//            UploadTaskManager uploadTaskMananger = UploadTaskManager.getInstance();
//            uploadTaskMananger.addDownloadTask(new UploadTask(items[i]));
//            try
//            {
//                Thread.sleep(2000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
    }
}
