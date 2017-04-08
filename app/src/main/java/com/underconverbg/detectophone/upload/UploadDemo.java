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

//        String []items=new String[]{"向晨宇1","向晨宇2","向晨宇3","向晨宇4","向晨宇5","向晨宇6","向晨宇7","向晨宇1","向晨宇2"};
//
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
