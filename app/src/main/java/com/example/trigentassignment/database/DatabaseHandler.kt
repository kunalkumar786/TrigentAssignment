package com.example.trigentassignment.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.trigentassignment.model.FeedModel

class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "FeedDatabase"
        private val TABLE_FEEDS = "FeedTable"
        private val KEY_IMAGE = "image"
        private val KEY_TITLE = "title"
        private val KEY_DESCRIPTION = "description"
        private val KEY_HEADER_TITLE = "header_title"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_FEEDS + "("
                + KEY_IMAGE + " TEXT," + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT," + KEY_HEADER_TITLE +" TEXT"+ ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDS)
        onCreate(db)
    }


    //method to insert data
    fun addFeeds(feed: FeedModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_IMAGE, feed.imageHref)
        contentValues.put(KEY_TITLE, feed.title) // EmpModelClass Name
        contentValues.put(KEY_DESCRIPTION,feed.description ) // EmpModelClass Phone
        contentValues.put(KEY_HEADER_TITLE,feed.Header_title ) // EmpModelClass Phone
        // Inserting Row
        val success = db.insert(TABLE_FEEDS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to read data
    fun viewFeed():List<FeedModel>{
        val empList:ArrayList<FeedModel> = ArrayList<FeedModel>()
        val selectQuery = "SELECT  * FROM $TABLE_FEEDS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var image:String
        var title: String
        var description: String
        var header_title: String
        if (cursor.moveToFirst()) {
            do {
                image = cursor.getString(cursor.getColumnIndex("image"))
                title = cursor.getString(cursor.getColumnIndex("title"))
                description = cursor.getString(cursor.getColumnIndex("description"))
                header_title = cursor.getString(cursor.getColumnIndex("header_title"))
                val emp= FeedModel(imageHref = image, title = title, description = description,Header_title = header_title)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }
    //method to update data
    fun updateFeed(feed: FeedModel):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_IMAGE, feed.imageHref)
        contentValues.put(KEY_TITLE, feed.title) // EmpModelClass Name
        contentValues.put(KEY_DESCRIPTION,feed.description) // EmpModelClass Email
        contentValues.put(KEY_HEADER_TITLE,feed.Header_title) // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_FEEDS, contentValues,null/*"id="+feed.userId*/,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteEmployee(feed: FeedModel):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_IMAGE, feed.imageHref) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_FEEDS,null/*"id="+emp.userId*/,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}