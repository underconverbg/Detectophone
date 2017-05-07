package com.underconverbg.detectophone;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//测试用的
@Deprecated
public class MyService extends Service
{
    public static final String LOG_TAG = "MyService";
    public List<String> list = new ArrayList<String>();

    public MyService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
       return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doInThread();
        return super.onStartCommand(intent, flags, startId);
    }



    public void doInThread()
    {
        Log.e(LOG_TAG, "doInThread() doInThread");
        RecorderFactory factory =  RecorderFactory.getInstancei();
        list.add(factory.startRecorder("luyin",MyService.this,RecorderFactory.TYPE_MIC));
    }

    @Override
    public void onDestroy()
    {
        Log.e(LOG_TAG, "onDestroy() onDestroy");
        RecorderFactory factory =  RecorderFactory.getInstancei();
        factory.stopRecorder();
        super.onDestroy();
    }

    public List<String> getRecorderList() {
        return list;
    }

    public class MyBinder extends Binder
    {
        public MyService getService()
        {
            return MyService.this;
        }
    }
}
