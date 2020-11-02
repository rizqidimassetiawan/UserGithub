package com.setiawan.consumer

import android.bluetooth.BluetoothAdapter
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.setiawan.consumer.adapter.ListDataFavoriteAdapter
import com.setiawan.consumer.db.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.setiawan.consumer.repository.DataFavorite
import com.setiawan.consumer.db.MappingHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_FAVORITE = "extra_favorite"
    }

    private lateinit var adapter: ListDataFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            val list = savedInstanceState.getParcelableArrayList<DataFavorite>(BluetoothAdapter.EXTRA_STATE)
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
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadListFavorite()
    }
}
