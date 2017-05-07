package com.underconverbg.detectophone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    public static final String LOG_TAG = "MainActivity";
    public final static String B_ACTION_NEW_OUTGOING_CALL = Intent.ACTION_NEW_OUTGOING_CALL;

    Button start_btn;
    Button start_call_btn;
    Button stop_btn;
    Button stop_call_btn;

    ListView listview;
    private UPlayer player;

    private Intent myServiceIntent;
    private Intent phoneServiceIntent;
    private Intent phoneCallStateServiceIntent;

    private final  int WRITE_CODE = 123;

//    <!-- 读取电话状态权限 -->
//    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
//    <!-- SD卡读写权限 -->
//    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
//    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
//    <!-- 录音权限 -->
//    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
//    <!-- 震动权限 -->
//    <uses-permission android:name="android.permission.VIBRATE"/>
//
//    <!--在sdcard中创建/删除文件的权限 -->
//    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
//    <uses-permission android:name="android.permission.CAPTURE_AUDIO_OUTPUT" />
//    <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />
//    <uses-permission android:name="android.permission.READ_LOGS"/>

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)
        {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO
                            ,Manifest.permission.VIBRATE   ,Manifest.permission.READ_EXTERNAL_STORAGE
                            ,Manifest.permission.CAPTURE_AUDIO_OUTPUT,
                            Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.READ_LOGS},
                    WRITE_CODE);
        }

        start_btn = (Button) findViewById(R.id.start_btn);
        stop_btn = (Button) findViewById(R.id.stop_btn);
        stop_btn.setClickable(false);

        start_call_btn = (Button) findViewById(R.id.start_call_btn);

        stop_call_btn = (Button) findViewById(R.id.stop_call_btn);
        stop_call_btn.setClickable(false);

        listview = (ListView) findViewById(R.id.listview);

        start_btn.setOnClickListener(this);
        stop_btn.setOnClickListener(this);
        start_call_btn.setOnClickListener(this);
        stop_call_btn.setOnClickListener(this);


        myServiceIntent  = new Intent(MainActivity.this, MyService.class);
        phoneServiceIntent = new Intent(MainActivity.this, PhoneService.class);
        phoneCallStateServiceIntent = new Intent(MainActivity.this,  PhoneCallStateService.class);


        String path =  FileUnit.folderCreate(this);
        Log.e(LOG_TAG, "onCreate fileName:"+path);
        getFileDir(path);

        player = new UPlayer();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                String sountPath = paths.get(i);
                Log.e(LOG_TAG, "onItemClick:"+sountPath);
                player.start(sountPath);

//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                String bpath = sountPath;
//                intent.setDataAndType(Uri.parse( bpath), "audio/3gp");
//                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view)
    {
       int id =  view.getId();

        switch (id)
        {
            case R.id.start_btn:
                startService(myServiceIntent);
                start_btn.setClickable(false);
                stop_btn.setClickable(true);

                break;
            case R.id.stop_btn:
                start_btn.setClickable(true);
                stop_btn.setClickable(false);
                stopService(myServiceIntent);
                String path =  FileUnit.folderCreate(this);
                Log.e(LOG_TAG, "onCreate fileName:"+path);
                getFileDir(path);
                break;

            case R.id.start_call_btn:

//                mBroadcastReceiver = new BroadcastReceiverMgr();
//                IntentFilter intentFilter = new IntentFilter();
//                intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
//                intentFilter.setPriority(Integer.MAX_VALUE);
//                registerReceiver(mBroadcastReceiver, intentFilter);

//                startService(phoneServiceIntent);
                startService(phoneCallStateServiceIntent);
                stop_call_btn.setClickable(true);
                start_call_btn.setClickable(false);


                break;

            case R.id.stop_call_btn:
                stopService(phoneCallStateServiceIntent);
                start_call_btn.setClickable(true);
                stop_call_btn.setClickable(false);

                String path2 =  FileUnit.folderCreate(this);
                Log.e(LOG_TAG, "onCreate fileName:"+path2);
                getFileDir(path2);

                break;

            default:
                break;
        }
    }

    private List<String> items = null;//存放名称
    private List<String> paths = null;//存放路径
    private String rootPath = "/";

    public void getFileDir(String filePath) {
        try{
            items = new ArrayList<String>();
            paths = new ArrayList<String>();
            File f = new File(filePath);
            File[] files = f.listFiles();// 列出所有文件
            // 如果不是根目录,则列出返回根目录和上一目录选项
//            if (!filePath.equals(rootPath))
//            {
//                paths.add(rootPath);
//                paths.add(f.getParent());
//            }
            // 将所有文件存入list中
            if(files != null)
            {
                int count = files.length;// 文件个数
                for (int i = 0; i < count; i++)
                {
                    File file = files[i];
                    items.add(file.getName());
                    paths.add(file.getPath());
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, items);
            listview.setAdapter(adapter);



        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
