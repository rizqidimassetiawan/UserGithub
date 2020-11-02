package com.setiawan.mysubmission.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.setiawan.mysubmission.ui.fragment.FollowersFragment
import com.setiawan.mysubmission.repository.DataUsers
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FollowersViewModel : ViewModel() {

    val listFollowersNonMutable = ArrayList<DataUsers>()
    val listFollowersMuttable = MutableLiveData<ArrayList<DataUsers>>()

    fun getListFollowers():LiveData<ArrayList<DataUsers>>{
        return listFollowersMuttable
    }
    fun getDataAPI(context: Context,id : String){
        val client = AsyncHttpClient()
        client.addHeader("Authorization","token 2051b8a591c44627a9b22493be3ee5203879ffc6")
        client.addHeader("User-Agent","request")

        val url = "https://api.github.com/users/$id/followers"
        client.get(url,object :AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray?
            ) {
                val respond = String(responseBody!!)
                Log.d(FollowersFragment.TAG,respond)
                try {
                    val jsonArray = JSONArray(respond)
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val usernameLogin = jsonObject.getString("login")
                        getDataAPIDetail(usernameLogin,context)
                    }
                }catch (e:Exception){
                    Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getDataAPIDetail(username : String , context: Context){
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
                Log.d(FollowersFragment.TAG,respond)
                try {
                    val jsonObject = JSONObject(respond)
                    val userDataFollowers = DataUsers()
                    userDataFollowers.username = jsonObject.getString("login")
                    userDataFollowers.name = jsonObject.getString("name")
                    userDataFollowers.avatar = jsonObject.getString("avatar_url")
                    userDataFollowers.company = jsonObject.getString("company")
                    userDataFollowers.location = jsonObject.getString("location")
                    userDataFollowers.repository = jsonObject.getString("public_repos")
                    userDataFollowers.followers = jsonObject.getString("followers")
                    userDataFollowers.following = jsonObject.getString("following")
                    userDataFollowers.bio = jsonObject.getString("bio")

                    listFollowersNonMutable.add(userDataFollowers)
                    listFollowersMuttable.postValue(listFollowersNonMutable)
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