package com.setiawan.mysubmission.database

import android.net.Uri
import android.provider.BaseColumns

internal class DatabaseContract {

    companion object{
        const val AUTHORITY = "com.setiawan.mysubmission"
        const val SCHEME = "content"
    }

    internal class UserFavoriteColumns : BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite_user"
            const val USERNAME= "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val BIO = "bio"

            val CONTENT_URI : Uri = Uri.Builder().scheme(SCHEME).authority(AUTHORITY).appendPath(
                TABLE_NAME).build()
        }
    }
}