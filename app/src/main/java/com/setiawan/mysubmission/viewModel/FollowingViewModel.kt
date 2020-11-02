package com.setiawan.mysubmission.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.setiawan.mysubmission.ui.fragment.FollowingFragment
import com.setiawan.mysubmission.repository.DataUsers
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FollowingViewModel : ViewModel() {

    private val listFollowingNonMutable = ArrayList<DataUsers>()
    private val listFollowingMutable = MutableLiveData<ArrayList<DataUsers>>()

    fun getListFollowing():LiveData<ArrayList<DataUsers>>{
        return listFollowingMutable
    }

    fun getDataAPI(context: Context,id : String){
        val client = AsyncHttpClient()
        client.addHeader("Authorization","token 2051b8a591c44627a9b22493be3ee5203879ffc6")
        client.addHeader("User-Agent","request")
        val url = "https://api.github.com/users/$id/following"
        client.get(url,object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray?
            ) {
                val respond = String(responseBody!!)
                Log.d(FollowingFragment.TAG,respond)
                try {
                    val jsonArray = JSONArray(respond)
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val usernameLogin = jsonObject.getString("login")
                        getDataAPIDetail(usernameLogin,context)
                    }
                }catch (e:Exception){
                    Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getDataAPIDetail(username:String,context: Context){
        val client = AsyncHttpClient()
        client.addHeader("Authorization","token 2051b8a591c44627a9b22493be3ee5203879ffc6")
        client.addHeader("User-Agent","request")

        val url = "https://api.github.com/users/$username"
        client.get(url,object:AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val respond = String(responseBody!!)
                Log.d(FollowingFragment.TAG,respond)
                try {
                    val jsonObject = JSONObject(respond)
                    val userDataFollowing = DataUsers()
                    userDataFollowing.username = jsonObject.getString("login")
                    userDataFollowing.name = jsonObject.getString("name")
                    userDataFollowing.avatar = jsonObject.getString("avatar_url")
                    userDataFollowing.company = jsonObject.getString("company")
                    userDataFollowing.location = jsonObject.getString("location")
                    userDataFollowing.repository = jsonObject.getString("public_repos")
                    userDataFollowing.followers = jsonObject.getString("followers")
                    userDataFollowing.following = jsonObject.getString("following")
                    userDataFollowing.bio = jsonObject.getString("bio")
                    listFollowingNonMutable.add(userDataFollowing)
                    listFollowingMutable.postValue(listFollowingNonMutable)
                }catch (e:Exception){
                    Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }
}