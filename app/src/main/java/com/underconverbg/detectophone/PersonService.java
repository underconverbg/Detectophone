package com.underconverbg.detectophone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.underconverbg.detectophone.bean.Detect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by underconverbg on 2017/5/17.
 */

public class PersonService
{
    private DBOpenHelper dbOpenHelper;

    public PersonService(Context context)
    {
        // TODO Auto-generated constructor stub
        dbOpenHelper=new DBOpenHelper(context);
    }

    public void save(Detect person)
    {
        Log.e("PersonService","save:" + person.toString());
        SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
        db.execSQL("insert into detect(userid,phonenum,callphonenum,datetime,recordfilepath,type,recordtime) values(?,?,?,?,?,?,?)",new Object[]
                {person.getUserid(),person.getPhonenum(),person.getCallphonenum(),
                        person.getDatetime(),person.getRecordFilePath(),
                        person.getType(),person.getRecordtime()});
    }

    public void delete(String recordfilepath){
        SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
         db.execSQL("delete from detect where recordfilepath=?",new Object[]{recordfilepath});
        Log.e("delete","删除");

    }

//    public Detect find(Integer _id){
//        SQLiteDatabase db=dbOpenHelper.getReadableDatabase();
//        Cursor cursor=db.rawQuery("select * from person where _id=?", new String[]{_id.toString()});
//        if(cursor.moveToFirst()){
//            int id = cursor.getInt(cursor.getColumnIndex("_id"));
//            String name = cursor.getString(cursor.getColumnIndex("name"));
//            String age = cursor.getString(cursor.getColumnIndex("age"));
//            Detect person = new Detect();
//            person.set_id(id);
//            person.setName(name);
//            person.setAge(age);
//            return person;
//        }
//        return null;
//    }

    public List<Detect> findAll()
    {
        SQLiteDatabase db=dbOpenHelper.getReadableDatabase();
        List<Detect> persons = new ArrayList<Detect>();

        Cursor cursor=db.rawQuery("select * from detect", null);
        while(cursor.moveToNext())
        {
            Detect person=new Detect();
            String userid=cursor.getString(cursor.getColumnIndex("userid"));
            String phonenum=cursor.getString(cursor.getColumnIndex("phonenum"));
            String callphonenum=cursor.getString(cursor.getColumnIndex("callphonenum"));
            String datetime=cursor.getString(cursor.getColumnIndex("datetime"));
            String recordfilepath=cursor.getString(cursor.getColumnIndex("recordfilepath"));
            String type=cursor.getString(cursor.getColumnIndex("type"));
            String recordtime=cursor.getString(cursor.getColumnIndex("recordtime"));


            person.setUserid(userid);
            person.setPhonenum(phonenum);
            person.setCallphonenum(callphonenum);
            person.setDatetime(datetime);
            person.setRecordFilePath(recordfilepath);
            person.setType(type);
            person.setRecordtime(recordtime);
            persons.add(person);
        }
        cursor.close();
        return persons;

    }
}
