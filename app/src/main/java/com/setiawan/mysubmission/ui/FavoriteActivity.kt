package com.setiawan.mysubmission.ui

import android.bluetooth.BluetoothAdapter.EXTRA_STATE
import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.setiawan.mysubmission.R
import com.setiawan.mysubmission.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.setiawan.mysubmission.helper.MappingHelper
import com.setiawan.mysubmission.helper.QueryHelper
import com.setiawan.mysubmission.repository.DataFavorite
import com.setiawan.mysubmission.ui.adapter.ListDataFavoriteAdapter
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.zip.Inflater

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter : ListDataFavoriteAdapter
    private lateinit var dbHelper : QueryHelper

    companion object{
        const val EXTRA_FAVORITE = "extra_favorite"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = QueryHelper.getInstance(applicationContext)
        dbHelper.open()

        adapter = ListDataFavoriteAdapter(this)
        setAdapter()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadListFavorite()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI,true,myObserver)
        if (savedInstanceState == null) {
            loadListFavorite()
        } else {
            val list = savedInstanceState.getParcelableArrayList<DataFavorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }

        if (supportActionBar != null) {
            supportActionBar?.title = getString(R.string.favorite)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_FAVORITE,adapter.listFavorite)
    }

    private fun setAdapter(){
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        rv_favorite.adapter = adapter
    }

    private fun setLabel(state:Boolean){
        if (state){
            img_fav.visibility = View.VISIBLE
            tv_title_fav.visibility = View.VISIBLE
            tv_dsc_fav.visibility = View.VISIBLE
        }else{
            img_fav.visibility = View.GONE
            tv_title_fav.visibility = View.GONE
            tv_dsc_fav.visibility = View.GONE
        }
    }

    private fun loadListFavorite(){
        GlobalScope.launch(Dispatchers.Main){
            setLabel(false)
            val deferredFav = async(Dispatchers.IO){
                val cursor = contentResolver.query(CONTENT_URI,null,null,null,null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favData = deferredFav.await()
            if (favData.size > 0){
                adapter.listFavorite = favData
                //setLabel(false)
            }else{
                adapter.listFavorite = ArrayList()
                setLabel(true)
                //Snackbar.make(rv_favorite,"Data is Null",Snackbar.LENGTH_SHORT).show()

            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadListFavorite()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.item_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_fav -> {
                val move = Intent(this,FavoriteActivity::class.java)
                startActivity(move)
                true
            }
            R.id.menu_setting -> {
                val move = Intent(this,SettingsActivity::class.java)
                startActivity(move)
                true
            }
            android.R.id.home ->{
                finish()
                true
            }
            else -> true
        }
    }

}