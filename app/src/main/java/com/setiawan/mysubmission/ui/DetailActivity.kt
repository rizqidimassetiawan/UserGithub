package com.setiawan.mysubmission.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.setiawan.mysubmission.R
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.AVATAR
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.BIO
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.COMPANY
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.FOLLOWERS
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.FOLLOWING
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.LOCATION
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.NAME
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.REPOSITORY
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.USERNAME
import com.setiawan.mysubmission.helper.QueryHelper
import com.setiawan.mysubmission.repository.DataUsers
import com.setiawan.mysubmission.ui.adapter.ViewPagerDetailAdapter
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(),View.OnClickListener {

    companion object{
        const val EXTRA_DETAIL = "extra_detail"
    }
    private lateinit var dbHelper : QueryHelper
    private var status = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = QueryHelper.getInstance(applicationContext)
        dbHelper.open()

        val username : DataUsers? = intent.getParcelableExtra(EXTRA_DETAIL)
        val cursor = dbHelper.queryByUsername(username?.username.toString())

        if (cursor.moveToNext()){
            status = true
            setStatusFavotie(true)
        }

        if (supportActionBar != null){
            supportActionBar?.title = resources.getString(R.string.detail)
        }


        setData()
        btn_floting.setOnClickListener(this)
        viewPagerConfig()
    }

    private fun viewPagerConfig(){
        val viewPagerDetail = ViewPagerDetailAdapter(this,supportFragmentManager)
        viewpager.adapter = viewPagerDetail
        tabs.setupWithViewPager(viewpager)
        supportActionBar?.elevation = 0f
    }

    private fun setData(){
        val dataUser : DataUsers? = intent.getParcelableExtra(EXTRA_DETAIL)
        Glide.with(this)
            .load(dataUser?.avatar)
            .error(R.drawable.ic_account_circle_black)
            .apply(RequestOptions().override(130,130))
            .into(image)

        tv_name.text = dataUser?.name
        tv_location.text = dataUser?.location
        tv_repository.text = getString(R.string.repo,dataUser?.repository)
        tv_company.text = dataUser?.company
        tv_following.text = getString(R.string.value_following,dataUser?.following)
        tv_followers.text = getString(R.string.value_followers,dataUser?.followers)
        tv_bio.text = "\"${dataUser?.bio}\""
    }

    private fun setStatusFavotie(state : Boolean){
        if (state){
           btn_floting.setImageResource(R.drawable.ic_baseline_favorite_24)
        }else{
            btn_floting.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    override fun onClick(v: View?) {
        val data : DataUsers? = intent.getParcelableExtra(EXTRA_DETAIL)
        when(v?.id){
            R.id.btn_floting -> {
                if (status){
                    val idUser = data?.username.toString()
                    dbHelper.deleteById(idUser)
                    Toast.makeText(this, "Data Deleted from Favorite", Toast.LENGTH_SHORT).show()
                    setStatusFavotie(false)
                    status = true
                }else{
                    val values = ContentValues()
                    values.put(USERNAME,data?.username)
                    values.put(NAME,data?.name)
                    values.put(AVATAR,data?.avatar)
                    values.put(COMPANY,data?.company)
                    values.put(LOCATION,data?.location)
                    values.put(REPOSITORY,data?.repository)
                    values.put(FOLLOWERS,data?.followers)
                    values.put(FOLLOWING,data?.following)
                    values.put(BIO,data?.bio)

                    status = false
                    contentResolver.insert(CONTENT_URI,values)
                    Toast.makeText(this, "Data Added to Favorite", Toast.LENGTH_SHORT).show()
                    setStatusFavotie(true)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.item_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_setting -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_fav -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }
    }
}
