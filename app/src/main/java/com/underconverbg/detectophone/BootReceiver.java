package com.underconverbg.detectophone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by underconverbg on 2017/4/9.
 */

public class BootReceiver extends BroadcastReceiver
{
    public static final String LOG_TAG = "DETECT_MYBOOTRECEIVER";

//    private Intent myServiceIntent;
    private Intent phoneCallStateServiceIntent;

    @Override
    public void onReceive(Context context, Intent arg1)
    {
        Log.e(LOG_TAG, "-----MyBootReceiver:onReceive start");

        Log.e(LOG_TAG, arg1.getAction());
//        myServiceIntent  = new Intent(context, MyService.class);
//        context.startService(myServiceIntent);

        if (phoneCallStateServiceIntent == null)
        {
            phoneCallStateServiceIntent = new Intent(context, PhoneCallStateService.class);
            context.startService(phoneCallStateServiceIntent);
        }
        else
        {
            context.startService(phoneCallStateServiceIntent);
        }
//        Intent ootStartIntent=new Intent(context,MainActivity.class);
//        ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(ootStartIntent);
        
        Log.e(LOG_TAG, "-----MyBootReceiver:onReceive end");
    }
}