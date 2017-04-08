package com.underconverbg.detectophone.upload;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by user on 2017/4/8.
 */

// 线程管理类
public class UploadTaskManager
{
    private static final String TAG="UploadTaskManager";
    // UI请求队列
    private LinkedList<UploadTask> uploadTasks;
    // 任务不能重复
    private Set<String> taskIdSet;

    private static UploadTaskManager uploadTaskMananger;

    private UploadTaskManager()
    {
        uploadTasks = new LinkedList<UploadTask>();
        taskIdSet = new HashSet<String>();
    }

    public static synchronized UploadTaskManager getInstance()
    {
        if (uploadTaskMananger == null) {
            uploadTaskMananger = new UploadTaskManager();
        }
        return uploadTaskMananger;
    }

    //1.先执行
    public void addDownloadTask(UploadTask uploadTask)
    {
        synchronized (uploadTask)
        {
            if (!isTaskRepeat(uploadTask.getFileId()))
            {
                // 增加下载任务
                uploadTasks.addLast(uploadTask);
            }
        }
    }

    public boolean isTaskRepeat(String fileId)
    {
        synchronized (taskIdSet)
        {
            if (taskIdSet.contains(fileId))
            {
                return true;
            }
            else
            {
                System.out.println("上传任务管理器增加："+ fileId);
                taskIdSet.add(fileId);
                return false;
            }
        }
    }

    public UploadTask getUploadTask()
    {
        synchronized (uploadTasks)
        {
            if (uploadTasks.size() > 0)
            {
                System.out.println("上传管理器增加下载任务："+"取出任务");
                UploadTask downloadTask = uploadTasks.removeFirst();
                return downloadTask;
            }
        }
        return null;
    }
}
