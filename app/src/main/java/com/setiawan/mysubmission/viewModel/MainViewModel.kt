package com.setiawan.mysubmission.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.setiawan.mysubmission.ui.MainActivity
import com.setiawan.mysubmission.repository.DataUsers
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {

    private val listUserNonMutable = ArrayList<DataUsers>()
    private val listUserMutable = MutableLiveData<ArrayList<DataUsers>>()

    fun getListUser() : LiveData<ArrayList<DataUsers>> {
        return listUserMutable
    }
    fun getaDataGitSearch(query: String,context: Context){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$query"

        client.addHeader("Authorization", "token 2051b8a591c44627a9b22493be3ee5203879ffc6")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?) {
                val result = String(responseBody!!)
                Log.d(MainActivity.TAG,result)
                try {
                    val respondObject = JSONObject(result)
                    val items = respondObject.getJSONArray("items")
                    for (i in 0 until items.length()){
                        val jsonObject = items.getJSONObject(i)
                        val userName = jsonObject.getString("login")
                        getDataGitDetail(userName,context)
                    }
                    listUserMutable.postValue(listUserNonMutable)
                }catch (e:Exception){
                    Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable?) {
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
    fun getDataGitDetail(username : String , context: Context){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"

        client.addHeader("Authorization", "token 2051b8a591c44627a9b22493be3ee5203879ffc6")
        client.addHeader("User-Agent", "request")
        client.get(url,object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array< Header>?,
                responseBody: ByteArray?
            ) {
                val respond = String(responseBody!!)
                Log.d(MainActivity.TAG,respond)
                try {
                    val jsonObject = JSONObject(respond)
                    val userItem = DataUsers()
                        userItem.username = jsonObject.getString("login")
                        userItem.name = jsonObject.getString("name")
                        userItem.avatar = jsonObject.getString("avatar_url")
                        userItem.company = jsonObject.getString("company")
                        userItem.location = jsonObject.getString("location")
                        userItem.repository = jsonObject.getString("public_repos")
                        userItem.followers = jsonObject.getString("followers")
                        userItem.following = jsonObject.getString("following")

                        listUserNonMutable.add(userItem)
                        listUserMutable.postValue(listUserNonMutable)
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
}