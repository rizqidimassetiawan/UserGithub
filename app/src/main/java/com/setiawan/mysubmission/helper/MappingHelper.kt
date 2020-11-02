package com.setiawan.mysubmission.helper

import android.database.Cursor
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.AVATAR
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.BIO
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.COMPANY
//import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.FAVORITE
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.FOLLOWERS
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.FOLLOWING
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.LOCATION
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.NAME
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.REPOSITORY
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.USERNAME
import com.setiawan.mysubmission.repository.DataFavorite

object MappingHelper {

    fun mapCursorToArrayList(cursor: Cursor?):ArrayList<DataFavorite>{
        val favList = ArrayList<DataFavorite>()

        cursor?.apply {
            while (moveToNext()){
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val name = getString(getColumnIndexOrThrow(NAME))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val company = getString(getColumnIndexOrThrow(COMPANY))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val repository = getString(getColumnIndexOrThrow(REPOSITORY))
                val followers = getString(getColumnIndexOrThrow(FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(FOLLOWING))
                val bio = getString(getColumnIndexOrThrow(BIO))
              //  val isFav = getString(getColumnIndexOrThrow(FAVORITE))

                favList.add(
                    DataFavorite(
                        username, name, avatar, company, location, repository, followers, following, bio//, isFav
                    )
                )
            }
        }
        return favList
    }
}