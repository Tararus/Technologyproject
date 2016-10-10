package com.example.administrator.mytechnologyproject.util.DBManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.mytechnologyproject.gloable.Contacts;

/**
 * Created by Administrator on 2016/9/10.
 */
public class DBManager extends SQLiteOpenHelper {
    public static final String DB_NAME = "news.db";
    public static final String NEWSTYPE_NAME = "news_type";
    public static final String NEWS_NAME = "news";
    public static final String NEWSFAVORITE_NAME = "news_favorite";


    /*
    context属于一个上下文的参数
    name；属于数据库的名字
    CursorFactory给null
    rersion 1
    当实例化DBTools的时候，执行完构造器的时候数据库已经建立
     */
    public DBManager(Context context) {
        super(context, DB_NAME, null, Contacts.VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_1 = "create table " + NEWSTYPE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "subid INTEGER,subgroup TEXT);";
        String sql_2 = "create table " + NEWS_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "summary TEXT,icon TEXT,stamp TEXT,title TEXT,nid INTEGER,link TEXT,type INTEGER);";
        String sql_3 = "create table " + NEWSFAVORITE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "summary TEXT,icon TEXT,stamp TEXT,title TEXT,nid INTEGER,link TEXT,type INTEGER);";
        db.execSQL(sql_1);
        db.execSQL(sql_2);
        db.execSQL(sql_3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
