package com.setiawan.mysubmission.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.setiawan.mysubmission.R
import com.setiawan.mysubmission.viewModel.MainViewModel
import com.setiawan.mysubmission.repository.DataUsers
import com.setiawan.mysubmission.ui.adapter.UserAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var viewModel : MainViewModel
    private val listDataUser = ArrayList<DataUsers>()


    companion object{
         val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showProgresBar(false)
        adapter = UserAdapter(listDataUser)
        viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        searchData()
        setView()
        configMainViewModel(adapter)

        supportActionBar?.title = "User GitHub"

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when(item.itemId){
           R.id.menu_fav -> {
               val intent = Intent(this,FavoriteActivity::class.java)
               startActivity(intent)
               true
           }
           R.id.menu_setting -> {
               val intent = Intent(this,SettingsActivity::class.java)
               startActivity(intent)
               true
           }else -> true
       }
    }

    private fun configMainViewModel(adapter: UserAdapter){
        viewModel.getListUser().observe(this, Observer { listUser ->
            if (listUser != null){
                adapter.setData(listUser)
                showProgresBar(false)
            }
        })
    }

    fun showProgresBar(state : Boolean){
        if (state){
            progressbar.visibility = View.VISIBLE
        }else{
            progressbar.visibility = View.GONE
        }
    }

    fun setLabel(state:Boolean){
        if (state){
            img_search.visibility = View.VISIBLE
            tv_label_search.visibility = View.VISIBLE
            tv_label_dsc.visibility = View.VISIBLE
        }else{
            img_search.visibility = View.GONE
            tv_label_search.visibility = View.GONE
            tv_label_dsc.visibility = View.GONE
        }
    }
    private fun setView(){
        rv_user.setHasFixedSize(true)
        rv_user.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
        rv_user.adapter = adapter
    }

    private fun searchData(){
        search_view.setOnQueryTextListener(object :
        androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()){
                    listDataUser.clear()
                    setView()
                    viewModel.getaDataGitSearch(query,applicationContext)
                    showProgresBar(false)
                    setLabel(false)
                   configMainViewModel(adapter)
                }else{
                    setLabel(true)

                }
                showProgresBar(true)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }


}
