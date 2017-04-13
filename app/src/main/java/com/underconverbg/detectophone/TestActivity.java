package com.underconverbg.detectophone;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        BootReceiver receiver=new BootReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.underconverbg.detectophone.BootReceiver");
        //注册receiver
        registerReceiver(receiver, filter);
        Intent intent=new Intent();
        //设置Intent的Action属性
        intent.setAction("com.underconverbg.detectophone.BootReceiver");
        //如果只传一个bundle的信息，可以不包bundle，直接放在intent里
        //发送广播
        sendBroadcast(intent);
        finish();
    }
}
