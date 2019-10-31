package com.example.lab9_database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.prefs.PreferencesFactory

class MyDBAdapter(_context: Context) {
    private var DATABALSE_NAME:String = "name"
    private var mContext:Context?
    private var mDbHelper:MyDBHelper?
    private var mSqLiteDatabase:SQLiteDatabase? = null
    private var DATABASE_VERSION = 1

    init {
        this.mContext = _context
        mDbHelper = MyDBHelper(_context, DATABALSE_NAME, null, DATABASE_VERSION)
    }

    public fun open(){
        mSqLiteDatabase = mDbHelper?.writableDatabase
    }

    public fun insertStudent(name: String?, faculty:Int){
        val cv:ContentValues = ContentValues()
        cv.put("name", name)
        cv.put("faculty", faculty)
        mSqLiteDatabase?.insert("students", null, cv)
    }

    public fun selectAllStudents(): List<String> {
        var allStudents:MutableList<String> = ArrayList();
        var cursor : Cursor = mSqLiteDatabase?.query("students", null,null,null,null,null,null)!!
        if(cursor.moveToFirst()){
            do {
                allStudents.add(cursor.getString(1))
            }while (cursor.moveToNext())
        }
        return allStudents
    }

    public fun deleteAllEngineers(){
        mSqLiteDatabase?.delete("students", null, null)
    }

    inner class MyDBHelper(context: Context?, name:String?,
                           factory: SQLiteDatabase.CursorFactory?,
                           version:Int):SQLiteOpenHelper(context, name, factory, version) {

        override fun onCreate(_db: SQLiteDatabase?) {
            val query = "CREATE TABLE students(id integer primary key autoincrement, name text, faculty integer);"
            _db?.execSQL(query)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            val query = "DROP TABLE IF EXISTS students;"
            db?.execSQL(query)
            onCreate(db)
        }
    }

}