package com.setiawan.mysubmission.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.setiawan.mysubmission.database.DatabaseContract.Companion.AUTHORITY
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME
import com.setiawan.mysubmission.helper.QueryHelper

class MyContentProvider : ContentProvider() {

    companion object{
       private const val FAV = 1
       private const val FAV_ID = 2
        private lateinit var dbHelper : QueryHelper
        private val sUriMathcer = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMathcer.addURI(AUTHORITY,TABLE_NAME, FAV)
            sUriMathcer.addURI(AUTHORITY,"${TABLE_NAME}/#", FAV_ID)
        }

    }

    override fun onCreate(): Boolean {
        dbHelper = QueryHelper.getInstance(context as Context)
        dbHelper.open()
        return true
    }
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val insert: Long = when (FAV) {
            sUriMathcer.match(uri) -> dbHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$insert")
    }
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (sUriMathcer.match(uri)) {
            FAV -> dbHelper.queryAll()
            FAV_ID -> dbHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val update :Int = when(FAV_ID){
            sUriMathcer.match(uri) -> dbHelper.update(uri.lastPathSegment.toString(),values!!)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI,null)
        return update
    }
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val delete : Int = when(FAV_ID){
            sUriMathcer.match(uri) -> dbHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI,null)
        return delete
    }

    override fun getType(uri: Uri): String? {
        return null
    }

}