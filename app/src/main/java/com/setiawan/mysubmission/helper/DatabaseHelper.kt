package com.setiawan.mysubmission.helper

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.AVATAR
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.BIO
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.COMPANY
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.FOLLOWERS
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.FOLLOWING
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.LOCATION
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.NAME
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.REPOSITORY
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.USERNAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "final_project"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE ="CREATE TABLE $TABLE_NAME" +
                "($USERNAME TEXT NOT NULL,"+
                "$NAME TEXT NOT NULL,"+
                "$AVATAR TEXT NOT NULL,"+
                "$COMPANY TEXT NOT NULL,"+
                "$LOCATION TEXT NOT NULL,"+
                "$REPOSITORY TEXT NOT NULL,"+
                "$FOLLOWERS TEXT NOT NULL,"+
                "$FOLLOWING TEXT NOT NULL," +
                "$BIO TEXT NOT NULL)"
    }
    @SuppressLint("SQLiteString")
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}